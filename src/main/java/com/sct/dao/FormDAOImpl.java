package com.sct.dao;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.sct.factory.Log;
import com.sct.models.Employee;
import com.sct.models.Form;
import com.sct.models.Upload;
import com.sct.utils.CassandraUtil;

@Log
public class FormDAOImpl implements FormDAO {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static Logger log = LogManager.getLogger(FormDAOImpl.class);

	@Override
	public void submitForm(Form f, Employee e) throws Exception {
		ZoneId appZ = ZoneId.of(ZoneId.SHORT_IDS.get(f.getApplicationTimeZone()));
		ZonedDateTime submissionTime = ZonedDateTime.now(appZ);
		ZoneId eventZ = ZoneId.of(ZoneId.SHORT_IDS.get(f.getEventTimeZone()));
		
		if(submissionTime.plusDays(14).isAfter(f.getEventDateTime())) {
			f.setStatus(Form.Status.URGENT);
		} else {
			f.setStatus(Form.Status.SUBMITTED);
		}
		f.setAdjustedAward(Math.min(e.getAvailableReimbursement(), f.getProjectedAward()));
		

		String query = "Insert into forms (type, status, grade, formId, " + "employeeId, employeeFirstName, employeeLastName, "
				+ "employeeEmail, applicationTimeZone, applicationDateTime, "
				+ "eventLocation, eventDescription, eventTimeZone, " + "eventDateTime, eventCost, justification, "
				+ "superApproved, deptApproved, benCoApproved, projectedAward, adjustedAward, awardValue) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s)
				.bind(f.getType().toString(), 
				f.getStatus().toString(),
				f.getGrade().toString(),
				f.getFormId(), 
				e.getEmployeeId(), 
				e.getEmployeeFirstName(), 
				e.getEmployeeLastName(),
				e.getEmployeeEmail(), 
				appZ.toString(), 
				submissionTime, 
				f.getEventLocation(),
				f.getEventDescription(), 
				eventZ.toString(), 
				f.getEventDateTime(), 
				f.getEventCost(),
				f.getJustification(), 
				null, 
				null, 
				null, 
				f.getProjectedAward(),
				f.getAdjustedAward(),
				(float) 0
				);

		session.execute(bound);
	}

	@Override
	public List<Form> viewAllForms(Employee e) {
		List<Form> allForms = new ArrayList<Form>();

		String query = "SELECT * FROM forms WHERE employeeId = ?;";
		BoundStatement bound = session.prepare(query).bind(e.getEmployeeId());
		ResultSet rs = session.execute(bound);

		rs.forEach(data -> {
			ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
			ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));

			Form f = new Form();
			f.setType(Form.EventType.valueOf(data.getString("type")));
			f.setStatus(Form.Status.valueOf(data.getString("status")));
			f.setGrade(Form.GradingFormat.valueOf(data.getString("grade")));
			f.setFormId(data.getInt("formId"));
			f.setEmployeeId(data.getInt("employeeId"));
			f.setEmployeeFirstName(data.getString("employeeFirstName"));
			f.setEmployeeLastName(data.getString("employeeLastName"));
			f.setEmployeeEmail(data.getString("employeeEmail"));
			
			f.setApplicationTimeZone(appZ.toString());
			f.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
			
			f.setEventLocation(data.getString("eventLocation"));
			f.setEventDescription(data.getString("eventDescription"));
			f.setEventTimeZone(eventZ.toString());
			f.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
			f.setEventCost(data.getFloat("eventCost"));
			f.setJustification(data.getString("justification"));
			
			f.setSuperApproved(data.getBoolean("superApproved"));
			f.setDeptApproved(data.getBoolean("deptApproved"));
			f.setBenCoApproved(data.getBoolean("benCoApproved"));
			
			f.setAdjustedAward(data.getFloat("adjustedAward"));
			f.setAwardValue(data.getFloat("awardValue"));
			log.trace("Form added to list: "+f.toString());
			allForms.add(f);
		});

		return allForms;
	}

	@Override
	public List<Form> viewAllForms(Employee e, Form.Status s) {
		List<Form> allForms = new ArrayList<Form>();

		String query = "SELECT * FROM forms WHERE employeeId = ?;";
        BoundStatement bound = session.prepare(query).bind(e.getEmployeeId());
        ResultSet rs = session.execute(bound);

        rs.forEach(data -> {
            ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
            ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));

            Form f = new Form();
            f.setType(Form.EventType.valueOf(data.getString("type")));
            f.setStatus(Form.Status.valueOf(data.getString("status")));
            f.setGrade(Form.GradingFormat.valueOf(data.getString("grade")));
            f.setFormId(data.getInt("formId"));
            f.setEmployeeId(data.getInt("employeeId"));
            f.setEmployeeFirstName(data.getString("employeeFirstName"));
            f.setEmployeeLastName(data.getString("employeeLastName"));
            f.setEmployeeEmail(data.getString("employeeEmail"));
            f.setApplicationTimeZone(appZ.toString());
            f.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
            f.setEventLocation(data.getString("eventLocation"));
            f.setEventDescription(data.getString("eventDescription"));
            f.setEventTimeZone(eventZ.toString());
            f.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
            f.setEventCost(data.getFloat("eventCost"));
            f.setJustification(data.getString("justification"));
            f.setSuperApproved(data.getBoolean("superApproved"));
            f.setDeptApproved(data.getBoolean("deptApproved"));
            f.setBenCoApproved(data.getBoolean("benCoApproved"));
            f.setAdjustedAward(data.getFloat("adjustedAward"));
            f.setAwardValue(data.getFloat("awardValue"));
            if(f.getStatus().equals(s)) {
            	log.trace("Form added to list: "+f.toString());
            	allForms.add(f);
            }
        });

        return allForms;
    }

	@Override
	public Form getForm(Employee e, int formId) {
		Form f = null;
		String query = "SELECT * FROM forms WHERE employeeId = ? AND formId = ?;";
		BoundStatement bound = session.prepare(query).bind(e.getEmployeeId(), formId);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data != null) {
			f = new Form();
			
			ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
			ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));

			f.setType(Form.EventType.valueOf(data.getString("type")));
			f.setStatus(Form.Status.valueOf(data.getString("status")));
			f.setGrade(Form.GradingFormat.valueOf(data.getString("grade")));
			f.setFormId(data.getInt("formId"));
			f.setEmployeeId(data.getInt("employeeId"));
			f.setEmployeeFirstName(data.getString("employeeFirstName"));
			f.setEmployeeLastName(data.getString("employeeLastName"));
			f.setEmployeeEmail(data.getString("employeeEmail"));
			f.setApplicationTimeZone(appZ.toString());
			f.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
			f.setEventLocation(data.getString("eventLocation"));
			f.setEventDescription(data.getString("eventDescription"));
			f.setEventTimeZone(eventZ.toString());
			f.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
			f.setEventCost(data.getFloat("eventCost"));
			f.setJustification(data.getString("justification"));
			f.setSuperApproved(data.getBoolean("superApproved"));
			f.setDeptApproved(data.getBoolean("deptApproved"));
			f.setBenCoApproved(data.getBoolean("benCoApproved"));
			f.setAdjustedAward(data.getFloat("adjustedAward"));
			f.setAwardValue(data.getFloat("awardValue"));
		}

		return f;
	}

	@Override
	public boolean updateForm(Form f) {
		try {
		log.trace("FDAO: updating form: "+f.toString());
		String query = "UPDATE forms set type = ?, status = ?, grade = ?, employeeFirstName = ?, employeeLastName = ?, "
				+ "employeeEmail = ?, applicationTimeZone = ?, eventLocation = ?, eventDescription = ?, "
				+ "eventTimeZone = ?, eventDateTime = ?, eventCost = ?, justification = ?, superApproved = ?, deptApproved = ?, "
				+ "benCoApproved = ?, projectedAward = ?, adjustedAward = ?, awardValue = ? WHERE employeeId = ? AND formId = ? ";
		
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(
				f.getType().toString(), 
				f.getStatus().toString(),
				f.getGrade().toString(),
				f.getEmployeeFirstName(), 
				f.getEmployeeLastName(),
				f.getEmployeeEmail(), 
				
				f.getApplicationTimeZone(),  
				f.getEventLocation(),
				f.getEventDescription(), 
				f.getEventTimeZone(), 
				f.getEventDateTime().toInstant(), 
				f.getEventCost(),
				f.getJustification(), 
				
				f.isSuperApproved(), 
				f.isDeptApproved(), 
				f.isBenCoApproved(), 
				f.getProjectedAward(),
				f.getAdjustedAward(),
				f.getAwardValue(),

				f.getEmployeeId(),
				f.getFormId()
				);
		session.execute(bound);
		return true;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}

	@Override
	public void autoUpdaterTimeout() {
		try {
			String query = "SELECT * FROM forms;";
			ResultSet rs = session.execute(query);

			rs.forEach(data -> {
				ZoneId appZ = ZoneId.of(data.getString("applicationTimeZone"));
				ZoneId eventZ = ZoneId.of(data.getString("eventTimeZone"));
				ZonedDateTime current = ZonedDateTime.now(appZ);
				
				Form f = new Form();
				f.setType(Form.EventType.valueOf(data.getString("type")));
				f.setStatus(Form.Status.valueOf(data.getString("status")));
				f.setGrade(Form.GradingFormat.valueOf(data.getString("grade")));
				f.setFormId(data.getInt("formId"));
				f.setEmployeeId(data.getInt("employeeId"));
				f.setEmployeeFirstName(data.getString("employeeFirstName"));
				f.setEmployeeLastName(data.getString("employeeLastName"));
				f.setEmployeeEmail(data.getString("employeeEmail"));
				f.setApplicationTimeZone(appZ.toString());
				f.setApplicationDateTime((ZonedDateTime) data.getInstant("applicationDateTime").atZone(appZ));
				f.setEventLocation(data.getString("eventLocation"));
				f.setEventDescription(data.getString("eventDescription"));
				f.setEventTimeZone(eventZ.toString());
				f.setEventDateTime((ZonedDateTime) data.getInstant("eventDateTime").atZone(eventZ));
				f.setEventCost(data.getFloat("eventCost"));
				f.setJustification(data.getString("justification"));
				
				// if 7 days have passed since application and the status is still URGENT or SUBMITTED (not cancelled), supervisor and deptHead approvals automatically flag to true
/* actual code*/if((f.getStatus().equals(Form.Status.SUBMITTED) | f.getStatus().equals(Form.Status.URGENT)) && current.isAfter(f.getApplicationDateTime().plusDays(7))) {
// for demo	if((f.getStatus().equals(Form.Status.SUBMITTED) | f.getStatus().equals(Form.Status.URGENT)) && current.isAfter(f.getApplicationDateTime().plusMinutes(1l))) {
					f.setSuperApproved(true);
					f.setDeptApproved(true);
					log.trace("Auto-updating Supervisor and Dept Head approvals: 7 day timeout reached.");
				}
				else {
					f.setSuperApproved(data.getBoolean("superApproved"));
					f.setDeptApproved(data.getBoolean("deptApproved"));
				}
				if(f.getStatus() == Form.Status.SUBMITTED && current.plusDays(14).isAfter(f.getEventDateTime())) {
					f.setStatus(Form.Status.URGENT);
					log.trace("Form status updated to URGENT: "+f.toString());
				}
				f.setBenCoApproved(data.getBoolean("benCoApproved"));
				f.setAdjustedAward(data.getFloat("adjustedAward"));
				f.setAwardValue(data.getFloat("awardValue"));
				log.trace("Updating form: "+f.toString());
				updateForm(f);
				
			});

			//return allForms;
		} catch (Exception x) {
			x.printStackTrace();
		}
		
	}

	@Override
	public boolean assignFileUpload(int employeeId, int formId, String key) throws Exception {
		try {
			String query = "Insert into uploads (employeeId, formId, bucketKey) values (?,?,?);";
			SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement bound = session.prepare(s).bind(employeeId, formId, key);
			session.execute(bound);
			
			Upload up = new Upload();
			up.setBucketKey(key);
			up.setEmployeeId(employeeId);
			up.setFormId(formId);
			
			return true;
			} catch (Exception x) {
				x.printStackTrace();
				throw new Exception ("FDAO: failed to assign upload");
			}
		
	}
	
	@Override
	public List<Upload> checkUploads(int employeeId, int formId) {
		List<Upload> allUploads = new ArrayList<Upload>();
		try {
			String query = "SELECT * FROM uploads WHERE employeeId = ? AND formId = ? ALLOW FILTERING;";
			BoundStatement bound = session.prepare(query).bind(employeeId, formId);
			ResultSet rs = session.execute(bound);
			rs.forEach(data -> {
				Upload up = new Upload();
				up.setEmployeeId(data.getInt("employeeId"));
				up.setFormId(data.getInt("formId"));
				up.setBucketKey(data.getString("bucketKey"));
				allUploads.add(up);
			});
		} catch (Exception x) {
			x.printStackTrace();
		}
		return allUploads;
	}
	
}
