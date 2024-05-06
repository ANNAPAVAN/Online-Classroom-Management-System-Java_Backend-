package com.pavan.service;

import java.util.List;

import com.pavan.dto.Submit;
import com.pavan.dto.Task;
import com.pavan.dto.Users;

public interface MyService {
	public String register(Users user);
	public String login(Users user);
	
	public String addNewTask(Task task);
	public String modifyTask(Task task, int id);
	public String deletTask(int id);
	public List<Task> getMyTasks(String email);
	
	public List<Task> getStudentTasks(int page);
	public String submitMyTask(Submit stask);
	
	public List<Submit> getSubmits(String email);
	public String updateMyMarks(String mail , int id, int marks);
}
