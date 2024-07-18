package com.joel.database.mappers

interface EntityMapper<Entity, Response> {
    fun asEntity(response: Response) : Entity
}