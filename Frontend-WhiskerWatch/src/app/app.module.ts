import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule  } from '@angular/forms';
import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule} from '@angular/material/snack-bar';
import { GoogleMapsModule } from '@angular/google-maps';

import { AppComponent } from './app.component';
import { PaginaNoEncontradaComponent } from './views/pagina-no-encontrada/pagina-no-encontrada.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { LoginComponent } from './views/login/login.component';
import { UserRegisterComponent } from './views/register/user-register/user-register.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { AssociationRegisterComponent } from './views/register/association-register/association-register.component';
import { MenuPrincipalComponent } from './views/principal/menu-principal/menu-principal.component';
import { MapsPrincipalComponent } from './views/principal/maps-principal/maps-principal.component';

@NgModule({
  declarations: [
    AppComponent,
    PaginaNoEncontradaComponent,
    LoginComponent,
    UserRegisterComponent,
    AssociationRegisterComponent,
    MenuPrincipalComponent,
    MapsPrincipalComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NoopAnimationsModule,
    MatSnackBarModule,
    GoogleMapsModule
  ],
  providers: [
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 5000}}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
