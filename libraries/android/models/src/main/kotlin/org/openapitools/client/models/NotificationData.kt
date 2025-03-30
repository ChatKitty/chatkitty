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
 * Additional context data of this notification
 *
 * @param type The type of notification that was received. This specifies the schema of the notification data
 */


interface NotificationData {

    /* The type of notification that was received. This specifies the schema of the notification data */
    @Json(name = "type")
    val type: kotlin.String
}

