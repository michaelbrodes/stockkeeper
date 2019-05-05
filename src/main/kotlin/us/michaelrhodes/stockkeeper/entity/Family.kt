package us.michaelrhodes.stockkeeper.entity

import java.time.OffsetDateTime
import java.util.*

data class Family (val uuid: UUID, 
                   val name: String, 
                   val createdAt: OffsetDateTime, 
                   val updatedAt: OffsetDateTime, 
                   val deletedAt: OffsetDateTime? = null)