package com.joel.models

enum class GridItemType {
    Temperature,
    UVIndex,
    Humidity,
    Wind,
    Precipitation,
    Pressure
}

data class GridItem(
    val type: GridItemType,
    val data: Any? = null // Use this to pass specific data for each type
)