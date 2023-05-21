import { Component, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-calendar-principal',
  templateUrl: './calendar-principal.component.html',
  styleUrls: ['./calendar-principal.component.css']
})
export class CalendarPrincipalComponent{
  public actualEvent!: string;
  public newDate: string;
  public newTask: string;

  constructor(){
    this.newDate = '';
    this.newTask = '';
  }

  public changeSelectedEvent(event: string): void {
    this.actualEvent = event;
  }

  public sendNewEvent(): void {

  }

  public dateIsCorrect(): Boolean {
    let transformedNewDate = new Date(this.newDate);
    let actualDate = new Date();
    if(this.newTask.length == 0 || transformedNewDate <= actualDate) return false;
    return true;
  }

}

