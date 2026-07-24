package com.erp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.app.entities.Members;
import com.erp.app.entities.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

	Role findByName(String string);

}
