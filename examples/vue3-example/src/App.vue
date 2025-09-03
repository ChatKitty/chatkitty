<script setup lang="ts">
import {useApiConnection, ChatUi, type ApiConnection, type CurrentUser, type Notification} from '@chatkitty/vue'

const connection = useApiConnection(
	(connection: ApiConnection) => {
		const { user, unreadChannelsCount, notifications } = connection;

		console.log('Connected as user:', user.value);

		user.watch((u: CurrentUser) => {
			console.log('User:', u);
		});

		unreadChannelsCount.watch((count: number) => {
			console.log('Unread channels count:', count);
		});

		notifications.watch((n: Notification) => {
			console.log('Received notification:', n);
		});
	},
	{
		apiKey: 'afaac908-1db3-4b5c-a7ae-c040b9684403',
		username: '2989c53a-d0c5-4222-af8d-fbf7b0c74ec6',
	}
);
</script>

<template>
	<ChatUi widget-id="UWiEkKvdAaUJ1xut" :connection="connection" />
</template>
