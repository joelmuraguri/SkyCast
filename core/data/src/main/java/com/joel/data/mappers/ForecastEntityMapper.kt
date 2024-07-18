package com.joel.data.mappers

import com.joel.database.entity.DailyEntity
import com.joel.database.entity.HourlyEntity
import com.joel.database.entity.WeatherEntity
import com.joel.models.Location
import com.joel.models.Weather
import com.joel.network.models.ForecastResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ForecastEntityMapper : EntityMapper<WeatherEntity, ForecastResponse>{
    override fun asEntity(response: ForecastResponse): WeatherEntity {
        val dailyForecast = response.daily.toDailyForecast()
        val hourlyForecast = response.hourly.toHourlyForecast()
        return WeatherEntity(
            locationName = "",
            longitude = response.longitude,
            latitude = response.latitude,
            dailyForecast = Json.encodeToString(dailyForecast),
            hourlyForecast = Json.encodeToString(hourlyForecast)
        )
    }
}

object ForecastDomainMapper : DomainMapper<Weather, WeatherEntity>{
    override fun asDomain(entity: WeatherEntity): Weather {
        val dailyForecast = Json.decodeFromString<List<DailyEntity>>(entity.dailyForecast)
        val hourlyForecast = Json.decodeFromString<List<HourlyEntity>>(entity.hourlyForecast)
        return Weather(
            location = Location(entity.longitude, entity.latitude),
            dailyForeCast = dailyForecast.map { it.toDomain() },
            hourlyForecast = hourlyForecast.map { it.toDomain() },
            name = entity.locationName
        )
    }
}

fun ForecastResponse.Daily.toDailyForecast(): List<DailyEntity> {
    return this.time.mapIndexed { index, time ->
        DailyEntity(
            date = time,
            weather = this.weatherCode.getOrNull(index) ?: 0,
            lowTemp = this.temperatureMin.getOrNull(index)?.toInt() ?: 0,
            highTemp = this.temperatureMax.getOrNull(index)?.toInt() ?: 0,
            uv = this.uvIndex.getOrNull(index)?.toInt() ?: 0,
            sunrise = this.sunrise.getOrNull(index)?.toString() ?: "",
            sunset = this.sunset.getOrNull(index)?.toString() ?: ""
        )
    }
}

fun ForecastResponse.Hourly.toHourlyForecast(): List<HourlyEntity> {
    return this.time.mapIndexed { index, time ->
        HourlyEntity(
            time = time,
            temp = this.apparentTemperature.getOrNull(index)?.toString() ?: "0",
            weather = this.weatherCode.getOrNull(index) ?: 0,
            wind = this.windSpeed.getOrNull(index)?.toInt() ?: 0,
            pressure = this.surfacePressure.getOrNull(index)?.toInt() ?: 0,
            visibility = this.weatherCode.getOrNull(index)?.toInt() ?: 0,
            isDay = this.isDay.getOrNull(index) ?: 0,
            humidity = this.humidity.getOrNull(index) ?: 0,
        )
    }
}

fun DailyEntity.toDomain(): Weather.Daily {
    return Weather.Daily(
        uv = this.uv,
        date = this.date,
        weather = this.weather,
        lowTemp = this.lowTemp,
        highTemp = this.highTemp,
        sunrise = this.sunrise,
        sunset = this.sunset
    )
}

fun HourlyEntity.toDomain(): Weather.Hourly {
    return Weather.Hourly(
        time = this.time,
        temp = this.temp,
        weather = this.weather,
        wind = this.wind,
        pressure = this.pressure,
        visibility = this.visibility,
        isDay = this.isDay,
        humidity = this.humidity
    )
}

fun ForecastResponse.asEntity(): WeatherEntity {
    return ForecastEntityMapper.asEntity(this)
}
fun WeatherEntity.asDomain(): Weather {
    return ForecastDomainMapper.asDomain(this)
}


