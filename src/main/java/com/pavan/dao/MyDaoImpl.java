package com.pavan.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.pavan.dto.Submit;
import com.pavan.dto.Task;
import com.pavan.dto.Users;
import com.pavan.mapper.TaskRowMapper;
import com.pavan.mapper.UserRowMapper;
import com.pavan.utils.MyJwtToken;
import com.pavan.mapper.SubmitRowMapper;

@Repository 
public class MyDaoImpl implements MyDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	MyJwtToken jwtToken;
	
	@Override
	public String registerUser(Users user) {
		String status = "";
		Users usr = search(user.getEmail());
		if(usr==null) {
			
			int rowCount = jdbcTemplate.update("insert into users (name,email,password,role) values('"+user.getName()+"','"+user.getEmail()+"','"+user.getPassword()+"','"+user.getRole()+"')");
			if(rowCount==1) {
				status = "success";
			}else {
				status="failure";
			}
		}else {
			status = "existed";
		}
		return status;
	}
	
	
	
	public Users search(String email) {
		Users user = null;
	    List<Users> userList = jdbcTemplate.query("SELECT * FROM users WHERE email = '"+email+"'", new UserRowMapper());
		
		if(userList.isEmpty()) {
			user = null;
		}else {
			user = userList.get(0);
		}
		
		return user;
	}
	
	@Override
	public String loginUser(Users user){
		String status = "";
		Users usr = search(user.getEmail());
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		if(usr == null) {
			status = "not a valid user";
		}else if(bcrypt.matches(user.getPassword(),usr.getPassword())) {
			String token = jwtToken.generateJwt(user);
			status = "success-->"+token;
		}else {
			status = "failure-- invalid password";
		}
		return status;
	}
	
	@Override
	public String addTask(Task task) {
		String status = "";
		
		int rowCount = jdbcTemplate.update("INSERT INTO Assignments (title, description, user_email, submission_date, marks) VALUES (?, ?, ?, ?, ?)",
		        task.getTitle(), task.getDescription(), task.getUser_email(), task.getSubmission_date(), task.getMarks());

		if(rowCount==1) {
			status = "success";
		}else {
			status="failure";
		}
		return status;
	}
	
	@Override
	public String updateTask(Task task, int id) {
		String status = "";
		int rC = jdbcTemplate.update("UPDATE Assignments SET title = ?, description = ? WHERE assignment_id = ?",
				task.getTitle(),task.getDescription(), id);
		if(rC==1) {
			status = "success";
		}else {
			status="failure";
		}
		return status;
	}
	
	@Override 
	public String deleteTask(int id) {
		String status = "";
		String query = "delete from assignments where assignment_id = '"+id+"'";
		
		int rC = jdbcTemplate.update(query);
		
		if(rC<1) {
			status = "failure";
		}else {
			status = "success";
		}
		
		return status;
	}
	
	@Override 
	public List<Task> getTasks(String email) {
		List<Task> res = jdbcTemplate.query("SELECT * FROM Assignments WHERE user_email = '"+email+"' ORDER BY submission_date",new TaskRowMapper());
		
		return res;
	}
	
	@Override
	public List<Task> getAllTasks(int page){
		int k = page*10;
		int kk = k-10;
		List<Task> res = jdbcTemplate.query("SELECT title, description, submission_date, marks FROM Assignments ORDER BY submission_date limit "+kk+" ,"+k+"",new TaskRowMapper());
		
		return res;
	}
	
	@Override 
	public String submitTask(Submit stask) {
		String status = "";
	    String query = "INSERT INTO submit (assignment_id, std_email, task_sol, date_of_Submission, total_marks) VALUES (?, ?, ?, ?, ?)";
	    int rC = jdbcTemplate.update(query, stask.getAssignment_id(), stask.getStd_mail(), stask.getTask_sol(), stask.getDate_of_Submission(), stask.getTotal_marks());
		if(rC<1) {
			status = "failure";
		}else {
			status = "success";
		}
		return status;
	}
	
	@Override 
	public List<Submit> getSubmissions(String email){
		String query = "SELECT submit.assignment_id, submit.std_email, submit.task_sol, submit.total_marks, submit.date_of_Submission FROM submitINNER JOIN assignments ON submit.assignment_id = assignments.assignment_id WHERE assignments.user_email = '"+email+"';";
		List<Submit> result = jdbcTemplate.query(query, new SubmitRowMapper());
		
		return result;
	}
	
	@Override 
	public String updateMarks(String mail , int id, int marks) {
		String status = "";
		String query = "UPDATE submit SET obtained_marks = ? WHERE std_email = ? AND assignment_id = ?";
		int rC = jdbcTemplate.update(query,marks,mail,id);
		if(rC<1) {
			status = "failure";
		}else {
			status = "success";
		}
		
		return status;
	}
}
