package co.edu.unbosque.springfirstapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.springfirstapp.dto.UserDTO;
import co.edu.unbosque.springfirstapp.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081" })
@Transactional
@Tag(name = "User Management", description = "Endpoints for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

	@Autowired
	private UserService userServ;

	public UserController() {
	}

	// JWT Authentication and Authorization is required for all endpoints
	// Endpoints /getall, /count, /exists/*, /getbyid/* are accessible to users with ROLE_USER or ROLE_ADMIN
	// All other endpoints require ROLE_ADMIN

	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createNewWithJSON(@RequestBody UserDTO newUser) {
		if (newUser.getUsername().contains("<")||newUser.getUsername().contains(">")) {
			return new ResponseEntity<>("Solicitud con caracteres invalidos",HttpStatus.BAD_REQUEST);
		}
		int status = userServ.create(newUser);


		if (status == 0) {
			return new ResponseEntity<>("User create successfully", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Error on created user, maybe username already in use",
					HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PostMapping(path = "/create")
	ResponseEntity<String> createNew(@RequestParam String username, @RequestParam String password) {
		UserDTO newUser = new UserDTO(username, password);
		int status = userServ.create(newUser);

		if (status == 0) {
			return new ResponseEntity<>("User create successfully", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Error on created user, maybe username already in use",
					HttpStatus.NOT_ACCEPTABLE);
		}
	}


	@GetMapping("/getall")
	ResponseEntity<List<UserDTO>> getAll() {
		List<UserDTO> users = userServ.getAll();
		if (users.isEmpty()) {
			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
		}
	}

	@GetMapping("/count")
	ResponseEntity<Long> countAll() {
		Long count = userServ.count();
		if (count == 0) {
			return new ResponseEntity<>(count, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(count, HttpStatus.ACCEPTED);
		}
	}

	@GetMapping("/exists/{id}")
	ResponseEntity<Boolean> exists(@PathVariable Long id) {
		boolean found = userServ.exist(id);
		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/getbyid/{id}")
	ResponseEntity<UserDTO> getById(@PathVariable Long id) {
		UserDTO found = userServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(new UserDTO(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> updateNewWithJSON(@RequestParam Long id, @RequestBody UserDTO newUser) {

		int status = userServ.updateById(id, newUser);

		if (status == 0) {
			return new ResponseEntity<>("User updated successfully", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("New username already taken", HttpStatus.IM_USED);
		} else if (status == 2) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Error on update", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "/update")
	ResponseEntity<String> updateNew(@RequestParam long id, @RequestParam String newUsername,
			@RequestParam String newPassword) {
		UserDTO newUser = new UserDTO(newUsername, newPassword);

		int status = userServ.updateById(id, newUser);

		if (status == 0) {
			return new ResponseEntity<>("User updated successfully", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("New username already taken", HttpStatus.IM_USED);
		} else if (status == 2) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Error on update", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/deletebyid/{id}")
	ResponseEntity<String> deleteById(@PathVariable Long id) {
		int status = userServ.deleteById(id);

		if (status == 0) {
			return new ResponseEntity<>("User deleted successfully", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Error on delete", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletebyname")
	ResponseEntity<String> deleteById(@RequestParam String name) {
		int status = userServ.deleteByUsername(name);
		if (status == 0) {
			return new ResponseEntity<>("User deleted successfully", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Error on delete", HttpStatus.NOT_FOUND);
		}
	}

}
