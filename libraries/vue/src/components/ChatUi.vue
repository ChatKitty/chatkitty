import {connectApi, loadChatUi} from "@chatkitty/core";

<script setup lang="ts">

import {connectApi, loadChatUi} from "@chatkitty/core";

const {apiKey, username, widgetId} = defineProps<{ apiKey: string, username: string, widgetId: string }>()

const initializeChat = async () => {
  const connection = await connectApi({
    apiKey: apiKey,
    username: username
  });

  loadChatUi(
      {
        widgetId: widgetId,
        container: {height: "100%"},
        audio: {enabled: true},
        components: {
          chat: (_context) => ({
            menuActions: [],
            onMounted: () => {
            },
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
