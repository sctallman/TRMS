package com.sct.tests;

import static org.mockito.ArgumentMatchers.anyInt;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sct.dao.EmployeeDAO;
import com.sct.models.Employee;
import com.sct.services.EmployeeService;
import com.sct.services.EmployeeServiceImpl;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	@Mock
	private EmployeeDAO edao = null;
	private EmployeeService eServ = null;
	
	@BeforeEach
	private void setup() {
		Employee e = new Employee();
		Employee f = new Employee();
		Employee g = new Employee();
		
		e.setEmployeeId(1);
		e.setEmployeeEmail("e@m.com");
		e.setEmployeeFirstName("Justin");
		e.setEmployeeLastName("Case");
		e.setDepartment("testing");
		e.setDirectSuper(true);
		e.setDeptHead(true);
		e.setBenCo(true);
		e.setDirectSuperId(42);
		e.setDeptHeadId(0);
		e.setTotalReimbursement(1000f);
		e.setPendingReimbursements(0f);
		e.setAwardedReimbursements(0f);
		Float available = e.getTotalReimbursement() - e.getPendingReimbursements() - e.getAwardedReimbursements();
		e.setAvailableReimbursement(available);
		
		f.setEmployeeId(2);
		f.setEmployeeEmail("f@m.com");
		f.setEmployeeFirstName("Marty");
		f.setEmployeeLastName("McFly");
		f.setDepartment("testing");
		f.setDirectSuper(true);
		f.setDeptHead(true);
		f.setBenCo(true);
		f.setDirectSuperId(42);
		f.setDeptHeadId(0);
		f.setTotalReimbursement(1000f);
		f.setPendingReimbursements(0f);
		f.setAwardedReimbursements(0f);
		Float available2 = e.getTotalReimbursement() - e.getPendingReimbursements() - e.getAwardedReimbursements();
		f.setAvailableReimbursement(available2);
		
		g.setEmployeeId(3);
		g.setEmployeeEmail("g@m.com");
		g.setEmployeeFirstName("Actual");
		g.setEmployeeLastName("Vampire");
		g.setDepartment("testing");
		g.setDirectSuper(true);
		g.setDeptHead(true);
		g.setBenCo(true);
		g.setDirectSuperId(42);
		g.setDeptHeadId(0);
		g.setTotalReimbursement(1000f);
		g.setPendingReimbursements(0f);
		g.setAwardedReimbursements(0f);
		Float available3 = e.getTotalReimbursement() - e.getPendingReimbursements() - e.getAwardedReimbursements();
		g.setAvailableReimbursement(available3);
		
		Set<Employee> set = new HashSet<>();
		set.add(e);
		set.add(f);
		set.add(g);
		
		Mockito.when(edao.getEmployeeById(anyInt())).then(j ->{
			int id = (int) j.getArguments()[0];
			if(id == 1) {
				return e;
			} if(id == 2) {
				return f;
			} if(id == 3) {
				return g;
			}
			return null;
		});
		
		try {
			Mockito.when(edao.addEmployee(Mockito.any(Employee.class))).then(j -> {
				Employee employee = (Employee) j.getArguments()[0];
				set.add(employee);
				System.out.println("Created new employee: "+employee);
				return employee;
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.eServ = new EmployeeServiceImpl(edao);
		
	}
	
	@Test
	private void getEmployeeTest() {
		Employee x = eServ.getEmployee(1);
		Assertions.assertEquals("Justin", x.getEmployeeFirstName());
	}
	
	@Test
	private void getEmployeeTest2() {
		Employee y = eServ.getEmployee(5);
		Assertions.assertNull(y.getEmployeeFirstName());
	}
	
	@Test
	private void getEmployeeTest3() {
		int[] ids = {1,2,3};
		for(int i : ids) {
			Assertions.assertEquals(i, eServ.getEmployee(i).getEmployeeId());
		}
	}
}
