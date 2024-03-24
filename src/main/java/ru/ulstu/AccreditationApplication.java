package ru.ulstu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ulstu.model.User;
import ru.ulstu.model.enums.Role;
import ru.ulstu.repository.UserRepository;

@SpringBootApplication
public class AccreditationApplication implements CommandLineRunner {
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(AccreditationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setLogin("admin");
		user.setPassword(encoder.encode("admin"));
		user.setName("Админ");
		user.setSurname("Админ");
		user.setPatronymic("Админ");
		user.setRole(Role.ADMIN);
		if(userRepository.findByLogin("admin")==null) userRepository.save(user);
	}
}
