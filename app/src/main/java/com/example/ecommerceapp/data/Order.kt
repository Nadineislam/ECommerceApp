package com.example.ecommerceapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextLong
@Parcelize
data class Order(
    val orderStatus: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address, val date: String = SimpleDateFormat(
        "yyyy--MM-dd",
        Locale.ENGLISH
    ).format(Date()),
    val orderId: Long = nextLong(0, 100_000_000_000) + totalPrice.toLong()
):Parcelable {
    constructor() : this("", 0f, emptyList(), Address())
}
