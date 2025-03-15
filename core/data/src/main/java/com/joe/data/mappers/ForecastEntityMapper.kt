package com.joe.data.mappers

import com.joe.database.entity.DailyEntity
import com.joe.database.entity.HourlyEntity
import com.joe.database.entity.WeatherEntity
import com.joe.models.ForecastInfo
import com.joe.models.Location
import com.joe.models.WeatherDomain
import com.joe.models.WeatherType
import com.joe.network.models.ForecastResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ForecastEntityMapper : EntityMapper<WeatherEntity, ForecastResponse>{
    override fun asEntity(response: ForecastResponse, timestamp : Long, locationName : String): WeatherEntity {
        val dailyForecast = response.daily.toDailyForecast()
        val hourlyForecast = response.hourly.toHourlyForecast()
        return WeatherEntity(
            locationName = locationName,
            longitude = response.longitude,
            latitude = response.latitude,
            dailyForecast = Json.encodeToString(dailyForecast),
            hourlyForecast = Json.encodeToString(hourlyForecast),
            timeStamp = timestamp
        )
    }
}

object ForecastDomainMapper : DomainMapper<WeatherDomain, WeatherEntity>{
    override fun asDomain(entity: WeatherEntity): WeatherDomain {
        val dailyForecast = Json.decodeFromString<List<DailyEntity>>(entity.dailyForecast)
        val hourlyForecast = Json.decodeFromString<List<HourlyEntity>>(entity.hourlyForecast)
        return WeatherDomain(
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

fun DailyEntity.toDomain(): WeatherDomain.Daily {
    return WeatherDomain.Daily(
        uv = this.uv,
        date = this.date,
        weather = this.weather,
        lowTemp = this.lowTemp,
        highTemp = this.highTemp,
        sunrise = this.sunrise,
        sunset = this.sunset
    )
}

fun HourlyEntity.toDomain(): WeatherDomain.Hourly {
    return WeatherDomain.Hourly(
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

object ForecastPresentationMapper : PresentationMapper<ForecastInfo, WeatherDomain>{
    override fun asPresentation(domain: WeatherDomain): ForecastInfo {
        return ForecastInfo(
            location = domain.location,
            dailyForeCast = domain.dailyForeCast.map { it.toPresentation() },
            hourlyForecast = domain.hourlyForecast.map { it.toPresentation() },
            name = domain.name
        )
    }
}

fun WeatherDomain.Hourly.toPresentation() : ForecastInfo.HourlyForecast {
    return ForecastInfo.HourlyForecast(
        time = this.time,
        temp = this.temp,
        weather = WeatherType.fromWMO(this.weather),
        wind = this.wind,
        pressure = this.pressure,
        visibility = this.visibility,
        isDay = this.isDay,
        humidity = this.humidity
    )
}

fun WeatherDomain.Daily.toPresentation() : ForecastInfo.DailyForecast{
    return ForecastInfo.DailyForecast(
        uv = this.uv,
        date = this.date,
        weather = WeatherType.fromWMO(this.weather),
        lowTemp = this.lowTemp,
        highTemp = this.highTemp,
        sunrise = this.sunrise,
        sunset = this.sunset
    )
}

fun ForecastResponse.asEntity(timestamp: Long, locationName: String): WeatherEntity {
    return ForecastEntityMapper.asEntity(this, timestamp, locationName)
}
fun WeatherEntity.asDomain(): WeatherDomain {
    return ForecastDomainMapper.asDomain(this)
}

fun WeatherDomain.asPresentation(): ForecastInfo{
    return ForecastPresentationMapper.asPresentation(this)
}

