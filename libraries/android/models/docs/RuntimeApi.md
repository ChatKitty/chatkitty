# RuntimeApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createNodejsRuntimeFunction**](RuntimeApi.md#createNodejsRuntimeFunction) | **POST** /v1/runtimes/nodejs/functions | Create a NodeJS chat runtime function
[**listNodejsRuntimeFunctions**](RuntimeApi.md#listNodejsRuntimeFunctions) | **GET** /v1/runtimes/nodejs/functions | List NodeJS chat runtime functions
[**retrieveNodejsRuntime**](RuntimeApi.md#retrieveNodejsRuntime) | **GET** /v1/runtimes/nodejs | Retrieve NodeJS chat runtime
[**updateNodejsRuntimeDependencies**](RuntimeApi.md#updateNodejsRuntimeDependencies) | **PUT** /v1/runtimes/nodejs/dependencies | Update NodeJS chat runtime NPM dependencies
[**updateNodejsRuntimeEnvironmentVariables**](RuntimeApi.md#updateNodejsRuntimeEnvironmentVariables) | **PUT** /v1/runtimes/nodejs/environment-variables | Update NodeJS chat runtime environment variables
[**updateNodejsRuntimeInitializationScript**](RuntimeApi.md#updateNodejsRuntimeInitializationScript) | **PUT** /v1/runtimes/nodejs/initialization-script | Update NodeJS chat runtime initialization script


<a id="createNodejsRuntimeFunction"></a>
# **createNodejsRuntimeFunction**
> ChatFunctionResource createNodejsRuntimeFunction(createChatFunctionResource)

Create a NodeJS chat runtime function

Creates a NodeJS chat function for this runtime

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = RuntimeApi()
val createChatFunctionResource : CreateChatFunctionResource =  // CreateChatFunctionResource | 
try {
    val result : ChatFunctionResource = apiInstance.createNodejsRuntimeFunction(createChatFunctionResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RuntimeApi#createNodejsRuntimeFunction")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RuntimeApi#createNodejsRuntimeFunction")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createChatFunctionResource** | [**CreateChatFunctionResource**](CreateChatFunctionResource.md)|  |

### Return type

[**ChatFunctionResource**](ChatFunctionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listNodejsRuntimeFunctions"></a>
# **listNodejsRuntimeFunctions**
> PagedModelChatFunctionResource listNodejsRuntimeFunctions(page, size, sort)

List NodeJS chat runtime functions

Returns a page of functions for this application&#39;s NodeJS chat runtime

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = RuntimeApi()
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChatFunctionResource = apiInstance.listNodejsRuntimeFunctions(page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RuntimeApi#listNodejsRuntimeFunctions")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RuntimeApi#listNodejsRuntimeFunctions")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChatFunctionResource**](PagedModelChatFunctionResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveNodejsRuntime"></a>
# **retrieveNodejsRuntime**
> ChatRuntimeResource retrieveNodejsRuntime()

Retrieve NodeJS chat runtime

Return this application&#39;s NodeJS chat runtime

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = RuntimeApi()
try {
    val result : ChatRuntimeResource = apiInstance.retrieveNodejsRuntime()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RuntimeApi#retrieveNodejsRuntime")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RuntimeApi#retrieveNodejsRuntime")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ChatRuntimeResource**](ChatRuntimeResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateNodejsRuntimeDependencies"></a>
# **updateNodejsRuntimeDependencies**
> ChatRuntimeResource updateNodejsRuntimeDependencies(chatRuntimeDependencyProperties)

Update NodeJS chat runtime NPM dependencies

Updates the NPM dependencies for this application&#39;s NodeJS chat runtime

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = RuntimeApi()
val chatRuntimeDependencyProperties : kotlin.collections.List<ChatRuntimeDependencyProperties> =  // kotlin.collections.List<ChatRuntimeDependencyProperties> | 
try {
    val result : ChatRuntimeResource = apiInstance.updateNodejsRuntimeDependencies(chatRuntimeDependencyProperties)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RuntimeApi#updateNodejsRuntimeDependencies")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RuntimeApi#updateNodejsRuntimeDependencies")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatRuntimeDependencyProperties** | [**kotlin.collections.List&lt;ChatRuntimeDependencyProperties&gt;**](ChatRuntimeDependencyProperties.md)|  |

### Return type

[**ChatRuntimeResource**](ChatRuntimeResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateNodejsRuntimeEnvironmentVariables"></a>
# **updateNodejsRuntimeEnvironmentVariables**
> ChatRuntimeResource updateNodejsRuntimeEnvironmentVariables(chatRuntimeEnvironmentVariablesProperties)

Update NodeJS chat runtime environment variables

Updates the runtime environment variables of this application&#39;s NodeJS chat runtime

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = RuntimeApi()
val chatRuntimeEnvironmentVariablesProperties : ChatRuntimeEnvironmentVariablesProperties =  // ChatRuntimeEnvironmentVariablesProperties | 
try {
    val result : ChatRuntimeResource = apiInstance.updateNodejsRuntimeEnvironmentVariables(chatRuntimeEnvironmentVariablesProperties)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RuntimeApi#updateNodejsRuntimeEnvironmentVariables")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RuntimeApi#updateNodejsRuntimeEnvironmentVariables")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatRuntimeEnvironmentVariablesProperties** | [**ChatRuntimeEnvironmentVariablesProperties**](ChatRuntimeEnvironmentVariablesProperties.md)|  |

### Return type

[**ChatRuntimeResource**](ChatRuntimeResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateNodejsRuntimeInitializationScript"></a>
# **updateNodejsRuntimeInitializationScript**
> ChatRuntimeResource updateNodejsRuntimeInitializationScript(chatRuntimeScriptProperties)

Update NodeJS chat runtime initialization script

Updates the initialization script for this application&#39;s NodeJS chat runtime

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = RuntimeApi()
val chatRuntimeScriptProperties : ChatRuntimeScriptProperties =  // ChatRuntimeScriptProperties | 
try {
    val result : ChatRuntimeResource = apiInstance.updateNodejsRuntimeInitializationScript(chatRuntimeScriptProperties)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RuntimeApi#updateNodejsRuntimeInitializationScript")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RuntimeApi#updateNodejsRuntimeInitializationScript")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatRuntimeScriptProperties** | [**ChatRuntimeScriptProperties**](ChatRuntimeScriptProperties.md)|  |

### Return type

[**ChatRuntimeResource**](ChatRuntimeResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

