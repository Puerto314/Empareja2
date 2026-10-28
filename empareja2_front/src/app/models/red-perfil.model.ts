// Refleja RedPerfilDTO: enlace del usuario a una red social.
export interface RedPerfilModel {
  idRedSocial: number;
  redSocial: string;
  urlPerfil: string;
  alias: string | null;
}
