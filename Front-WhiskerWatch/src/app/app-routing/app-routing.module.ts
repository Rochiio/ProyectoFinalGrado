import { NgModule } from '@angular/core';
import { RouterModule, Routes} from '@angular/router';

import { PaginaNoEncontradaComponent } from '../views/pagina-no-encontrada/pagina-no-encontrada.component';
import { LoginComponent } from '../views/login/login.component';
import { UserRegisterComponent } from '../views/register/user-register/user-register.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'user-register', component: UserRegisterComponent},

  {path:'', redirectTo: '/login', pathMatch:'full'},
  {path: '**', component: PaginaNoEncontradaComponent}
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule { }
