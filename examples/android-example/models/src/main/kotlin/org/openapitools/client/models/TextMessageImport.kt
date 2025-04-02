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
 * @param body The text body of this message
 * @param channelId ID of the channel this message belongs to
 * @param groupTag Tag identifying the message group this message belongs to
 * @param idempotencyKey Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended
 * @param properties Map of custom data attached to this message
 */


data class TextMessageImport (

    /* The text body of this message */
    @Json(name = "body")
    val body: kotlin.String,

    /* ID of the channel this message belongs to */
    @Json(name = "channelId")
    val channelId: kotlin.Long,

    /* Tag identifying the message group this message belongs to */
    @Json(name = "groupTag")
    val groupTag: kotlin.String? = null,

    /* Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended */
    @Json(name = "idempotencyKey")
    val idempotencyKey: kotlin.String? = null,

    /* Map of custom data attached to this message */
    @Json(name = "properties")
    val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>? = null

)

