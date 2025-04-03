# ThreadsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**listThreadMessages**](ThreadsApi.md#listThreadMessages) | **GET** /v1/threads/{id}/messages | List reply thread messages
[**retrieveThread**](ThreadsApi.md#retrieveThread) | **GET** /v1/threads/{id} | Retrieve a thread
[**sendThreadKeystrokes**](ThreadsApi.md#sendThreadKeystrokes) | **POST** /v1/threads/{id}/keystrokes | Send thread keystrokes
[**sendThreadMessage**](ThreadsApi.md#sendThreadMessage) | **POST** /v1/threads/{id}/messages | Send a reply thread message


<a id="listThreadMessages"></a>
# **listThreadMessages**
> CursorPagedModelMessageResource listThreadMessages(id, size, start, next, relation)

List reply thread messages

Returns a page of replies sent in this thread

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ThreadsApi()
val id : kotlin.Long = 789 // kotlin.Long | 
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val start : kotlin.Long = 789 // kotlin.Long | Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val next : kotlin.Long = 789 // kotlin.Long | Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages
val relation : kotlin.String = relation_example // kotlin.String | Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
try {
    val result : CursorPagedModelMessageResource = apiInstance.listThreadMessages(id, size, start, next, relation)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ThreadsApi#listThreadMessages")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ThreadsApi#listThreadMessages")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**|  |
 **size** | **kotlin.Int**| The size of the page to be returned | [optional]
 **start** | **kotlin.Long**| Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional]
 **next** | **kotlin.Long**| Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages | [optional]
 **relation** | **kotlin.String**| Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional] [enum: SELF, PREVIOUS, NEXT]

### Return type

[**CursorPagedModelMessageResource**](CursorPagedModelMessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveThread"></a>
# **retrieveThread**
> ReplyThreadResource retrieveThread(id)

Retrieve a thread

Returns a thread by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ThreadsApi()
val id : kotlin.Long = 789 // kotlin.Long | Reply thread ID
try {
    val result : ReplyThreadResource = apiInstance.retrieveThread(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ThreadsApi#retrieveThread")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ThreadsApi#retrieveThread")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Reply thread ID |

### Return type

[**ReplyThreadResource**](ReplyThreadResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="sendThreadKeystrokes"></a>
# **sendThreadKeystrokes**
> ReplyThreadKeystrokesResource sendThreadKeystrokes(id, createDelegatedReplyThreadKeystrokesResource)

Send thread keystrokes

Sends keystrokes in this thread on behalf of a user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ThreadsApi()
val id : kotlin.Long = 789 // kotlin.Long | 
val createDelegatedReplyThreadKeystrokesResource : CreateDelegatedReplyThreadKeystrokesResource =  // CreateDelegatedReplyThreadKeystrokesResource | 
try {
    val result : ReplyThreadKeystrokesResource = apiInstance.sendThreadKeystrokes(id, createDelegatedReplyThreadKeystrokesResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ThreadsApi#sendThreadKeystrokes")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ThreadsApi#sendThreadKeystrokes")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**|  |
 **createDelegatedReplyThreadKeystrokesResource** | [**CreateDelegatedReplyThreadKeystrokesResource**](CreateDelegatedReplyThreadKeystrokesResource.md)|  |

### Return type

[**ReplyThreadKeystrokesResource**](ReplyThreadKeystrokesResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="sendThreadMessage"></a>
# **sendThreadMessage**
> MessageResource sendThreadMessage(id, sendChannelMessageRequest)

Send a reply thread message

Sends a reply message in this thread as the system or on behalf of a user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ThreadsApi()
val id : kotlin.Long = 789 // kotlin.Long | 
val sendChannelMessageRequest : SendChannelMessageRequest =  // SendChannelMessageRequest | 
try {
    val result : MessageResource = apiInstance.sendThreadMessage(id, sendChannelMessageRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ThreadsApi#sendThreadMessage")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ThreadsApi#sendThreadMessage")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**|  |
 **sendChannelMessageRequest** | [**SendChannelMessageRequest**](SendChannelMessageRequest.md)|  |

### Return type

[**MessageResource**](MessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

