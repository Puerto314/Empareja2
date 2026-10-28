import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { AppComponent } from './app.component';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async () => {
    localStorage.clear();
    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
  });

  it('debe crearse', () => {
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('no debe mostrar la barra superior antes de navegar', () => {
    expect(fixture.nativeElement.querySelector('.topbar')).toBeNull();
  });

  it('debe leer el nombre del usuario guardado en la sesión', () => {
    localStorage.setItem('usuario', JSON.stringify({ nombre: 'Ana' }));
    expect(fixture.componentInstance.nombre).toBe('Ana');
  });
});
