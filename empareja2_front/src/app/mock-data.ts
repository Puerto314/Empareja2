// Datos de prueba con la misma forma que StudentPublicDTO. Los campos opcionales no existen aún en el backend.
export interface Student {
  id: number; fullName: string; career: string; semester: number; university: string;
  bio?: string; interests?: string[]; photos?: string[];
}

export const STUDENTS: Student[] = [
  { id: 1, fullName: 'Valentina Ríos', career: 'Ingeniería de Sistemas', semester: 4, university: 'Universidad El Bosque' },
  { id: 2, fullName: 'Santiago Pardo', career: 'Medicina', semester: 7, university: 'Universidad El Bosque' },
  { id: 3, fullName: 'Camila Torres', career: 'Psicología', semester: 2, university: 'Universidad El Bosque' },
  { id: 4, fullName: 'Andrés Molina', career: 'Diseño Industrial', semester: 5, university: 'Universidad El Bosque' },
  { id: 5, fullName: 'Laura Beltrán', career: 'Biología', semester: 9, university: 'Universidad Nacional' },
  { id: 6, fullName: 'Daniel Quintero', career: 'Ingeniería Mecánica', semester: 3, university: 'Universidad Nacional' },
  { id: 7, fullName: 'Mariana Gómez', career: 'Comunicación Social', semester: 6, university: 'Pontificia Universidad Javeriana' },
  { id: 8, fullName: 'Felipe Acosta', career: 'Derecho', semester: 8, university: 'Pontificia Universidad Javeriana' },
  { id: 9, fullName: 'Sofía Herrera', career: 'Arquitectura', semester: 1, university: 'Universidad de los Andes' },
  { id: 10, fullName: 'Julián Castro', career: 'Economía', semester: 10, university: 'Universidad del Rosario' },
  { id: 11, fullName: 'Natalia Vargas', career: 'Relaciones Internacionales', semester: 4, university: 'Universidad Externado' }
];

// El backend manda "university" como texto: las universidades salen de agrupar ese texto.
export const universitiesOf = (list: Student[]) =>
  Array.from(new Set(list.map(s => s.university))).sort((a, b) => a.localeCompare(b));

export const hueOf = (t: string) => [...t].reduce((a, c) => (a * 31 + c.charCodeAt(0)) % 360, 7);
export const gradient = (h: number) => `linear-gradient(135deg, hsl(${h} 75% 62%), hsl(${(h + 45) % 360} 70% 42%))`;
export const initials = (n: string) => n.split(' ').map(p => p[0]).slice(0, 2).join('');
