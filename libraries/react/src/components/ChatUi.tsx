import { useEffect } from 'react';
import { loadChatUi, ApiConnection } from '@chatkitty/core';

export type ChatUiProps = {
	widgetId: string;
	username?: string;
	connection?: ApiConnection;
};

export const ChatUi = ({ widgetId, username, connection }: ChatUiProps) => {
	useEffect(() => {
		if (!username && !connection) return;

		loadChatUi(
			{
				widgetId,
				username,
				container: { height: '100%' },
			},
			{
				connection,
			},
		);
	}, [widgetId, username, connection]);

	return <div id="chat-ui"></div>;
};
