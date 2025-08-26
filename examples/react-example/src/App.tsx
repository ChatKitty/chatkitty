import './App.css';

import { useApiConnection, ApiConnection, ChatUi, CurrentUser, Notification } from '@chatkitty/react';

function App() {
	const connection = useApiConnection(
		(connection: ApiConnection) => {
			const { user, unreadChannelsCount, notifications } = connection;

			console.log('Connected as user:', user.value);

			user.watch((user: CurrentUser) => console.log('User:', user));

			unreadChannelsCount.watch((count: number) => console.log('Unread channels count:', count));

			notifications.watch((notification: Notification) =>
				console.log('Received notification:', notification),
			);
		},
		{
			apiKey: 'afaac908-1db3-4b5c-a7ae-c040b9684403',
			username: '2989c53a-d0c5-4222-af8d-fbf7b0c74ec6',
		},
	);

	return (
		<>
			<ChatUi widgetId="UWiEkKvdAaUJ1xut" connection={connection} />
		</>
	);
}

export default App;
