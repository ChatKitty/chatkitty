
# ApplicationJobResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**inline**](#Type) | The type of application job | 
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  | 
**state** | [**inline**](#State) | The running state of an application job | 
**endedTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional]
**file** | **kotlin.String** |  |  [optional]
**links** | [**kotlin.collections.Map&lt;kotlin.String, Link&gt;**](Link.md) |  |  [optional]


<a id="Type"></a>
## Enum: type
Name | Value
---- | -----
type | CHANNEL_IMPORT, CHANNEL_MEMBERS_IMPORT, MESSAGE_IMPORT, USER_IMPORT, MESSAGE_ANALYTICS_EXPORT


<a id="State"></a>
## Enum: state
Name | Value
---- | -----
state | PENDING, RUNNING, FINISHED, FAILED



