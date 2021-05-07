package com.sct.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sct.dao.EmployeeDAO;
import com.sct.dao.EmployeeDAOImpl;
import com.sct.dao.FormDAO;
import com.sct.dao.FormDAOImpl;
import com.sct.factory.Log;
import com.sct.factory.ModelFactory;
import com.sct.models.Employee;
import com.sct.models.Form;
import com.sct.models.Upload;

@Log
public class FormServiceImpl implements FormService {
	private static Logger log = LogManager.getLogger(FormServiceImpl.class);
	private FormDAO fdao = (FormDAO) ModelFactory.getFactory().get(FormDAO.class, FormDAOImpl.class);
	private EmployeeDAO edao = (EmployeeDAO) ModelFactory.getFactory().get(EmployeeDAO.class, EmployeeDAOImpl.class);

	@Override
	public boolean submitForm(Form f, Employee e) {
		log.trace("FormServ: forwarding submitForm request to fdao for " + e.getEmployeeId());
		try {
			fdao.submitForm(f, e);
			return true;
		} catch (Exception x) {
			x.printStackTrace();
			log.warn("FDAO: form submission failure.");
			return false;
		}
	}

	@Override
	public List<Form> viewAllForms(Employee e) {
		log.trace("FormServ: forwarding viewForms request to fdao for " + e.getEmployeeId());
		try {
			return fdao.viewAllForms(e);
		} catch (Exception x) {
			x.printStackTrace();
			log.warn("FDAO: failure to retrieve forms list for " + e.getEmployeeId());
			return null;
		}

	}


	@Override
	public List<Form> checkToDo(Employee boss) {
		try {
			List<Form> formsToDo = new ArrayList<Form>();
			List<Employee> underlings = edao.getEmployees(boss);
			if (underlings != null) {
				for (Employee e : underlings) {
					List<Form> urgentForms = fdao.viewAllForms(e, Form.Status.URGENT);
					if (urgentForms != null) {
						for (Form f : urgentForms) {
							formsToDo.add(f);
						}
					}
					List<Form> submittedForms = fdao.viewAllForms(e, Form.Status.SUBMITTED);
					if (submittedForms != null) {
						for (Form f : submittedForms) {
							formsToDo.add(f);
						}
					}
				}
			}
			return formsToDo;
		} catch (Exception x) {
			x.printStackTrace();
			log.warn("FormServ: TODO list retrieval error");
			return null;
		}
	}

	@Override
	public boolean cancelForm(Employee e, int formId) {
		try {
			log.trace("FormServ: forwarding cancelForm request to fdao for employee: " + e.getEmployeeId() + ", form: "
					+ formId);
			Form f = fdao.getForm(e, formId);
			f.setStatus(Form.Status.CANCELLED);
			log.trace("FormServ: form status should be cancelled: "+f.getStatus().toString());
			return fdao.updateForm(f);
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean requestApproval(Employee boss, int employeeId, int formId, boolean approval) {
			Employee e = edao.getEmployeeById(employeeId);
			Form f = fdao.getForm(e, formId);
		try {
			if (boss.isDirectSuper() && e.getDirectSuperId() == boss.getEmployeeId()) { // if the boss is a direct Supervisor, and their ID matches the employee's direct supervisor's id
				f.setSuperApproved(approval);											// then set the employee's supervisor approval to their approval judgement
				if (boss.isDeptHead() == true && e.getDeptHeadId() == boss.getEmployeeId()) { // if the boss is also a dept head & the employee's dept head
					f.setDeptApproved(approval);											  // then set the dept head approval to their approval judgement
					if (boss.isBenCo() == true) {
						f.setBenCoApproved(approval);
						if (f.isSuperApproved() && f.isDeptApproved() && f.isBenCoApproved()) {
							f.setStatus(Form.Status.PENDING);
							log.trace("FormServ: form status updated to PENDING: "+f.toString());
						}
					}
				}
				log.trace("FormServ: Updating form "+f.toString());
				return fdao.updateForm(f);
				
			}
			if (boss.isDeptHead() && e.getDeptHeadId() == boss.getEmployeeId() && f.isSuperApproved()) { // if the boss is a dept head & their ID matches the employee's dept Head's id
				f.setDeptApproved(approval);															 // then set the employee's dept head approval to the boss' approval judgement
				if(boss.isBenCo()) {
					f.setBenCoApproved(approval);
					if(f.isSuperApproved() && f.isDeptApproved() && f.isBenCoApproved()) {
						f.setStatus(Form.Status.PENDING);
						log.trace("FormServ: form status updated to PENDING: "+f.toString());
					}
				}
				log.trace("FormServ: Updating form "+f.toString());
				return fdao.updateForm(f);
			}
			if (boss.isBenCo()) {
				if (f.isSuperApproved() && f.isDeptApproved()) {
					f.setBenCoApproved(approval);
					if (f.isBenCoApproved()) {
						f.setStatus(Form.Status.PENDING);
						log.trace("FormServ: form status updated to PENDING: "+f.toString());
					}
					log.trace("FormServ: Updating form "+f.toString());
					return fdao.updateForm(f);
				}

			}
		log.trace("FormServ: Could not update form "+f.toString());
		return false;
		} catch (Exception x) {
			x.printStackTrace();
			log.trace("FormServ: Error updating form "+f.toString());
			return false;
		}
	}
	
	@Override
	public boolean assignAwardValue(Employee boss, int employeeId, int formId) {
		if(!boss.isBenCo()) {
			log.warn("Logged in user is not a benefits coordinator.");
			return false;
		}
		try {
			Employee e = edao.getEmployeeById(employeeId);
			Form f = fdao.getForm(e, formId);
			f.setAwardValue(f.getAdjustedAward());
			fdao.updateForm(f);
		} catch (Exception x) {
			x.printStackTrace();
			log.warn("FormServ: failed to assign award value");
			return false;
		}
		
		return false;
	}

	@Override
	public boolean assignAwardValue(Employee boss, int employeeId, int formId, float value) {
		if(!boss.isBenCo()) {
			log.warn("Logged in user is not a benefits coordinator.");
			return false;
		}
		if(boss.getEmployeeId() == employeeId) {
			log.warn("You're trying to alter your own reimbursement award? Does that count as embezzlement?");
			return false;
		}
		try {
			Employee e = edao.getEmployeeById(employeeId);
			Form f = fdao.getForm(e, formId);
			f.setAwardValue(value);
			fdao.updateForm(f);
		} catch (Exception x) {
			x.printStackTrace();
			log.warn("FormServ: failed to assign award value");
			return false;
		}
		
		return false;
	}

	
	@Override
	public boolean assignFileUpload(int employeeId, int formId, String key) {
		try {
			log.trace("FormServ: forwarding upload object assignment to fdao");
			return fdao.assignFileUpload(employeeId, formId, key);
		} catch (Exception x) {
			log.warn("FormServ: failed to assign upload object.");
			x.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Upload> checkUploads(int employeeId, int formId) {
		try {
			log.trace("FormServ: forwarding check uploads request to fdao");
			return fdao.checkUploads(employeeId, formId);
			
		} catch (Exception x) {
			log.warn("FormServ: failed to retrieve upload list");
			x.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean confirmSkipApproval(Employee benco, int employeeId, int formId, String approvalType) {
		if(!benco.isBenCo()) {
			log.warn("Only benefits coordinators may confirm attachments for approval process");
			return false;
		}
		if(benco.getEmployeeId() == employeeId) {
			log.warn("You can't confirm your own reimbursement :) ");
			return false;
		}
		try {
			log.trace("Confirming attachment as valid approval material.");
			Employee e = edao.getEmployeeById(employeeId);
			Form f = fdao.getForm(e, formId);
			if(approvalType.equalsIgnoreCase("directSuper")) {
				f.setSuperApproved(true);
				return fdao.updateForm(f);
			}
			if(approvalType.equalsIgnoreCase("deptHead")) {
				f.setDeptApproved(true);
				return fdao.updateForm(f);
			}
			if(approvalType.equalsIgnoreCase("both")) {
				f.setSuperApproved(true);
				f.setDeptApproved(true);
				return fdao.updateForm(f);
			}
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	@Override
	public boolean confirmAwardDispersal(Employee benco, int employeeId, int formId) {
		if(!benco.isBenCo()) {
			log.warn("Only benefits coordinators may confirm reimbursement awards for dispersal.");
			return false;
		}
		if(benco.getEmployeeId() == employeeId) {
			log.warn("You can't confirm your own award for dispersal :) ");
			return false;
		}
		try {
			Employee e = edao.getEmployeeById(employeeId);
			Form f = fdao.getForm(e, formId);
			if(f.isSuperApproved() && f.isDeptApproved() && f.isBenCoApproved()) {
				f.setStatus(Form.Status.AWARDED);
				return fdao.updateForm(f);
			}
			return false;
		} catch (Exception x) {
			x.printStackTrace();
			return false;
		}
	}
}
