package com.sct.dao;

import java.util.List;

import com.sct.models.Employee;

public interface EmployeeDAO {

	List<Employee> getEmployees();
	// get all employees
	
	List<Employee> getEmployees(Employee boss); 
	// input super/deptHead/benCo employee, retrieve associates they oversee

	public boolean addEmployee(Employee e) throws Exception;
	// add the specified employee to the keyspace
	
	Employee getEmployeeById(int id);
	// retrieve the specified employee
	
	public void updateEmployee(Employee e);
	// update the specified employee (pulls by employeeId; passed object holds updated data)
	
	public void autoUpdaterAllotments();
	// updates reimbursement values for all employees
}
