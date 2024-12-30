package com.orhankoc_hw2



fun Restaurant.toEntity(): RestaurantEntity {
    return when (this) {
        is Meal -> RestaurantEntity(companyName = companyName, description = description)
        is Drink -> RestaurantEntity(companyName = companyName, description = description)
        else -> throw IllegalArgumentException("Unsupported type")
    }
}

fun RestaurantEntity.toDomainModel(): Restaurant {
    return if (description.contains("Drink", ignoreCase = true)) {
        Drink(companyName = companyName, description = description)
    } else {
        Meal(companyName = companyName, description = description)
    }
}
