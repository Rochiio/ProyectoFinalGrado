import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserCreate } from 'src/app/models/user/user-create/user-create';
import { UserToken } from 'src/app/models/user/user-token/user-token';
import { UserRestClientService } from 'src/app/services/api/user/user-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent {
  public repeatPass: string= '' ;
  public userCreate: UserCreate

  constructor(
    private userRest: UserRestClientService,
    private notificationService: NotificationsService,
    private router: Router
  ){
    this.userCreate = new UserCreate();
  }

  public registerUser(): void {
    this.userRest.userRegister(this.userCreate).subscribe(
      (data: UserToken) => {
        this.notificationService.showCorrect('Cuenta creada correctamente');
        this.router.navigate(['/login']);
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }

  public checkCorrectPassword(): boolean{
    return this.userCreate.password == this.repeatPass
  }

}
