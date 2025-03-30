
# ChatRuntimeResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**inline**](#Type) | The type of this runtime. Always NODEJS | 
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**dependencies** | [**kotlin.collections.List&lt;ChatRuntimeDependencyProperties&gt;**](ChatRuntimeDependencyProperties.md) | The NPM dependencies version of this runtime | 
**environmentVariables** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Environment variable set for this runtime. Accessible in the initialization script and chat functions associated with this runtime | 
**version** | **kotlin.String** | The semantic version of this runtime | 
**initializationScript** | [**ChatRuntimeScriptProperties**](ChatRuntimeScriptProperties.md) |  |  [optional]
**links** | [**kotlin.collections.Map&lt;kotlin.String, Link&gt;**](Link.md) |  |  [optional]


<a id="Type"></a>
## Enum: type
Name | Value
---- | -----
type | NODEJS



