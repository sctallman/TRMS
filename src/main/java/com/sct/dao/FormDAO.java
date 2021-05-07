package com.sct.dao;

import java.util.List;

import com.sct.models.Employee;
import com.sct.models.Form;
import com.sct.models.Upload;

public interface FormDAO {

	public void submitForm(Form f, Employee e) throws Exception;
	
	public List<Form> viewAllForms(Employee e);
	
	public List<Form> viewAllForms(Employee e, Form.Status s);
	
	public Form getForm(Employee e, int formId);
	
	public boolean updateForm(Form f);
	
	public void autoUpdaterTimeout();
	
	public boolean assignFileUpload(int employeeId, int formId, String key) throws Exception;

	public List<Upload> checkUploads(int employeeId, int formId);
}
