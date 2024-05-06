package com.pavan.controller;

import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pavan.dto.Submit;
import com.pavan.dto.Task;
import com.pavan.dto.Users;
import com.pavan.service.MyService;
import com.pavan.utils.MyJwtToken;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;


@Controller
@ResponseBody 
public class MyController {
	private final int MAX_REQUESTS_PER_SECOND = 1;
    private final long TIME_WINDOW_IN_MILLIS = 10000; 
	private final Map<String, Queue<Long>> requestMap = new ConcurrentHashMap<>();

	@Autowired
	MyService myService;
	
	@Autowired
	MyJwtToken jwtToken;
	
	@GetMapping("/test")
    public Map<String, Object> testing() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("status", "This is my Testing File");
        return response;
    }

	
 	@PostMapping("/api/auth/register")
    public Map<String, Object> registerUser(@Valid @RequestBody Users user, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.put("status", "failure");
            response.put("errors", errors);
            
        }else {
            String status = myService.register(user);
            response.put("status", status);
        }
        return response;
    } 
	
	@PostMapping("/api/auth/login")
	public Map<String, Object> search(@RequestBody Users user) {
		Map<String, Object> response = new HashMap<>();
		String status = myService.login(user);
		response.put("status", status);
	    return response;
	}
	
	@PostMapping("/api/teach/addtask")
	public Map<String, Object> addTask(@RequestBody Task task, @RequestHeader(name = "Authorization") String token) throws Exception{
		Map<String, Object> response = new HashMap<>();
		Claims verification = jwtToken.verify(token);
		if(verification == null) {
			response.put("status", "token invalid -- not authorized");
			return response;
		}
		String status = myService.addNewTask(task);
		response.put("status", status);
	    return response;		
	}
	
//	http://localhost:3025/api/teach/updatetask?Id=10
	@PostMapping("/api/teach/updatetask")
	public Map<String, Object> updateTask(@RequestBody Task task, @RequestParam int Id, @RequestHeader(name = "Authorization") String token) throws Exception{
		Map<String, Object> response = new HashMap<>();
		Claims verification = jwtToken.verify(token);
		if(verification == null) {
			response.put("status", "token invalid -- not authorized");
			return response;
		}
		String status = myService.modifyTask(task,Id);
		
		response.put("status",status);
	    return response;	
	}
	
//	http://localhost:3025/api/teach/deletetask?Id=9
	@PostMapping("/api/teach/deletetask")
	public Map<String, Object> deleteTask(@RequestParam int Id, @RequestHeader(name = "Authorization") String token) throws Exception{
		Map<String, Object> response = new HashMap<>();
		Claims verification = jwtToken.verify(token);
		if(verification == null) {
			response.put("status", "token invalid -- not authorized");
			return response;
		}
		String status = myService.deletTask(Id);
		
		response.put("status", status);
	    return response;	
	}
	
//	http://localhost:3025/api/teach/getalltasks?email=ramesh@gmail.com
	@GetMapping("/api/teach/getalltasks")
	public Map<String, Object> getAllTasks(@RequestParam String email, @RequestHeader(name = "Authorization") String token) throws Exception{
		Map<String, Object> response = new HashMap<>();
		Claims verification = jwtToken.verify(token);
		if(verification == null) {
			response.put("status", "token invalid -- not authorized");
			return response;
		}
		List<Task> tasks = myService.getMyTasks(email);
		
		response.put("tasks", tasks);
	    return response;	
	}
	
	
	@GetMapping("/api/teach/getstdans")
	public Map<String, Object> getStdSubmits(@RequestParam String email, @RequestHeader(name = "Authorization") String token) throws Exception{
		if (!allowRequest(token)) {
            return createRateLimitResponse();
        }
		Map<String, Object> response = new HashMap<>();
		Claims verification = jwtToken.verify(token);
		if(verification == null) {
			response.put("status", "token invalid -- not authorized");
			return response;
		}
		
		List<Submit> getSubmissions = myService.getSubmits(email);
		
		response.put("submissions", getSubmissions);
		
		return response;
		
	}
	
	
	@PostMapping("/api/teach/updatemarks")
	public Map<String, Object> updateMarks(
	                                       @RequestHeader(name = "Authorization") String token,
	                                       @RequestBody Map<String, Object> requestBody) throws Exception {
											if (!allowRequest(token)) {
												return createRateLimitResponse();
											}
	    Map<String, Object> response = new HashMap<>();
	    Claims verification = jwtToken.verify(token);
	    if (verification == null) {
	        response.put("status", "token invalid -- not authorized");
	        return response;
	    }
	    
	    String std_mail = (String) requestBody.get("std_mail");
	    int assignment_id = (int) requestBody.get("assignment_id");
	    int marks = (int) requestBody.get("obtained_marks");

	    String status = myService.updateMyMarks(std_mail, assignment_id, marks);

	    response.put("status", status);
	    
	    return response;
	}


	
	@GetMapping("/api/std/getallstdtasks")
	public Map<String, Object> getAllStdTasks(@RequestParam int page,@RequestHeader(name = "Authorization") String token) throws Exception{
		System.out.println("api request ----------------");
		if (!allowRequest(token)) {
            return createRateLimitResponse();
        }
		Map<String, Object> response = new HashMap<>();
		Claims verification = jwtToken.verify(token);
		if(verification == null) {
			response.put("status", "token invalid -- not authorized");
			return response;
		}
		List<Task> tasks = myService.getStudentTasks(page);
		response.put("tasks", tasks);
	    return response;
	}
	
	@PostMapping("/api/std/postans")
	public Map<String, Object> submitTask(@RequestBody Submit stask, @RequestHeader(name = "Authorization") String token) throws Exception{
		Map<String, Object> response = new HashMap<>();
//		stask.setEmail(verification.getIssuer());
		Claims verification = jwtToken.verify(token);
		System.out.println(verification.getIssuer());
		stask.setStd_mail(verification.getIssuer());

		if(verification == null) {
			response.put("status", "token invalid -- not authorized");
			return response;
		}
		String status = myService.submitMyTask(stask);
		response.put("status", status);
		
		return response;
	}
    private boolean allowRequest(String token) {
        long currentTime = System.currentTimeMillis();
        Queue<Long> requests = requestMap.computeIfAbsent(token, k -> new ConcurrentLinkedQueue<>());
        while (!requests.isEmpty() && currentTime - requests.peek() > TIME_WINDOW_IN_MILLIS) {
            requests.poll(); // Remove expired requests
        }
        if (requests.size() >= MAX_REQUESTS_PER_SECOND) {
            return false; // Too many requests
        }
        requests.offer(currentTime);
        return true;
    }

	private Map<String, Object> createRateLimitResponse() {
		Map<String, Object> response = new HashMap<>();
		response.put("statusCode", 500);
		response.put("status", "Internal Server Error");
		return response;
	}
	
	
	
}

