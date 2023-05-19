import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CalendarOptions, EventClickArg, EventSourceInput } from '@fullcalendar/core';
import { aR } from '@fullcalendar/core/internal-common';
import dayGridPlugin from '@fullcalendar/daygrid';
import { CalendarDto } from 'src/app/models/calendar/calendar-dto/calendar-dto';
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

  constructor(
    private calendarService: CalendarRestClientService,
    private notificationService: NotificationsService
  ) {}

  ngOnInit(): void {
    let events: { id: string; title: string; start: string; }[] = [] //= [{id:'1234', title:'pruebabababa', start:'2023-05-17'}];

    this.calendarService.getCalendarByMapsId(localStorage.getItem('access_token')! ,localStorage.getItem('actual_maps_id')!).subscribe(
      (data: CalendarDto) => {
        console.log(data);
        this.actualCalendar = data;
        for (let event of data.listTasks) {
          events.push({id:event.id, title:event.task, start:event.date.toDateString()});
        }
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );

    this.calendarOptions = {
      initialView: 'dayGridMonth',
      locale: 'es',
      events: events,
      plugins: [dayGridPlugin],
      eventClick: (event) => this.onClick(event)
    };

    this.actualEventSelected.emit('');
  }

  private onClick(arg: EventClickArg) {
    let event = arg.event.title;
    this.actualEventSelected.emit(event);
  }


}
