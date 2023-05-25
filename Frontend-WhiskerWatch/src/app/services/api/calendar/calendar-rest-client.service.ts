import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CalendarCreate } from 'src/app/models/calendar/calendar-create/calendar-create';
import { CalendarDto } from 'src/app/models/calendar/calendar-dto/calendar-dto';

const DIR = 'http://127.0.0.1:6969/calendar'
@Injectable({
  providedIn: 'root'
})
export class CalendarRestClientService {

  constructor(private httpClient: HttpClient) { }

  public getCalendarByMapsId(token: string, mapsId: string): Observable<CalendarDto> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get<CalendarDto>(DIR+"/mapsId/"+mapsId, {headers});
  }

  public updateCalendar(token: string, calendarId: string, newData: CalendarCreate): Observable<CalendarDto> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.put<CalendarDto>(DIR+"/"+calendarId, newData, {headers});
  }
}
