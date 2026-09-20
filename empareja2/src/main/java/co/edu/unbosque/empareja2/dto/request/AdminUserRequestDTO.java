package co.edu.unbosque.empareja2.dto.request;

import java.time.LocalDate;

import co.edu.unbosque.empareja2.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * Datos que un administrador envia para crear o editar cualquier
 * usuario del sistema (estudiante o administrador).
 *
 * A diferencia del registro publico, aqui el "email" se usa como
 * nombre de usuario y no se exige formato de correo, ya que un
 * administrador puede tener un usuario simple (ej: "Puerto314").
 *
 * La contrasena es obligatoria al crear un usuario nuevo; al editar
 * un usuario existente puede dejarse en blanco para no modificarla.
 */
public class AdminUserRequestDTO {

    @NotBlank(message = "El usuario/correo es obligatorio")
    private String email;

    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    private String password;

    private Role role;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    private String career;

    private Integer semester;

    private String university;

    private String address;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate birthDate;

    public AdminUserRequestDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
