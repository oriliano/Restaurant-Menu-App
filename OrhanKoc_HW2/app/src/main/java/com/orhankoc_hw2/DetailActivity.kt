package com.orhankoc_hw2

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val companyName: TextView = findViewById(R.id.tvCompanyNameDetail)
        val description: TextView = findViewById(R.id.tvDescriptionDetail)
        val image: ImageView = findViewById(R.id.imgDetail)

        // Gelen veriyi alalım
        val restaurant: Restaurant? = intent.getSerializableExtra("restaurant") as Restaurant?

        restaurant?.let {
            companyName.text = it.companyName
            description.text = it.description
            image.setImageResource(it.imageResId)  // Resmi de ayarlıyoruz
        }
    }
}
