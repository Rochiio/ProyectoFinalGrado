import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ForumDto } from 'src/app/models/forum/forum-dto/forum-dto';

const DIR = 'http://127.0.0.1:6969/forum'
@Injectable({
  providedIn: 'root'
})
export class ForumRestClientService {

  constructor(private httpClient: HttpClient) { }

  public getForumByMapsId(token: string, mapsId: string): Observable<Array<ForumDto>> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get<Array<ForumDto>>(DIR+"/mapsId/"+mapsId, {headers})
  }
}
