import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { UniversidadesComponent } from './universidades/universidades.component';
import { EstudiantesComponent } from './estudiantes/estudiantes.component';
import { PerfilComponent } from './perfil/perfil.component';
import { RegistroComponent } from './registro/registro.component';
import { PerfilFormComponent } from './perfil-form/perfil-form.component';
import { MiPerfilComponent } from './mi-perfil/mi-perfil.component';
import { MensajesComponent } from './mensajes/mensajes.component';
import { RankingComponent } from './ranking/ranking.component';
import { AdminComponent } from './admin/admin.component';
import { AdminUsuariosComponent } from './admin/admin-usuarios.component';
import { AdminReportesComponent } from './admin/admin-reportes.component';
import { AdminComentariosComponent } from './admin/admin-comentarios.component';
import { AdminMeGustaComponent } from './admin/admin-megusta.component';
import { AdminCatalogosComponent } from './admin/admin-catalogos.component';
import { AuthInterceptor } from './services/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    UniversidadesComponent,
    EstudiantesComponent,
    PerfilComponent,
    RegistroComponent,
    PerfilFormComponent,
    MiPerfilComponent,
    MensajesComponent,
    RankingComponent,
    AdminComponent,
    AdminUsuariosComponent,
    AdminReportesComponent,
    AdminComentariosComponent,
    AdminMeGustaComponent,
    AdminCatalogosComponent
  ],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [
    provideHttpClient(withInterceptorsFromDi()),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
