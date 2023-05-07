import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AssociationCreate } from 'src/app/models/association/association-create/association-create';
import { AssociationToken } from 'src/app/models/association/association-token/association-token';
import { AssociationRestClientService } from 'src/app/services/api/association/association-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

@Component({
  selector: 'app-association-register',
  templateUrl: './association-register.component.html',
  styleUrls: ['./association-register.component.css']
})
export class AssociationRegisterComponent {
  public repeatPass: string= '' ;
  public associationCreate: AssociationCreate;

  constructor(
    private associationRest: AssociationRestClientService,
    private notificationService: NotificationsService,
    private router: Router
  ){
    this.associationCreate = new AssociationCreate();
  }

  public registerAssociation(): void {
    this.associationRest.associationRegister(this.associationCreate).subscribe(
      (data: AssociationToken) => {
        this.router.navigate(['/login']);
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }

  public checkCorrectPassword(): boolean{
    return this.associationCreate.password == this.repeatPass
  }
}
