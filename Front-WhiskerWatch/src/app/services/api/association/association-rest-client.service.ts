import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AssociationCreate } from 'src/app/models/association/association-create/association-create';
import { AssociationToken } from 'src/app/models/association/association-token/association-token';
import { Login } from 'src/app/models/login/login';

const DIR = 'http://127.0.0.1:6969/association'
@Injectable({
  providedIn: 'root'
})
export class AssociationRestClientService {

  constructor(private httpClient: HttpClient) { }

  public associationLogin(login: Login): Observable<AssociationToken> {
    return this.httpClient.post<AssociationToken>(DIR + '/login', login)
  }

  public associationRegister(register: AssociationCreate): Observable<AssociationToken> {
    return this.httpClient.post<AssociationToken>(DIR + '/register', register)
  }

}
