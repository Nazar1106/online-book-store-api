package com.example.bookstoreapp.repository.role;

import com.example.bookstoreapp.entity.Role;
import com.example.bookstoreapp.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.role = :roleName")
    Role byRoleName(RoleName roleName);
}
