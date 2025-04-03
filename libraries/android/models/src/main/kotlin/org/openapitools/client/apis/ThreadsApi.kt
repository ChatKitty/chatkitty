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

package org.openapitools.client.apis

import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.HttpUrl

import org.openapitools.client.models.ApiError
import org.openapitools.client.models.AuthenticationError
import org.openapitools.client.models.CreateDelegatedReplyThreadKeystrokesResource
import org.openapitools.client.models.CursorPagedModelMessageResource
import org.openapitools.client.models.MessageResource
import org.openapitools.client.models.ReplyThreadKeystrokesResource
import org.openapitools.client.models.ReplyThreadResource
import org.openapitools.client.models.SendChannelMessageRequest
import org.openapitools.client.models.SendThreadMessageRequest

import com.squareup.moshi.Json

import org.openapitools.client.infrastructure.ApiClient
import org.openapitools.client.infrastructure.ApiResponse
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ClientError
import org.openapitools.client.infrastructure.ServerException
import org.openapitools.client.infrastructure.ServerError
import org.openapitools.client.infrastructure.MultiValueMap
import org.openapitools.client.infrastructure.PartConfig
import org.openapitools.client.infrastructure.RequestConfig
import org.openapitools.client.infrastructure.RequestMethod
import org.openapitools.client.infrastructure.ResponseType
import org.openapitools.client.infrastructure.Success
import org.openapitools.client.infrastructure.toMultiValue

class ThreadsApi(basePath: kotlin.String = defaultBasePath, client: OkHttpClient = ApiClient.defaultClient) : ApiClient(basePath, client) {
    companion object {
        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(ApiClient.baseUrlKey, "https://api.chatkitty.com")
        }
    }

    /**
     * enum for parameter relation
     */
     enum class RelationListThreadMessages(val value: kotlin.String) {
         @Json(name = "SELF") sELF("SELF"),
         @Json(name = "PREVIOUS") pREVIOUS("PREVIOUS"),
         @Json(name = "NEXT") nEXT("NEXT")
     }

    /**
     * List reply thread messages
     * Returns a page of replies sent in this thread
     * @param id 
     * @param size The size of the page to be returned (optional)
     * @param start Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages (optional)
     * @param next Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages (optional)
     * @param relation Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages (optional)
     * @return CursorPagedModelMessageResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun listThreadMessages(id: kotlin.Long, size: kotlin.Int? = null, start: kotlin.Long? = null, next: kotlin.Long? = null, relation: RelationListThreadMessages? = null) : CursorPagedModelMessageResource {
        val localVarResponse = listThreadMessagesWithHttpInfo(id = id, size = size, start = start, next = next, relation = relation)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as CursorPagedModelMessageResource
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()} ${localVarError.body}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * List reply thread messages
     * Returns a page of replies sent in this thread
     * @param id 
     * @param size The size of the page to be returned (optional)
     * @param start Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages (optional)
     * @param next Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages (optional)
     * @param relation Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages (optional)
     * @return ApiResponse<CursorPagedModelMessageResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun listThreadMessagesWithHttpInfo(id: kotlin.Long, size: kotlin.Int?, start: kotlin.Long?, next: kotlin.Long?, relation: RelationListThreadMessages?) : ApiResponse<CursorPagedModelMessageResource?> {
        val localVariableConfig = listThreadMessagesRequestConfig(id = id, size = size, start = start, next = next, relation = relation)

        return request<Unit, CursorPagedModelMessageResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation listThreadMessages
     *
     * @param id 
     * @param size The size of the page to be returned (optional)
     * @param start Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages (optional)
     * @param next Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages (optional)
     * @param relation Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages (optional)
     * @return RequestConfig
     */
    fun listThreadMessagesRequestConfig(id: kotlin.Long, size: kotlin.Int?, start: kotlin.Long?, next: kotlin.Long?, relation: RelationListThreadMessages?) : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf<kotlin.String, kotlin.collections.List<kotlin.String>>()
            .apply {
                if (size != null) {
                    put("size", listOf(size.toString()))
                }
                if (start != null) {
                    put("start", listOf(start.toString()))
                }
                if (next != null) {
                    put("next", listOf(next.toString()))
                }
                if (relation != null) {
                    put("relation", listOf(relation.value))
                }
            }
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/v1/threads/{id}/messages".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }

    /**
     * Retrieve a thread
     * Returns a thread by ID
     * @param id Reply thread ID
     * @return ReplyThreadResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun retrieveThread(id: kotlin.Long) : ReplyThreadResource {
        val localVarResponse = retrieveThreadWithHttpInfo(id = id)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as ReplyThreadResource
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()} ${localVarError.body}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * Retrieve a thread
     * Returns a thread by ID
     * @param id Reply thread ID
     * @return ApiResponse<ReplyThreadResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun retrieveThreadWithHttpInfo(id: kotlin.Long) : ApiResponse<ReplyThreadResource?> {
        val localVariableConfig = retrieveThreadRequestConfig(id = id)

        return request<Unit, ReplyThreadResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation retrieveThread
     *
     * @param id Reply thread ID
     * @return RequestConfig
     */
    fun retrieveThreadRequestConfig(id: kotlin.Long) : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/v1/threads/{id}".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }

    /**
     * Send thread keystrokes
     * Sends keystrokes in this thread on behalf of a user
     * @param id 
     * @param createDelegatedReplyThreadKeystrokesResource 
     * @return ReplyThreadKeystrokesResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun sendThreadKeystrokes(id: kotlin.Long, createDelegatedReplyThreadKeystrokesResource: CreateDelegatedReplyThreadKeystrokesResource) : ReplyThreadKeystrokesResource {
        val localVarResponse = sendThreadKeystrokesWithHttpInfo(id = id, createDelegatedReplyThreadKeystrokesResource = createDelegatedReplyThreadKeystrokesResource)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as ReplyThreadKeystrokesResource
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()} ${localVarError.body}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * Send thread keystrokes
     * Sends keystrokes in this thread on behalf of a user
     * @param id 
     * @param createDelegatedReplyThreadKeystrokesResource 
     * @return ApiResponse<ReplyThreadKeystrokesResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun sendThreadKeystrokesWithHttpInfo(id: kotlin.Long, createDelegatedReplyThreadKeystrokesResource: CreateDelegatedReplyThreadKeystrokesResource) : ApiResponse<ReplyThreadKeystrokesResource?> {
        val localVariableConfig = sendThreadKeystrokesRequestConfig(id = id, createDelegatedReplyThreadKeystrokesResource = createDelegatedReplyThreadKeystrokesResource)

        return request<CreateDelegatedReplyThreadKeystrokesResource, ReplyThreadKeystrokesResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation sendThreadKeystrokes
     *
     * @param id 
     * @param createDelegatedReplyThreadKeystrokesResource 
     * @return RequestConfig
     */
    fun sendThreadKeystrokesRequestConfig(id: kotlin.Long, createDelegatedReplyThreadKeystrokesResource: CreateDelegatedReplyThreadKeystrokesResource) : RequestConfig<CreateDelegatedReplyThreadKeystrokesResource> {
        val localVariableBody = createDelegatedReplyThreadKeystrokesResource
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Content-Type"] = "application/json"
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/v1/threads/{id}/keystrokes".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }

    /**
     * Send a reply thread message
     * Sends a reply message in this thread as the system or on behalf of a user
     * @param id 
     * @param sendChannelMessageRequest 
     * @return MessageResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun sendThreadMessage(id: kotlin.Long, sendChannelMessageRequest: SendChannelMessageRequest) : MessageResource {
        val localVarResponse = sendThreadMessageWithHttpInfo(id = id, sendChannelMessageRequest = sendChannelMessageRequest)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as MessageResource
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()} ${localVarError.body}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * Send a reply thread message
     * Sends a reply message in this thread as the system or on behalf of a user
     * @param id 
     * @param sendChannelMessageRequest 
     * @return ApiResponse<MessageResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun sendThreadMessageWithHttpInfo(id: kotlin.Long, sendChannelMessageRequest: SendChannelMessageRequest) : ApiResponse<MessageResource?> {
        val localVariableConfig = sendThreadMessageRequestConfig(id = id, sendChannelMessageRequest = sendChannelMessageRequest)

        return request<SendChannelMessageRequest, MessageResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation sendThreadMessage
     *
     * @param id 
     * @param sendChannelMessageRequest 
     * @return RequestConfig
     */
    fun sendThreadMessageRequestConfig(id: kotlin.Long, sendChannelMessageRequest: SendChannelMessageRequest) : RequestConfig<SendChannelMessageRequest> {
        val localVariableBody = sendChannelMessageRequest
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Content-Type"] = "application/json"
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/v1/threads/{id}/messages".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }


    private fun encodeURIComponent(uriComponent: kotlin.String): kotlin.String =
        HttpUrl.Builder().scheme("http").host("localhost").addPathSegment(uriComponent).build().encodedPathSegments[0]
}
