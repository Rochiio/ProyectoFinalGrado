import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AssociationCreate } from 'src/app/models/association/association-create/association-create';
import { AssociationDto } from 'src/app/models/association/association-dto/association-dto';
import { AssociationToken } from 'src/app/models/association/association-token/association-token';
import { Login } from 'src/app/models/login/login';

const DIR = 'http://127.0.0.1:6969/association'
@Injectable({
  providedIn: 'root'
})
export class AssociationRestClientService {

  constructor(private httpClient: HttpClient) { }

  public associationLogin(login: Login): Observable<AssociationToken> {
    return this.httpClient.post<AssociationToken>(DIR + '/login', login);
  }

  public associationRegister(register: AssociationCreate): Observable<AssociationToken> {
    return this.httpClient.post<AssociationToken>(DIR + '/register', register);
  }

  public getAllAssociations(token: string): Observable<Array<AssociationDto>> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get<Array<AssociationDto>>(DIR, {headers});
  }

  public deleteAssociation(token: string, id: string): Observable<{}> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.delete<{}>(DIR+"/"+id, {headers});
  }

  public getAssociationImage(token: string, id: string): Observable<Blob> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.get(DIR+"/image/"+id, { headers:headers, responseType: 'blob' });
  }

  public postAssociationImage(token: string, id: string, image:any): Observable<{}>{
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.post<string>(DIR+"/image/"+id, image, {headers});
  }

}
