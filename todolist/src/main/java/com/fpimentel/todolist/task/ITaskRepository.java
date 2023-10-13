package com.fpimentel.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

	List<TaskModel> findByIdUser(UUID idUser);
	
}
