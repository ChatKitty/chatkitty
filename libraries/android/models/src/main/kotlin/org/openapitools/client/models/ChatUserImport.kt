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

import org.openapitools.client.models.FileImport

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param displayName Human readable name of this user. Shown to other users
 * @param guest True if this user was created by a guest user session
 * @param name The unique name used to identify this user across ChatKitty. Also known as username
 * @param properties Custom data associated with this user
 * @param displayPicture 
 * @param idempotencyKey Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended
 */


data class ChatUserImport (

    /* Human readable name of this user. Shown to other users */
    @Json(name = "displayName")
    val displayName: kotlin.String,

    /* True if this user was created by a guest user session */
    @Json(name = "guest")
    val guest: kotlin.Boolean,

    /* The unique name used to identify this user across ChatKitty. Also known as username */
    @Json(name = "name")
    val name: kotlin.String,

    /* Custom data associated with this user */
    @Json(name = "properties")
    val properties: kotlin.collections.Map<kotlin.String, kotlin.Any>,

    @Json(name = "displayPicture")
    val displayPicture: FileImport? = null,

    /* Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended */
    @Json(name = "idempotencyKey")
    val idempotencyKey: kotlin.String? = null

)

