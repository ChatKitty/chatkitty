import {Component, Input} from '@angular/core';
import { loadChatUi, type ApiConnection } from '@chatkitty/core';

@Component({
  selector: 'ChatUi',
  imports: [],
  template: `
    <div id="chat-ui"></div>
  `,
  standalone: true,
  styles: ``
})
export class ChatUi {

  @Input({required: true}) widgetId: string = "";

  @Input({required: false}) username: string | undefined;

  @Input({required: false}) connection: ApiConnection | undefined;

  @Input({required: false}) mode: ("sandbox" | "live") = "live";


  async ngOnInit() {
    const connection = this.connection;

    loadChatUi(
      {
        widgetId: this.widgetId,
        username: this.username,
        container: { height: "100%" }
      },
      {
        ...(connection && { connection }),
        mode: this.mode || "live",
      }
    );
  }

}
