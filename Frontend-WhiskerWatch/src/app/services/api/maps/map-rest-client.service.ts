import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MapDto } from 'src/app/models/maps/map-dto/map-dto';
import { Observable } from 'rxjs';

const DIR = 'http://127.0.0.1:6969/map'
@Injectable({
  providedIn: 'root'
})
export class MapRestClientService {

  constructor(private httpClient: HttpClient) { }

  public getAllMaps(token: string): Observable<Array<MapDto>> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get<Array<MapDto>>(DIR, {headers});
  }

  public getById(id: string, token: string): Observable<MapDto> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get<MapDto>(DIR+'/'+id, {headers});
  }

  public deleteMap(id: string, token: string): Observable<{}> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.delete<{}>(DIR+"/"+id, {headers});
  }

  public deleteMapAssociation(id: string, token: string ): Observable<{}> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.delete<{}>(DIR+"/adoption/"+id, {headers});
  }
}
