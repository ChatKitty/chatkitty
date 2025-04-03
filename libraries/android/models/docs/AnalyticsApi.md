# AnalyticsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**exportMessageAnalytics**](AnalyticsApi.md#exportMessageAnalytics) | **POST** /v1/analytics/messages | Export message analytics
[**exportUserAnalytics**](AnalyticsApi.md#exportUserAnalytics) | **POST** /v1/analytics/users | Export user analytics


<a id="exportMessageAnalytics"></a>
# **exportMessageAnalytics**
> ApplicationJobResource exportMessageAnalytics()

Export message analytics

Batch export message analytics data

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AnalyticsApi()
try {
    val result : ApplicationJobResource = apiInstance.exportMessageAnalytics()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AnalyticsApi#exportMessageAnalytics")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AnalyticsApi#exportMessageAnalytics")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ApplicationJobResource**](ApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="exportUserAnalytics"></a>
# **exportUserAnalytics**
> ApplicationJobResource exportUserAnalytics()

Export user analytics

Batch export user analytics data

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AnalyticsApi()
try {
    val result : ApplicationJobResource = apiInstance.exportUserAnalytics()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AnalyticsApi#exportUserAnalytics")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AnalyticsApi#exportUserAnalytics")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ApplicationJobResource**](ApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

