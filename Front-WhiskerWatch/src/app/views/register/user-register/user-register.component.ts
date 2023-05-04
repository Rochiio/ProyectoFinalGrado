import { Component } from '@angular/core';
import { UserCreate } from 'src/app/models/user/user-create/user-create';
import { UserToken } from 'src/app/models/user/user-token/user-token';
import { UserRestClientService } from 'src/app/services/api/user/user-rest-client.service';

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent {
  public iconRoute = 'assets/icons/'
  public userCreate: UserCreate

  constructor(
    private userRest: UserRestClientService
  ){
    this.userCreate = new UserCreate();
  }

  public registerUser(): void {
    this.userRest.userRegister(this.userCreate).subscribe(
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
