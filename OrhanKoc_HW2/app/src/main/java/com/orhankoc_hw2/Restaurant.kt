package com.orhankoc_hw2


import java.io.Serializable

open class Restaurant(
    val companyName: String,
    val description: String,
    val type: String,
    val imageResId: Int
) : Serializable