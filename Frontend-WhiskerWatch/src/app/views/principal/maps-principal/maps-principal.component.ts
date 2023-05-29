import { Component, OnInit, AfterViewInit, Input, ElementRef } from '@angular/core';
import { MapCreate } from 'src/app/models/maps/map-create/map-create';
import { MapDto } from 'src/app/models/maps/map-dto/map-dto';
import { UserToken } from 'src/app/models/user/user-token/user-token';
import { MapRestClientService } from 'src/app/services/api/maps/map-rest-client.service';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

@Component({
  selector: 'app-maps-principal',
  templateUrl: './maps-principal.component.html',
  styleUrls: ['./maps-principal.component.css']
})
export class MapsPrincipalComponent implements OnInit {
  public lat!: number;
  public lon!: number;
  public disabled!: boolean;
  public isAdmin: boolean = false;
  public isAssociation: boolean = false;

  public newLatitude!: string;
  public newLongitude!: string;

  constructor(
    private mapService: MapRestClientService,
    private notificationService: NotificationsService
  ){}

    ngOnInit(): void {
      if(localStorage.getItem('isAssociation')! == 'false'){
        let user: UserToken = JSON.parse(localStorage.getItem('currentUser')!);
        if(user.user.rol == 'ADMIN'){
          this.isAdmin = true;
        }
      }else{
        this.isAssociation = true;
      }
    }


  /**
   *  Cambiar la latitud y longitud a mostrar.
   * @param newItem el nuevo numero para poner como coordenada
   * @param isLat si el latitud o longitud
   */
  addItem(newItem: number, isLat: boolean) {
    if(isLat){
      this.lat = newItem;
    }else{
      this.lon = newItem;
    }
  }

  /**
   * Cambiar si esta desactivado realizar ciertas acciones.
   */
  changeDisabled(item: boolean): void{
    this.disabled = item;
  }


  /**
   * Eliminar un localizacion
   */
  deleteLocation(): void {
    this.mapService.deleteMap(localStorage.getItem('actual_maps_id')!, localStorage.getItem('access_token')!).subscribe(
      (data: any) => {
        window.location.reload();
        this.notificationService.showCorrect('Localización eliminada correctamente');
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );
  }


  /**
   * Eliminar un localizacion por recogida de asociacion.
   */
  deleteLocationAssociation(): void {
    this.mapService.deleteMapAssociation(localStorage.getItem('actual_maps_id')!, localStorage.getItem('access_token')!).subscribe(
      (data: any) => {
        window.location.reload();
        this.notificationService.showCorrect('Localización eliminada por recogida correctamente');
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );
  }

  /**
   * Crear una nueva localizacion en el mapa.
   */
  sendNewLocation(): void {
    if(this.areCoordinatesCorrect()){
      let createMap: MapCreate = {latitude: this.newLatitude, longitude: this.newLongitude};
      this.mapService.postMap(localStorage.getItem('access_token')!, createMap).subscribe(
        (data: MapDto) => {
          window.location.reload();
          this.notificationService.showCorrect('Localización creada correctamente');
        },
        (err: Error) => {
          this.notificationService.showError(err.message);
        }
      );
    }else{
      this.notificationService.showError('Latitud o Longitud incorrecta (máximo decimales: 9)');
    }
  }

  /**
   * Comprueba si los nuevos datos de longitud y latitud son correctos.
   */
  private areCoordinatesCorrect() :boolean {
    if (this.newLatitude.length == 0 || this.newLongitude.length == 0
      || !this.newLatitude.match('^([+-])?(?:90(?:\.0{1,6})?|((?:|[1-8])[0-9])(?:\.[0-9]{1,9})?)$')
      || !this.newLongitude.match('^([+-])?(?:180(?:\.0{1,6})?|((?:|[1-9]|1[0-7])[0-9])(?:\.[0-9]{1,9})?)$')){
        return false;
      }
      return true;
  }


}


