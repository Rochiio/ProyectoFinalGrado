import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Login } from 'src/app/models/login/login';
import { UserCreate } from 'src/app/models/user/user-create/user-create';
import { UserToken } from 'src/app/models/user/user-token/user-token';

const DIR = 'http://127.0.0.1:6969/user'
@Injectable({
  providedIn: 'root'
})
export class UserRestClientService {

  constructor(private httpClient: HttpClient) { }

  public userLogin(login: Login): Observable<UserToken> {
    return this.httpClient.post<UserToken>(DIR + '/login', login)
  }

  public userRegister(register: UserCreate): Observable<UserToken> {
    return this.httpClient.post<UserToken>(DIR + '/register', register)
  }

  public deleteUser(token: string, id: string): Observable<{}> {
    let headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.httpClient.delete<{}>(DIR+"/"+id, {headers});
  }


}
