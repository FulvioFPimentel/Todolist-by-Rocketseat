package com.fpimentel.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private ITaskRepository taskRepository;
		
	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
		taskModel.setIdUser((UUID) request.getAttribute("idUser"));
		
		LocalDateTime currentDate = LocalDateTime.now();
		if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data inicial e final, devem ser maior que a data atual");
		}
		
		if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data inicial deve ser menor que a data final");
		}
		
		var task = taskRepository.save(taskModel);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<TaskModel>> list(HttpServletRequest request) {
	
		List<TaskModel> tasks = this.taskRepository.
				findByIdUser((UUID) request.getAttribute("idUser"));
		
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

}	
