package com.sct.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sct.factory.ModelFactory;
import com.sct.models.Employee;
import com.sct.models.Form;
import com.sct.models.Upload;
import com.sct.services.FormService;
import com.sct.services.FormServiceImpl;

import io.javalin.http.Context;

public class FormController {
		private static FormService formServ = (FormService) ModelFactory.getFactory().get(FormService.class,
				FormServiceImpl.class);
		private static final Logger log = LogManager.getLogger(FormController.class);
		private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		
		public static void submitForm(Context ctx) {
			if (ctx.sessionAttribute("Employee") == null) {
				ctx.status(401);
				log.warn("You must be logged in to submit a request form.");
				return;
			}
			Employee e = ctx.sessionAttribute("Employee");
			Form f = new Form();
			f.setType(Form.EventType.valueOf(ctx.formParam("type").toUpperCase()));
			f.setGrade(Form.GradingFormat.valueOf(ctx.formParam("grade").toUpperCase()));
			f.setFormId(Integer.parseInt(ctx.formParam("formId")));
			f.setApplicationTimeZone(ctx.formParam("applicationTimeZone"));
			f.setEventLocation(ctx.formParam("eventLocation"));
			f.setEventDescription(ctx.formParam("eventDescription"));
			f.setEventTimeZone(ctx.formParam("eventTimeZone"));
			LocalDateTime localEventDate = LocalDateTime.parse(ctx.formParam("eventDateTime"), formatter);
			ZoneId eventTimeZone = ZoneId.of(ZoneId.SHORT_IDS.get(f.getEventTimeZone()));
			ZonedDateTime eventDateTime = ZonedDateTime.of(localEventDate, eventTimeZone);
			f.setEventDateTime(eventDateTime);
			f.setEventCost(Float.parseFloat(ctx.formParam("eventCost")));
			f.setJustification(ctx.formParam("justification"));
			log.trace("FormCon: forwarding to formServ: submit request for "+e.getEmployeeId());
			log.trace("FormCon: form being submitted: "+f.toString());
			boolean submitted = formServ.submitForm(f, e);
			if(submitted) {
				ctx.json(f);
			}
			else {
				ctx.status(409);
				log.warn("FormCon: form submission request failure.");
			}
		}
		
		
		
		public static void viewAllForms(Context ctx) {
			if (ctx.sessionAttribute("Employee") == null) {
				ctx.status(401);
				log.warn("You must be logged in to view forms.");
				return;
			}
			Employee e = ctx.sessionAttribute("Employee");
			List<Form> allForms = formServ.viewAllForms(e);
			if(allForms == null | allForms.isEmpty()) {
				ctx.status(204);
				log.trace("No forms available for the specified employee :) ");
			}
			else {
				ctx.status(200);
				ctx.json(allForms);
			}
		}
		
		public static void checkToDo(Context ctx) {
			if (ctx.sessionAttribute("Employee") ==  null) {
				ctx.status(401);
				log.warn("You must be logged in to check your TODO list.");
				return;
			}
			Employee e = ctx.sessionAttribute("Employee");
			try {
				List<Form> formsToDo = formServ.checkToDo(e);
				if (formsToDo != null && !formsToDo.isEmpty()) {
					ctx.json(formsToDo);
				}
				else {
					ctx.status(204);
					return;
				}
			} catch (Exception x) {
				x.printStackTrace();
				ctx.status(404);
			}
		}
		public static void cancelForm(Context ctx) {
			if(ctx.sessionAttribute("Employee") == null) {
				ctx.status(405);
				log.warn("You must be logged in to cancel a reimbursement request.");
				return;
			}
			Employee e = ctx.sessionAttribute("Employee");
			int formId = Integer.parseInt(ctx.pathParam("formId"));
			try {
				boolean cancelled = formServ.cancelForm(e, formId);
				if(cancelled) {
					ctx.status(204);
				}
			} catch (Exception x) {
				x.printStackTrace();
				ctx.status(400);
			}
		}
		
		public static void requestApproval(Context ctx) {
			if(ctx.sessionAttribute("Employee") == null) {
				ctx.status(401);
				log.warn("You must be logged in to approve reimbursement requests.");
				return;
			}
			Employee boss = ctx.sessionAttribute("Employee");
			int e = Integer.parseInt(ctx.pathParam("employeeId"));
			int formId = Integer.parseInt(ctx.pathParam("formId"));
			boolean approval = Boolean.parseBoolean(ctx.formParam("approval"));
			if(boss.getEmployeeId() == e) {
				log.warn("You cannot submit approvals on your own requests.");
				ctx.status(401);
				return;
			}
			try {
				boolean approved = formServ.requestApproval(boss, e, formId, approval);
				if(ctx.formParam("value") != null && !ctx.formParam("value").isEmpty()) {
					float value = Float.parseFloat(ctx.formParam("value"));
					formServ.assignAwardValue(boss, e, formId, value);
				} else {
					formServ.assignAwardValue(boss, e, formId);
				}
				if(approved) {
					ctx.status(200);
				}
				if(!approved) {
					ctx.status(401);
				}
			} catch (Exception x) {
				x.printStackTrace();
				ctx.status(400);
			}
		}
		
		public static void assignAwardValue(Context ctx) {
			if(ctx.sessionAttribute("Employee") == null) {
				ctx.status(401);
				log.warn("Log in first to assign award values.");
				return;
			}
			Employee boss = ctx.sessionAttribute("Employee");
			int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
			int formId = Integer.parseInt(ctx.pathParam("formId"));
			float value = Float.parseFloat(ctx.formParam("value"));
			try {
				boolean success = formServ.assignAwardValue(boss, employeeId, formId, value);
				if(success) {
					ctx.status(200);
				}
			} catch (Exception x) {
				x.printStackTrace();
				ctx.status(400);
			}
			
		}
		
		public static void checkUploads(Context ctx) {
			if(ctx.sessionAttribute("Employee") == null) {
				ctx.status(401);
				log.warn("You must be logged in to view file uploads");
				return;
			}
			int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
			int formId = Integer.parseInt(ctx.pathParam("formId"));
			try {
				List<Upload> uploads = formServ.checkUploads(employeeId, formId);
				ctx.json(uploads);
			} catch (Exception x) {
				x.printStackTrace();
				ctx.status(400);
			}
		}
		
		public static void confirmSkipApproval(Context ctx) {
			if(ctx.sessionAttribute("Employee") == null) {
				ctx.status(401);
				log.warn("You must be logged in as a benefits coordinator to confirm approval attachments.");
				return;
			}
			Employee benco = ctx.sessionAttribute("Employee");
			int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
			int formId = Integer.parseInt(ctx.pathParam("formId"));
			String approvalType = ctx.formParam("approvalType");
			try {
				boolean confirmed = formServ.confirmSkipApproval(benco, employeeId, formId, approvalType);
				if(confirmed) {
					ctx.status(200);
				}
			} catch (Exception x) {
				x.printStackTrace();
				ctx.status(400);
			}
		}
		
		public static void confirmAwardDispersal(Context ctx) {
			if(ctx.sessionAttribute("Employee") == null) {
				ctx.status(401);
				log.warn("You must be logged in as a benefits coordinator to confirm passing grades.");
				return;
			}
			Employee benco = ctx.sessionAttribute("Employee");
			int employeeId = Integer.parseInt(ctx.pathParam("employeeId"));
			int formId = Integer.parseInt(ctx.pathParam("formId"));
			try {
				boolean awarded = formServ.confirmAwardDispersal(benco, employeeId, formId);
				if(awarded) {
					ctx.status(200);
				} 
			} catch (Exception x) {
					x.printStackTrace();
					ctx.status(400);
			}
			
		}
}
