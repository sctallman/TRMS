package com.sct.dao;

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
import com.sct.factory.ModelFactory;
import com.sct.models.Employee;
import com.sct.models.Form;
import com.sct.utils.CassandraUtil;

@Log
public class EmployeeDAOImpl implements EmployeeDAO {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private FormDAO fdao = (FormDAO) ModelFactory.getFactory().get(FormDAO.class, FormDAOImpl.class);
	private static Logger log = LogManager.getLogger(EmployeeDAOImpl.class);
	
	@Override
	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		try {
		String query = "SELECT * FROM employees ALLOW FILTERING";
		ResultSet rs = session.execute(query);

		rs.forEach(data -> {
			Employee e = new Employee();
			e.setEmployeeId(data.getInt("employeeId"));
			e.setEmployeeEmail(data.getString("employeeEmail"));
			e.setEmployeeFirstName(data.getString("employeeFirstName"));
			e.setEmployeeLastName(data.getString("employeeLastName"));
			e.setDepartment(data.getString("department"));
			e.setDirectSuper(data.getBoolean("directSuper"));
			e.setDeptHead(data.getBoolean("deptHead"));
			e.setBenCo(data.getBoolean("benCo"));
			e.setDirectSuperId(data.getInt("directSuperId"));
			e.setDeptHeadId(data.getInt("deptHeadId"));
			e.setTotalReimbursement(data.getFloat("totalReimbursement"));
			e.setPendingReimbursements(data.getFloat("pendingReimbursements"));
			e.setAwardedReimbursements(data.getFloat("awardedReimbursements"));
			Float available = e.getTotalReimbursement() - e.getPendingReimbursements() - e.getAwardedReimbursements();
			e.setAvailableReimbursement(available);
			employees.add(e);
		});

		return employees;
		} catch (Exception x) {
			log.warn("Failed to retrieve all employees");
			return null;
		}
	}
	
	@Override
	public List<Employee> getEmployees(Employee boss) {
		List<Employee> employees = new ArrayList<Employee>();
		try {
		String query = "SELECT * FROM employees WHERE directSuperId = ? OR deptHeadId = ? ALLOW FILTERING";
		BoundStatement bound = session.prepare(query).bind(boss.getEmployeeId(), boss.getEmployeeId());
		ResultSet rs = session.execute(bound);

		rs.forEach(data -> {
			Employee e = new Employee();
			e.setEmployeeId(data.getInt("employeeId"));
			e.setEmployeeEmail(data.getString("employeeEmail"));
			e.setEmployeeFirstName(data.getString("employeeFirstName"));
			e.setEmployeeLastName(data.getString("employeeLastName"));
			e.setDepartment(data.getString("department"));
			e.setDirectSuper(data.getBoolean("directSuper"));
			e.setDeptHead(data.getBoolean("deptHead"));
			e.setBenCo(data.getBoolean("benCo"));
			e.setDirectSuperId(data.getInt("directSuperId"));
			e.setDeptHeadId(data.getInt("deptHeadId"));
			e.setTotalReimbursement(data.getFloat("totalReimbursement"));
			e.setPendingReimbursements(data.getFloat("pendingReimbursements"));
			e.setAwardedReimbursements(data.getFloat("awardedReimbursements"));
			Float available = e.getTotalReimbursement() - e.getPendingReimbursements() - e.getAwardedReimbursements();
			e.setAvailableReimbursement(available);
			employees.add(e);
		});

		return employees;
		} catch (Exception x) {
			log.warn("Failed to retrieve all employees under the specified boss");
			return null;
		}
	}

	@Override
	public Employee getEmployeeById(int employeeId) {
		Employee e = null;
		String query = "SELECT * FROM employees WHERE employeeId = ?;";
		BoundStatement bound = session.prepare(query).bind(employeeId);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data != null) {
			e = new Employee();
			e.setEmployeeId(data.getInt("employeeId"));
			e.setEmployeeEmail(data.getString("employeeEmail"));
			e.setEmployeeFirstName(data.getString("employeeFirstName"));
			e.setEmployeeLastName(data.getString("employeeLastName"));
			e.setDepartment(data.getString("department"));
			e.setDirectSuper(data.getBoolean("directSuper"));
			e.setDeptHead(data.getBoolean("deptHead"));
			e.setBenCo(data.getBoolean("benCo"));
			e.setDirectSuperId(data.getInt("directSuperId"));
			e.setDeptHeadId(data.getInt("deptHeadId"));
			e.setTotalReimbursement(data.getFloat("totalReimbursement"));
			e.setPendingReimbursements(data.getFloat("pendingReimbursements"));
			e.setAwardedReimbursements(data.getFloat("awardedReimbursements"));
			Float available = e.getTotalReimbursement() - e.getPendingReimbursements() - e.getAwardedReimbursements();
			e.setAvailableReimbursement(available);
		}
		return e;
	}

	@Override
	public boolean addEmployee(Employee e) throws Exception {
		try {
		String query = "Insert into employees (employeeId, employeeEmail, employeeFirstName, employeeLastName, department, directSuper, deptHead, benCo, directSuperId, deptHeadId, totalReimbursement, pendingReimbursements, awardedReimbursements, availableReimbursement) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s)
				.bind(e.getEmployeeId(), 
				e.getEmployeeEmail(),
				e.getEmployeeFirstName(),
				e.getEmployeeLastName(), 
				e.getDepartment(), 
				
				e.isDirectSuper(), 
				e.isDeptHead(), 
				e.isBenCo(),
				
				e.getDirectSuperId(), 
				e.getDeptHeadId(),
				
				e.getTotalReimbursement(),
				e.getPendingReimbursements(),
				e.getAwardedReimbursements(),
				e.getAvailableReimbursement());
				
		session.execute(bound);
		return true;
		} catch (Exception x) {
			x.printStackTrace();
			throw new Exception ("EDAO: failed to add employee.");
		}
	}

	@Override
	public void updateEmployee(Employee e) {
		String query = "UPDATE employees SET employeeEmail = ?, employeeFirstName = ?, employeeLastName = ?, department = ?, "
				+ "directSuper = ?, deptHead = ?, benCo = ?, directSuperId = ?, deptHeadId = ?, totalReimbursement = ?, "
				+ "pendingReimbursements = ?, awardedReimbursements = ?, availableReimbursement = ? WHERE employeeId = ?";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(e.getEmployeeEmail(),
				e.getEmployeeFirstName(),
				e.getEmployeeLastName(), 
				e.getDepartment(), 
				
				e.isDirectSuper(), 
				e.isDeptHead(), 
				e.isBenCo(),
				
				e.getDirectSuperId(), 
				e.getDeptHeadId(),
				
				e.getTotalReimbursement(),
				e.getPendingReimbursements(),
				e.getAwardedReimbursements(),
				e.getAvailableReimbursement(),
				e.getEmployeeId()
				);
				
		session.execute(bound);
	}

	@Override
	public void autoUpdaterAllotments() {
		List<Employee> employees = getEmployees();
		for(Employee e : employees) {
			float pending = 0f;
			float awarded = 0f;
			List<Form> forms = fdao.viewAllForms(e);
			for(Form f : forms) {
				if(f.getStatus().equals(Form.Status.PENDING) && f.getApplicationDateTime().getYear() == ZonedDateTime.now().getYear()) {
					pending += f.getAdjustedAward();
				}
				if(f.getStatus().equals(Form.Status.AWARDED) && f.getApplicationDateTime().getYear() == ZonedDateTime.now().getYear()) {
					awarded += f.getAwardValue();
				}
			}
			e.setPendingReimbursements(pending);
			e.setAwardedReimbursements(awarded);
			e.setAvailableReimbursement(e.getTotalReimbursement() - e.getPendingReimbursements() - e.getAwardedReimbursements());
		}
	}
}
