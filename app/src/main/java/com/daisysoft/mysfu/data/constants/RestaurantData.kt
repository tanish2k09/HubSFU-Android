package com.daisysoft.mysfu.data.constants

import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.data.model.RestaurantInfo

sealed class RestaurantData {
    companion object {
        val restaurants = arrayListOf(
            RestaurantInfo(
                "A&W",
                "University High Street",
                "9am - 10pm",
                "$10 - $20",
                R.drawable.a_w,
                "Fast Food",
                R.color.a_w,
                R.color.a_w_button
            ),
            RestaurantInfo(
                "Bamboo Garden",
                "University High Street",
                "9am - 10pm",
                "$20 - $30",
                R.drawable.bamboo,
                "Chinese",
                R.color.bamboo,
                R.color.bamboo_button
            ),
            RestaurantInfo(
                "Pizza Hut",
                "University High Street",
                "9am - 10pm",
                "$20 - $30",
                R.drawable.pizza,
                "Chinese",
                R.color.pizza,
                R.color.pizza_button
            )
        )
    }
}
