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
 * Image data of this link preview
 *
 * @param source The url source of this image
 */


data class MessageLinkPreviewImageProperties (

    /* The url source of this image */
    @Json(name = "source")
    val source: kotlin.String

)

