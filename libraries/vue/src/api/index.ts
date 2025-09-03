import { ref, onMounted, watch, unref, type Ref } from 'vue';
import { connectApi, type ConnectApiOptions, type ApiConnection } from '@chatkitty/core';

export function useApiConnection(
	setup: (connection: ApiConnection) => void | Promise<void>,
	options: ConnectApiOptions | Ref<ConnectApiOptions>
) {
	const connection = ref<ApiConnection | undefined>(undefined);
	const loading = ref(false);

	const initializeApiConnection = async () => {
		if (connection.value || loading.value) return;

		loading.value = true;

		const value = await connectApi(unref(options));

		value.onDispose(async () => {
			connection.value = undefined;
		});

		connection.value = value;

		await setup(value);

		loading.value = false;
	};

	onMounted(initializeApiConnection);

	watch(
		() => unref(options),
		() => { void initializeApiConnection(); },
		{ deep: true }
	);

	watch(
		() => setup,
		() => { void initializeApiConnection(); }
	);

	return connection;
}
