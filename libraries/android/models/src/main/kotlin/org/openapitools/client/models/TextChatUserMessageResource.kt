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
import org.openapitools.client.models.MessageLinkProperties
import org.openapitools.client.models.MessageMentionProperties
import org.openapitools.client.models.MessageReactionsSummaryProperties

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param type The type of this message
 * @param id 64-bit integer identifier associated with this resource
 * @param body The text body of this message
 * @param channelId The ID of the channel this message belongs to
 * @param createdTime The time this message was created
 * @param nestedLevel The nested thread level of this message
 * @param properties Custom data associated with this message
 * @param user 
 * @param groupTag Optional string to associate this message with other messages. Can be used to group messages into a gallery
 * @param lastEditedTime The time this message was last edited
 * @param links Link previews in this message
 * @param mentions Mentions in this message
 * @param reactions Reactions to this message
 * @param repliesCount The number of replies to this message
 * @param links 
 */


data class TextChatUserMessageResource (

    /* The type of this message */
    @Json(name = "type")
    val type: TextChatUserMessageResource.Type,

    /* 64-bit integer identifier associated with this resource */
    @Json(name = "id")
    val id: kotlin.Long,

    /* The text body of this message */
    @Json(name = "body")
    val body: kotlin.String,

    /* The ID of the channel this message belongs to */
    @Json(name = "channelId")
    val channelId: kotlin.Long,

    /* The time this message was created */
    @Json(name = "createdTime")
    val createdTime: java.time.OffsetDateTime,

    /* The nested thread level of this message */
    @Json(name = "nestedLevel")
    val nestedLevel: kotlin.Int,

    /* Custom data associated with this message */
    @Json(name = "properties")
    val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>,

    @Json(name = "user")
    val user: ChatUserProperties,

    /* Optional string to associate this message with other messages. Can be used to group messages into a gallery */
    @Json(name = "groupTag")
    val groupTag: kotlin.String? = null,

    /* The time this message was last edited */
    @Json(name = "lastEditedTime")
    val lastEditedTime: java.time.OffsetDateTime? = null,

    /* Link previews in this message */
    @Json(name = "links")
    val links: kotlin.collections.List<MessageLinkProperties>? = null,

    /* Mentions in this message */
    @Json(name = "mentions")
    val mentions: kotlin.collections.List<MessageMentionProperties>? = null,

    /* Reactions to this message */
    @Json(name = "reactions")
    val reactions: kotlin.collections.List<MessageReactionsSummaryProperties>? = null,

    /* The number of replies to this message */
    @Json(name = "repliesCount")
    val repliesCount: kotlin.Long? = null,

    @Json(name = "_links")
    val links: kotlin.collections.Map<kotlin.String, Link>? = null

) {

    /**
     * The type of this message
     *
     * Values: tEXT,fILE,sYSTEMTEXT,sYSTEMFILE
     */
    @JsonClass(generateAdapter = false)
    enum class Type(val value: kotlin.String) {
        @Json(name = "TEXT") tEXT("TEXT"),
        @Json(name = "FILE") fILE("FILE"),
        @Json(name = "SYSTEM_TEXT") sYSTEMTEXT("SYSTEM_TEXT"),
        @Json(name = "SYSTEM_FILE") sYSTEMFILE("SYSTEM_FILE");
    }
}

