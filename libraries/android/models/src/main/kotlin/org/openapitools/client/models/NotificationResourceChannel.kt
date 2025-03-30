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
import org.openapitools.client.models.DirectChannelProperties
import org.openapitools.client.models.MessageProperties
import org.openapitools.client.models.PrivateChannelProperties
import org.openapitools.client.models.PublicChannelProperties

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param type The type of this channel
 * @param id 64-bit integer identifier associated with this resource
 * @param createdTime The ISO date-time this channel was created
 * @param members The members of this channel. Present if this is a direct channel. For other channel types, use [**list channel messages**](https://chatkitty.com/docs/api/reference#tag/channels/operation/list-channel-messages)
 * @param properties Custom data associated with this channel
 * @param displayName Human readable name of this channel shown to users
 * @param name The unique name of this channel used to reference the channel
 * @param creator 
 * @param lastReceivedMessage 
 */


interface NotificationResourceChannel {

    /* The type of this channel */
    @Json(name = "type")
    val type: NotificationResourceChannel.Type
    /* 64-bit integer identifier associated with this resource */
    @Json(name = "id")
    val id: kotlin.Long
    /* The ISO date-time this channel was created */
    @Json(name = "createdTime")
    val createdTime: java.time.OffsetDateTime
    /* The members of this channel. Present if this is a direct channel. For other channel types, use [**list channel messages**](https://chatkitty.com/docs/api/reference#tag/channels/operation/list-channel-messages) */
    @Json(name = "members")
    val members: kotlin.collections.List<ChatUserProperties>
    /* Custom data associated with this channel */
    @Json(name = "properties")
    val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>
    /* Human readable name of this channel shown to users */
    @Json(name = "displayName")
    val displayName: kotlin.String
    /* The unique name of this channel used to reference the channel */
    @Json(name = "name")
    val name: kotlin.String
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

