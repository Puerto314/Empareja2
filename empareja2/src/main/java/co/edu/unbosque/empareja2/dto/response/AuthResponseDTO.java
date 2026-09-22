package co.edu.unbosque.empareja2.dto.response;

/**
 * Respuesta enviada tras un login o registro exitoso.
 */
public class AuthResponseDTO {

	private String token;
	private String tokenType = "Bearer";
	private Long expiresInMs;
	private StudentProfileDTO student;

	public AuthResponseDTO() {
	}

	public AuthResponseDTO(String token, Long expiresInMs, StudentProfileDTO student) {
		this.token = token;
		this.expiresInMs = expiresInMs;
		this.student = student;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Long getExpiresInMs() {
		return expiresInMs;
	}

	public void setExpiresInMs(Long expiresInMs) {
		this.expiresInMs = expiresInMs;
	}

	public StudentProfileDTO getStudent() {
		return student;
	}

	public void setStudent(StudentProfileDTO student) {
		this.student = student;
	}
}
