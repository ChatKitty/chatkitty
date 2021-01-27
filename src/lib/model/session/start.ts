import { ChatKittyError } from '../../error';
import { ChatKittyFailedResult, ChatKittySucceededResult } from '../../result';

import { Session } from './index';

export type StartSessionResult = StartedSessionResult | ChatKittyFailedResult;

export declare class StartSessionRequest {
  username: string;
  authParams?: unknown;
}

export class StartedSessionResult extends ChatKittySucceededResult {
  constructor(public session: Session) {
    super();
  }
}

export function startedSession(
  result: StartSessionResult
): result is StartedSessionResult {
  return (result as StartedSessionResult).session !== undefined;
}

export class NoActiveSessionError extends ChatKittyError {
  constructor() {
    super('NoActiveSessionError', "You're not connected to ChatKitty.");
  }
}
