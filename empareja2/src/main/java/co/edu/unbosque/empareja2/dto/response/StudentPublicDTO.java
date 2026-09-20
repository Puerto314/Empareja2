package co.edu.unbosque.empareja2.dto.response;

/**
 * Informacion NO sensible de un estudiante, visible para cualquier usuario
 * autenticado del catalogo. Deliberadamente NO incluye: email, password,
 * direccion ni fecha de nacimiento.
 */
public class StudentPublicDTO {

	private Long id;
	private String fullName;
	private String career;
	private Integer semester;
	private String university;

	public StudentPublicDTO() {
	}

	public StudentPublicDTO(Long id, String fullName, String career, Integer semester, String university) {
		this.id = id;
		this.fullName = fullName;
		this.career = career;
		this.semester = semester;
		this.university = university;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
