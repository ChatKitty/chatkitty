
# MessageMentionChannelProperties

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**inline**](#Type) | The type of this channel | 
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The ISO date-time this channel was created | 
**properties** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Custom data associated with this channel | 
**creator** | [**ChatUserProperties**](ChatUserProperties.md) |  |  [optional]
**displayName** | **kotlin.String** | Human readable name of this channel shown to users. Present if this is a group channel |  [optional]
**name** | **kotlin.String** | The unique name of this channel used to reference the channel. Present if this is a group channel |  [optional]


<a id="Type"></a>
## Enum: type
Name | Value
---- | -----
type | DIRECT, PUBLIC, PRIVATE



