import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UserToken } from '../models/user/user-token/user-token';
import { AssociationToken } from '../models/association/association-token/association-token';

@Injectable( {
  providedIn: 'root'
})
export class CanActivateAuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot,
     state: RouterStateSnapshot
     ): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
      return this.checkRoles(route);
  }

  checkRoles(route: ActivatedRouteSnapshot): boolean {
    let isAssociation = localStorage.getItem('isAssociation');
    const {scopes = []} = route.data["role"];
    if(isAssociation == 'true'){
      let association: AssociationToken = JSON.parse(localStorage.getItem('currentAssociation')!);
      return scopes.includes(association.association.rol);
    }else{
      let user: UserToken = JSON.parse(localStorage.getItem('currentUser')!);
      return scopes.includes(user.user.rol);
    }
  }

}
