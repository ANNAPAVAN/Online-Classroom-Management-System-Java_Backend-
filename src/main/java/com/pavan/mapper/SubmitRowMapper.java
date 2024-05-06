package com.pavan.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pavan.dto.Submit;

public class SubmitRowMapper implements RowMapper<Submit> {
	@Override
	public Submit mapRow(ResultSet rs, int rowNum) throws SQLException {
		Submit task = new Submit();

//		task.setTitle(rs.getString("title"));
//		String query = "SELECT submit.assignment_id, submit.std_email, submit.task_sol, submit.total_marks, submit.date_of_Submission FROM submitINNER JOIN assignments ON submit.assignment_id = assignments.assignment_id WHERE assignments.user_email = ?;";

		
		task.setAssignment_id(rs.getInt("assignment_id"));
		task.setStd_mail(rs.getString("std_mail"));
		task.setTask_sol(rs.getString("task_sol"));
		task.setTotal_marks(rs.getDouble("total_marks"));
		task.setDate_of_Submission(rs.getString("date_of_Submission"));
		
		return task;
	}
}
