package com.forestsoftware.send.repository;

import com.forestsoftware.send.model.ERole;
import com.forestsoftware.send.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(ERole name);
}
