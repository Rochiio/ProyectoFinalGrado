import { TestBed } from '@angular/core/testing';

import { MapRestClientService } from './map-rest-client.service';

describe('MapRestClientService', () => {
  let service: MapRestClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MapRestClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
