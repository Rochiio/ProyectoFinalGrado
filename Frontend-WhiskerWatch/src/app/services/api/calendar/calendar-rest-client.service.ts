import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CalendarDto } from 'src/app/models/calendar/calendar-dto/calendar-dto';

const DIR = 'http://127.0.0.1:6969/calendar'
@Injectable({
  providedIn: 'root'
})
export class CalendarRestClientService {

  constructor(private httpClient: HttpClient) { }

  public getCalendarByMapsId(token: string, mapsId: string): Observable<Array<CalendarDto>> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get<Array<CalendarDto>>(DIR+"/mapsId/"+mapsId, {headers})
  }
}
