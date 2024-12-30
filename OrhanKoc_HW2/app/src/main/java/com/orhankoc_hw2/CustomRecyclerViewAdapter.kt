package com.orhankoc_hw2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerViewAdapter(
    private val context: Context,
    private val recyclerItemValues: ArrayList<Restaurant>

) : RecyclerView.Adapter<CustomRecyclerViewAdapter.BaseItemHolder>() {

    interface OrganizationRecyclerAdapterInterface {
        fun displayItem(org: Restaurant)
        fun addItem(org: Restaurant)
        fun updateItem(position: Int, org: Restaurant)
        fun deleteItem(position: Int)
    }
    private val defaultRestaurants = listOf(
        Meal("Burger", "Cheeseburger", R.drawable.burger),
        Drink("Cola", "Zero",R.drawable.cola),
        Meal("Pizza", "Margaritha",R.drawable.pizza),
        Drink("Lipton", "IceTea",R.drawable.lipton)
    )
    private val orgAdapterInterface = context as OrganizationRecyclerAdapterInterface

    companion object {
        private const val TYPE_MEAL = 0
        private const val TYPE_DRINK = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_MEAL -> inflater.inflate(R.layout.recycler_meal_item, parent, false)
            TYPE_DRINK -> inflater.inflate(R.layout.recycler_drink_item, parent, false)
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
        return if (viewType == TYPE_MEAL) MealItemHolder(view) else DrinkItemHolder(view)
    }

    override fun onBindViewHolder(holder: BaseItemHolder, position: Int) {
        val org = recyclerItemValues[position]
        holder.bind(org)

        // Background color based on type
        when (holder) {
            is MealItemHolder -> holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            is DrinkItemHolder -> holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        }

        // Handle long click for edit/delete
        holder.itemView.setOnLongClickListener {
            showEditDeleteDialog(position, org)
            true
        }

        // Handle click for opening DetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("restaurant", org)  // Pass the restaurant object
            context.startActivity(intent)  // Start DetailActivity
        }
    }

    override fun getItemCount(): Int = recyclerItemValues.size

    override fun getItemViewType(position: Int): Int {
        return when (recyclerItemValues[position]) {
            is Meal -> TYPE_MEAL
            is Drink -> TYPE_DRINK
            else -> throw IllegalArgumentException("Unknown type at position $position")
        }
    }

    private fun showEditDeleteDialog(position: Int, org: Restaurant) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(context)
            .setTitle("Choose an action")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditDialog(position, org)
                    1 -> orgAdapterInterface.deleteItem(position)
                }
            }.show()
    }

    private fun showEditDialog(position: Int, org: Restaurant) {
        val dialog = AlertDialog.Builder(context).create()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_item, null)
        dialog.setView(dialogView)

        val etCompanyName = dialogView.findViewById<TextView>(R.id.etCompanyName)
        val etDescription = dialogView.findViewById<TextView>(R.id.etDescription)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        etCompanyName.text = org.companyName
        etDescription.text = org.description

        btnSave.setOnClickListener {
            val updatedCompanyName = etCompanyName.text.toString()
            val updatedDescription = etDescription.text.toString()

            if (updatedCompanyName.isNotBlank() && updatedDescription.isNotBlank()) {
                val updatedItem = if (org is Meal) Meal(updatedCompanyName, updatedDescription) else Drink(updatedCompanyName, updatedDescription)
                orgAdapterInterface.updateItem(position, updatedItem)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    abstract class BaseItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Restaurant)
    }

    inner class MealItemHolder(itemView: View) : BaseItemHolder(itemView) {
        private val tvCompanyName: TextView = itemView.findViewById(R.id.tvItemMealCompanyName)
        private val tvConcept: TextView = itemView.findViewById(R.id.tvItemMealConcept)
        private val imgConcept: ImageView = itemView.findViewById(R.id.imgItemMealColor)
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.itemMealConstraintLayout)

        override fun bind(item: Restaurant) {
            if (item is Meal) {
                tvCompanyName.text = item.companyName
                tvConcept.text = item.description
                /* imgConcept.setImageResource(item.imageResId) */ // Different image for each meal
                parentLayout.setOnClickListener { orgAdapterInterface.displayItem(item) }
            }
        }
    }

    inner class DrinkItemHolder(itemView: View) : BaseItemHolder(itemView) {
        private val tvCompanyName: TextView = itemView.findViewById(R.id.tvItemDrinkCompanyName)
        private val tvType: TextView = itemView.findViewById(R.id.tvItemDrinkType)
        private val imgType: ImageView = itemView.findViewById(R.id.imgItemDrinkColor)
        val parentLayout: ConstraintLayout = itemView.findViewById(R.id.itemDrinkConstraintLayout)

        override fun bind(item: Restaurant) {
            if (item is Drink) {
                tvCompanyName.text = item.companyName
                tvType.text = item.description
                imgType.setImageResource(item.imageResId)  // Different image for each drink
                parentLayout.setOnClickListener { orgAdapterInterface.displayItem(item) }
            }
        }
    }
}