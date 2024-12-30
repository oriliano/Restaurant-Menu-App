package com.orhankoc_hw2

object RestaurantSys {
    private val restaurantArrayList: ArrayList<Restaurant> = ArrayList()

    fun prepareData() {
        restaurantArrayList.clear()
        restaurantArrayList.add(Drink("Lipton", "Ice Tea"))
        restaurantArrayList.add(Drink("Coca-Cola", "Cola"))
        restaurantArrayList.add(Meal("Burger", "CheeseBurger"))
        restaurantArrayList.add(Meal("Pizza", "Margaritha"))
        /* restaurantArrayList.add(Meal("Wrap", "Crunch-CornCarne"))*/




    }

    fun getOrganizationArrayList(): ArrayList<Restaurant> = restaurantArrayList
}