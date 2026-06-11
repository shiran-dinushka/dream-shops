package com.dailywork.dreamshops.data;

import com.dailywork.dreamshops.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByName(String role);
}
