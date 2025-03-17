import {useEffect} from "react";
import {connectApi, loadChatUi} from "chatkitty";
import './ChatUi.css';

export const ChatUi = ({apiKey, username, widgetId}: {apiKey: string, username: string, widgetId: string}) => {
    useEffect(() => {
        const initializeChat = async () => {
            const connection = await connectApi({
                apiKey: apiKey,
                username: username
            });

            const { user, unreadChannelsCount, notifications, updateUser } = connection;
            console.log("Connected as user:", user.value);

            user.watch((user) => console.log("User:", user));
            unreadChannelsCount.watch((count) => console.log("Unread channels count:", count));
            notifications.watch((notification) => console.log("Received notification:", notification));

            loadChatUi(
                {
                    widgetId: widgetId,
                    container: { height: "100%" },
                    audio: { enabled: true },
                    components: {
                        chat: (context) => ({
                            menuActions: [],
                            onMounted: () => {
                                console.log("Chat UI mounted with context:", context);
                                updateUser({ properties: { lastUpdated: new Date().toISOString() } });
                            },
                            onHeaderSelected: (channel) => console.log(channel),
                            onMenuActionSelected: (action) => console.log(action)
                        })
                    },
                    templates: {
                    }
                },
                {
                    timeout: 50000,
                    connection
                }
            );
        };

        initializeChat();
    }, []);

    return (
        <div id="chat-ui"></div>
    );
};
