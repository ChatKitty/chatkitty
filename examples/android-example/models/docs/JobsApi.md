# JobsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**listJobs**](JobsApi.md#listJobs) | **GET** /v1/jobs | List jobs
[**retrieveJob**](JobsApi.md#retrieveJob) | **GET** /v1/jobs/{id} | Retrieve a job


<a id="listJobs"></a>
# **listJobs**
> PagedModelApplicationJobResource listJobs(page, size, sort, running)

List jobs

Returns a page of jobs created for this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = JobsApi()
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
val running : kotlin.Boolean = true // kotlin.Boolean | Filters for jobs currently running
try {
    val result : PagedModelApplicationJobResource = apiInstance.listJobs(page, size, sort, running)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling JobsApi#listJobs")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling JobsApi#listJobs")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]
 **running** | **kotlin.Boolean**| Filters for jobs currently running | [optional]

### Return type

[**PagedModelApplicationJobResource**](PagedModelApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveJob"></a>
# **retrieveJob**
> ApplicationJobResource retrieveJob(id)

Retrieve a job

Returns a job by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = JobsApi()
val id : kotlin.Long = 789 // kotlin.Long | Job ID
try {
    val result : ApplicationJobResource = apiInstance.retrieveJob(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling JobsApi#retrieveJob")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling JobsApi#retrieveJob")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Job ID |

### Return type

[**ApplicationJobResource**](ApplicationJobResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

