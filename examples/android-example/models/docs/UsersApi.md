# UsersApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**checkUserExists**](UsersApi.md#checkUserExists) | **HEAD** /v1/users | Check a user exists
[**createUser**](UsersApi.md#createUser) | **POST** /v1/users | Create a user
[**deleteUser**](UsersApi.md#deleteUser) | **DELETE** /v1/users/{id} | Delete a user
[**listUserChannels**](UsersApi.md#listUserChannels) | **GET** /v1/users/{id}/channels | List a user&#39;s channels
[**listUserMessages**](UsersApi.md#listUserMessages) | **GET** /v1/users/{id}/messages | List a user&#39;s messages
[**listUserNotifications**](UsersApi.md#listUserNotifications) | **GET** /v1/users/{id}/notifications | List a user&#39;s notifications
[**listUsers**](UsersApi.md#listUsers) | **GET** /v1/users | List users
[**removeUserSecret**](UsersApi.md#removeUserSecret) | **DELETE** /v1/users/{id}/secrets/{name} | Remove a user secret
[**retrieveUser**](UsersApi.md#retrieveUser) | **GET** /v1/users/{id} | Retrieve a user
[**retrieveUserSecret**](UsersApi.md#retrieveUserSecret) | **GET** /v1/users/{id}/secrets/{name} | Retrieve a user secret
[**setUserSecret**](UsersApi.md#setUserSecret) | **PUT** /v1/users/{id}/secrets/{name} | Set a user secret
[**updateUser**](UsersApi.md#updateUser) | **PATCH** /v1/users/{id} | Update a user
[**updateUserDisplayPicture**](UsersApi.md#updateUserDisplayPicture) | **POST** /v1/users/{id}/display-picture | Update a user&#39;s display picture


<a id="checkUserExists"></a>
# **checkUserExists**
> kotlin.Any checkUserExists(name)

Check a user exists

Checks if a user exists

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val name : kotlin.String = name_example // kotlin.String | Username of the user
try {
    val result : kotlin.Any = apiInstance.checkUserExists(name)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#checkUserExists")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#checkUserExists")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **kotlin.String**| Username of the user |

### Return type

[**kotlin.Any**](kotlin.Any.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="createUser"></a>
# **createUser**
> ChatUserResource createUser(createPersonChatUserResource)

Create a user

Creates a new user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val createPersonChatUserResource : CreatePersonChatUserResource =  // CreatePersonChatUserResource | 
try {
    val result : ChatUserResource = apiInstance.createUser(createPersonChatUserResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#createUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#createUser")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createPersonChatUserResource** | [**CreatePersonChatUserResource**](CreatePersonChatUserResource.md)|  |

### Return type

[**ChatUserResource**](ChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="deleteUser"></a>
# **deleteUser**
> ApplicationResource deleteUser(id)

Delete a user

Delets a user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
try {
    val result : ApplicationResource = apiInstance.deleteUser(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#deleteUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#deleteUser")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |

### Return type

[**ApplicationResource**](ApplicationResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listUserChannels"></a>
# **listUserChannels**
> PagedModelChannelResource listUserChannels(id, page, size, sort)

List a user&#39;s channels

Returns a page of channels for this user created or joined

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChannelResource = apiInstance.listUserChannels(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#listUserChannels")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#listUserChannels")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChannelResource**](PagedModelChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listUserMessages"></a>
# **listUserMessages**
> CursorPagedModelMessageResource listUserMessages(id, size, start, next, relation, unread)

List a user&#39;s messages

Returns a page of messages sent by this user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val start : kotlin.Long = 789 // kotlin.Long | Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val next : kotlin.Long = 789 // kotlin.Long | Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages
val relation : kotlin.String = relation_example // kotlin.String | Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val unread : kotlin.Boolean = true // kotlin.Boolean | Filters by returning unread messages
try {
    val result : CursorPagedModelMessageResource = apiInstance.listUserMessages(id, size, start, next, relation, unread)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#listUserMessages")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#listUserMessages")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **size** | **kotlin.Int**| The size of the page to be returned | [optional]
 **start** | **kotlin.Long**| Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional]
 **next** | **kotlin.Long**| Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages | [optional]
 **relation** | **kotlin.String**| Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional] [enum: SELF, PREVIOUS, NEXT]
 **unread** | **kotlin.Boolean**| Filters by returning unread messages | [optional]

### Return type

[**CursorPagedModelMessageResource**](CursorPagedModelMessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listUserNotifications"></a>
# **listUserNotifications**
> CursorPagedModelNotificationResource listUserNotifications(id, size, start, next, relation)

List a user&#39;s notifications

Returns a page of notifications received by this user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val start : kotlin.Long = 789 // kotlin.Long | Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val next : kotlin.Long = 789 // kotlin.Long | Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages
val relation : kotlin.String = relation_example // kotlin.String | Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
try {
    val result : CursorPagedModelNotificationResource = apiInstance.listUserNotifications(id, size, start, next, relation)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#listUserNotifications")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#listUserNotifications")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **size** | **kotlin.Int**| The size of the page to be returned | [optional]
 **start** | **kotlin.Long**| Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional]
 **next** | **kotlin.Long**| Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages | [optional]
 **relation** | **kotlin.String**| Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional] [enum: SELF, PREVIOUS, NEXT]

### Return type

[**CursorPagedModelNotificationResource**](CursorPagedModelNotificationResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listUsers"></a>
# **listUsers**
> PagedModelChatUserResource listUsers(page, size, sort, name, properties)

List users

Returns a page of users belonging to this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
val name : kotlin.String = name_example // kotlin.String | Filters by username
val properties : kotlin.String = properties_example // kotlin.String | Filters by user custom properties
try {
    val result : PagedModelChatUserResource = apiInstance.listUsers(page, size, sort, name, properties)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#listUsers")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#listUsers")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]
 **name** | **kotlin.String**| Filters by username | [optional]
 **properties** | **kotlin.String**| Filters by user custom properties | [optional]

### Return type

[**PagedModelChatUserResource**](PagedModelChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="removeUserSecret"></a>
# **removeUserSecret**
> ChatUserResource removeUserSecret(id, name)

Remove a user secret

Removes a user secret&#39;s value

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val name : kotlin.String = name_example // kotlin.String | The secret's name
try {
    val result : ChatUserResource = apiInstance.removeUserSecret(id, name)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#removeUserSecret")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#removeUserSecret")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **name** | **kotlin.String**| The secret&#39;s name |

### Return type

[**ChatUserResource**](ChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveUser"></a>
# **retrieveUser**
> ChatUserResource retrieveUser(id)

Retrieve a user

Returns a user by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
try {
    val result : ChatUserResource = apiInstance.retrieveUser(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#retrieveUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#retrieveUser")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |

### Return type

[**ChatUserResource**](ChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveUserSecret"></a>
# **retrieveUserSecret**
> SecretResource retrieveUserSecret(id, name)

Retrieve a user secret

Returns a user secret

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val name : kotlin.String = name_example // kotlin.String | The secret's name
try {
    val result : SecretResource = apiInstance.retrieveUserSecret(id, name)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#retrieveUserSecret")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#retrieveUserSecret")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **name** | **kotlin.String**| The secret&#39;s name |

### Return type

[**SecretResource**](SecretResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="setUserSecret"></a>
# **setUserSecret**
> ChatUserResource setUserSecret(id, name, secretResource)

Set a user secret

Sets a user secret&#39;s value

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val name : kotlin.String = name_example // kotlin.String | The secret's name
val secretResource : SecretResource =  // SecretResource | 
try {
    val result : ChatUserResource = apiInstance.setUserSecret(id, name, secretResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#setUserSecret")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#setUserSecret")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **name** | **kotlin.String**| The secret&#39;s name |
 **secretResource** | [**SecretResource**](SecretResource.md)|  |

### Return type

[**ChatUserResource**](ChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateUser"></a>
# **updateUser**
> ChatUserResource updateUser(id, chatUserPropertiesPatch)

Update a user

Updates a user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val chatUserPropertiesPatch : ChatUserPropertiesPatch =  // ChatUserPropertiesPatch | 
try {
    val result : ChatUserResource = apiInstance.updateUser(id, chatUserPropertiesPatch)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#updateUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#updateUser")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **chatUserPropertiesPatch** | [**ChatUserPropertiesPatch**](ChatUserPropertiesPatch.md)|  | [optional]

### Return type

[**ChatUserResource**](ChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateUserDisplayPicture"></a>
# **updateUserDisplayPicture**
> ChatUserResource updateUserDisplayPicture(id, createExternalFileProperties)

Update a user&#39;s display picture

Updates a user&#39;s display picture

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UsersApi()
val id : kotlin.Long = 789 // kotlin.Long | User ID
val createExternalFileProperties : CreateExternalFileProperties =  // CreateExternalFileProperties | 
try {
    val result : ChatUserResource = apiInstance.updateUserDisplayPicture(id, createExternalFileProperties)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#updateUserDisplayPicture")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#updateUserDisplayPicture")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| User ID |
 **createExternalFileProperties** | [**CreateExternalFileProperties**](CreateExternalFileProperties.md)|  |

### Return type

[**ChatUserResource**](ChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

