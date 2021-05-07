package com.sct.utils;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

public class KeyspaceSetupUtil {

	public static void employeesTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("employees (") // table name
				.append("employeeId int PRIMARY KEY,")
				.append("employeeEmail text,")
				.append("employeeFirstName text,")
				.append("employeeLastName text,")
				.append("department text,")
				
				.append("directSuper boolean,")
				.append("deptHead boolean,")
				.append("benCo boolean,")
				
				.append("directSuperId int,")
				.append("deptHeadId int,")
				
				.append("totalReimbursement float,")
				.append("pendingReimbursements float,")
				.append("awardedReimbursements float,")
				.append("availableReimbursement float);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}

	public static void formsTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("forms (")
				.append("formId int,")
				.append("employeeId int,")
				.append("type text,")
				.append("status text,")
				.append("grade text,")
				.append("employeeFirstName text,")
				.append("employeeLastName text,")
				.append("employeeEmail text,")
				
				.append("applicationTimeZone text,")
				.append("applicationDateTime timestamp,")
				
				.append("eventLocation text,")
				.append("eventDescription text,")
				.append("eventTimeZone text,")
				.append("eventDateTime timestamp,")
				.append("eventCost float,")
				.append("justification text,")
				
				.append("superApproved boolean,")
				.append("deptApproved boolean,")
				.append("benCoApproved boolean,")
				
				.append("projectedAward float,")
				.append("adjustedAward float,")
				.append("awardValue float,")
				.append("PRIMARY KEY(employeeId, formId));");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
				
	}
	
	
	
	public static void uploadsTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
				.append("uploads (") // table name
				.append("employeeId int PRIMARY KEY,")
				.append("formId int,")
				.append("bucketKey text);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}

	public static void dbtest() {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
        		.append("trms with replication = {")
                .append("'class':'SimpleStrategy','replication_factor':1};");
        DriverConfigLoader loader = DriverConfigLoader.fromClasspath("application.conf");
        CqlSession.builder().withConfigLoader(loader).build().execute(sb.toString());
    }

}
