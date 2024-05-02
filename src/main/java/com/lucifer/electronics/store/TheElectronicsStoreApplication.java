package com.lucifer.electronics.store;

import com.lucifer.electronics.store.entities.Role;
import com.lucifer.electronics.store.entities.User;
import com.lucifer.electronics.store.repositories.RoleRepository;
import com.lucifer.electronics.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class TheElectronicsStoreApplication implements CommandLineRunner{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${role.admin.id}")
    private String role_admin_id;

    @Value("${role.normal.id}")
    private String role_normal_id;

    public static void main(String[] args) {
        SpringApplication.run(TheElectronicsStoreApplication.class, args);
    }

//    @Component
//    public class MyCommandLineRunner implements CommandLineRunner {
//
        @Override
        public void run(String... args) throws Exception {
            try {
                Role roleAdmin = Role.builder()
                        .roleId(role_admin_id)
                        .roleName("ROLE_ADMIN")
                        .build();

                Role roleNormal = Role.builder()
                        .roleId(role_normal_id)
                        .roleName("ROLE_NORMAL")
                        .build();

                User adminUser = User.builder()
                        .name("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123"))
                        .gender("Male")
                        .imageName("default.png")
                        .roles(Set.of(roleAdmin, roleNormal))
                        .userId(UUID.randomUUID().toString())
                        .about("I am admin User")
                        .build();

                User normalUser = User.builder()
                        .name("durgesh")
                        .email("durgesh@gmail.com")
                        .password(passwordEncoder.encode("durgesh123"))
                        .gender("Male")
                        .imageName("default.png")
                        .roles(Set.of(roleNormal))
                        .userId(UUID.randomUUID().toString())
                        .about("I am Normal User")
                        .build();
                roleRepository.save(roleAdmin);
                roleRepository.save(roleNormal);

                userRepository.save(adminUser);
                userRepository.save(normalUser);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//    }
}
