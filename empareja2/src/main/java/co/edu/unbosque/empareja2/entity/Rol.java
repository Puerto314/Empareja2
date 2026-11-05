package co.edu.unbosque.empareja2.entity;

/**
 * Roles del atributo "rol" de USUARIO. Un ESTUDIANTE aparece en el catalogo;
 * un ADMIN administra el sistema y no aparece en el catalogo.
 */
public enum Rol {

    /** Usuario normal que aparece en el catalogo. */
    ESTUDIANTE,

    /** Usuario que administra el sistema y no aparece en el catalogo. */
    ADMIN
}
