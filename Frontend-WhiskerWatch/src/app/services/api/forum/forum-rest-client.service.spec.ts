import { TestBed } from '@angular/core/testing';

import { ForumRestClientService } from './forum-rest-client.service';

describe('ForumRestClientService', () => {
  let service: ForumRestClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ForumRestClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
