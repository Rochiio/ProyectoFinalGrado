import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarEntryComponent } from './calendar-entry.component';

describe('CalendarEntryComponent', () => {
  let component: CalendarEntryComponent;
  let fixture: ComponentFixture<CalendarEntryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CalendarEntryComponent]
    });
    fixture = TestBed.createComponent(CalendarEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
