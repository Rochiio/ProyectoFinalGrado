import { Component, OnInit, AfterViewInit, Input, ElementRef, Output, EventEmitter } from '@angular/core';
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import * as Proj from 'ol/proj';
import {defaults as defaultControls} from 'ol/control';
import { Feature } from 'ol';
import { Circle, Point } from 'ol/geom';
import Style from 'ol/style/Style';
import Icon from 'ol/style/Icon';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { MapRestClientService } from 'src/app/services/api/maps/map-rest-client.service';
import { MapDto } from 'src/app/models/maps/map-dto/map-dto';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';
import Select, { SelectEvent } from 'ol/interaction/Select';


export const DEFAULT_HEIGHT = '500px';
export const DEFAULT_WIDTH = '100%';
@Component({
  selector: 'app-ol-map',
  templateUrl: './ol-map.component.html',
  styleUrls: ['./ol-map.component.css']
})
export class OlMapComponent implements OnInit, AfterViewInit{
  @Output() acutalLat = new EventEmitter<number>();
  @Output() acutalLng = new EventEmitter<number>();
  @Output() disabledButton = new EventEmitter<boolean>();

  private lat: number = 40.364002116999536;
  private lon: number = -3.7455721740194017;
  private zoom: number = 16;
  private icon = 'assets/icons/marker.png'

  private listaMapas:MapDto[] = [];

  marcadores: Feature<Point>[] = [];


  map!:Map;
  private mapEl!: HTMLElement;

  constructor(
    private element: ElementRef,
    private mapRest: MapRestClientService,
    private notificationService: NotificationsService) {
    }

  ngOnInit(): void {
    this.permitGeolocation();
    this.mapEl = this.element.nativeElement.querySelector('#map');
    this.setSize();
  }

  ngAfterViewInit(): void {
    let center = Proj.fromLonLat([this.lon, this.lat]);

    this.map = new Map({
      target: 'map',
      layers: [
        new TileLayer({
          source: new XYZ({
            url: 'http://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
          })
        })
      ],

      view: new View({
        center: center,
        zoom: this.zoom
      }),
      controls: defaultControls().extend([])
    });


    this.addMarkers();
    this.addMapEvents();
    this.addSelection();

    this.acutalLat.emit(this.lat);
    this.acutalLng.emit(this.lon);
    this.disabledButton.emit(true);
  }

  private setSize(): void {
    if(this.mapEl){
      const styles = this.mapEl.style;
      styles.height = DEFAULT_HEIGHT;
      styles.width = DEFAULT_WIDTH;
    }
  }

  /**
  *Añadir los marcadores al mapa.
  * @returns vector con todos los marcadores añadidos.
  */
  private addMarkers() {
    let ultimaCapa;

    this.mapRest.getAllMaps(localStorage.getItem('access_token')!).subscribe(
      (data: Array<MapDto>) => {
        this.listaMapas = data;
        data.forEach(coordenada => {
          let marcador = new Feature({
              geometry: new Point(
                  Proj.fromLonLat([Number.parseFloat(coordenada.longitude), Number.parseFloat(coordenada.latitude)])
              ),
          });
          marcador.setId(coordenada.id);
          marcador.setStyle(new Style({
              image: new Icon(({
                  src: this.icon,
                  scale: 0.1
              }))
          }));
          this.marcadores.push(marcador);
      });
      ultimaCapa = new VectorLayer({
          source: new VectorSource({
              features: this.marcadores,
          }),
      });
      this.map.addLayer(ultimaCapa);
      },

      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );
  }

  /**
   * Añadir los eventos del mapa.
   * @param map mapa añadir el evento.
   */
  private addMapEvents() {
    this.map.on('singleclick', (evt) => {
      this.map.forEachFeatureAtPixel(evt.pixel, (feature, layer) => {
        this.disabledButton.emit(false);
        localStorage.setItem('actual_maps_id', feature.getId()!.toString());
        console.log(feature.getId()!.toString());
      });
    })
  }


  /**
   * Añadir seleccion al mapa para poder añadirlo como localizacion.
   */
  private addSelection() {
    this.map.on('click', (evt) => {
       var lonLat = Proj.toLonLat(evt.coordinate);
       this.acutalLat.emit(lonLat[1]);
       this.acutalLng.emit(lonLat[0]);
    });
  }


  private permitGeolocation(): void{
    this.getPosition().then(pos => {
      this.lat = pos.lat;
      this.lon = pos.lng;
  });

  }

  getPosition(): Promise<any> {
    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resp => {
                resolve({lng: resp.coords.longitude, lat: resp.coords.latitude});
            },
            err => {
                reject(err);
          });
    });
}

}






