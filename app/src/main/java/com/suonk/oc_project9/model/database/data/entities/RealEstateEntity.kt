package com.suonk.oc_project9.model.database.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_estate")
data class RealEstateEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "living_space") val livingSpace: Double,
    @ColumnInfo(name = "number_total_rooms") val numberRooms: Int,
    @ColumnInfo(name = "number_bedroom") val numberBedroom: Int,
    @ColumnInfo(name = "number_bathroom") val numberBathroom: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "full_address") val fullAddress: String,
    @ColumnInfo(name = "postal_code") val postalCode: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "street_name") val streetName: String,
    @ColumnInfo(name = "grid_zone") val gridZone: String,
    @ColumnInfo(name = "point_of_interest") val pointOfInterest: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "entry_date") val entryDate: Long,
    @ColumnInfo(name = "sale_date") val saleDate: Long?,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "agent_in_charge_id") val agentInChargeId: Long?,
)