package com.forestsoftware.send.repository;

import com.forestsoftware.send.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User>  findByEmail(String email);
    Optional<User> findByPhone( String phone);
    Boolean existsByEmail(String e);
    Boolean existsByPhone(String s);
    User findUserByEmail(String email);
    User findUserByPhone(String s);
    User findByVerificationCode(int code);

}
