package com.lex.makeupshop.network

import com.squareup.moshi.Json
import java.io.Serializable

data class MakeupItem (
    @Json(name = "name")
    val name: String? = "",
    @Json(name = "brand")
    val brand: String? = "",
    @Json(name = "price")
    val price: Double? = 0.0,
    @Json(name = "description")
    val description: String? = "",
    @Json(name = "price_sign")
    val price_sign: String? = "",
    @Json(name = "image_link")
    val image: String? = "",
    @Json(name = "category")
    var category: String? = "",
    val product_link: String? = "",
    @Json(name = "product_colors")
    val product_colors: List<ProductColors>?
): Serializable


data class ProductColors(
    @Json(name = "hex_value")
    val hex_value: String? = "#FFFFF",
    @Json(name = "colour_name")
    val colour_name: String? = ""
): Serializable
