package co.edu.unbosque.empareja2.entity;

/**
 * Valores posibles del atributo "tipo_documento" de USUARIO. TI (tarjeta de
 * identidad) solo la tienen los menores de 18 anos y el catalogo exige ser mayor
 * de edad, por eso ya no se acepta en registros nuevos (se conserva el valor
 * solo para poder leer filas antiguas).
 */
public enum TipoDocumento {
    CC, TI, CE, PASAPORTE
}
