package com.sct.services;

import java.util.List;

import com.sct.models.Employee;
import com.sct.models.Form;
import com.sct.models.Upload;

public interface FormService {

	public boolean submitForm(Form f, Employee e);

	public List<Form> viewAllForms(Employee e); // only by employee/super/dept/benco
	
	public List<Form> checkToDo(Employee boss);
	
	public boolean cancelForm(Employee e, int formId);
	
	public boolean requestApproval(Employee boss, int employeeId, int formId, boolean approval);

	public boolean assignFileUpload(int employeeId, int formId, String key);
	
	public List<Upload> checkUploads(int employeeId, int formId);
	
	public boolean assignAwardValue(Employee boss, int employeeId, int formId);
	
	public boolean assignAwardValue(Employee boss, int employeeId, int formId, float value);
	
	public boolean confirmSkipApproval(Employee benco, int employeeId, int formId, String approvalType);

	public boolean confirmAwardDispersal(Employee benco, int employeeId, int formId);
}
