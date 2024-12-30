package com.orhankoc_hw2

import android.os.Parcel
import android.os.Parcelable


class Drink(
    companyName: String,
    description: String,
    imageResId: Int = R.drawable.can
) : Restaurant(companyName, description, type = "",imageResId=imageResId) {

}