package com.sct.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sct.dao.EmployeeDAO;
import com.sct.dao.EmployeeDAOImpl;
import com.sct.factory.Log;
import com.sct.factory.ModelFactory;
import com.sct.models.Employee;

@Log
public class EmployeeServiceImpl implements EmployeeService {
	private static Logger log = LogManager.getLogger(EmployeeServiceImpl.class);
	private EmployeeDAO edao = (EmployeeDAO) ModelFactory.getFactory().get(EmployeeDAO.class, EmployeeDAOImpl.class);

	public EmployeeServiceImpl(EmployeeDAO edao) {
		this.edao = edao;
	}
	public EmployeeServiceImpl() {
		super();
	}
	
	@Override
	public Employee getEmployee(int employeeId) {
		log.trace("EmpServ: forward getEmployeeById request to edao for "+employeeId);
		return edao.getEmployeeById(employeeId);
	}

	@Override
	public boolean addEmployee(Employee e) {
		try {
			edao.addEmployee(e);
			return true;
		} catch (Exception x) {
			x.printStackTrace();
			log.warn("User already exists " + e.getEmployeeId());
			return false;
		}
	}

}
