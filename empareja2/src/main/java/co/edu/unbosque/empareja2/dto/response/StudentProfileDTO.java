package co.edu.unbosque.empareja2.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import co.edu.unbosque.empareja2.entity.Role;

/**
 * Perfil completo de un usuario, incluyendo sus propios datos privados
 * (direccion, fecha de nacimiento, email, rol). Solo se devuelve al propio
 * dueno del recurso o a un administrador, nunca a terceros.
 */
public class StudentProfileDTO {

	private Long id;
	private String email;
	private Role role;
	private String fullName;
	private String career;
	private Integer semester;
	private String university;
	private String address;
	private LocalDate birthDate;
	private LocalDateTime createdAt;

	public StudentProfileDTO() {
	}

	public StudentProfileDTO(Long id, String email, Role role, String fullName, String career, Integer semester,
			String university, String address, LocalDate birthDate, LocalDateTime createdAt) {
		this.id = id;
		this.email = email;
		this.role = role;
		this.fullName = fullName;
		this.career = career;
		this.semester = semester;
		this.university = university;
		this.address = address;
		this.birthDate = birthDate;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
