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

import org.openapitools.client.models.ChannelImport

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Imports a public channel
 *
 * @param type 
 * @param members List of usernames of members of this channel
 * @param creator Username of the user who created this channel
 * @param idempotencyKey Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended
 * @param properties Custom data associated with this channel
 * @param name The unique name of this channel used to reference the channel. If absent defaults to a random UUID
 * @param displayName Human readable name of this channel shown to users. If absent defaults to the channel name
 */


data class PublicChannelImport (

    @Json(name = "type")
    override val type: kotlin.String,

    /* List of usernames of members of this channel */
    @Json(name = "members")
    override val members: kotlin.collections.List<kotlin.String>,

    /* Username of the user who created this channel */
    @Json(name = "creator")
    override val creator: kotlin.String? = null,

    /* Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended */
    @Json(name = "idempotencyKey")
    override val idempotencyKey: kotlin.String? = null,

    /* Custom data associated with this channel */
    @Json(name = "properties")
    override val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>? = null,

    /* The unique name of this channel used to reference the channel. If absent defaults to a random UUID */
    @Json(name = "name")
    val name: kotlin.String? = null,

    /* Human readable name of this channel shown to users. If absent defaults to the channel name */
    @Json(name = "displayName")
    val displayName: kotlin.String? = null

) : ChannelImport

