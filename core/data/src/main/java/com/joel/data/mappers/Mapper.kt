package com.joel.data.mappers

interface EntityMapper<Entity, Response> {
    fun asEntity(response: Response, timestamp :Long, locationName : String) : Entity
}

interface DomainMapper<Domain, Entity>{
    fun asDomain(entity: Entity) : Domain
}

interface PresentationMapper<Presentation, Domain>{
    fun asPresentation(domain: Domain) : Presentation
}