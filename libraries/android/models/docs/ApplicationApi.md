# ApplicationApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**retrieveAuthenticatedApplication**](ApplicationApi.md#retrieveAuthenticatedApplication) | **GET** /v1/application | Retrieve the authenticated application
[**retrieveAuthenticatedApplicationSettings**](ApplicationApi.md#retrieveAuthenticatedApplicationSettings) | **GET** /v1/application/settings | Retrieve the authenticated application settings
[**updateAuthenticatedApplicationSettings**](ApplicationApi.md#updateAuthenticatedApplicationSettings) | **PUT** /v1/application/settings | Update the authenticated application settings


<a id="retrieveAuthenticatedApplication"></a>
# **retrieveAuthenticatedApplication**
> ApplicationResource retrieveAuthenticatedApplication()

Retrieve the authenticated application

Returns the ChatKitty application associated with the authentication credentials used.  You must use an **OAuth V2 Bearer token** to access this endpoint.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApplicationApi()
try {
    val result : ApplicationResource = apiInstance.retrieveAuthenticatedApplication()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApplicationApi#retrieveAuthenticatedApplication")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApplicationApi#retrieveAuthenticatedApplication")
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

<a id="retrieveAuthenticatedApplicationSettings"></a>
# **retrieveAuthenticatedApplicationSettings**
> ApplicationSettingsResource retrieveAuthenticatedApplicationSettings()

Retrieve the authenticated application settings

Returns the current settings configuring this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApplicationApi()
try {
    val result : ApplicationSettingsResource = apiInstance.retrieveAuthenticatedApplicationSettings()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApplicationApi#retrieveAuthenticatedApplicationSettings")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApplicationApi#retrieveAuthenticatedApplicationSettings")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ApplicationSettingsResource**](ApplicationSettingsResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateAuthenticatedApplicationSettings"></a>
# **updateAuthenticatedApplicationSettings**
> ApplicationSettingsResource updateAuthenticatedApplicationSettings(applicationSettingsProperties)

Update the authenticated application settings

Update the settings configuring this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ApplicationApi()
val applicationSettingsProperties : ApplicationSettingsProperties =  // ApplicationSettingsProperties | 
try {
    val result : ApplicationSettingsResource = apiInstance.updateAuthenticatedApplicationSettings(applicationSettingsProperties)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApplicationApi#updateAuthenticatedApplicationSettings")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApplicationApi#updateAuthenticatedApplicationSettings")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **applicationSettingsProperties** | [**ApplicationSettingsProperties**](ApplicationSettingsProperties.md)|  |

### Return type

[**ApplicationSettingsResource**](ApplicationSettingsResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

