/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package org.openapitools.client.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param propertySize 
 * @param next 
 * @param start 
 */


data class CursorPageMetadata (

    @Json(name = "size")
    val propertySize: kotlin.Long,

    @Json(name = "next")
    val next: kotlin.String? = null,

    @Json(name = "start")
    val start: kotlin.String? = null

)

