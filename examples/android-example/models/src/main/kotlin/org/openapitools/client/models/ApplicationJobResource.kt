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
 * @param type The type of application job
 * @param id 64-bit integer identifier associated with this resource
 * @param createdTime 
 * @param state The running state of an application job
 * @param endedTime 
 * @param file 
 * @param links 
 */


data class ApplicationJobResource (

    /* The type of application job */
    @Json(name = "type")
    val type: ApplicationJobResource.Type,

    /* 64-bit integer identifier associated with this resource */
    @Json(name = "id")
    val id: kotlin.Long,

    @Json(name = "createdTime")
    val createdTime: java.time.OffsetDateTime,

    /* The running state of an application job */
    @Json(name = "state")
    val state: ApplicationJobResource.State,

    @Json(name = "endedTime")
    val endedTime: java.time.OffsetDateTime? = null,

    @Json(name = "file")
    val file: kotlin.String? = null,

    @Json(name = "_links")
    val links: kotlin.collections.Map<kotlin.String, Link>? = null

) {

    /**
     * The type of application job
     *
     * Values: cHANNELIMPORT,cHANNELMEMBERSIMPORT,mESSAGEIMPORT,uSERIMPORT,mESSAGEANALYTICSEXPORT
     */
    @JsonClass(generateAdapter = false)
    enum class Type(val value: kotlin.String) {
        @Json(name = "CHANNEL_IMPORT") cHANNELIMPORT("CHANNEL_IMPORT"),
        @Json(name = "CHANNEL_MEMBERS_IMPORT") cHANNELMEMBERSIMPORT("CHANNEL_MEMBERS_IMPORT"),
        @Json(name = "MESSAGE_IMPORT") mESSAGEIMPORT("MESSAGE_IMPORT"),
        @Json(name = "USER_IMPORT") uSERIMPORT("USER_IMPORT"),
        @Json(name = "MESSAGE_ANALYTICS_EXPORT") mESSAGEANALYTICSEXPORT("MESSAGE_ANALYTICS_EXPORT");
    }
    /**
     * The running state of an application job
     *
     * Values: pENDING,rUNNING,fINISHED,fAILED
     */
    @JsonClass(generateAdapter = false)
    enum class State(val value: kotlin.String) {
        @Json(name = "PENDING") pENDING("PENDING"),
        @Json(name = "RUNNING") rUNNING("RUNNING"),
        @Json(name = "FINISHED") fINISHED("FINISHED"),
        @Json(name = "FAILED") fAILED("FAILED");
    }
}

