import { Component, OnInit } from '@angular/core';
import { AssociationDto } from 'src/app/models/association/association-dto/association-dto';
import { AssociationRestClientService } from 'src/app/services/api/association/association-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

@Component({
  selector: 'app-association-principal',
  templateUrl: './association-principal.component.html',
  styleUrls: ['./association-principal.component.css']
})
export class AssociationPrincipalComponent implements OnInit {
  public associations: AssociationDto[];

  constructor(
    private associationService: AssociationRestClientService,
    private notificationService: NotificationsService
  ){
    this.associations = [];
  }

  ngOnInit(): void {
      this.getAllAssociations();
  }

  private getAllAssociations(): void {
    this.associationService.getAllAssociations(localStorage.getItem('access_token')!).subscribe(
      (data: AssociationDto[]) => {
        this.associations = data;
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    )
  }

}
