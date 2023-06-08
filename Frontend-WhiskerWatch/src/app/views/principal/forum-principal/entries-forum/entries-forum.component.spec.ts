import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntriesForumComponent } from './entries-forum.component';

describe('EntriesForumComponent', () => {
  let component: EntriesForumComponent;
  let fixture: ComponentFixture<EntriesForumComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EntriesForumComponent]
    });
    fixture = TestBed.createComponent(EntriesForumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
