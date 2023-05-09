import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Front-WhiskerWatch';
  private routes = ['/login', '/user-register', '/association'];

  constructor(private router :Router){}

  public showMenu() :boolean {
    var canShow = true;
    for (var i = 0; i < this.routes.length; i++){
      if (this.router.url.includes(this.routes[i])){
        canShow = false;
      }
    }
    return canShow;
  }
}
