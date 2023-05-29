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
import { AssociationCreate } from 'src/app/models/association/association-create/association-create';
import { AssociationDto } from 'src/app/models/association/association-dto/association-dto';
import { UserCreate } from 'src/app/models/user/user-create/user-create';
import { UserDto } from 'src/app/models/user/user-dto/user-dto';
import { er } from '@fullcalendar/core/internal-common';

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
  private actualAssociation!: AssociationDto;
  private actualUser!: UserDto;


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
      this.actualAssociation = actualAssociation.association;
      this.actualProfile = {name: actualAssociation.association.name, email: actualAssociation.association.email, username: actualAssociation.association.username,
         password:'', repeat_password: '', description:actualAssociation.association.description, url:actualAssociation.association.url, img:''};
    }else{
      let actualUser: UserToken = JSON.parse(localStorage.getItem('currentUser')!);
      this.actualUser = actualUser.user;
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


  /**
   * Actualizar el perfil del usuario loggeado.
   */
  public updateProfile(): void {
    if(this.isAssociationProfile){
      let updateAssociation: AssociationCreate = {name: this.actualProfile.name, email: this.actualProfile.email, username: this.actualProfile.username,
        password: this.actualProfile.password, rol: this.actualAssociation.rol, description: this.actualProfile.description, url: this.actualProfile.url}
      this.associationService.putAssociation(localStorage.getItem('access_token')!, this.actualAssociation.id, updateAssociation).subscribe(
        (data: AssociationDto) => {
          this.actualAssociation = data;
          this.actualProfile = {name: data.name, email: data.email, username: data.username,
            password:'', repeat_password: '', description:data.description, url:data.url, img:''};
          this.notificationService.showCorrect("Perfil modificado correctamente");
        },
        (err: Error) => {
          this.notificationService.showError(err.message);
        }
      );
    }else{
      let updateUser: UserCreate = {name: this.actualProfile.name, email: this.actualProfile.email, username: this.actualProfile.username,
        password: this.actualProfile.password, rol: this.actualUser.rol}
      this.userService.putUser(localStorage.getItem('access_token')!, this.actualUser.id, updateUser).subscribe(
        (data: UserDto) => {
          this.actualUser = data;
          this.actualProfile = {name: data.name, email: data.email, username: data.username,
            password:'', repeat_password: '', description:'', url:'', img:''};
          this.notificationService.showCorrect("Perfil modificado correctamente");
        },
        (err: Error) => {
          this.notificationService.showError(err.message);
        }
      );
    }
  }


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
      this.associationService.deleteAssociation(localStorage.getItem('access_token')!, this.actualAssociation.id).subscribe(
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
      this.userService.deleteUser(localStorage.getItem('access_token')!, this.actualUser.id).subscribe(
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
      this.associationService.postAssociationImage(localStorage.getItem("access_token")!, this.actualAssociation.id!, data).subscribe(
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
