import { useMemo, useRef, useState } from 'react';

import { connectApi, ConnectApiOptions, ApiConnection } from '@chatkitty/core';

export const useApiConnection = (setup: (connection: ApiConnection) => void, options: ConnectApiOptions) => {
	const loading = useRef(false);

	const [connection, setConnection] = useState<ApiConnection>();

	useMemo(() => {
		if (loading.current) return;

		if (connection) return;

		const initializeApiConnection = async () => {
			const value = await connectApi(options);

			const superDispose = value.dispose.bind(value);

			value.dispose = async () => {
				await superDispose();

				setConnection(undefined);
			};

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
