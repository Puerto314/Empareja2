package co.edu.unbosque.empareja2.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Una foto vacia o en blanco debe guardarse como NULL en usuario.foto_perfil.
 */
class UsuarioFotoPerfilTest {

    @Test
    void fotoVaciaOEnBlancoSeGuardaComoNull() {
        Usuario usuario = new Usuario();

        usuario.setFotoPerfil("");
        assertThat(usuario.getFotoPerfil()).isNull();

        usuario.setFotoPerfil("   ");
        assertThat(usuario.getFotoPerfil()).isNull();

        usuario.setFotoPerfil(null);
        assertThat(usuario.getFotoPerfil()).isNull();
    }

    @Test
    void fotoConEnlaceSeConservaSinEspacios() {
        Usuario usuario = new Usuario();

        usuario.setFotoPerfil("  https://img/ana.png ");

        assertThat(usuario.getFotoPerfil()).isEqualTo("https://img/ana.png");
    }
}
