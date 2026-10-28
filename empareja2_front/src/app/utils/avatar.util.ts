export const hueOf = (t: string) => [...t].reduce((a, c) => (a * 31 + c.charCodeAt(0)) % 360, 7);
export const gradient = (h: number) => `linear-gradient(135deg, hsl(${h} 75% 62%), hsl(${(h + 45) % 360} 70% 42%))`;
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
