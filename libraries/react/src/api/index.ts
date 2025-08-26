import { useEffect, useRef, useState } from 'react';

import { connectApi, ConnectApiOptions, ApiConnection } from '@chatkitty/core';

export const useApiConnection = (setup: (connection: ApiConnection) => void, options: ConnectApiOptions) => {
	const [connection, setConnection] = useState<ApiConnection>();

	const loading = useRef(false);

	useEffect(() => {
		if (connection) return;

		if (loading.current) return;

		const initializeApiConnection = async () => {
			const value = await connectApi(options);

			value.onDispose(async () => {
				setConnection(undefined);
			});

			setConnection(value);

			setup(value);
		};

		loading.current = true;
		initializeApiConnection().then(() => {
			loading.current = false;
		});
	}, [connection, setup, options]);

	return connection;
}
