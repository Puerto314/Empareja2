import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { UniversidadesComponent } from './universidades/universidades.component';
import { EstudiantesComponent } from './estudiantes/estudiantes.component';
import { PerfilComponent } from './perfil/perfil.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { RegistroComponent } from './registro/registro.component';
import { MiPerfilComponent } from './mi-perfil/mi-perfil.component';
import { MensajesComponent } from './mensajes/mensajes.component';
import { RankingComponent } from './ranking/ranking.component';
import { AdminComponent } from './admin/admin.component';
import { AdminUsuariosComponent } from './admin/admin-usuarios.component';
import { AdminReportesComponent } from './admin/admin-reportes.component';
import { AdminComentariosComponent } from './admin/admin-comentarios.component';
import { AdminMeGustaComponent } from './admin/admin-megusta.component';
import { AdminCatalogosComponent } from './admin/admin-catalogos.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'universidades', component: UniversidadesComponent, canActivate: [authGuard] },
  { path: 'universidades/:id', component: EstudiantesComponent, canActivate: [authGuard] },
  { path: 'estudiantes/:id', component: PerfilComponent, canActivate: [authGuard] },
  { path: 'ranking', component: RankingComponent, canActivate: [authGuard] },
  { path: 'mi-perfil', component: MiPerfilComponent, canActivate: [authGuard] },
  { path: 'mensajes', component: MensajesComponent, canActivate: [authGuard] },
  {
    path: 'admin', component: AdminComponent, canActivate: [adminGuard],
    children: [
      { path: '', redirectTo: 'usuarios', pathMatch: 'full' },
      { path: 'usuarios', component: AdminUsuariosComponent },
      { path: 'reportes', component: AdminReportesComponent },
      { path: 'comentarios', component: AdminComentariosComponent },
      { path: 'me-gusta', component: AdminMeGustaComponent },
      { path: 'catalogos', component: AdminCatalogosComponent }
    ]
  },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
