import { TestBed } from '@angular/core/testing';

import { CanActiveAutoGuardService } from './can-active-auto-guard.service';

describe('CanActiveAutoGuardService', () => {
  let service: CanActiveAutoGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CanActiveAutoGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
