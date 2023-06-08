import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForumPrincipalComponent } from './forum-principal.component';

describe('ForumPrincipalComponent', () => {
  let component: ForumPrincipalComponent;
  let fixture: ComponentFixture<ForumPrincipalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ForumPrincipalComponent]
    });
    fixture = TestBed.createComponent(ForumPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
