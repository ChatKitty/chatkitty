# FunctionsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createFunctionVersion**](FunctionsApi.md#createFunctionVersion) | **POST** /v1/functions/{id}/versions | Create a chat function version
[**listFunctionInvocations**](FunctionsApi.md#listFunctionInvocations) | **GET** /v1/functions/{id}/invocations | List chat function invocations
[**listFunctionVersions**](FunctionsApi.md#listFunctionVersions) | **GET** /v1/functions/{id}/versions | List chat function versions
[**retrieveFunction**](FunctionsApi.md#retrieveFunction) | **GET** /v1/functions/{id} | Retrieve a chat function
[**retrieveFunctionCurrentVersion**](FunctionsApi.md#retrieveFunctionCurrentVersion) | **GET** /v1/functions/{id}/current-version | Retrieve chat function current version


<a id="createFunctionVersion"></a>
# **createFunctionVersion**
> ChatFunctionVersionResource createFunctionVersion(id, createChatFunctionVersionResource)

Create a chat function version

Creates a new version of this chat function

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FunctionsApi()
val id : kotlin.Long = 789 // kotlin.Long | Chat function ID
val createChatFunctionVersionResource : CreateChatFunctionVersionResource =  // CreateChatFunctionVersionResource | 
try {
    val result : ChatFunctionVersionResource = apiInstance.createFunctionVersion(id, createChatFunctionVersionResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FunctionsApi#createFunctionVersion")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FunctionsApi#createFunctionVersion")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Chat function ID |
 **createChatFunctionVersionResource** | [**CreateChatFunctionVersionResource**](CreateChatFunctionVersionResource.md)|  |

### Return type

[**ChatFunctionVersionResource**](ChatFunctionVersionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listFunctionInvocations"></a>
# **listFunctionInvocations**
> PagedModelChatFunctionInvocationResource listFunctionInvocations(id, page, size, sort)

List chat function invocations

Returns a page of invocations of this chat function. A log of previous runs of the function

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FunctionsApi()
val id : kotlin.Long = 789 // kotlin.Long | Chat function ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChatFunctionInvocationResource = apiInstance.listFunctionInvocations(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FunctionsApi#listFunctionInvocations")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FunctionsApi#listFunctionInvocations")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Chat function ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChatFunctionInvocationResource**](PagedModelChatFunctionInvocationResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listFunctionVersions"></a>
# **listFunctionVersions**
> PagedModelChatFunctionVersionResource listFunctionVersions(id, page, size, sort)

List chat function versions

Returns a page of versions of this chat function

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FunctionsApi()
val id : kotlin.Long = 789 // kotlin.Long | Chat function ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChatFunctionVersionResource = apiInstance.listFunctionVersions(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FunctionsApi#listFunctionVersions")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FunctionsApi#listFunctionVersions")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Chat function ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChatFunctionVersionResource**](PagedModelChatFunctionVersionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveFunction"></a>
# **retrieveFunction**
> ChatFunctionResource retrieveFunction(id)

Retrieve a chat function

Returns a chat function by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FunctionsApi()
val id : kotlin.Long = 789 // kotlin.Long | Chat function ID
try {
    val result : ChatFunctionResource = apiInstance.retrieveFunction(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FunctionsApi#retrieveFunction")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FunctionsApi#retrieveFunction")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Chat function ID |

### Return type

[**ChatFunctionResource**](ChatFunctionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveFunctionCurrentVersion"></a>
# **retrieveFunctionCurrentVersion**
> ChatFunctionVersionResource retrieveFunctionCurrentVersion(id)

Retrieve chat function current version

Returns the version of this chat function currently deployed

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FunctionsApi()
val id : kotlin.Long = 789 // kotlin.Long | Chat function ID
try {
    val result : ChatFunctionVersionResource = apiInstance.retrieveFunctionCurrentVersion(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FunctionsApi#retrieveFunctionCurrentVersion")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FunctionsApi#retrieveFunctionCurrentVersion")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Chat function ID |

### Return type

[**ChatFunctionVersionResource**](ChatFunctionVersionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

