import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Front-WhiskerWatch';

  constructor(private router :Router){}

  public showMenu() :boolean {
    if(this.router.url.includes('/login')){
      return false;
    }
    return true;
  }
}
