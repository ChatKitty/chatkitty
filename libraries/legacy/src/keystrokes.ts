import { Channel } from './channel';
import { Thread } from './thread';

export declare class Keystrokes {
	username: string;
	keys: string;
	/** @internal */
	_relays: KeystrokesRelays;
}

declare class KeystrokesRelays {
	thread: string;
	user: string;
}

export type SendKeystrokesRequest = SendChannelKeystrokesRequest | SendThreadKeystrokesRequest;

export declare class SendChannelKeystrokesRequest {
	channel: Channel;
	keys: string;
}

export declare class SendThreadKeystrokesRequest {
	thread: Thread;
	keys: string;
}
