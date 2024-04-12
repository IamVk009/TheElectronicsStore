package com.lucifer.electronics.store;

import com.lucifer.electronics.store.entities.Role;
import com.lucifer.electronics.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class TheElectronicsStoreApplication {

//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Value("${role.admin.id}")
//    private String role_admin_id;
//
//    @Value("${role.normal.id}")
//    private String role_normal_id;

    public static void main(String[] args) {
        SpringApplication.run(TheElectronicsStoreApplication.class, args);
    }

//    @Component
//    public class MyCommandLineRunner implements CommandLineRunner {
//
//        @Override
//        public void run(String... args) throws Exception {
//            try {
//                Role roleAdmin = Role.builder()
//                        .roleId(role_admin_id)
//                        .roleName("ROLE_ADMIN")
//                        .build();
//
//                Role roleNormal = Role.builder()
//                        .roleId(role_normal_id)
//                        .roleName("ROLE_NORMAL")
//                        .build();
//
//                roleRepository.save(roleAdmin);
//                roleRepository.save(roleNormal);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
