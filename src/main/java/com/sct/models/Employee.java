package com.sct.models;

public class Employee {

	public Employee() {
		super();
	}

	private int employeeId;
	private String employeeEmail;
	private String employeeFirstName;
	private String employeeLastName;
	private String department;

	private boolean directSuper;
	private boolean deptHead;
	private boolean benCo;

	private int directSuperId;
	private int deptHeadId;
	
	private float totalReimbursement = (float) 1000.00f;
	private float pendingReimbursements;
	private float awardedReimbursements;
	private float availableReimbursement;// = totalReimbursement - pendingReimbursements - awardedReimbursements

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public boolean isDirectSuper() {
		return directSuper;
	}

	public void setDirectSuper(boolean directSuper) {
		this.directSuper = directSuper;
	}

	public boolean isDeptHead() {
		return deptHead;
	}

	public void setDeptHead(boolean deptHead) {
		this.deptHead = deptHead;
	}

	public boolean isBenCo() {
		return benCo;
	}

	public void setBenCo(boolean benCo) {
		this.benCo = benCo;
	}

	public int getDirectSuperId() {
		return directSuperId;
	}

	public void setDirectSuperId(int directSuperId) {
		this.directSuperId = directSuperId;
	}

	public int getDeptHeadId() {
		return deptHeadId;
	}

	public void setDeptHeadId(int deptHeadId) {
		this.deptHeadId = deptHeadId;
	}

	public float getTotalReimbursement() {
		return totalReimbursement;
	}

	public void setTotalReimbursement(float totalReimbursement) {
		this.totalReimbursement = totalReimbursement;
	}

	public float getPendingReimbursements() {
		return pendingReimbursements;
	}

	public void setPendingReimbursements(float pendingReimbursements) {
		this.pendingReimbursements = pendingReimbursements;
	}

	public float getAwardedReimbursements() {
		return awardedReimbursements;
	}

	public void setAwardedReimbursements(float awardedReimbursements) {
		this.awardedReimbursements = awardedReimbursements;
	}

	public float getAvailableReimbursement() {
		return availableReimbursement;
	}

	public void setAvailableReimbursement(float availableReimbursement) {
		this.availableReimbursement = availableReimbursement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(availableReimbursement);
		result = prime * result + Float.floatToIntBits(awardedReimbursements);
		result = prime * result + (benCo ? 1231 : 1237);
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + (deptHead ? 1231 : 1237);
		result = prime * result + deptHeadId;
		result = prime * result + (directSuper ? 1231 : 1237);
		result = prime * result + directSuperId;
		result = prime * result + ((employeeEmail == null) ? 0 : employeeEmail.hashCode());
		result = prime * result + ((employeeFirstName == null) ? 0 : employeeFirstName.hashCode());
		result = prime * result + employeeId;
		result = prime * result + ((employeeLastName == null) ? 0 : employeeLastName.hashCode());
		result = prime * result + Float.floatToIntBits(pendingReimbursements);
		result = prime * result + Float.floatToIntBits(totalReimbursement);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (Float.floatToIntBits(availableReimbursement) != Float.floatToIntBits(other.availableReimbursement))
			return false;
		if (Float.floatToIntBits(awardedReimbursements) != Float.floatToIntBits(other.awardedReimbursements))
			return false;
		if (benCo != other.benCo)
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (deptHead != other.deptHead)
			return false;
		if (deptHeadId != other.deptHeadId)
			return false;
		if (directSuper != other.directSuper)
			return false;
		if (directSuperId != other.directSuperId)
			return false;
		if (employeeEmail == null) {
			if (other.employeeEmail != null)
				return false;
		} else if (!employeeEmail.equals(other.employeeEmail))
			return false;
		if (employeeFirstName == null) {
			if (other.employeeFirstName != null)
				return false;
		} else if (!employeeFirstName.equals(other.employeeFirstName))
			return false;
		if (employeeId != other.employeeId)
			return false;
		if (employeeLastName == null) {
			if (other.employeeLastName != null)
				return false;
		} else if (!employeeLastName.equals(other.employeeLastName))
			return false;
		if (Float.floatToIntBits(pendingReimbursements) != Float.floatToIntBits(other.pendingReimbursements))
			return false;
		if (Float.floatToIntBits(totalReimbursement) != Float.floatToIntBits(other.totalReimbursement))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeEmail=" + employeeEmail + ", employeeFirstName="
				+ employeeFirstName + ", employeeLastName=" + employeeLastName + ", department=" + department
				+ ", directSuper=" + directSuper + ", deptHead=" + deptHead + ", benCo=" + benCo + ", directSuperId="
				+ directSuperId + ", deptHeadId=" + deptHeadId + ", totalReimbursement=" + totalReimbursement
				+ ", pendingReimbursements=" + pendingReimbursements + ", awardedReimbursements="
				+ awardedReimbursements + ", availableReimbursement=" + availableReimbursement + "]";
	}



}
