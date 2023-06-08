import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarPrincipalComponent } from './calendar-principal.component';

describe('CalendarPrincipalComponent', () => {
  let component: CalendarPrincipalComponent;
  let fixture: ComponentFixture<CalendarPrincipalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CalendarPrincipalComponent]
    });
    fixture = TestBed.createComponent(CalendarPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
