package com.pavan.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pavan.dto.Task;

public class TaskRowMapper implements RowMapper<Task> {
	@Override
	public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
		Task task = new Task();

		task.setTitle(rs.getString("title"));
		task.setDescription(rs.getString("description"));
		task.setSubmission_date(rs.getString("submission_date"));
		task.setMarks(rs.getDouble("marks"));
		
		return task;
	}
}
