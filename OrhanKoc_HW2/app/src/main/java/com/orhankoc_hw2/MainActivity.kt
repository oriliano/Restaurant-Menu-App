package com.orhankoc_hw2

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.orhankoc_hw2.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CustomRecyclerViewAdapter.OrganizationRecyclerAdapterInterface {

    private lateinit var database: AppDatabase
    private lateinit var restaurantDao: RestaurantDao
    private lateinit var adapter: CustomRecyclerViewAdapter
    private val restaurantList = ArrayList<Restaurant>()

    private val defaultRestaurants = listOf(
        Meal("Burger", "Cheeseburger", R.drawable.burger),
        Drink("Cola", "Zero",R.drawable.cola),
        Meal("Pizza", "Margaritha",R.drawable.pizza),
        Drink("Lipton", "IceTea",R.drawable.lipton)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veritabanını başlat
        database = DatabaseModule.provideDatabase(this)
        restaurantDao = database.restaurantDao()

        // RecyclerView ayarları
        adapter = CustomRecyclerViewAdapter(this, restaurantList)
        binding.recyclerOrg.adapter = adapter
        binding.recyclerOrg.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Uygulama açıldığında veritabanını sıfırla
        resetDatabase()

        // FAB ile yeni öğe ekleme diyalogunu göster
        binding.fabAddItem.setOnClickListener {
            showAddDialog()
        }
    }

    private fun resetDatabase() {
        lifecycleScope.launch {
            try {
                // Veritabanındaki tüm verileri sil
                restaurantDao.deleteAllRestaurants()

                // Sabit listeyi yeniden ekle
                restaurantDao.insertAllRestaurants(defaultRestaurants.map { it.toEntity() })

                // JSON'dan gelen verileri tekrar ekle
                fetchJsonData()
            } catch (e: Exception) {
                Log.e("Database", "Error resetting database: ${e.message}")
            }
        }
    }

    override fun displayItem(org: Restaurant) {
        Toast.makeText(this, "Selected: ${org.companyName}", Toast.LENGTH_SHORT).show()
    }

    override fun addItem(org: Restaurant) {
        lifecycleScope.launch {
            try {
                val newEntity = org.toEntity()
                restaurantDao.insertRestaurant(newEntity)
                restaurantList.add(org)
                adapter.notifyItemInserted(restaurantList.size - 1)
                Log.d("Database", "Inserted: ${newEntity.companyName}")
            } catch (e: Exception) {
                Log.e("Database", "Error inserting item: ${e.message}")
            }
        }
    }

    override fun updateItem(position: Int, org: Restaurant) {
        if (position in 0 until restaurantList.size) {
            lifecycleScope.launch {
                try {
                    val entityToUpdate = restaurantList[position].toEntity()
                    val updatedEntity = entityToUpdate.copy(
                        companyName = org.companyName,
                        description = org.description
                    )
                    restaurantDao.updateRestaurant(updatedEntity)
                    restaurantList[position] = org
                    adapter.notifyItemChanged(position)
                    Log.d("Database", "Updated: ${updatedEntity.companyName}")
                } catch (e: Exception) {
                    Log.e("Database", "Error updating item: ${e.message}")
                }
            }
        } else {
            Log.e("UpdateItem", "Invalid position: $position")
        }
    }

    override fun deleteItem(position: Int) {
        if (position in 0 until restaurantList.size) {
            lifecycleScope.launch {
                try {
                    val entityToDelete = restaurantList[position].toEntity()
                    restaurantDao.deleteRestaurant(entityToDelete)
                    restaurantList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Log.d("Database", "Deleted: ${entityToDelete.companyName}")
                } catch (e: Exception) {
                    Log.e("Database", "Error deleting item: ${e.message}")
                }
            }
        } else {
            Log.e("DeleteItem", "Invalid position: $position")
        }
    }

    private fun loadRestaurantsFromDatabase() {
        lifecycleScope.launch {
            try {
                val restaurants = restaurantDao.getAllRestaurants()
                Log.d("Database", "Fetched data: $restaurants")
                restaurantList.clear()
                restaurantList.addAll(restaurants.map { it.toDomainModel() })
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("Database", "Error loading data: ${e.message}")
            }
        }
    }

    private fun fetchJsonData() {
        ApiClient.retrofit.getRestaurants().enqueue(object : retrofit2.Callback<List<Restaurant>> {
            override fun onResponse(
                call: retrofit2.Call<List<Restaurant>>,
                response: retrofit2.Response<List<Restaurant>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val fetchedRestaurants = response.body()!!.map { response ->
                        if (response.type == "Meal") {
                            Meal(response.companyName, response.description, R.drawable.food) // Default image
                        } else {
                            Drink(response.companyName, response.description, R.drawable.can) // Default image
                        }
                    }

                    lifecycleScope.launch {
                        try {
                            // Veritabanına kaydet ve RecyclerView'e ekle
                            restaurantDao.insertAllRestaurants(fetchedRestaurants.map { it.toEntity() })
                            loadRestaurantsFromDatabase() // Yeni verileri yükle
                        } catch (e: Exception) {
                            Log.e("Database", "Error saving JSON data: ${e.message}")
                        }
                    }
                } else {
                    Log.e("FetchError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Restaurant>>, t: Throwable) {
                Log.e("FetchError", "Failure: ${t.message}")
            }
        })
    }

    private fun showAddDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_item)

        val etCompanyName = dialog.findViewById<EditText>(R.id.etCompanyName)
        val etDescription = dialog.findViewById<EditText>(R.id.etDescription)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val companyName = etCompanyName.text.toString()
            val description = etDescription.text.toString()

            if (companyName.isNotBlank() && description.isNotBlank()) {
                // Varsayılan Meal resmi kullanılacak
                addItem(Meal(companyName, description, R.drawable.food))
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}