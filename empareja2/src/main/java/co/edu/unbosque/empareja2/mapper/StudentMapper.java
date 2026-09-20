package co.edu.unbosque.empareja2.mapper;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.dto.request.AdminUserRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegisterRequestDTO;
import co.edu.unbosque.empareja2.dto.response.StudentProfileDTO;
import co.edu.unbosque.empareja2.dto.response.StudentPublicDTO;
import co.edu.unbosque.empareja2.entity.Role;
import co.edu.unbosque.empareja2.entity.Student;

/**
 * Traduce entre la entidad Student y los DTOs de entrada/salida. Mantener esta
 * logica separada evita "filtrar" campos sensibles (como password) hacia las
 * respuestas de la API.
 */
@Component
public class StudentMapper {

	public Student toEntity(RegisterRequestDTO dto, String encodedPassword) {
		Student student = new Student();
		student.setEmail(dto.getEmail());
		student.setPassword(encodedPassword);
		student.setRole(Role.STUDENT);
		student.setFullName(dto.getFullName());
		student.setCareer(dto.getCareer());
		student.setSemester(dto.getSemester());
		student.setUniversity(dto.getUniversity());
		student.setAddress(dto.getAddress());
		student.setBirthDate(dto.getBirthDate());
		return student;
	}

	/**
	 * Crea una nueva entidad a partir de los datos enviados por un administrador.
	 * El rol se toma del request (STUDENT o ADMIN).
	 */
	public Student toEntity(AdminUserRequestDTO dto, String encodedPassword) {
		Student student = new Student();
		student.setEmail(dto.getEmail());
		student.setPassword(encodedPassword);
		student.setRole(dto.getRole() != null ? dto.getRole() : Role.STUDENT);
		applyAdminFields(student, dto);
		return student;
	}

	/**
	 * Aplica al estudiante existente los campos editables por un administrador (sin
	 * tocar el password si no fue enviado; eso lo decide la capa de servicio).
	 */
	public void applyAdminFields(Student student, AdminUserRequestDTO dto) {
		student.setFullName(dto.getFullName());
		student.setCareer(dto.getCareer());
		student.setSemester(dto.getSemester());
		student.setUniversity(dto.getUniversity());
		student.setAddress(dto.getAddress());
		student.setBirthDate(dto.getBirthDate());
	}

	public StudentPublicDTO toPublicDTO(Student student) {
		return new StudentPublicDTO(student.getId(), student.getFullName(), student.getCareer(), student.getSemester(),
				student.getUniversity());
	}

	public StudentProfileDTO toProfileDTO(Student student) {
		return new StudentProfileDTO(student.getId(), student.getEmail(), student.getRole(), student.getFullName(),
				student.getCareer(), student.getSemester(), student.getUniversity(), student.getAddress(),
				student.getBirthDate(), student.getCreatedAt());
	}
}
