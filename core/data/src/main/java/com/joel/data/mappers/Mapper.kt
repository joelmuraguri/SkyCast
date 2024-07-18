package com.joel.data.mappers

interface EntityMapper<Entity, Response> {
    fun asEntity(response: Response) : Entity
}

interface DomainMapper<Domain, Entity>{
    fun asDomain(entity: Entity) : Domain
}