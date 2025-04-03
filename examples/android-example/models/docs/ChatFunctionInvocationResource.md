
# ChatFunctionInvocationResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**args** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | The function arguments passed into this function for this invocation | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | The ISO date-time of this invocation when the function was called | 
**result** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | The result of this invocation when it completed | 
**status** | [**inline**](#Status) | The running status of this invocation | 
**versionNumber** | **kotlin.Long** | The version number of this function version when this invocation occured | 
**links** | [**kotlin.collections.Map&lt;kotlin.String, Link&gt;**](Link.md) |  |  [optional]


<a id="Status"></a>
## Enum: status
Name | Value
---- | -----
status | RUNNING, SUCCEEDED, FAILED



