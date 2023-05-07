import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu-principal',
  templateUrl: './menu-principal.component.html',
  styleUrls: ['./menu-principal.component.css']
})
export class MenuPrincipalComponent {
  public imgUrl: string = 'assets/icons'

  constructor(
    private router: Router
  ) {}

  public closeSession() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
