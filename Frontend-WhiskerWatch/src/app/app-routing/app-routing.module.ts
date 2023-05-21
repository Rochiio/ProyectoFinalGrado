import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';

import { PaginaNoEncontradaComponent } from '../views/pagina-no-encontrada/pagina-no-encontrada.component';
import { LoginComponent } from '../views/login/login.component';
import { UserRegisterComponent } from '../views/register/user-register/user-register.component';
import { AssociationRegisterComponent } from '../views/register/association-register/association-register.component';
import { CanActivateAuthGuard } from '../guard/can-active-auto-guard.service';
import { MapsPrincipalComponent } from '../views/principal/maps-principal/maps-principal.component';
import { ForumPrincipalComponent } from '../views/principal/forum-principal/forum-principal.component';
import { CalendarPrincipalComponent } from '../views/principal/calendar-principal/calendar-principal.component';
import { AssociationPrincipalComponent } from '../views/principal/association-principal/association-principal.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'user-register', component: UserRegisterComponent},
  {path: 'association-register', component: AssociationRegisterComponent},
  //{path: 'principal', component: MapsPrincipalComponent},
  {path: 'principal', data: {role:['ADMIN','USER','ASSOCIATION']}, canActivate: [CanActivateAuthGuard], component: MapsPrincipalComponent },
  {path: 'forum', component: ForumPrincipalComponent},
  {path: 'calendar', component: CalendarPrincipalComponent},
  {path: 'associations', component: AssociationPrincipalComponent},

  {path:'', redirectTo: '/login', pathMatch:'full'},
  {path: '**', component: PaginaNoEncontradaComponent}
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule { }
