package com.sct.models;

public class Upload {
	//upload object; contains bucket key + associated formID + employeeID
	public Upload() {
		super();
	}
	
	private String bucketKey;
	private int formId;
	private int employeeId;
	
	public String getBucketKey() {
		return bucketKey;
	}
	public void setBucketKey(String bucketKey) {
		this.bucketKey = bucketKey;
	}
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucketKey == null) ? 0 : bucketKey.hashCode());
		result = prime * result + employeeId;
		result = prime * result + formId;
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
		Upload other = (Upload) obj;
		if (bucketKey == null) {
			if (other.bucketKey != null)
				return false;
		} else if (!bucketKey.equals(other.bucketKey))
			return false;
		if (employeeId != other.employeeId)
			return false;
		if (formId != other.formId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Upload [bucketKey=" + bucketKey + ", formId=" + formId + ", employeeId=" + employeeId + "]";
	}

	
}
