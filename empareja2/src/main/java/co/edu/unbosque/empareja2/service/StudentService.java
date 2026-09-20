package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.request.AdminUserRequestDTO;
import co.edu.unbosque.empareja2.dto.request.UpdateProfileRequestDTO;
import co.edu.unbosque.empareja2.dto.response.StudentProfileDTO;
import co.edu.unbosque.empareja2.dto.response.StudentPublicDTO;

/**
 * Contrato para las operaciones del catalogo de estudiantes, del
 * perfil propio de un usuario, y de la administracion de usuarios
 * (exclusiva para el rol ADMIN).
 */
public interface StudentService {

    // ---------- Catalogo publico ----------
    List<StudentPublicDTO> getAllPublicProfiles();

    StudentPublicDTO getPublicProfileById(Long id);

    // ---------- Cuenta propia (STUDENT o ADMIN autenticado) ----------
    StudentProfileDTO getOwnProfile(String email);

    StudentProfileDTO updateOwnProfile(String email, UpdateProfileRequestDTO request);

    void deleteOwnAccount(String email);

    // ---------- Administracion de usuarios (solo ADMIN) ----------
    List<StudentProfileDTO> getAllUsersForAdmin();

    StudentProfileDTO getUserByIdForAdmin(Long id);

    StudentProfileDTO createUserByAdmin(AdminUserRequestDTO request);

    StudentProfileDTO updateUserByAdmin(Long id, AdminUserRequestDTO request);

    void deleteUserByAdmin(Long id);
}
