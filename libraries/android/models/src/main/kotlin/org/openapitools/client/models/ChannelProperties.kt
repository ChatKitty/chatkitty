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
import org.openapitools.client.models.MessageProperties

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param type The type of this channel
 * @param id 64-bit integer identifier associated with this resource
 * @param createdTime The ISO date-time this channel was created
 * @param properties Custom data associated with this channel
 * @param creator 
 * @param lastReceivedMessage 
 */


interface ChannelProperties {

    /* The type of this channel */
    @Json(name = "type")
    val type: ChannelProperties.Type
    /* 64-bit integer identifier associated with this resource */
    @Json(name = "id")
    val id: kotlin.Long
    /* The ISO date-time this channel was created */
    @Json(name = "createdTime")
    val createdTime: java.time.OffsetDateTime
    /* Custom data associated with this channel */
    @Json(name = "properties")
    val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>
    @Json(name = "creator")
    val creator: ChatUserProperties?
    @Json(name = "lastReceivedMessage")
    val lastReceivedMessage: MessageProperties?
    /**
     * The type of this channel
     *
     * Values: dIRECT,pUBLIC,pRIVATE
     */
    @JsonClass(generateAdapter = false)
    enum class Type(val value: kotlin.String) {
        @Json(name = "DIRECT") dIRECT("DIRECT"),
        @Json(name = "PUBLIC") pUBLIC("PUBLIC"),
        @Json(name = "PRIVATE") pRIVATE("PRIVATE");
    }
}

