import { Component, OnInit, AfterViewInit, Input, ElementRef } from '@angular/core';
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import * as Proj from 'ol/proj';
import {defaults as defaultControls} from 'ol/control';
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import Style from 'ol/style/Style';
import Icon from 'ol/style/Icon';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { MapRestClientService } from 'src/app/services/api/maps/map-rest-client.service';
import { MapDto } from 'src/app/models/maps/map-dto/map-dto';
import { NotificationsService } from 'src/app/services/notifications/notifications.service';

export const DEFAULT_HEIGHT = '500px';
export const DEFAULT_WIDTH = '100%';
@Component({
  selector: 'app-ol-map',
  templateUrl: './ol-map.component.html',
  styleUrls: ['./ol-map.component.css']
})
export class OlMapComponent implements OnInit, AfterViewInit{
  private lat: number = 40.364002116999536;
  private lon: number = -3.7455721740194017;
  private zoom: number = 16;

  map!:Map;
  private mapEl!: HTMLElement;

  constructor(
    private element: ElementRef,
    private mapRest: MapRestClientService,
    private notificationService: NotificationsService) {
    }

  ngOnInit(): void {
    this.mapEl = this.element.nativeElement.querySelector('#map');
    this.setSize();
  }

  ngAfterViewInit(): void {
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
        center: Proj.fromLonLat([this.lon, this.lat]),
        zoom: this.zoom
      }),
      controls: defaultControls().extend([])
    });


    var capa = this.addMarkers();
    this.map.addLayer(capa);
    this.addMapEvents(this.map);
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
  private addMarkers(): VectorLayer<VectorSource<Point>> {
    const markers_list: Feature<Point>[] = [];

    this.mapRest.getAllMaps(localStorage.getItem('access_token')!).subscribe(
      (data: Array<MapDto>) => {
        for (let i = 0; i < data.length; i++) {
          var map = data[i];
          let marker = new Feature({geometry: new Point(Proj.fromLonLat([Number.parseInt(map.longitude), Number.parseInt(map.latitude)])),});
          marker.setStyle(new Style({
            image: new Icon({
              src: 'assets/icons/marker.png',
              scale: 0.1
            })
          }));

          markers_list.push(marker);
        }
      },
      (err: Error) => {
        this.notificationService.showError(err.message);
      }
    );

    return new VectorLayer({
      source: new VectorSource({
        features: markers_list
      }),
    });
  }

    /**
   * Añadir los eventos del mapa.
   * @param map mapa añadir el evento.
   */
  private addMapEvents(map: Map) {
    map.on('singleclick', (evt) => {
      var feature = map.forEachFeatureAtPixel(evt.pixel, (feature, layer) => {
        var lonLat = Proj.toLonLat(evt.coordinate);
        return lonLat;
      });
      if(feature){
        console.log(feature);
      }
    })
  }


}






