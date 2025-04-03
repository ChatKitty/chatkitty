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
 * External file properties
 *
 * @param contentType The mime type of this file
 * @param name The file name
 * @param propertySize The size of this file in bytes
 * @param url The file URL
 * @param idempotencyKey Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended
 */


data class FileImport (

    /* The mime type of this file */
    @Json(name = "contentType")
    val contentType: kotlin.String,

    /* The file name */
    @Json(name = "name")
    val name: kotlin.String,

    /* The size of this file in bytes */
    @Json(name = "size")
    val propertySize: kotlin.Long,

    /* The file URL */
    @Json(name = "url")
    val url: kotlin.String,

    /* Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended */
    @Json(name = "idempotencyKey")
    val idempotencyKey: kotlin.String? = null

)

