import { TestBed } from '@angular/core/testing';

import { CalendarRestClientService } from './calendar-rest-client.service';

describe('CalendarRestClientService', () => {
  let service: CalendarRestClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CalendarRestClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
