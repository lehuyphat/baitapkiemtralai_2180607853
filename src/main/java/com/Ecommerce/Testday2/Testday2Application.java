package com.Ecommerce.Testday2;

import com.Ecommerce.Testday2.model.Role;
import com.Ecommerce.Testday2.model.User;
import com.Ecommerce.Testday2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class Testday2Application implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(Testday2Application.class, args);
	}

	public void run(String... args) {
		List<User> adminAccount = userRepository.findByRole(Role.ADMIN);
		if (adminAccount.isEmpty()) {
			User user = new User();
			user.setRole(Role.ADMIN);
			user.setEmail("admin@gmail.com");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}
	}
}
