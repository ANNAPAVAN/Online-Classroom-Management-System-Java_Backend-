package com.pavan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pavan.dao.MyDao;
import com.pavan.dto.Submit;
import com.pavan.dto.Task;
import com.pavan.dto.Users;

@Service
public class MyServiceImpl implements MyService {
	
	@Autowired
	MyDao myDao;
	
	@Override
	public String register(Users user) {
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String encryptedPwd = bcrypt.encode(user.getPassword());
		user.setPassword(encryptedPwd);
		
		String status = myDao.registerUser(user);
		return status;
	}
	
	@Override 
	public String login(Users user) {
		String status = myDao.loginUser(user);
		return status;
	}
	
	@Override
	public String addNewTask(Task task) {
		String status = myDao.addTask(task);
		return status;
	}
	
	@Override 
	public String modifyTask(Task task, int id) {
		String status = myDao.updateTask(task ,id);
		return status;
	}
	
	@Override 
	public String deletTask(int id) {
		String status = myDao.deleteTask(id);
		return status;
	}
	
	@Override
	public List<Task> getMyTasks(String email) {
		List<Task> task = myDao.getTasks(email);
		
		return task;
	}
	
	@Override
	public List<Task> getStudentTasks(int page){
		List<Task> task = myDao.getAllTasks(page);
		
		return task;
	}
	
	@Override
	public String submitMyTask(Submit stask) {
		String status = myDao.submitTask(stask);
		
		return status;
	}
	
	@Override
	public List<Submit> getSubmits(String email){
		List<Submit> res = myDao.getSubmissions(email);
		
		return res;
	}
	
	@Override
	public String updateMyMarks(String mail , int id, int marks) {
		String status = myDao.updateMarks(mail,id,marks);
		
		return status;
	}
}
