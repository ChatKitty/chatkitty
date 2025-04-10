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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 64-bit integer identifier associated with this resource
 * @param args The function arguments passed into this function for this invocation
 * @param createdTime The ISO date-time of this invocation when the function was called
 * @param result The result of this invocation when it completed
 * @param status The running status of this invocation
 * @param versionNumber The version number of this function version when this invocation occured
 * @param links 
 */


data class ChatFunctionInvocationResource (

    /* 64-bit integer identifier associated with this resource */
    @Json(name = "id")
    val id: kotlin.Long,

    /* The function arguments passed into this function for this invocation */
    @Json(name = "args")
    val args: kotlin.collections.Map<kotlin.String, kotlin.Any>,

    /* The ISO date-time of this invocation when the function was called */
    @Json(name = "createdTime")
    val createdTime: java.time.OffsetDateTime,

    /* The result of this invocation when it completed */
    @Json(name = "result")
    val result: kotlin.collections.Map<kotlin.String, kotlin.Any>,

    /* The running status of this invocation */
    @Json(name = "status")
    val status: ChatFunctionInvocationResource.Status,

    /* The version number of this function version when this invocation occured */
    @Json(name = "versionNumber")
    val versionNumber: kotlin.Long,

    @Json(name = "_links")
    val links: kotlin.collections.Map<kotlin.String, Link>? = null

) {

    /**
     * The running status of this invocation
     *
     * Values: rUNNING,sUCCEEDED,fAILED
     */
    @JsonClass(generateAdapter = false)
    enum class Status(val value: kotlin.String) {
        @Json(name = "RUNNING") rUNNING("RUNNING"),
        @Json(name = "SUCCEEDED") sUCCEEDED("SUCCEEDED"),
        @Json(name = "FAILED") fAILED("FAILED");
    }
}

