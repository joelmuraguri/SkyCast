package com.joe.data.mappers

interface EntityMapper<Entity, Response> {
    fun asEntity(response: Response, timestamp :Long, locationName : String) : Entity
}

interface LocationsEntityMapper<Entity, Response> {
    fun asEntity(response: Response) : Entity
}

interface DomainMapper<Domain, Entity>{
    fun asDomain(entity: Entity) : Domain
}

interface PresentationMapper<Presentation, Domain>{
    fun asPresentation(domain: Domain) : Presentation
}