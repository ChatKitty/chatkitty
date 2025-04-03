
# CreateChannelRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | **kotlin.String** |  | 
**creator** | [**AddChannelMemberRequest**](AddChannelMemberRequest.md) |  |  [optional]
**members** | [**kotlin.collections.Set&lt;AddChannelMemberRequest&gt;**](AddChannelMemberRequest.md) | List of user references of members of this channel |  [optional]
**properties** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Custom data associated with this channel |  [optional]
**name** | **kotlin.String** | The unique name of this channel used to reference the channel. If absent defaults to a random UUID |  [optional]
**displayName** | **kotlin.String** | Human readable name of this channel shown to users. If absent defaults to the channel name |  [optional]



