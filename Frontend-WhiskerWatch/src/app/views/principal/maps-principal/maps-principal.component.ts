import { Component, OnInit} from '@angular/core';
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
      this.newLatitude = newItem.toString();
    }else{
      this.newLongitude = newItem.toString();
    }
  }

  /**
   * Cambiar si esta desactivado realizar ciertas acciones.
   */
  changeDisabled(item: boolean): void{
    this.disabled = item;
  }


  /**
   * Comprobar si se puede enviar para añadir una nueva localizacion
   * @returns si es correcto o no.
   */
  public correctToSend(): boolean {
    if(this.newLatitude && this.newLongitude){
      return true;
    }else{
      return false;
    }
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
      (err: ErrorEvent) => {
        this.notificationService.showError(err.error);
      }
    );
  }


  /**
   * Eliminar un localizacion por recogida de asociacion.
   */
  deleteLocationAssociation(): void {
    this.mapService.deleteMapAssociation(localStorage.getItem('actual_maps_id')!, localStorage.getItem('access_token')!).subscribe(
      (data: any) => {
        this.notificationService.showCorrect('Localización eliminada por recogida correctamente');
        window.location.reload();
      },
      (err: ErrorEvent) => {
        this.notificationService.showError(err.error);
      }
    );
  }

  /**
   * Crear una nueva localizacion en el mapa.
   */
  sendNewLocation(): void {
    let createMap: MapCreate = {latitude: this.newLatitude, longitude: this.newLongitude};
    this.mapService.postMap(localStorage.getItem('access_token')!, createMap).subscribe(
      (data: MapDto) => {
        this.notificationService.showCorrect('Localización creada correctamente');
        window.location.reload();
       },
      (err: ErrorEvent) => {
        this.notificationService.showError(err.error);
      }
    );
  }


}


