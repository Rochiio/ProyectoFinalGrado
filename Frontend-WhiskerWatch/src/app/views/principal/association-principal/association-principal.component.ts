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
    let aso = new AssociationDto();
    aso.email = "prueba@gmail.com"
    aso.img = "null";
    aso.name="Rivanimal";
    aso.url = "https://rivanimal.org/";
    aso.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non lacus id nisi faucibus venenatis sed non mauris. Maecenas in luctus quam. Duis semper ultricies nisi a vehicula. Vivamus nulla ex, bibendum vitae pharetra eu, auctor id nisi. Sed quam nisl, ornare a purus id, elementum rutrum odio. Duis vel egestas erat, ut pulvinar massa. Fusce efficitur gravida ante, pellentesque porttitor velit euismod ut. Ut ullamcorper quis ligula vitae eleifend. In hac habitasse platea dictumst. Mauris eleifend odio vel ex mattis ultricies. Fusce mauris justo, iaculis in tristique venenatis, elementum at nibh. Duis ultricies sapien augue, eu commodo lorem cursus a. Curabitur rutrum arcu odio, sit amet finibus sem pulvinar at. Aliquam sit amet sapien non dolor rutrum cursus quis sit amet sapien. Praesent at leo felis. Maecenas a ullamcorper ante."

    this.associations = [aso, aso];
    //this.getAllAssociations();
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
