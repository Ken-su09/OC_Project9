package com.suonk.oc_project9.content_provider

import android.net.Uri

object RealEstateContract {
    private const val AUTHORITY = "com.suonk.oc_project9.content_provider.RealEstateContentProvider"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/properties")

    const val TABLE_NAME = "real_estate"
    const val COLUMN_ID = "id"
    const val COLUMN_TYPE = "type"
    const val COLUMN_PRICE = "price"
    const val COLUMN_LIVING_SPACE = "living_space"
    const val COLUMN_ROOMS = "number_total_rooms"
    const val COLUMN_BEDROOMS = "number_bedroom"
    const val COLUMN_BATHROOMS = "number_bathroom"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_POSTAL_CODE = "postal_code"
    const val COLUMN_STATE = "state"
    const val COLUMN_CITY = "city"
    const val COLUMN_STREET_NAME = "street_name"
    const val COLUMN_GRID_ZONE = "grid_zone"
    const val COLUMN_STATUS = "status"
    const val COLUMN_ENTRY_DATE = "entry_date"
    const val COLUMN_SALE_DATE = "sale_date"
    const val COLUMN_LATITUDE = "latitude"
    const val COLUMN_LONGITUDE = "longitude"
    const val COLUMN_AGENT_IN_CHARGE_ID = "agent_in_charge_id"
}