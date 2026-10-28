// Saca un mensaje legible de la respuesta de error del backend (ErrorResponseDTO).
export const mensajeDeError = (err: any, porDefecto: string): string => {
  if (err?.status === 0) return 'No pudimos conectar con el servidor. Revisa que el backend esté encendido.';
  const detalles: string[] | undefined = err?.error?.details;
  if (detalles?.length) return detalles.join(' · ');
  return err?.error?.message || porDefecto;
};
