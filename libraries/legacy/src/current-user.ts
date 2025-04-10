import { ChatKittyUploadProgressListener, CreateChatKittyFileProperties } from './file';
import { ChatKittyFailedResult, ChatKittySucceededResult } from './result';
import { BaseUser } from './user';

export type CurrentUser = BaseUser & {
	/** @internal */
	_relays: CurrentUserRelays;
	/** @internal */
	_topics: CurrentUserTopics;
	/** @internal */
	_actions: CurrentUserActions;
	/** @internal */
	_streams: CurrentUserStreams;
};

declare class CurrentUserRelays {
	self: string;
	readFileAccessGrant: string;
	writeFileAccessGrant: string;
	channelsCount: string;
	channels: string;
	messages: string;
	joinableChannels: string;
	unreadChannelsCount: string;
	unreadChannels: string;
	unreadMessagesCount: string;
	contactsCount: string;
	contacts: string;
	userBlockedRecords: string;
}

declare class CurrentUserTopics {
	self: string;
	channels: string;
	messages: string;
	notifications: string;
	contacts: string;
	participants: string;
	users: string;
	reactions: string;
	threads: string;
	calls: string;
}

declare class CurrentUserActions {
	update: string;
	createChannel: string;
	updateDisplayPicture: string;
}

declare class CurrentUserStreams {
	displayPicture: string;
}

export type RetrieveCurrentUserResult = RetrieveCurrentUserSuccessfulResult | ChatKittyFailedResult;

export class RetrieveCurrentUserSuccessfulResult extends ChatKittySucceededResult {
	constructor(public user: CurrentUser) {
		super();
	}
}

export type UpdateCurrentUserResult = UpdatedCurrentUserResult | ChatKittyFailedResult;

export class UpdatedCurrentUserResult extends ChatKittySucceededResult {
	constructor(public user: CurrentUser) {
		super();
	}
}

export declare class UpdateCurrentUserDisplayPictureRequest {
	file: CreateChatKittyFileProperties;
	progressListener?: ChatKittyUploadProgressListener;
}

export type UpdateCurrentUserDisplayPictureResult =
	| UpdatedCurrentUserDisplayPictureResult
	| ChatKittyFailedResult;

export class UpdatedCurrentUserDisplayPictureResult extends ChatKittySucceededResult {
	constructor(public user: CurrentUser) {
		super();
	}
}
