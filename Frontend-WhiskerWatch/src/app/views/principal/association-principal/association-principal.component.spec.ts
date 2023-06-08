import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssociationPrincipalComponent } from './association-principal.component';

describe('AssociationPrincipalComponent', () => {
  let component: AssociationPrincipalComponent;
  let fixture: ComponentFixture<AssociationPrincipalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssociationPrincipalComponent]
    });
    fixture = TestBed.createComponent(AssociationPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
