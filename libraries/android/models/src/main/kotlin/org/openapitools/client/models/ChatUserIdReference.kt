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
 * Reference to a user by ID
 *
 * @param id User ID associated with this user 
 */


data class ChatUserIdReference (

    /* User ID associated with this user  */
    @Json(name = "id")
    val id: kotlin.Long

)

