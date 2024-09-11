package com.joel.data.mappers

import com.joel.database.entity.FavouritePlace
import com.joel.models.Location
import com.joel.models.Place
import com.joel.network.models.GeoCodingResponse

object FavouriteEntityMapper : EntityMapper<FavouritePlace, GeoCodingResponse.Place>{
//    override fun asEntity(response: GeoCodingResponse.Place): FavouritePlace {
//        return FavouritePlace(
//            longitude = 0.0,
//            latitude = 0.0,
//            locationName = response.name,
//            country = response.country,
//            countryCode = response.countryCode,
//            timeZone = response.timezone
//        )
//    }

    override fun asEntity(response: GeoCodingResponse.Place, timestamp: Long): FavouritePlace {
        TODO("Not yet implemented")
    }

}
object FavouriteDomainMapper : DomainMapper<Place, FavouritePlace>{
    override fun asDomain(entity: FavouritePlace): Place {
        return Place(
            location = Location(entity.longitude, entity.latitude),
            name = entity.locationName,
            country = entity.country,
            countryCode = entity.countryCode,
            timeZone = entity.timeZone
        )
    }
}

//fun GeoCodingResponse.Place.asEntity(): FavouritePlace {
//    return FavouriteEntityMapper.asEntity(this)
//}
fun FavouritePlace.asDomain(): Place {
    return FavouriteDomainMapper.asDomain(this)
}