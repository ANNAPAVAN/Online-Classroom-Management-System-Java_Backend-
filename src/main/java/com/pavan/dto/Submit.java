package com.pavan.dto;

public class Submit {
	private int submit_id;
	private int assignment_id;
	private String std_mail;
	private String task_sol;
	private double obtained_marks;
	private String date_of_Submission;
	private double total_marks;
	
	public int getSubmit_id() {
		return submit_id;
	}
	public void setSubmit_id(int submit_id) {
		this.submit_id = submit_id;
	}
	public int getAssignment_id() {
		return assignment_id;
	}
	public void setAssignment_id(int assignment_id) {
		this.assignment_id = assignment_id;
	}
	public String getStd_mail() {
		return std_mail;
	}
	public void setStd_mail(String std_mail) {
		this.std_mail = std_mail;
	}
	public String getTask_sol() {
		return task_sol;
	}
	public void setTask_sol(String task_sol) {
		this.task_sol = task_sol;
	}
	public double getObtained_marks() {
		return obtained_marks;
	}
	public void setObtained_marks(double obtained_marks) {
		this.obtained_marks = obtained_marks;
	}
	public String getDate_of_Submission() {
		return date_of_Submission;
	}
	public void setDate_of_Submission(String date_of_Submission) {
		this.date_of_Submission = date_of_Submission;
	}
	public double getTotal_marks() {
		return total_marks;
	}
	public void setTotal_marks(double total_marks) {
		this.total_marks = total_marks;
	}
		
}
