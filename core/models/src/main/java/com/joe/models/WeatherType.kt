package com.joe.models

import androidx.annotation.DrawableRes

sealed class WeatherType(
    val weatherDesc: String,
    @DrawableRes val iconRes: Int,
    val code: Int
) {
    data object ClearSky : WeatherType("Clear sky", R.drawable.ic_sunny, 0)
    data object MainlyClear : WeatherType("Mainly clear", R.drawable.ic_cloudy, 1)
    data object PartlyCloudy : WeatherType("Partly cloudy", R.drawable.ic_cloudy, 2)
    data object Overcast : WeatherType("Overcast", R.drawable.ic_cloudy, 3)
    data object Foggy : WeatherType("Foggy", R.drawable.ic_very_cloudy, 45)
    data object DepositingRimeFog : WeatherType("Depositing rime fog", R.drawable.ic_very_cloudy, 48)

    data object LightDrizzle : WeatherType("Light drizzle", R.drawable.ic_rainshower, 51)
    data object ModerateDrizzle : WeatherType("Moderate drizzle", R.drawable.ic_rainshower, 53)
    data object DenseDrizzle : WeatherType("Dense drizzle", R.drawable.ic_rainshower, 55)

    data object LightFreezingDrizzle : WeatherType("Slight freezing drizzle", R.drawable.ic_snowyrainy, 56)
    data object DenseFreezingDrizzle : WeatherType("Dense freezing drizzle", R.drawable.ic_snowyrainy, 57)

    data object SlightRain : WeatherType("Slight rain", R.drawable.ic_rainy, 61)
    data object ModerateRain : WeatherType("Moderate rain", R.drawable.ic_rainy, 63)
    data object HeavyRain : WeatherType("Heavy rain", R.drawable.ic_rainy, 65)

    data object LightFreezingRain : WeatherType("Light freezing rain", R.drawable.ic_snowyrainy, 66)
    data object HeavyFreezingRain : WeatherType("Heavy freezing rain", R.drawable.ic_snowyrainy, 67)

    data object SlightSnowFall : WeatherType("Slight snow fall", R.drawable.ic_snowy, 71)
    data object ModerateSnowFall : WeatherType("Moderate snow fall", R.drawable.ic_heavysnow, 73)
    data object HeavySnowFall : WeatherType("Heavy snow fall", R.drawable.ic_heavysnow, 75)

    data object SnowGrains : WeatherType("Snow grains", R.drawable.ic_heavysnow, 77)

    data object SlightRainShowers : WeatherType("Slight rain showers", R.drawable.ic_rainshower, 80)
    data object ModerateRainShowers : WeatherType("Moderate rain showers", R.drawable.ic_rainshower, 81)
    data object ViolentRainShowers : WeatherType("Violent rain showers", R.drawable.ic_rainshower, 82)

    data object SlightSnowShowers : WeatherType("Light snow showers", R.drawable.ic_snowy, 85)
    data object HeavySnowShowers : WeatherType("Heavy snow showers", R.drawable.ic_snowy, 86)

    data object ModerateThunderstorm : WeatherType("Moderate thunderstorm", R.drawable.ic_thunder, 95)
    data object SlightHailThunderstorm : WeatherType("Thunderstorm with slight hail", R.drawable.ic_rainythunder, 96)
    data object HeavyHailThunderstorm : WeatherType("Thunderstorm with heavy hail", R.drawable.ic_rainythunder, 99)

    companion object {
        fun fromWMO(code: Int): WeatherType {
            return when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingRain
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
        }
    }
}
