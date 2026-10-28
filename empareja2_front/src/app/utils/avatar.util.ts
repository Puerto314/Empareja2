export const hueOf = (t: string) => [...t].reduce((a, c) => (a * 31 + c.charCodeAt(0)) % 360, 7);
export const gradient = (h: number) => `linear-gradient(135deg, hsl(${h} 75% 62%), hsl(${(h + 45) % 360} 70% 42%))`;
export const initials = (n: string) => n.split(' ').map(p => p[0]).slice(0, 2).join('');
export const siglaDe = (nombre: string) =>
  nombre.split(' ').filter(w => /^[A-ZÁÉÍÓÚ]/.test(w)).map(w => w[0]).join('');
