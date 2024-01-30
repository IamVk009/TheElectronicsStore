package com.lucifer.electronics.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_password", length = 20)
    private String password;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_address")
    private String address;

    @Column(name = "user_gender")
    private String gender;

    @Column(name = "about_user", length = 200)
    private String about;

    @Column(name = "user_image_name")
    private String imageName;
}
