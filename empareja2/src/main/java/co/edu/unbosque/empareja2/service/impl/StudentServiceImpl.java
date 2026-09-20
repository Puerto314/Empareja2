package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.AdminUserRequestDTO;
import co.edu.unbosque.empareja2.dto.request.UpdateProfileRequestDTO;
import co.edu.unbosque.empareja2.dto.response.StudentProfileDTO;
import co.edu.unbosque.empareja2.dto.response.StudentPublicDTO;
import co.edu.unbosque.empareja2.entity.Student;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.StudentMapper;
import co.edu.unbosque.empareja2.repository.StudentRepository;
import co.edu.unbosque.empareja2.service.StudentService;

/**
 * Implementa las reglas de negocio del catalogo de estudiantes, del
 * perfil propio y de la administracion de usuarios. Es la unica capa
 * que conoce el repositorio; los controladores nunca acceden a el
 * directamente.
 */
@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- Catalogo publico ----------

    @Override
    public List<StudentPublicDTO> getAllPublicProfiles() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toPublicDTO)
                .toList();
    }

    @Override
    public StudentPublicDTO getPublicProfileById(Long id) {
        Student student = findByIdOrThrow(id);
        return studentMapper.toPublicDTO(student);
    }

    // ---------- Cuenta propia ----------

    @Override
    public StudentProfileDTO getOwnProfile(String email) {
        Student student = findByEmailOrThrow(email);
        return studentMapper.toProfileDTO(student);
    }

    @Override
    @Transactional
    public StudentProfileDTO updateOwnProfile(String email, UpdateProfileRequestDTO request) {
        Student student = findByEmailOrThrow(email);

        student.setFullName(request.getFullName());
        student.setCareer(request.getCareer());
        student.setSemester(request.getSemester());
        student.setUniversity(request.getUniversity());
        student.setAddress(request.getAddress());
        student.setBirthDate(request.getBirthDate());

        Student updated = studentRepository.save(student);
        return studentMapper.toProfileDTO(updated);
    }

    @Override
    @Transactional
    public void deleteOwnAccount(String email) {
        Student student = findByEmailOrThrow(email);
        studentRepository.delete(student);
    }

    // ---------- Administracion de usuarios (solo ADMIN) ----------

    @Override
    public List<StudentProfileDTO> getAllUsersForAdmin() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toProfileDTO)
                .toList();
    }

    @Override
    public StudentProfileDTO getUserByIdForAdmin(Long id) {
        Student student = findByIdOrThrow(id);
        return studentMapper.toProfileDTO(student);
    }

    @Override
    @Transactional
    public StudentProfileDTO createUserByAdmin(AdminUserRequestDTO request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese correo/usuario");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria al crear un usuario");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Student student = studentMapper.toEntity(request, encodedPassword);
        Student saved = studentRepository.save(student);
        return studentMapper.toProfileDTO(saved);
    }

    @Override
    @Transactional
    public StudentProfileDTO updateUserByAdmin(Long id, AdminUserRequestDTO request) {
        Student student = findByIdOrThrow(id);

        if (!student.getEmail().equals(request.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese correo/usuario");
        }

        student.setEmail(request.getEmail());
        studentMapper.applyAdminFields(student, request);
        if (request.getRole() != null) {
            student.setRole(request.getRole());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            student.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Student updated = studentRepository.save(student);
        return studentMapper.toProfileDTO(updated);
    }

    @Override
    @Transactional
    public void deleteUserByAdmin(Long id) {
        Student student = findByIdOrThrow(id);
        studentRepository.delete(student);
    }

    private Student findByIdOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con id " + id));
    }

    private Student findByEmailOrThrow(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con correo " + email));
    }
}
