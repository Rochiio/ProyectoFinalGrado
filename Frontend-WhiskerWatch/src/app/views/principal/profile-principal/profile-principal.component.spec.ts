import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilePrincipalComponent } from './profile-principal.component';

describe('ProfilePrincipalComponent', () => {
  let component: ProfilePrincipalComponent;
  let fixture: ComponentFixture<ProfilePrincipalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfilePrincipalComponent]
    });
    fixture = TestBed.createComponent(ProfilePrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
