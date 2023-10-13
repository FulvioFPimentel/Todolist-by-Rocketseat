package com.fpimentel.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID> {

	UserModel findByUsername(String username);

	
}
