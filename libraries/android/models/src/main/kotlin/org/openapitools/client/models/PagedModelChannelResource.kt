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

import org.openapitools.client.models.Link
import org.openapitools.client.models.PageMetadata
import org.openapitools.client.models.PagedModelChannelResourceEmbedded

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param embedded 
 * @param page 
 * @param links 
 */


data class PagedModelChannelResource (

    @Json(name = "_embedded")
    val embedded: PagedModelChannelResourceEmbedded? = null,

    @Json(name = "page")
    val page: PageMetadata? = null,

    @Json(name = "_links")
    val links: kotlin.collections.Map<kotlin.String, Link>? = null

)

