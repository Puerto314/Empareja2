import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { UniversidadesComponent } from './pages/universidades/universidades.component';
import { EstudiantesComponent } from './pages/estudiantes/estudiantes.component';
import { PerfilComponent } from './pages/perfil/perfil.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'login', component: LoginComponent },
  { path: 'universidades', component: UniversidadesComponent },
  { path: 'universidades/:id', component: EstudiantesComponent },
  { path: 'estudiantes/:id', component: PerfilComponent },
  { path: '**', redirectTo: 'login' }
];
