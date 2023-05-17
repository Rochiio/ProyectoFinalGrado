import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AssociationToken } from 'src/app/models/association/association-token/association-token';

import { Login } from 'src/app/models/login/login';
import { UserToken } from 'src/app/models/user/user-token/user-token';
import { AssociationRestClientService } from 'src/app/services/api/association/association-rest-client.service';
import { UserRestClientService } from 'src/app/services/api/user/user-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

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
    private userRest: UserRestClientService,
    private associationRest: AssociationRestClientService,
    private notificationService: NotificationsService
    ){
    this.login = new Login();
    this.association = false;
  }

  ngOnInit(): void {
    localStorage.clear;
  }

  public submitUser(): void {
    if (this.association){
        this.associationRest.associationLogin(this.login).subscribe(
          (data: AssociationToken) => {
            localStorage.setItem('isAssociation', 'true');
            localStorage.setItem('currentAssociation', JSON.stringify(data));
            localStorage.setItem('access_token', data.token);
            localStorage.setItem('actual_username', data.association.username);
            this.router.navigate(['/principal']);
          },
          (err: Error) => {
            this.notificationService.showError(err.message);
          }
        )
    }else{
      this.userRest.userLogin(this.login).subscribe(
        (data: UserToken) => {
          localStorage.setItem('isAssociation', 'false');
          localStorage.setItem('currentUser', JSON.stringify(data));
          localStorage.setItem('access_token', data.token);
          localStorage.setItem('actual_username', data.user.username);
          this.router.navigate(['/principal']);
        },
        (err: Error) => {
          this.notificationService.showError(err.message);
        }
      )
    }

  }




}
