# ChannelsApi

All URIs are relative to *https://api.chatkitty.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addChannelMember**](ChannelsApi.md#addChannelMember) | **POST** /v1/channels/{id}/members | Add a channel member
[**addChannelModerator**](ChannelsApi.md#addChannelModerator) | **POST** /v1/channels/{id}/moderators | Add a channel moderator
[**createChannel**](ChannelsApi.md#createChannel) | **POST** /v1/channels | Create a channel
[**deleteChannel**](ChannelsApi.md#deleteChannel) | **DELETE** /v1/channels/{id} | Delete a channel
[**listChannelInvites**](ChannelsApi.md#listChannelInvites) | **GET** /v1/channels/{id}/invites | List channel invites
[**listChannelMembers**](ChannelsApi.md#listChannelMembers) | **GET** /v1/channels/{id}/members | List a channel&#39;s members
[**listChannelMemberships**](ChannelsApi.md#listChannelMemberships) | **GET** /v1/channels/{id}/memberships | List channel memberships
[**listChannelMessages**](ChannelsApi.md#listChannelMessages) | **GET** /v1/channels/{id}/messages | List channel messages
[**listChannelModerators**](ChannelsApi.md#listChannelModerators) | **GET** /v1/channels/{id}/moderators | Lists a channel&#39;s moderators
[**listChannelParticipants**](ChannelsApi.md#listChannelParticipants) | **GET** /v1/channels/{id}/participants | List channel participants
[**listChannels**](ChannelsApi.md#listChannels) | **GET** /v1/channels | List channels
[**removeChannelMember**](ChannelsApi.md#removeChannelMember) | **DELETE** /v1/channels/{id}/members/{user_id} | Remove a channel member
[**removeChannelModerator**](ChannelsApi.md#removeChannelModerator) | **DELETE** /v1/channels/{id}/moderators/{user_id} | Remove a channel moderator
[**retrieveChannel**](ChannelsApi.md#retrieveChannel) | **GET** /v1/channels/{id} | Retrieve a channel
[**sendChannelEvent**](ChannelsApi.md#sendChannelEvent) | **POST** /v1/channels/{id}/events | Send a channel event
[**sendChannelInvite**](ChannelsApi.md#sendChannelInvite) | **POST** /v1/channels/{id}/invites | Send a channel invite
[**sendChannelKeystrokes**](ChannelsApi.md#sendChannelKeystrokes) | **POST** /v1/channels/{id}/keystrokes | Send channel keystrokes
[**sendChannelMessage**](ChannelsApi.md#sendChannelMessage) | **POST** /v1/channels/{id}/messages | Send a channel message
[**updateChannel**](ChannelsApi.md#updateChannel) | **PATCH** /v1/channels/{id} | Update a channel


<a id="addChannelMember"></a>
# **addChannelMember**
> ChannelResource addChannelMember(id, addChannelMemberRequest)

Add a channel member

Makes a user a group channel member

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val addChannelMemberRequest : AddChannelMemberRequest =  // AddChannelMemberRequest | 
try {
    val result : ChannelResource = apiInstance.addChannelMember(id, addChannelMemberRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#addChannelMember")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#addChannelMember")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **addChannelMemberRequest** | [**AddChannelMemberRequest**](AddChannelMemberRequest.md)|  |

### Return type

[**ChannelResource**](ChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="addChannelModerator"></a>
# **addChannelModerator**
> ChatUserResource addChannelModerator(id, addChannelMemberRequest)

Add a channel moderator

Makes a user a group channel moderator

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val addChannelMemberRequest : AddChannelMemberRequest =  // AddChannelMemberRequest | 
try {
    val result : ChatUserResource = apiInstance.addChannelModerator(id, addChannelMemberRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#addChannelModerator")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#addChannelModerator")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **addChannelMemberRequest** | [**AddChannelMemberRequest**](AddChannelMemberRequest.md)|  |

### Return type

[**ChatUserResource**](ChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="createChannel"></a>
# **createChannel**
> ChannelResource createChannel(createChannelRequest)

Create a channel

Creates a new channel or returns an equivalent existing channel

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val createChannelRequest : CreateChannelRequest =  // CreateChannelRequest | 
try {
    val result : ChannelResource = apiInstance.createChannel(createChannelRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#createChannel")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#createChannel")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **createChannelRequest** | [**CreateChannelRequest**](CreateChannelRequest.md)|  |

### Return type

[**ChannelResource**](ChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="deleteChannel"></a>
# **deleteChannel**
> ApplicationResource deleteChannel(id)

Delete a channel

Deletes a channel by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
try {
    val result : ApplicationResource = apiInstance.deleteChannel(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#deleteChannel")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#deleteChannel")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |

### Return type

[**ApplicationResource**](ApplicationResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listChannelInvites"></a>
# **listChannelInvites**
> PagedModelChannelInviteResource listChannelInvites(id, page, size, sort)

List channel invites

Returns a page of invites sent to join this channel

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChannelInviteResource = apiInstance.listChannelInvites(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#listChannelInvites")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#listChannelInvites")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChannelInviteResource**](PagedModelChannelInviteResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listChannelMembers"></a>
# **listChannelMembers**
> PagedModelChatUserResource listChannelMembers(id, page, size, sort)

List a channel&#39;s members

Returns a page of channel members

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChatUserResource = apiInstance.listChannelMembers(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#listChannelMembers")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#listChannelMembers")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChatUserResource**](PagedModelChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listChannelMemberships"></a>
# **listChannelMemberships**
> PagedModelChannelMembershipResource listChannelMemberships(id, page, size, sort)

List channel memberships

Returns a page of channel membership info for this channel

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChannelMembershipResource = apiInstance.listChannelMemberships(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#listChannelMemberships")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#listChannelMemberships")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChannelMembershipResource**](PagedModelChannelMembershipResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listChannelMessages"></a>
# **listChannelMessages**
> CursorPagedModelMessageResource listChannelMessages(id, size, start, next, relation, username, query)

List channel messages

Returns a page of messages sent in this channel

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val start : kotlin.Long = 789 // kotlin.Long | Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val next : kotlin.Long = 789 // kotlin.Long | Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages
val relation : kotlin.String = relation_example // kotlin.String | Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages
val username : kotlin.String = username_example // kotlin.String | 
val query : kotlin.String = query_example // kotlin.String | 
try {
    val result : CursorPagedModelMessageResource = apiInstance.listChannelMessages(id, size, start, next, relation, username, query)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#listChannelMessages")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#listChannelMessages")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **size** | **kotlin.Int**| The size of the page to be returned | [optional]
 **start** | **kotlin.Long**| Start cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional]
 **next** | **kotlin.Long**| Next page cursor value. Do not set manually. Provided by the Platform API pagination engine to fetch subsequent pages | [optional]
 **relation** | **kotlin.String**| Page cursor relation. Do not set manually. Provided by the Platform API pagination engine to fetch previous or next pages | [optional] [enum: SELF, PREVIOUS, NEXT]
 **username** | **kotlin.String**|  | [optional]
 **query** | **kotlin.String**|  | [optional]

### Return type

[**CursorPagedModelMessageResource**](CursorPagedModelMessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listChannelModerators"></a>
# **listChannelModerators**
> PagedModelChatUserResource listChannelModerators(id, page, size, sort)

Lists a channel&#39;s moderators

Returns a page of channel moderators

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChatUserResource = apiInstance.listChannelModerators(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#listChannelModerators")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#listChannelModerators")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChatUserResource**](PagedModelChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listChannelParticipants"></a>
# **listChannelParticipants**
> PagedModelChatUserResource listChannelParticipants(id, page, size, sort)

List channel participants

Returns a page of channel active participants: members that currently online

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
try {
    val result : PagedModelChatUserResource = apiInstance.listChannelParticipants(id, page, size, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#listChannelParticipants")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#listChannelParticipants")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]

### Return type

[**PagedModelChatUserResource**](PagedModelChatUserResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="listChannels"></a>
# **listChannels**
> PagedModelChannelResource listChannels(page, size, sort, type, members, startTime, endTime, properties)

List channels

Returns a page of channels belonging to this application

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
val type : kotlin.String = type_example // kotlin.String | Filters by channel type
val members : kotlin.collections.Set<kotlin.String> =  // kotlin.collections.Set<kotlin.String> | Filters by channel members using their usernames
val startTime : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | Filters for channels created within a time range: start time
val endTime : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | Filters for channels created within a time range: end time
val properties : kotlin.String = properties_example // kotlin.String | Filters by channel custom properties
try {
    val result : PagedModelChannelResource = apiInstance.listChannels(page, size, sort, type, members, startTime, endTime, properties)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#listChannels")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#listChannels")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0]
 **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 25]
 **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional]
 **type** | **kotlin.String**| Filters by channel type | [optional] [enum: DIRECT, PUBLIC, PRIVATE]
 **members** | [**kotlin.collections.Set&lt;kotlin.String&gt;**](kotlin.String.md)| Filters by channel members using their usernames | [optional]
 **startTime** | **java.time.OffsetDateTime**| Filters for channels created within a time range: start time | [optional]
 **endTime** | **java.time.OffsetDateTime**| Filters for channels created within a time range: end time | [optional]
 **properties** | **kotlin.String**| Filters by channel custom properties | [optional]

### Return type

[**PagedModelChannelResource**](PagedModelChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="removeChannelMember"></a>
# **removeChannelMember**
> ChannelResource removeChannelMember(id, userId)

Remove a channel member

Removes a member from a group channel

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val userId : kotlin.Long = 789 // kotlin.Long | User ID of member to be removed
try {
    val result : ChannelResource = apiInstance.removeChannelMember(id, userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#removeChannelMember")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#removeChannelMember")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **userId** | **kotlin.Long**| User ID of member to be removed |

### Return type

[**ChannelResource**](ChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="removeChannelModerator"></a>
# **removeChannelModerator**
> ChannelResource removeChannelModerator(id, userId)

Remove a channel moderator

Removes a moderator from a group channel

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val userId : kotlin.Long = 789 // kotlin.Long | User ID of moderator to be removed
try {
    val result : ChannelResource = apiInstance.removeChannelModerator(id, userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#removeChannelModerator")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#removeChannelModerator")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **userId** | **kotlin.Long**| User ID of moderator to be removed |

### Return type

[**ChannelResource**](ChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="retrieveChannel"></a>
# **retrieveChannel**
> ChannelResource retrieveChannel(id)

Retrieve a channel

Returns a channel by ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
try {
    val result : ChannelResource = apiInstance.retrieveChannel(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#retrieveChannel")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#retrieveChannel")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |

### Return type

[**ChannelResource**](ChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="sendChannelEvent"></a>
# **sendChannelEvent**
> ChannelGenericEventResource sendChannelEvent(id, createChannelGenericEventResource)

Send a channel event

Sends a custom channel event

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val createChannelGenericEventResource : CreateChannelGenericEventResource =  // CreateChannelGenericEventResource | 
try {
    val result : ChannelGenericEventResource = apiInstance.sendChannelEvent(id, createChannelGenericEventResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#sendChannelEvent")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#sendChannelEvent")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **createChannelGenericEventResource** | [**CreateChannelGenericEventResource**](CreateChannelGenericEventResource.md)|  |

### Return type

[**ChannelGenericEventResource**](ChannelGenericEventResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="sendChannelInvite"></a>
# **sendChannelInvite**
> ChannelInviteResource sendChannelInvite(id, createChannelInviteResource)

Send a channel invite

Sends a channel invite to user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val createChannelInviteResource : CreateChannelInviteResource =  // CreateChannelInviteResource | 
try {
    val result : ChannelInviteResource = apiInstance.sendChannelInvite(id, createChannelInviteResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#sendChannelInvite")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#sendChannelInvite")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **createChannelInviteResource** | [**CreateChannelInviteResource**](CreateChannelInviteResource.md)|  |

### Return type

[**ChannelInviteResource**](ChannelInviteResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="sendChannelKeystrokes"></a>
# **sendChannelKeystrokes**
> ReplyThreadKeystrokesResource sendChannelKeystrokes(id, createDelegatedReplyThreadKeystrokesResource)

Send channel keystrokes

Sends keystrokes in this channel on behalf of a user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | 
val createDelegatedReplyThreadKeystrokesResource : CreateDelegatedReplyThreadKeystrokesResource =  // CreateDelegatedReplyThreadKeystrokesResource | 
try {
    val result : ReplyThreadKeystrokesResource = apiInstance.sendChannelKeystrokes(id, createDelegatedReplyThreadKeystrokesResource)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#sendChannelKeystrokes")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#sendChannelKeystrokes")
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

<a id="sendChannelMessage"></a>
# **sendChannelMessage**
> MessageResource sendChannelMessage(id, sendChannelMessageRequest)

Send a channel message

Sends a message in this channel as the system or on behalf of a user

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val sendChannelMessageRequest : SendChannelMessageRequest =  // SendChannelMessageRequest | 
try {
    val result : MessageResource = apiInstance.sendChannelMessage(id, sendChannelMessageRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#sendChannelMessage")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#sendChannelMessage")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **sendChannelMessageRequest** | [**SendChannelMessageRequest**](SendChannelMessageRequest.md)|  |

### Return type

[**MessageResource**](MessageResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

<a id="updateChannel"></a>
# **updateChannel**
> ChannelResource updateChannel(id, channelPropertiesPatch)

Update a channel

Updates a channel properties

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ChannelsApi()
val id : kotlin.Long = 789 // kotlin.Long | Channel ID
val channelPropertiesPatch : ChannelPropertiesPatch =  // ChannelPropertiesPatch | 
try {
    val result : ChannelResource = apiInstance.updateChannel(id, channelPropertiesPatch)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ChannelsApi#updateChannel")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ChannelsApi#updateChannel")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Long**| Channel ID |
 **channelPropertiesPatch** | [**ChannelPropertiesPatch**](ChannelPropertiesPatch.md)|  | [optional]

### Return type

[**ChannelResource**](ChannelResource.md)

### Authorization


Configure application_authorization:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/vnd.hal+json, application/hal+json

