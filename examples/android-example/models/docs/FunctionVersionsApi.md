# FunctionVersionsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**retrieveFunctionVersion**](FunctionVersionsApi.md#retrieveFunctionVersion) | **GET** /v1/function-versions/{id} | Retrieve a chat function version


<a id="retrieveFunctionVersion"></a>
# **retrieveFunctionVersion**
> ChatFunctionVersionResource retrieveFunctionVersion(id)

Retrieve a chat function version

Returns a chat function version by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FunctionVersionsApi()
val id : kotlin.Long = 789 // kotlin.Long | Chat function version ID
try {
    val result : ChatFunctionVersionResource = apiInstance.retrieveFunctionVersion(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FunctionVersionsApi#retrieveFunctionVersion")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FunctionVersionsApi#retrieveFunctionVersion")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Chat function version ID |

### Return type

[**ChatFunctionVersionResource**](ChatFunctionVersionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

