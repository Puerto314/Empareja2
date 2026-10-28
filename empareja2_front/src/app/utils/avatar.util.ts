export const hueOf = (t: string) => [...t].reduce((a, c) => (a * 31 + c.charCodeAt(0)) % 360, 7);
export const gradient = (h: number) => `linear-gradient(135deg, hsl(${h} 75% 62%), hsl(${(h + 45) % 360} 70% 42%))`;
<<<<<<< HEAD

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
=======
export const initials = (n: string) => n.split(' ').map(p => p[0]).slice(0, 2).join('');

interface ConAvatar { nombreCompleto: string; fotoPerfil?: string | null; }

// Fondo del avatar.
// PRUEBA D (fotos de perfil): descomenta las 3 líneas que siguen (u.fotoPerfil, ? y :). La línea gradient(...) se queda.
export const fondoAvatar = (u: ConAvatar) =>
  // u.fotoPerfil
  //   ? `url("${encodeURI(u.fotoPerfil)}") center/cover no-repeat, ${gradient(hueOf(u.nombreCompleto))}`
  //   :
  gradient(hueOf(u.nombreCompleto));

// Iniciales del avatar.
// PRUEBA D: cambia el cuerpo por  (u.fotoPerfil ? '' : initials(u.nombreCompleto))  para ocultarlas cuando hay foto.
export const inicialesAvatar = (u: ConAvatar) => initials(u.nombreCompleto);
>>>>>>> main
