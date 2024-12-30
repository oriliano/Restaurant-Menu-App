package com.orhankoc_hw2

class Meal(
    companyName: String,
    description: String,
    imageResId: Int = R.drawable.food

) : Restaurant(companyName, description, type = "", imageResId = imageResId)
