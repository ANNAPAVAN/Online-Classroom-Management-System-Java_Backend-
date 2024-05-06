package com.pavan.dao;

import java.util.List;

import com.pavan.dto.Submit;
import com.pavan.dto.Task;
import com.pavan.dto.Users;

public interface MyDao {
	public String registerUser(Users user);
	public String loginUser(Users user);
	
	public String addTask(Task task);
	public String updateTask(Task task, int id);
	public String deleteTask(int id);
	public List<Task> getTasks(String email);
	
	public List<Task> getAllTasks(int page);
	public String submitTask(Submit stask);
	
	public List<Submit> getSubmissions(String email);
	public String updateMarks(String mail , int id, int marks);
}
