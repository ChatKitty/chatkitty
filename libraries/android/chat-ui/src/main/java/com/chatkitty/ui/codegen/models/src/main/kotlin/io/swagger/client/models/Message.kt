/**
 * Messaging API
 * API for messaging system
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package io.swagger.client.models

import app.dvkyun.flexhybridand.FlexType
import io.swagger.client.models.User

/**
 * 
 * @param body 
 * @param createdTime 
 * @param user 
 */
data class Message (

    val body: kotlin.String? = null,
    val createdTime: kotlin.String? = null,
    val user: User? = null
) : FlexType{
}