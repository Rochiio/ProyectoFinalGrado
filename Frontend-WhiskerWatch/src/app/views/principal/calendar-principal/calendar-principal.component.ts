import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CalendarOptions, EventClickArg } from '@fullcalendar/core';
import { CalendarDto, TaskDto } from 'src/app/models/calendar/calendar-dto/calendar-dto';
import { EventsCalendarInt } from 'src/app/models/fullcalendar/events-calendar-int';
import { CalendarRestClientService } from 'src/app/services/api/calendar/calendar-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';
import dayGridPlugin from '@fullcalendar/daygrid';
import { MappersService } from 'src/app/mappers/mappers.service';
import { CalendarCreate, TaskCreate } from 'src/app/models/calendar/calendar-create/calendar-create';
import { UserToken } from 'src/app/models/user/user-token/user-token';

@Component({
  selector: 'app-calendar-principal',
  templateUrl: './calendar-principal.component.html',
  styleUrls: ['./calendar-principal.component.css']
})
export class CalendarPrincipalComponent implements OnInit {
  public actualIdEvent: string;
  public actualEvent: string;
  public newDate: string;
  public newTask: string;
  public actualCalendar!: CalendarDto;
  public calendarOptions!: CalendarOptions;
  public events!: EventsCalendarInt[];
  public isAdmin = false;



  constructor(
    private calendarService: CalendarRestClientService,
    private notificationService: NotificationsService,
    private mapperService: MappersService
  ){
    this.newDate = '';
    this.newTask = '';
    this.actualEvent = '';
    this.actualIdEvent = '';
    this.events = [];
  }


  ngOnInit(): void {
    this.getRol();
    this.getEvents();

    this.calendarOptions = {
      initialView: 'dayGridMonth',
      locale: 'es',
      events: this.events,
      plugins: [dayGridPlugin],
      eventClick: (event) => this.onClick(event)
    };

  }


  /**
   * Saber el rol del usuario loggeado.
   */
  private getRol(): void{
    if (localStorage.getItem('isAssociation') == 'false'){
      let user: UserToken = JSON.parse(localStorage.getItem('currentUser')!);
      (user.user.rol == 'ADMIN') ? this.isAdmin = true : this.isAdmin = false;
    }

  }


  /**
   * Evento OnClick del calendario
   * @param arg argumentos del evento on click del calendario
   */
  private onClick(arg: EventClickArg) {
    this.actualEvent = arg.event.title;
    this.actualIdEvent = arg.event.id;
  }


  /**
   * Conseguir los elementos almacenados en la API
   */
  private getEvents(): void{
    this.calendarService.getCalendarByMapsId(localStorage.getItem('access_token')! ,localStorage.getItem('actual_maps_id')!).subscribe(
      (data: CalendarDto) => {
        this.actualCalendar = data;
        this.events = this.mapperService.eventsToCalendarInt(data.listTasks);
        this.calendarOptions.events = this.events;
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );
  }


  /**
   * Enviar un nuevo evento del calendario.
   */
  public sendNewEvent(): void {
    let newEvent = new TaskDto();
    newEvent.date = this.newDate;
    newEvent.task = this.newTask;

    console.log(this.actualCalendar.listTasks)
    let newList: TaskDto[] = this.actualCalendar.listTasks;
    newList.push(newEvent);

    let calendarCreate= new CalendarCreate();
    calendarCreate.mapsId = this.actualCalendar.mapsId;
    calendarCreate.listTasks = this.mapperService.calendarTasksDtoToCreate(newList);

    this.calendarService.updateCalendar(localStorage.getItem('access_token')!, this.actualCalendar.id, calendarCreate).subscribe(
      (data: CalendarDto) => {
        this.actualCalendar = data;
        this.events = this.mapperService.eventsToCalendarInt(data.listTasks);
        this.calendarOptions.events = this.events;
        this.notificationService.showCorrect('Evento creado correctamente');
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }


  /**
   * Eliminar un elemento del calendiario
   */
  public deleteEntry() :void{
    let list: TaskDto[] = this.actualCalendar.listTasks.filter(task => task.id != this.actualIdEvent);

    let listMapper: TaskCreate[] = this.mapperService.calendarTasksDtoToCreate(list);

    let calendarUpdate = new CalendarCreate();
    calendarUpdate.mapsId = this.actualCalendar.mapsId;
    calendarUpdate.listTasks = listMapper;

    this.calendarService.updateCalendar(localStorage.getItem('access_token')!, this.actualCalendar.id, calendarUpdate).subscribe(
      (data: CalendarDto) => {
        this.actualCalendar = data;
        this.events = this.mapperService.eventsToCalendarInt(data.listTasks);
        this.calendarOptions.events = this.events;
        this.notificationService.showCorrect('Evento eliminado correctamente');
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }


  /**
   * Comprobar si las fechas y el nuevo texto son correctos
   * @returns si es correcta
   */
  public dateIsCorrect(): Boolean {
    let transformedNewDate = new Date(this.newDate);
    let actualDate = new Date();
    if((this.newTask.length == 0 || transformedNewDate <= actualDate) || this.newTask.length == 0) return false;
    return true;
  }

}

