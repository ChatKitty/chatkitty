import { ChatKittyPaginator } from './pagination';
import { ChatKittyFailedResult, ChatKittySucceededResult } from './result';
import { User } from './user';
export declare class UserBlockListItem {
    user: User;
    createdTime: string;
    _actions: UserBlockListItemActions;
}
export declare class UserBlockListItemActions {
    delete: string;
}
export declare type GetUserBlockListResult = GetUserBlockListSucceededResult | ChatKittyFailedResult;
export declare class GetUserBlockListSucceededResult extends ChatKittySucceededResult {
    paginator: ChatKittyPaginator<UserBlockListItem>;
    constructor(paginator: ChatKittyPaginator<UserBlockListItem>);
}
export declare class DeleteUserBlockListItemRequest {
    item: UserBlockListItem;
}
export declare type DeleteUserBlockListItemResult = DeleteUserBlockListItemSucceededResult | ChatKittyFailedResult;
export declare class DeleteUserBlockListItemSucceededResult extends ChatKittySucceededResult {
    user: User;
    constructor(user: User);
}
