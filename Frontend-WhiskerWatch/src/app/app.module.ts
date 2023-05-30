import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule  } from '@angular/forms';
import { FullCalendarModule } from '@fullcalendar/angular';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { DatePipe } from '@angular/common';

import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule} from '@angular/material/snack-bar';

import { AppComponent } from './app.component';
import { PaginaNoEncontradaComponent } from './views/pagina-no-encontrada/pagina-no-encontrada.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { LoginComponent } from './views/login/login.component';
import { UserRegisterComponent } from './views/register/user-register/user-register.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { AssociationRegisterComponent } from './views/register/association-register/association-register.component';
import { MenuPrincipalComponent } from './views/principal/menu-principal/menu-principal.component';
import { MapsPrincipalComponent } from './views/principal/maps-principal/maps-principal.component';
import { OlMapComponent } from './views/principal/maps-principal/ol-map/ol-map.component';
import { ForumPrincipalComponent } from './views/principal/forum-principal/forum-principal.component';
import { EntriesForumComponent } from './views/principal/forum-principal/entries-forum/entries-forum.component';
import { CalendarPrincipalComponent } from './views/principal/calendar-principal/calendar-principal.component';
import { AssociationPrincipalComponent } from './views/principal/association-principal/association-principal.component';
import { EntryAssociationComponent } from './views/principal/association-principal/entry-association/entry-association.component';
import { ProfilePrincipalComponent } from './views/principal/profile-principal/profile-principal.component';
import { DeleteAccountComponent } from './views/principal/profile-principal/delete-account/delete-account.component';
import { WebsocketComponent } from './views/websocket/websocket.component';


@NgModule({
  declarations: [
    AppComponent,
    PaginaNoEncontradaComponent,
    LoginComponent,
    UserRegisterComponent,
    AssociationRegisterComponent,
    MenuPrincipalComponent,
    MapsPrincipalComponent,
    OlMapComponent,
    ForumPrincipalComponent,
    EntriesForumComponent,
    CalendarPrincipalComponent,
    AssociationPrincipalComponent,
    EntryAssociationComponent,
    ProfilePrincipalComponent,
    DeleteAccountComponent,
    WebsocketComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NoopAnimationsModule,
    MatSnackBarModule,
    FullCalendarModule,
    MatDialogModule,
    MatButtonModule,
  ],
  providers: [
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 5000}},
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
