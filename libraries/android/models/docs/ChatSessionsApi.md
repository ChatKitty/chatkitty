# ChatSessionsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**listChatSessions**](ChatSessionsApi.md#listChatSessions) | **GET** /v1/chat-sessions | List chat sessions


<a id="listChatSessions"></a>
# **listChatSessions**
> PagedModelChatSessionResource listChatSessions(page, size, sort, state)

List chat sessions

Returns a page of chat sessions belonging to this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChatSessionsApi()
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
val state : kotlin.String = state_example // kotlin.String | Filters by state
try {
    val result : PagedModelChatSessionResource = apiInstance.listChatSessions(page, size, sort, state)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChatSessionsApi#listChatSessions")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChatSessionsApi#listChatSessions")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]
 **state** | **kotlin.String**| Filters by state | [optional] [enum: ACTIVE, ENDED]

### Return type

[**PagedModelChatSessionResource**](PagedModelChatSessionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

