# MessagesApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteMessage**](MessagesApi.md#deleteMessage) | **DELETE** /v1/messages/{id} | Delete a message
[**deleteMessages**](MessagesApi.md#deleteMessages) | **DELETE** /v1/messages | Delete messages
[**listMessageReadReceipts**](MessagesApi.md#listMessageReadReceipts) | **GET** /v1/messages/{id}/read-receipts | List message read receipts
[**listMessages**](MessagesApi.md#listMessages) | **GET** /v1/messages | List messages
[**retrieveMessage**](MessagesApi.md#retrieveMessage) | **GET** /v1/messages/{id} | Retrieve a message
[**updateMessage**](MessagesApi.md#updateMessage) | **PATCH** /v1/messages/{id} | Update a message


<a id="deleteMessage"></a>
# **deleteMessage**
> ReplyThreadResource deleteMessage(id)

Delete a message

Deletes a message by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MessagesApi()
val id : kotlin.Long = 789 // kotlin.Long | Message ID
try {
    val result : ReplyThreadResource = apiInstance.deleteMessage(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MessagesApi#deleteMessage")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MessagesApi#deleteMessage")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Message ID |

### Return type

[**ReplyThreadResource**](ReplyThreadResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="deleteMessages"></a>
# **deleteMessages**
> ApplicationResource deleteMessages()

Delete messages

Deletes all messages belonging to this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MessagesApi()
try {
    val result : ApplicationResource = apiInstance.deleteMessages()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MessagesApi#deleteMessages")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MessagesApi#deleteMessages")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ApplicationResource**](ApplicationResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listMessageReadReceipts"></a>
# **listMessageReadReceipts**
> PagedModelMessageReadReceiptResource listMessageReadReceipts(id, page, size, sort)

List message read receipts

Returns a page of read receipts for this message

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MessagesApi()
val id : kotlin.Long = 789 // kotlin.Long | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelMessageReadReceiptResource = apiInstance.listMessageReadReceipts(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MessagesApi#listMessageReadReceipts")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MessagesApi#listMessageReadReceipts")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**|  |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelMessageReadReceiptResource**](PagedModelMessageReadReceiptResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listMessages"></a>
# **listMessages**
> CursorPagedModelMessageResource listMessages(size, start, next, relation, username, query)

List messages

Returns a page of messages belonging to this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MessagesApi()
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val start : kotlin.Long = 789 // kotlin.Long | Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val next : kotlin.Long = 789 // kotlin.Long | Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages
val relation : kotlin.String = relation_example // kotlin.String | Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val username : kotlin.String = username_example // kotlin.String | Filters messages by a sender's username
val query : kotlin.String = query_example // kotlin.String | Filters text messages by text contained in the message body
try {
    val result : CursorPagedModelMessageResource = apiInstance.listMessages(size, start, next, relation, username, query)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MessagesApi#listMessages")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MessagesApi#listMessages")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **size** | **kotlin.Int**| The size of the page to be returned | [optional]
 **start** | **kotlin.Long**| Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional]
 **next** | **kotlin.Long**| Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages | [optional]
 **relation** | **kotlin.String**| Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional] [enum: SELF, PREVIOUS, NEXT]
 **username** | **kotlin.String**| Filters messages by a sender&#39;s username | [optional]
 **query** | **kotlin.String**| Filters text messages by text contained in the message body | [optional]

### Return type

[**CursorPagedModelMessageResource**](CursorPagedModelMessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveMessage"></a>
# **retrieveMessage**
> MessageResource retrieveMessage(id)

Retrieve a message

Returns a message by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MessagesApi()
val id : kotlin.Long = 789 // kotlin.Long | Message ID
try {
    val result : MessageResource = apiInstance.retrieveMessage(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MessagesApi#retrieveMessage")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MessagesApi#retrieveMessage")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Message ID |

### Return type

[**MessageResource**](MessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateMessage"></a>
# **updateMessage**
> MessageResource updateMessage(id, messagePropertiesPatch)

Update a message

Updates a message properties

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MessagesApi()
val id : kotlin.Long = 789 // kotlin.Long | Message ID
val messagePropertiesPatch : MessagePropertiesPatch =  // MessagePropertiesPatch | 
try {
    val result : MessageResource = apiInstance.updateMessage(id, messagePropertiesPatch)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MessagesApi#updateMessage")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MessagesApi#updateMessage")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Message ID |
 **messagePropertiesPatch** | [**MessagePropertiesPatch**](MessagePropertiesPatch.md)|  | [optional]

### Return type

[**MessageResource**](MessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

