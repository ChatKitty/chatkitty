<script setup lang="ts">
import { onMounted, watch } from 'vue';
import { loadChatUi, type ApiConnection } from '@chatkitty/core';

export interface ChatUiProps {
	widgetId: string;
	username?: string;
	connection?: ApiConnection;
  mode?: "sandbox" | "live";
}

const props = defineProps<ChatUiProps>();

onMounted(() => {
	watch(
		() => [props.widgetId, props.username, props.connection, props.mode] as const,
		([widgetId, username, connection, mode]) => {
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
				}
			);
		},
		{ immediate: true }
	);
});
</script>

<template>
	<div id="chat-ui"></div>
</template>
