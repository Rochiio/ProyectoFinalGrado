import { TestBed } from '@angular/core/testing';

import { AssociationRestClientService } from './association-rest-client.service';

describe('AssociationRestClientService', () => {
  let service: AssociationRestClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssociationRestClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
