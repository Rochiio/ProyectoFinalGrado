import { Component, OnInit } from '@angular/core';
import { webSocket } from "rxjs/webSocket";
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

@Component({
  selector: 'app-websocket',
  templateUrl: './websocket.component.html',
  styleUrls: ['./websocket.component.css']
})
export class WebsocketComponent implements OnInit{
  private subject = webSocket('ws://localhost:6969/map/notifications/adoption');

  constructor(
    private notificationService: NotificationsService
  ){}

  ngOnInit(): void {
    this.subject.subscribe(
      msg => this.notificationService.showAdoption('Se acaba de recoger una colonia de gatos'), // Called whenever there is a message from the server.
      err => console.log(err), // Called if at any point WebSocket API signals some kind of error.
      () => console.log('complete') // Called when connection is closed (for whatever reason).
    );
  }

}
