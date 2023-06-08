import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ForumCreate } from 'src/app/models/forum/forum-create/forum-create';
import { ForumDto } from 'src/app/models/forum/forum-dto/forum-dto';

const DIR = 'http://127.0.0.1:6969/forum'
@Injectable({
  providedIn: 'root'
})
export class ForumRestClientService {

  constructor(private httpClient: HttpClient) { }

  public getForumByMapsId(token: string, mapsId: string): Observable<ForumDto> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get<ForumDto>(DIR+"/mapsId/"+mapsId, {headers});
  }

  public updateForum(token:string, forumId: string, newData: ForumCreate): Observable<ForumDto> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.put<ForumDto>(DIR+"/"+forumId, newData, {headers});
  }
}
