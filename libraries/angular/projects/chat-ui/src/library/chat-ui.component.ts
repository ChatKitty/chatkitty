import {Component, Input} from '@angular/core';
import { connectApi, loadChatUi } from "chatkitty";

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

  @Input({ required: true }) apiKey: string = "";

  @Input({ required: true }) username: string = "";

  @Input({ required: true }) widgetId: string = "";


  async ngOnInit() {

    const initializeChat = async () => {
      const connection = await connectApi({
        apiKey: this.apiKey,
        username: this.username
      });

      loadChatUi(
        {
          widgetId: this.widgetId,
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

    await initializeChat();
  }

}
