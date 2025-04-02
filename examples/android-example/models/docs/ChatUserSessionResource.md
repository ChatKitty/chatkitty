
# ChatUserSessionResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Time this session was created | 
**state** | [**inline**](#State) | State of this session. ACTIVE or ENDED | 
**user** | [**ChatUserProperties**](ChatUserProperties.md) |  | 
**endTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Time this session ended. Present if state is ENDED |  [optional]
**userAgent** | **kotlin.String** | User agent used to start this session |  [optional]
**links** | [**kotlin.collections.Map&lt;kotlin.String, Link&gt;**](Link.md) |  |  [optional]


<a id="State"></a>
## Enum: state
Name | Value
---- | -----
state | ACTIVE, ENDED



