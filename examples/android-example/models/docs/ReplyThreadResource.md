
# ReplyThreadResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**inline**](#Type) | The type of this thread | 
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The ISO date-time this thread was created | 
**properties** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Custom data associated with this thread | 
**name** | **kotlin.String** | The name of this thread |  [optional]
**links** | [**kotlin.collections.Map&lt;kotlin.String, Link&gt;**](Link.md) |  |  [optional]


<a id="Type"></a>
## Enum: type
Name | Value
---- | -----
type | MAIN, STANDALONE, MESSAGE



