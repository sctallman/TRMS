package com.sct.services;

import com.sct.models.Employee;

public interface EmployeeService {

	boolean addEmployee(Employee e);

	Employee getEmployee(int id);

}
