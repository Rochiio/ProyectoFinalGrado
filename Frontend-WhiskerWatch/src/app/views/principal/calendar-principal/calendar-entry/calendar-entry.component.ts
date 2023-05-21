import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CalendarOptions, EventClickArg, EventSourceInput } from '@fullcalendar/core';
import { aR } from '@fullcalendar/core/internal-common';
import dayGridPlugin from '@fullcalendar/daygrid';
import { get } from 'ol/proj';
import { CalendarDto, TaskDto } from 'src/app/models/calendar/calendar-dto/calendar-dto';
import { EventsCalendarInt } from 'src/app/models/fullcalendar/events-calendar-int';
import { CalendarRestClientService } from 'src/app/services/api/calendar/calendar-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

@Component({
  selector: 'app-calendar-entry',
  templateUrl: './calendar-entry.component.html',
  styleUrls: ['./calendar-entry.component.css']
})
export class CalendarEntryComponent implements OnInit{
  @Output() actualEventSelected: EventEmitter<string> = new EventEmitter<string>();

  actualCalendar!: CalendarDto;
  calendarOptions!: CalendarOptions;
  events!: EventsCalendarInt[];

  constructor(
    private calendarService: CalendarRestClientService,
    private notificationService: NotificationsService
  ) {
    this.events = [];
  }

  ngOnInit(): void {
    this.getEvents();

    this.calendarOptions = {
      initialView: 'dayGridMonth',
      locale: 'es',
      events: this.events,
      plugins: [dayGridPlugin],
      eventClick: (event) => this.onClick(event)
    };

    this.actualEventSelected.emit('');
  }

  private onClick(arg: EventClickArg) {
    let event = arg.event.title;
    this.actualEventSelected.emit(event);
  }

  private getEvents(): void{
    this.calendarService.getCalendarByMapsId(localStorage.getItem('access_token')! ,localStorage.getItem('actual_maps_id')!).subscribe(
      (data: CalendarDto) => {
        this.events = data.listTasks.map((dt: TaskDto) => {
          let kk: EventsCalendarInt = {
            id: dt.id,
            title: dt.task,
            start: dt.date,
          };

          return kk;
        });
        this.calendarOptions.events = this.events;
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );
  }


}
