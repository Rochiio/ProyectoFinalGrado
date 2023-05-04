import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Login } from 'src/app/models/login/login';
import { UserToken } from 'src/app/models/user/user-token/user-token';
import { UserRestClientService } from 'src/app/services/api/user/user-rest-client.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public imageUrl: string = '/assets/images/welcome.png'

  public association: Boolean;
  public login: Login;

  constructor(
    private router: Router,
    private userRest: UserRestClientService
    ){
    this.login = new Login();
    this.association = false;
  }

  ngOnInit(): void {
    localStorage.clear;
  }

  public submitUser(): void {
    if (this.association){
        // TODO:
    }else{
      this.userRest.userLogin(this.login).subscribe(
        (data: UserToken) => {
          localStorage.setItem('isAssociation', 'false');
          localStorage.setItem('currentUser', JSON.stringify(data));
          alert('Usuario correcto' + JSON.stringify(data));
        },
        (err: Error) => {
          console.error(err.message);
        }
      )
    }

  }




}
