import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { MatDialog } from '@angular/material/dialog';
import { DeleteAccountComponent } from './delete-account/delete-account.component';
import { async } from 'rxjs';
import { AssociationToken } from 'src/app/models/association/association-token/association-token';
import { Profile } from 'src/app/models/profile/profile';
import { UserToken } from 'src/app/models/user/user-token/user-token';
import { AssociationRestClientService } from 'src/app/services/api/association/association-rest-client.service';
import { UserRestClientService } from 'src/app/services/api/user/user-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-principal',
  templateUrl: './profile-principal.component.html',
  styleUrls: ['./profile-principal.component.css']
})
export class ProfilePrincipalComponent implements OnInit{
  public actualProfile: Profile;
  public isAssociationProfile: boolean = false;
  public previousImg!: string;
  public loading: boolean = false;

  private image: any;
  private actualAssociation!: AssociationToken;
  private actualUser!: UserToken;


  constructor(
    private sanitizer: DomSanitizer,
    private associationService: AssociationRestClientService,
    private userService: UserRestClientService,
    private notificationService: NotificationsService,
    private router: Router,
    public dialogo: MatDialog
  ) {
    this.actualProfile = {name: '', email: '', username: '', password:'', repeat_password: '', description:'', url:'', img:''};
  }


  ngOnInit(): void {
    this.isAssociation();
    this.putActualData();
  }


  /**
   * Comprobar si el usuario loggeado es una asociacion.
   */
  private isAssociation(): void{
    let isAssociation = localStorage.getItem('isAssociation')!;
    this.isAssociationProfile = (isAssociation == 'true');
  }


  /**
   * Poner los datos actuales del usuario loggeado.
   */
  private putActualData(): void{
    if(this.isAssociationProfile == true){
      let actualAssociation: AssociationToken = JSON.parse(localStorage.getItem('currentAssociation')!);
      this.actualAssociation = actualAssociation;
      this.actualProfile = {name: actualAssociation.association.name, email: actualAssociation.association.email, username: actualAssociation.association.username,
         password:'', repeat_password: '', description:actualAssociation.association.description, url:actualAssociation.association.url, img:''};
    }else{
      let actualUser: UserToken = JSON.parse(localStorage.getItem('currentUser')!);
      this.actualUser = actualUser;
      this.actualProfile = {name: actualUser.user.name, email: actualUser.user.email, username: actualUser.user.username,
         password:'', repeat_password: '', description:'', url:'', img:''};
    }
  }


  /**
   * Comprobar si las contraseñas son iguales.
   * @returns si son iguales o no.
   */
  public checkCorrectPassword(): boolean{
    return this.actualProfile.password == this.actualProfile.repeat_password;
  }



  public updateProfile(): void {}


  /**
   * Eliminar la cuenta del usuario.
   */
  public deleteAccount(): void {
    this.dialogo.open(DeleteAccountComponent, {})
      .afterClosed()
      .subscribe((confirmado: Boolean) => {
        if (confirmado) {
          this.deleteAccountAction();
        }
    });
  }


  /**
   * Eliminar la cuenta de la API
   */
  public deleteAccountAction () : void {
    if(this.isAssociationProfile){
      console.log('Entro')
      this.associationService.deleteAssociation(localStorage.getItem('access_token')!, this.actualAssociation.association.id).subscribe(
        (data: any) => {
        console.log('Entro');
          this.notificationService.showCorrect('Cuenta eliminada correctamente');
          this.router.navigate(['/login']);
        },
        (err: Error) => {
          this.notificationService.showError(err.message);
        }
      )
    }else{
      this.userService.deleteUser(localStorage.getItem('access_token')!, this.actualUser.user.id).subscribe(
        (data: any) => {
          this.notificationService.showCorrect('Cuenta eliminada correctamente');
          this.router.navigate(['/login']);
        },
        (err: Error) => {
          this.notificationService.showError(err.message);
        }
      )
    }
  }


  /**
   * Accion al elegir una imagen
   * @param event imagen seleccionada.
   */
  public postImage(event: any): void{
    const captureImage = event.target.files[0];
    this.extractBase64(captureImage).then((imagen: any) => {
      this.previousImg = imagen.base;
    });

    this.image = captureImage;
    console.log(this.image);
  }


  /**
   * Subir la imagen seleccionada a la API.
   */
  public sentUpdatedImage(): void{
    try{
      this.loading = true;
      const data = new FormData();
      data.append('', this.image);

      //TODO lo hace pero aperece como si lo estuviese haciendo mal, pero en el back no da ningun problema ni error
      this.associationService.postAssociationImage(localStorage.getItem("access_token")!, this.actualAssociation.association.id!, data).subscribe(
        (data: any) => {
          this.loading = false;
          this.previousImg = '';
          this.notificationService.showCorrect("Imagen actualizada correctamente");
        },
        (err: Error) => {
          this.loading = false;
          this.notificationService.showError(err.message);
        }
      );
    }catch (e) {
      this.loading = false;
      console.log('Error: '+ e);
    }
  }


  /**
   * Pasar a Base64 la imagen seleccionada por el usuario para ver la previsualizacion de la imagen
   * @param $event imagen seleccionada
   * @returns la imagen en base64
   */
  extractBase64 = async ($event: any) => new Promise((resolve, reject) => {
      const unsafeImg = window.URL.createObjectURL($event);
      const image = this.sanitizer.bypassSecurityTrustUrl(unsafeImg);
      const reader = new FileReader();
      reader.readAsDataURL($event);

      reader.onload = () => resolve({base: reader.result});
      reader.onerror = error => reject(error);
  })


}
