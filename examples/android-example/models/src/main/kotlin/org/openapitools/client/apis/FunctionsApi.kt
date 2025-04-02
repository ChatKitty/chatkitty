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
import org.openapitools.client.models.ChatFunctionResource
import org.openapitools.client.models.ChatFunctionVersionResource
import org.openapitools.client.models.CreateChatFunctionVersionResource
import org.openapitools.client.models.PagedModelChatFunctionInvocationResource
import org.openapitools.client.models.PagedModelChatFunctionVersionResource

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

class FunctionsApi(basePath: kotlin.String = defaultBasePath, client: OkHttpClient = ApiClient.defaultClient) : ApiClient(basePath, client) {
    companion object {
        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(ApiClient.baseUrlKey, "https://api.chatkitty.com")
        }
    }

    /**
     * Create a chat function version
     * Creates a new version of this chat function
     * @param id Chat function ID
     * @param createChatFunctionVersionResource 
     * @return ChatFunctionVersionResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun createFunctionVersion(id: kotlin.Long, createChatFunctionVersionResource: CreateChatFunctionVersionResource) : ChatFunctionVersionResource {
        val localVarResponse = createFunctionVersionWithHttpInfo(id = id, createChatFunctionVersionResource = createChatFunctionVersionResource)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as ChatFunctionVersionResource
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
     * Create a chat function version
     * Creates a new version of this chat function
     * @param id Chat function ID
     * @param createChatFunctionVersionResource 
     * @return ApiResponse<ChatFunctionVersionResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun createFunctionVersionWithHttpInfo(id: kotlin.Long, createChatFunctionVersionResource: CreateChatFunctionVersionResource) : ApiResponse<ChatFunctionVersionResource?> {
        val localVariableConfig = createFunctionVersionRequestConfig(id = id, createChatFunctionVersionResource = createChatFunctionVersionResource)

        return request<CreateChatFunctionVersionResource, ChatFunctionVersionResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation createFunctionVersion
     *
     * @param id Chat function ID
     * @param createChatFunctionVersionResource 
     * @return RequestConfig
     */
    fun createFunctionVersionRequestConfig(id: kotlin.Long, createChatFunctionVersionResource: CreateChatFunctionVersionResource) : RequestConfig<CreateChatFunctionVersionResource> {
        val localVariableBody = createChatFunctionVersionResource
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Content-Type"] = "application/json"
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/v1/functions/{id}/versions".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }

    /**
     * List chat function invocations
     * Returns a page of invocations of this chat function. A log of previous runs of the function
     * @param id Chat function ID
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 25)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return PagedModelChatFunctionInvocationResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun listFunctionInvocations(id: kotlin.Long, page: kotlin.Int? = 0, size: kotlin.Int? = 25, sort: kotlin.collections.List<kotlin.String>? = null) : PagedModelChatFunctionInvocationResource {
        val localVarResponse = listFunctionInvocationsWithHttpInfo(id = id, page = page, size = size, sort = sort)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as PagedModelChatFunctionInvocationResource
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
     * List chat function invocations
     * Returns a page of invocations of this chat function. A log of previous runs of the function
     * @param id Chat function ID
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 25)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return ApiResponse<PagedModelChatFunctionInvocationResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun listFunctionInvocationsWithHttpInfo(id: kotlin.Long, page: kotlin.Int?, size: kotlin.Int?, sort: kotlin.collections.List<kotlin.String>?) : ApiResponse<PagedModelChatFunctionInvocationResource?> {
        val localVariableConfig = listFunctionInvocationsRequestConfig(id = id, page = page, size = size, sort = sort)

        return request<Unit, PagedModelChatFunctionInvocationResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation listFunctionInvocations
     *
     * @param id Chat function ID
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 25)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return RequestConfig
     */
    fun listFunctionInvocationsRequestConfig(id: kotlin.Long, page: kotlin.Int?, size: kotlin.Int?, sort: kotlin.collections.List<kotlin.String>?) : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf<kotlin.String, kotlin.collections.List<kotlin.String>>()
            .apply {
                if (page != null) {
                    put("page", listOf(page.toString()))
                }
                if (size != null) {
                    put("size", listOf(size.toString()))
                }
                if (sort != null) {
                    put("sort", toMultiValue(sort.toList(), "multi"))
                }
            }
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/v1/functions/{id}/invocations".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }

    /**
     * List chat function versions
     * Returns a page of versions of this chat function
     * @param id Chat function ID
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 25)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return PagedModelChatFunctionVersionResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun listFunctionVersions(id: kotlin.Long, page: kotlin.Int? = 0, size: kotlin.Int? = 25, sort: kotlin.collections.List<kotlin.String>? = null) : PagedModelChatFunctionVersionResource {
        val localVarResponse = listFunctionVersionsWithHttpInfo(id = id, page = page, size = size, sort = sort)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as PagedModelChatFunctionVersionResource
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
     * List chat function versions
     * Returns a page of versions of this chat function
     * @param id Chat function ID
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 25)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return ApiResponse<PagedModelChatFunctionVersionResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun listFunctionVersionsWithHttpInfo(id: kotlin.Long, page: kotlin.Int?, size: kotlin.Int?, sort: kotlin.collections.List<kotlin.String>?) : ApiResponse<PagedModelChatFunctionVersionResource?> {
        val localVariableConfig = listFunctionVersionsRequestConfig(id = id, page = page, size = size, sort = sort)

        return request<Unit, PagedModelChatFunctionVersionResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation listFunctionVersions
     *
     * @param id Chat function ID
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 25)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return RequestConfig
     */
    fun listFunctionVersionsRequestConfig(id: kotlin.Long, page: kotlin.Int?, size: kotlin.Int?, sort: kotlin.collections.List<kotlin.String>?) : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf<kotlin.String, kotlin.collections.List<kotlin.String>>()
            .apply {
                if (page != null) {
                    put("page", listOf(page.toString()))
                }
                if (size != null) {
                    put("size", listOf(size.toString()))
                }
                if (sort != null) {
                    put("sort", toMultiValue(sort.toList(), "multi"))
                }
            }
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/v1/functions/{id}/versions".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }

    /**
     * Retrieve a chat function
     * Returns a chat function by ID
     * @param id Chat function ID
     * @return ChatFunctionResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun retrieveFunction(id: kotlin.Long) : ChatFunctionResource {
        val localVarResponse = retrieveFunctionWithHttpInfo(id = id)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as ChatFunctionResource
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
     * Retrieve a chat function
     * Returns a chat function by ID
     * @param id Chat function ID
     * @return ApiResponse<ChatFunctionResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun retrieveFunctionWithHttpInfo(id: kotlin.Long) : ApiResponse<ChatFunctionResource?> {
        val localVariableConfig = retrieveFunctionRequestConfig(id = id)

        return request<Unit, ChatFunctionResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation retrieveFunction
     *
     * @param id Chat function ID
     * @return RequestConfig
     */
    fun retrieveFunctionRequestConfig(id: kotlin.Long) : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/v1/functions/{id}".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }

    /**
     * Retrieve chat function current version
     * Returns the version of this chat function currently deployed
     * @param id Chat function ID
     * @return ChatFunctionVersionResource
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun retrieveFunctionCurrentVersion(id: kotlin.Long) : ChatFunctionVersionResource {
        val localVarResponse = retrieveFunctionCurrentVersionWithHttpInfo(id = id)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as ChatFunctionVersionResource
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
     * Retrieve chat function current version
     * Returns the version of this chat function currently deployed
     * @param id Chat function ID
     * @return ApiResponse<ChatFunctionVersionResource?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun retrieveFunctionCurrentVersionWithHttpInfo(id: kotlin.Long) : ApiResponse<ChatFunctionVersionResource?> {
        val localVariableConfig = retrieveFunctionCurrentVersionRequestConfig(id = id)

        return request<Unit, ChatFunctionVersionResource>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation retrieveFunctionCurrentVersion
     *
     * @param id Chat function ID
     * @return RequestConfig
     */
    fun retrieveFunctionCurrentVersionRequestConfig(id: kotlin.Long) : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json, application/vnd.hal+json, application/hal+json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/v1/functions/{id}/current-version".replace("{"+"id"+"}", encodeURIComponent(id.toString())),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = true,
            body = localVariableBody
        )
    }


    private fun encodeURIComponent(uriComponent: kotlin.String): kotlin.String =
        HttpUrl.Builder().scheme("http").host("localhost").addPathSegment(uriComponent).build().encodedPathSegments[0]
}
