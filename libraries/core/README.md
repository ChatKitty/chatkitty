# @chatkitty/core

## Installation

```bash
npm install @chatkitty/core
```

## Basic Usage
Check out the [HTML example](../../examples/html-example) for a complete working example.

```bash
chatkitty html-example:serve
```

### Loading the Chat UI

```javascript
import { loadChatUi } from '@chatkitty/core';

await loadChatUi({
  widgetId: 'YOUR_WIDGET_ID',
  username: 'USER_USERNAME',
  container: {
    id: 'chat-ui',
    height: '100%',
  },
});

// Define the container in your HTML
// <div id="chat-ui"></div>
```

### Connecting to ChatKitty manually

To connect to the ChatKitty API manually, use your API key and the username of the user who is connecting. This is useful if you want to extend functionality programmatically or need access to the SDK client.

```javascript
import { connectApi } from '@chatkitty/core';

const apiKey = 'YOUR_CHATKITTY_API_KEY';
const username = 'USER_USERNAME';

const connection = await connectApi({
  apiKey: apiKey,
  username: username,
});

const { user, notifications, updateUser } = connection;

console.log('Connected as user:', user.value);

// Set event listeners if needed
user.watch((user) => console.log('User updated:', user));

// optional: get SDK client
const client = connection.getSdkClient();

// Load Chat UI using existing connection
await loadChatUi({
    widgetId: 'YOUR_WIDGET_ID',
    container: {
        id: 'chat-ui',
        height: '100%',
    }, { connection }
);

// Don't forget to disconnect when done
await connection.dispose();
```

### Customizing the Chat Experience

ChatKitty allows for extensive customization of the chat UI and behavior through themes, localization, audio notifications, and much more. For example, to customize the theme and provide a custom user profile:

```javascript
await loadChatUi({
  widgetId: 'YOUR_WIDGET_ID',
  theme: 'dark',
  profile: {
    displayName: 'John Doe',
    displayPicture: 'https://example.com/user-avatar.jpg',
  },
  // Other options...
});
```

Check out the [ChatKitty UI documentation](https://chatkitty.com/docs/ui) for a full list of customization options.

## Advanced Features

ChatKitty offers advanced features like audio notifications, localization for different languages, custom chat components, and error templates for a tailored chat experience. For more detailed documentation on these features and the complete API, visit the official [ChatKitty documentation](https://chatkitty.com/docs).
