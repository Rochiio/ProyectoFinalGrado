import { TestBed } from '@angular/core/testing';

import { UserRestClientService } from './user-rest-client.service';

describe('UserRestClientService', () => {
  let service: UserRestClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserRestClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
