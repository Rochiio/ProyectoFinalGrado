import { Component, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-calendar-principal',
  templateUrl: './calendar-principal.component.html',
  styleUrls: ['./calendar-principal.component.css']
})
export class CalendarPrincipalComponent{
  public actualEvent!: string;

  public changeSelectedEvent(event: string): void {
    this.actualEvent = event;
  }

}

