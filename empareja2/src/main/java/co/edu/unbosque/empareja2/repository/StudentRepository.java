package co.edu.unbosque.empareja2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.empareja2.entity.Student;

/**
 * Acceso a datos para la entidad Student. Spring Data JPA genera la
 * implementacion en tiempo de ejecucion.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByEmail(String email);

	boolean existsByEmail(String email);
}
