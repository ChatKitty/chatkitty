# ImportsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**importChannelMembers**](ImportsApi.md#importChannelMembers) | **POST** /v1/imports/channels/{id}/members | Import channel members
[**importChannels**](ImportsApi.md#importChannels) | **POST** /v1/imports/channels | Import channels
[**importMessages**](ImportsApi.md#importMessages) | **POST** /v1/imports/messages | Import messages
[**importUsers**](ImportsApi.md#importUsers) | **POST** /v1/imports/users | Import users


<a id="importChannelMembers"></a>
# **importChannelMembers**
> ApplicationJobResource importChannelMembers(id, file)

Import channel members

Batch imports channel members from a JSON array file

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ImportsApi()
val id : kotlin.Long = 789 // kotlin.Long | 
val file : java.io.File = BINARY_DATA_HERE // java.io.File | JSON array file with user references to be added as members
try {
    val result : ApplicationJobResource = apiInstance.importChannelMembers(id, file)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ImportsApi#importChannelMembers")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ImportsApi#importChannelMembers")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**|  |
 **file** | **java.io.File**| JSON array file with user references to be added as members |

### Return type

[**ApplicationJobResource**](ApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="importChannels"></a>
# **importChannels**
> ApplicationJobResource importChannels(file)

Import channels

Batch imports channels from a JSON array file

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ImportsApi()
val file : java.io.File = BINARY_DATA_HERE // java.io.File | JSON array file with channels
try {
    val result : ApplicationJobResource = apiInstance.importChannels(file)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ImportsApi#importChannels")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ImportsApi#importChannels")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **file** | **java.io.File**| JSON array file with channels |

### Return type

[**ApplicationJobResource**](ApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="importMessages"></a>
# **importMessages**
> ApplicationJobResource importMessages(file)

Import messages

Batch imports messages from a JSON array file

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ImportsApi()
val file : java.io.File = BINARY_DATA_HERE // java.io.File | JSON array file with messages
try {
    val result : ApplicationJobResource = apiInstance.importMessages(file)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ImportsApi#importMessages")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ImportsApi#importMessages")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **file** | **java.io.File**| JSON array file with messages |

### Return type

[**ApplicationJobResource**](ApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="importUsers"></a>
# **importUsers**
> ApplicationJobResource importUsers(file)

Import users

Batch imports users from a JSON array file

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ImportsApi()
val file : java.io.File = BINARY_DATA_HERE // java.io.File | JSON array file with users
try {
    val result : ApplicationJobResource = apiInstance.importUsers(file)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ImportsApi#importUsers")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ImportsApi#importUsers")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **file** | **java.io.File**| JSON array file with users |

### Return type

[**ApplicationJobResource**](ApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

