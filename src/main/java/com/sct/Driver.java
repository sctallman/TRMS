package com.sct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sct.controllers.EmployeeController;
import com.sct.controllers.FormController;
import com.sct.dao.EmployeeDAO;
import com.sct.dao.EmployeeDAOImpl;
import com.sct.dao.FormDAO;
import com.sct.dao.FormDAOImpl;
import com.sct.factory.ModelFactory;
import com.sct.utils.CassandraUtil;
import com.sct.utils.KeyspaceSetupUtil;

import io.javalin.Javalin;

public class Driver {
	private static final Logger log = LogManager.getLogger(Driver.class);
	private static FormDAO fdao = (FormDAO) ModelFactory.getFactory().get(FormDAO.class, FormDAOImpl.class);
	private static EmployeeDAO edao = (EmployeeDAO) ModelFactory.getFactory().get(EmployeeDAO.class, EmployeeDAOImpl.class);

	public static void main(String[] args) {
		// Trace the flow of an application
		log.trace("Begin the Tuition Reimbursement Management System application");
		javalin();

		KeyspaceSetupUtil.dbtest();
		KeyspaceSetupUtil.employeesTable();
		KeyspaceSetupUtil.formsTable();
		KeyspaceSetupUtil.uploadsTable();

		fdao.autoUpdaterTimeout();
		// auto-approves reimbursement requests that have existed for 7 days with no response
		edao.autoUpdaterAllotments(); 
		// auto-updates employee allotments for tuition reimbursement based on current years' forms
	}

	private static void javalin() {
		Javalin app = Javalin.create().start(8080);

		app.put("/employees", EmployeeController::register);	// self-evident
		app.post("/employees", EmployeeController::login);		// self-evident
		app.delete("/employees", EmployeeController::logout);	// self-evident

		app.get("employees/:employeeId", FormController::checkToDo);	// bosses check which forms need approval
		app.put("employees/:employeeId", FormController::submitForm);	// anyone can submit a form

		app.get("employees/:employeeId/forms", FormController::viewAllForms); // view all forms for the logged in employee

		app.delete("/employees/:employeeId/forms/:formId", FormController::cancelForm);		// logged in employee can cancel a form
		app.patch("employees/:employeeId/forms/:formId", FormController::requestApproval);	// bosses can pass judgement on forms
		app.put("employees/:employeeId/forms/:formId", FormController::assignAwardValue);	// adjust award value as benCo
		app.get("employees/:employeeId/forms/:formId", FormController::checkUploads);		// bosses can check form attachments
		app.post("/employees/:employeeId/forms/:formId", FormController::confirmAwardDispersal);	// bencos can pass final confirmation to disperse awards

		app.put("/employees/:employeeId/forms/:formId/:filename", EmployeeController::uploadFile);			// employees can upload attachments
		app.get("/employees/:employeeId/forms/:formId/:filename", EmployeeController::getFile);				// interested parties can retrieve files
		app.patch("/employees/:employeeId/forms/:formId/:filename", FormController::confirmSkipApproval);	// bencos can approve approval skips

		CassandraUtil.getInstance();
	}
}
