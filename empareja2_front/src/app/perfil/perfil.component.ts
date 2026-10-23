import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap } from 'rxjs';
import { EstudianteService } from '../services/estudiante.service';
import { gradient, hueOf, initials } from '../utils/avatar.util';

@Component({
  standalone: false,
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent {
  private estudianteService = inject(EstudianteService);
  private id$ = inject(ActivatedRoute).paramMap.pipe(map(p => Number(p.get('id'))));

  s = toSignal(this.id$.pipe(switchMap(id => this.estudianteService.obtenerPorId(id))));
  otros = toSignal(this.id$.pipe(switchMap(id => this.estudianteService.listarCompaneros(id))), { initialValue: [] });
  vacias = [0, 1, 2];
  grad = gradient; hue = hueOf; ini = initials;
}
