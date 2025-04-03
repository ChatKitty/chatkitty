
# ChatUserProperties

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**inline**](#Type) | Type of user | 
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**displayName** | **kotlin.String** | Human readable name of this user. Shown to other users | 
**displayPictureUrl** | **kotlin.String** | URL for this user&#39;s display picture | 
**isGuest** | **kotlin.Boolean** | True if this user was created by a guest user session | 
**name** | **kotlin.String** | The unique name used to identify this user across ChatKitty. Also known as username | 
**presence** | [**ChatUserPresenceProperties**](ChatUserPresenceProperties.md) |  | 
**properties** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Custom data associated with this user | 
**callStatus** | [**inline**](#CallStatus) | Call presence status of this user |  [optional]


<a id="Type"></a>
## Enum: type
Name | Value
---- | -----
type | PERSON, BOT


<a id="CallStatus"></a>
## Enum: callStatus
Name | Value
---- | -----
callStatus | AVAILABLE, IN_CALL



