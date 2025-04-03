
# FileChatUserMessageResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**inline**](#Type) | The type of this message | 
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**channelId** | **kotlin.Long** | The ID of the channel this message belongs to | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The time this message was created | 
**file** | [**FileProperties**](FileProperties.md) |  | 
**nestedLevel** | **kotlin.Int** | The nested thread level of this message | 
**properties** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Custom data associated with this message | 
**user** | [**ChatUserProperties**](ChatUserProperties.md) |  | 
**groupTag** | **kotlin.String** | Optional string to associate this message with other messages. Can be used to group messages into a gallery |  [optional]
**lastEditedTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The time this message was last edited |  [optional]
**reactions** | [**kotlin.collections.List&lt;MessageReactionsSummaryProperties&gt;**](MessageReactionsSummaryProperties.md) | Reactions to this message |  [optional]
**repliesCount** | **kotlin.Long** | The number of replies to this message |  [optional]
**links** | [**kotlin.collections.Map&lt;kotlin.String, Link&gt;**](Link.md) |  |  [optional]


<a id="Type"></a>
## Enum: type
Name | Value
---- | -----
type | TEXT, FILE, SYSTEM_TEXT, SYSTEM_FILE



