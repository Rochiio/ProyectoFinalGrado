import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapsPrincipalComponent } from './maps-principal.component';

describe('MapsPrincipalComponent', () => {
  let component: MapsPrincipalComponent;
  let fixture: ComponentFixture<MapsPrincipalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MapsPrincipalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MapsPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
