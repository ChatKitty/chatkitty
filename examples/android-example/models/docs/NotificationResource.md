
# NotificationResource

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **kotlin.Long** | 64-bit integer identifier associated with this resource | 
**body** | **kotlin.String** | The text body of this notification | 
**createdTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Time this notification was created | 
**&#x60;data&#x60;** | [**NotificationData**](NotificationData.md) |  | 
**muted** | **kotlin.Boolean** | True if this notification should be muted | 
**title** | **kotlin.String** | The title of this notification | 
**channel** | [**NotificationResourceChannel**](NotificationResourceChannel.md) |  |  [optional]
**readTime** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Time this notification was read |  [optional]
**links** | [**kotlin.collections.Map&lt;kotlin.String, Link&gt;**](Link.md) |  |  [optional]



