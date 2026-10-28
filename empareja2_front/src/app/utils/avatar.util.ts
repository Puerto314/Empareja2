export const hueOf = (t: string) => [...t].reduce((a, c) => (a * 31 + c.charCodeAt(0)) % 360, 7);
export const gradient = (h: number) => `linear-gradient(135deg, hsl(${h} 75% 62%), hsl(${(h + 45) % 360} 70% 42%))`;

// Iniciales: primera letra de la primera y de la última palabra ("María de los Ángeles Pérez" -> "MP").
export const initials = (n: string) => {
  const partes = n.split(/\s+/).filter(Boolean);
  const letras = partes.length > 1 ? [partes[0], partes[partes.length - 1]] : partes;
  return letras.map(p => p[0].toUpperCase()).join('');
};

export const siglaDe = (nombre: string) =>
  nombre.split(' ').filter(w => /^[A-ZÁÉÍÓÚ]/.test(w)).map(w => w[0]).join('');

// Sigla decorativa: la del backend si es corta (PUJ, UNAL); si no (UNBOSQUE), se arma con el nombre.
export const siglaCorta = (sigla: string, nombre: string) =>
  sigla && sigla.length <= 4 ? sigla : siglaDe(nombre);
