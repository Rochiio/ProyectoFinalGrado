import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Front-WhiskerWatch';
  private routes = ['/principal', '/forum', '/calendar', '/associations', '/profile'];

  constructor(private router :Router){}

  public showMenu() :boolean {
    var canShow = false;
    for (var i = 0; i < this.routes.length; i++){
      if (this.router.url.includes(this.routes[i])){
        canShow = true;
      }
    }
    return canShow;
  }
}
