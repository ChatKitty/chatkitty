import { useEffect } from "react";
import { connectApi, loadChatUi } from "chatkitty";

export const ChatUi = () => {
    useEffect(() => {
        const initializeChat = async () => {
            const connection = await connectApi({
                apiKey: "afaac908-1db3-4b5c-a7ae-c040b9684403",
                username: "2989c53a-d0c5-4222-af8d-fbf7b0c74ec6"
            });

            const { user, unreadChannelsCount, notifications, updateUser } = connection;
            console.log("Connected as user:", user.value);

            user.watch((user) => console.log("User:", user));
            unreadChannelsCount.watch((count) => console.log("Unread channels count:", count));
            notifications.watch((notification) => console.log("Received notification:", notification));

            loadChatUi(
                {
                    widgetId: "UWiEkKvdAaUJ1xut",
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
        <div className="flex flex-col h-screen">
            <div className="flex justify-center h-[125px] sm:hidden">
                <img src="./assets/logo.png" alt="Example App Logo" width="125" height="125" />
            </div>
            <div className="flex-1 min-h-0" id="chat-ui"></div>
        </div>
    );
};
