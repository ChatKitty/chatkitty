
# NotificationResourceChannel

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**inline**](#Type) | The type of this channel | 
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The ISO date-time this channel was created | 
**members** | [**kotlin.collections.List&lt;ChatUserProperties&gt;**](ChatUserProperties.md) | The members of this channel. Present if this is a direct channel. For other channel types, use [**list channel messages**](https://chatkitty.com/docs/api/reference#tag/channels/operation/list-channel-messages) | 
**properties** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Custom data associated with this channel | 
**displayName** | **kotlin.String** | Human readable name of this channel shown to users | 
**name** | **kotlin.String** | The unique name of this channel used to reference the channel | 
**creator** | [**ChatUserProperties**](ChatUserProperties.md) |  |  [optional]
**lastReceivedMessage** | [**MessageProperties**](MessageProperties.md) |  |  [optional]


<a id="Type"></a>
## Enum: type
Name | Value
---- | -----
type | DIRECT, PUBLIC, PRIVATE



