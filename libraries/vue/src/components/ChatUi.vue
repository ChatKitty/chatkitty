import {connectApi, loadChatUi} from "chatkitty";

<script setup lang="ts">

import {connectApi, loadChatUi} from "chatkitty";

const {apiKey, username, widgetId} = defineProps<{ apiKey: string, username: string, widgetId: string }>()

const initializeChat = async () => {
  const connection = await connectApi({
    apiKey: apiKey,
    username: username
  });

  const {user, unreadChannelsCount, notifications, updateUser} = connection;
  console.log("Connected as user:", user.value);

  user.watch((user) => console.log("User:", user));
  unreadChannelsCount.watch((count) => console.log("Unread channels count:", count));
  notifications.watch((notification) => console.log("Received notification:", notification));

  loadChatUi(
      {
        widgetId: widgetId,
        container: {height: "100%"},
        audio: {enabled: true},
        components: {
          chat: (context) => ({
            menuActions: [],
            onMounted: () => {
              console.log("Chat UI mounted with context:", context);
              updateUser({properties: {lastUpdated: new Date().toISOString()}});
            },
            onHeaderSelected: (channel) => console.log(channel),
            onMenuActionSelected: (action) => console.log(action)
          })
        },
        templates: {}
      },
      {
        timeout: 50000,
        connection
      }
  );
};

initializeChat();


</script>

<template>
  <div id="chat-ui"></div>
</template>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
