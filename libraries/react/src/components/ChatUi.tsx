import { useEffect } from 'react';
import { loadChatUi, ApiConnection } from '@chatkitty/core';

export type ChatUiProps = {
	widgetId: string;
	username?: string;
	connection?: ApiConnection;
	mode?: "sandbox" | "live";
};

export const ChatUi = ({ widgetId, username, connection, mode }: ChatUiProps) => {
	useEffect(() => {
		if (!username && !connection) return;

		loadChatUi(
			{
				widgetId,
				username,
				container: { height: '100%' },
			},
			{
				...(connection && { connection }),
				mode: mode || "live",
			},
		);
	}, [widgetId, username, connection]);

	return <div id="chat-ui"></div>;
};
