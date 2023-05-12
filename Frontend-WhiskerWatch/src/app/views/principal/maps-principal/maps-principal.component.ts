import { Component, OnInit, AfterViewInit, Input, ElementRef } from '@angular/core';

@Component({
  selector: 'app-maps-principal',
  templateUrl: './maps-principal.component.html',
  styleUrls: ['./maps-principal.component.css']
})
export class MapsPrincipalComponent {
  public lat!: number;
  public lon!: number;
  public disabled!: boolean;

  addItem(newItem: number, isLat: boolean) {
    if(isLat){
      this.lat = newItem;
    }else{
      this.lon = newItem;
    }
  }

  changeDisabled(item: boolean){
    this.disabled = item;
  }


}


