import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssociationRegisterComponent } from './association-register.component';

describe('AssociationRegisterComponent', () => {
  let component: AssociationRegisterComponent;
  let fixture: ComponentFixture<AssociationRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssociationRegisterComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssociationRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
