package com.example.fluent.data.network

const val BASE_URL = "https://mangakart.onrender.com"

object AuthRoutes {
    const val REGISTER = "$BASE_URL/auth/register"
    const val LOGIN = "$BASE_URL/auth/login"
    const val REFRESH_TOKEN = "$BASE_URL/auth/refresh"
}

object CartRoutes {
    const val GET_CART = "$BASE_URL/cart"
    const val ADD_TO_CART = "$BASE_URL/cart/add/"
}

object OrderRoutes {
    const val GET_ORDER = "$BASE_URL/order"
    const val ADD_TO_ORDER = "$BASE_URL/order/add/"
}