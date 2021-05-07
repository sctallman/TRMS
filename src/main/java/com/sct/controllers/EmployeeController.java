package com.sct.controllers;

import java.io.InputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sct.factory.ModelFactory;
import com.sct.models.Employee;
import com.sct.models.Form;
import com.sct.services.EmployeeService;
import com.sct.services.EmployeeServiceImpl;
import com.sct.services.FormService;
import com.sct.services.FormServiceImpl;
import com.sct.utils.S3Util;

import io.javalin.http.Context;

public class EmployeeController {
	private static EmployeeService empServ = (EmployeeService) ModelFactory.getFactory().get(EmployeeService.class,
			EmployeeServiceImpl.class);
	private static FormService formServ = (FormService) ModelFactory.getFactory().get(FormService.class,
			FormServiceImpl.class);

	private static final Logger log = LogManager.getLogger(EmployeeController.class);

	public static void register(Context ctx) {
		Employee e = ctx.bodyAsClass(Employee.class);
		log.trace("EmpCon: forwarding to empServ: add employee " + e.toString());
		boolean added = empServ.addEmployee(e);
		if (added) {
			ctx.json(e);
		} else {
			ctx.status(409);
		}
	}

	public static void login(Context ctx) {
		// We can get session information from the Context to use it elsewhere.
		if (ctx.sessionAttribute("Employee") != null) {
			ctx.status(204);
			return;
		}
		int employeeId = Integer.parseInt(ctx.formParam("employeeId"));
		log.trace("EmpCon: forwarding to empServ: login employee #" + employeeId);
		Employee e = empServ.getEmployee(employeeId);
		if (e == null) {
			ctx.status(401);
		} else {
			ctx.sessionAttribute("Employee", e);
			ctx.json(e);
		}
		if (e.isDirectSuper() | e.isDeptHead()) {
			try {
				List<Form> formsToDo = formServ.checkToDo(e);
				if (formsToDo != null && !formsToDo.isEmpty()) {
					ctx.json(formsToDo);
				} else {
					ctx.json(e);
					return;
				}
			} catch (Exception x) {
				x.printStackTrace();
				ctx.status(404);
			}
		}
	}

	public static void logout(Context ctx) {
		log.trace("Logging out.");
		ctx.req.getSession().invalidate();
	}

	public static void uploadFile(Context ctx) {
		if(ctx.sessionAttribute("Employee") == null) {
			log.warn("Please log in before attempting to upload files.");
			return;
		}
		int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
		int formId = Integer.parseInt(ctx.pathParam("formId"));
		String key = ctx.pathParam("filename");
		byte[] bytes = ctx.bodyAsBytes();
		try {
			S3Util.getInstance().uploadToBucket(key, bytes);
			formServ.assignFileUpload(employeeId, formId, key);
		} catch (Exception x) {
			ctx.status(500);
		}
		ctx.status(204);
	}

	public static void getFile(Context ctx) {
		if (ctx.sessionAttribute("Employee") == null) {
			ctx.status(401);
			log.warn("Log in first to view files.");
			return;
		}
		Employee loggedIn = ctx.sessionAttribute("Employee");
		int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
		String key = ctx.pathParam("filename");
		Employee e = empServ.getEmployee(employeeId);

		/*
		 * if the user the uploads belong to is not the user currently logged in, and
		 * the logged in user is not the upload user's directSuper or deptHead, and the
		 * logged in user is not a benco, return without accessing file.
		 */
		if (loggedIn != e & loggedIn.getEmployeeId() != e.getDirectSuperId()
				& loggedIn.getEmployeeId() != e.getDeptHeadId() & !loggedIn.isBenCo()) {
			return;
		}
		try {
			InputStream s = S3Util.getInstance().getObject(key);
			ctx.result(s);
		} catch (Exception x) {
			ctx.status(500);
		}
	}
}
