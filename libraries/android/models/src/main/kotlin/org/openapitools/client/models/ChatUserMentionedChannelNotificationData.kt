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

import org.openapitools.client.models.ChannelResource
import org.openapitools.client.models.TextMessageResource

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Sent when a user mentions a channel in a message
 *
 * @param type 
 * @param channelId The ID of the channel the mentioning message was sent. Deprecated: Use the channel property of this notification
 * @param mentionedChannel 
 * @param message 
 */


data class ChatUserMentionedChannelNotificationData (

    @Json(name = "type")
    val type: kotlin.String,

    /* The ID of the channel the mentioning message was sent. Deprecated: Use the channel property of this notification */
    @Json(name = "channelId")
    @Deprecated(message = "This property is deprecated.")
    val channelId: kotlin.Long,

    @Json(name = "mentionedChannel")
    val mentionedChannel: ChannelResource,

    @Json(name = "message")
    val message: TextMessageResource

)

