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

import org.openapitools.client.models.ChatUserProperties
import org.openapitools.client.models.Link

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param type Custom type of this event
 * @param properties Custom data associated with this event
 * @param user 
 * @param links 
 */


data class ChannelGenericEventResource (

    /* Custom type of this event */
    @Json(name = "type")
    val type: kotlin.String,

    /* Custom data associated with this event */
    @Json(name = "properties")
    val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>,

    @Json(name = "user")
    val user: ChatUserProperties? = null,

    @Json(name = "_links")
    val links: kotlin.collections.Map<kotlin.String, Link>? = null

)

