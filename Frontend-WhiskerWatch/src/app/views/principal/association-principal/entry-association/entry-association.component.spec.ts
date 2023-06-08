import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntryAssociationComponent } from './entry-association.component';

describe('EntryAssociationComponent', () => {
  let component: EntryAssociationComponent;
  let fixture: ComponentFixture<EntryAssociationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EntryAssociationComponent]
    });
    fixture = TestBed.createComponent(EntryAssociationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
