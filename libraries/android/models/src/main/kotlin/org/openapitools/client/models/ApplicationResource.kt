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

import org.openapitools.client.models.Link

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 64-bit integer identifier associated with this resource
 * @param createdTime ISO date-time this application was created
 * @param key Primary API key assigned to this application
 * @param properties Custom properties attached to this application
 * @param links 
 */


data class ApplicationResource (

    /* 64-bit integer identifier associated with this resource */
    @Json(name = "id")
    val id: kotlin.Long,

    /* ISO date-time this application was created */
    @Json(name = "createdTime")
    val createdTime: java.time.OffsetDateTime,

    /* Primary API key assigned to this application */
    @Json(name = "key")
    val key: kotlin.String,

    /* Custom properties attached to this application */
    @Json(name = "properties")
    val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>,

    @Json(name = "_links")
    val links: kotlin.collections.Map<kotlin.String, Link>? = null

)

