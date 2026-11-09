package co.edu.unbosque.springfirstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.springfirstapp.model.User;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;


/**
 * Repositorio para la entidad User.
 * Proporciona operaciones CRUD básicas heredadas de JpaRepository
 * y métodos personalizados para buscar y eliminar usuarios por nombre de usuario.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Busca un usuario por su nombre de usuario.
	 * 
	 * @param username El nombre de usuario a buscar
	 * @return Un Optional que contiene el usuario si existe, o vacío si no existe
	 */
	public Optional<User> findByUsername(String username);

	/**
	 * Elimina un usuario por su nombre de usuario.
	 * 
	 * @param username El nombre de usuario del usuario a eliminar
	 */
	public void deleteByUsername(String username); 
	
	/**
	 * Encuentra los usuarios por fecha nacimiento
	 * 
	 * @param dateOfBirth La fecha nacimiento de los usuarios
	 * @return Lista opcional con los usuarios que tienen esa fecha de nacimiento
	 */
	public Optional<List<User>> findByDateOfBirth(LocalDate dateOfBirth);
}
