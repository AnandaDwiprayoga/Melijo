package com.pasukanlangit.id.melijo.presentation.main.home.supplier

import com.pasukanlangit.id.melijo.R

data class DataMenuSupplier (
    val icon: Int,
    val title: String
)

fun getDataMenuSupplier() : List<DataMenuSupplier> = listOf(
    DataMenuSupplier(
        R.drawable.icon_terlaris,
        "Terlaris"
    ),
    DataMenuSupplier(
        R.drawable.icon_terbaru,
        "Terbaru"
    ),
    DataMenuSupplier(
        R.drawable.icon_promo,
        "Promo"
    ),
    DataMenuSupplier(
        R.drawable.icon_sayur,
        "Sayur"
    ),
    DataMenuSupplier(
        R.drawable.icon_bumbu,
        "Bumbu"
    ),
    DataMenuSupplier(
        R.drawable.icon_sembako,
        "Sembako"
    ),
    DataMenuSupplier(
        R.drawable.icon_buah,
        "Buah"
    ),
    DataMenuSupplier(
        R.drawable.icon_lainnya,
        "Lainnya"
    )
)