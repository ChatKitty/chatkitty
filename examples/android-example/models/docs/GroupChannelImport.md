
# GroupChannelImport

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**members** | **kotlin.collections.List&lt;kotlin.String&gt;** | List of usernames of members of this channel | 
**creator** | **kotlin.String** | Username of the user who created this channel |  [optional]
**displayName** | **kotlin.String** | Human readable name of this channel shown to users. If absent defaults to the channel name |  [optional]
**idempotencyKey** | **kotlin.String** | Unique value generated by the client which ChatKitty uses to recognize subsequent retries of the same request. Optional but recommended |  [optional]
**name** | **kotlin.String** | The unique name of this channel used to reference the channel. If absent defaults to a random UUID |  [optional]
**properties** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Custom data associated with this channel |  [optional]



