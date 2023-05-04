import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UserToken } from '../models/user/user-token/user-token';

@Injectable( {
  providedIn: 'root'
})
export class CanActivateAuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot,
     state: RouterStateSnapshot
     ): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
      return this.checkLogin(route);
  }

  checkLogin(route: ActivatedRouteSnapshot): boolean {
    const user:UserToken = JSON.parse(localStorage.getItem('currentUser')!);
    if(user!= null && route.data) {
      return true;
    }else {
      return false;
    }
  }

}
