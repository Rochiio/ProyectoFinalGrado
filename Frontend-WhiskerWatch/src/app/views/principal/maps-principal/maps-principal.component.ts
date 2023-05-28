import { Component, OnInit, AfterViewInit, Input, ElementRef } from '@angular/core';
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
        this.notificationService.showCorrect('Localización eliminada correctamente');
        window.location.reload();
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );
  }


  /**
   * Eliminar un localizacion por recogida de asociacion
   */
  deleteLocationAssociation(): void {
    this.mapService.deleteMapAssociation(localStorage.getItem('actual_maps_id')!, localStorage.getItem('access_token')!).subscribe(
      (data: any) => {
        this.notificationService.showCorrect('Localización eliminada por recogida correctamente');
        window.location.reload();
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );
  }



}


