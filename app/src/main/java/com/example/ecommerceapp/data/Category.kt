package com.example.ecommerceapp.data

sealed class Category(val category:String){
    object Chair:Category("Chair")
    object Cupboard:Category("Cupboard")
    object Furniture:Category("Furniture")
    object Table:Category("Table")
    object Accessory:Category("Accessory")
}
