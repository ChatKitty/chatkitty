import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ChatUi } from '@chatkitty/angular';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ChatUi],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular-example';
}
