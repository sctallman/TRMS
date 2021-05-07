package com.sct.models;

import java.time.ZonedDateTime;

public class Form {

	public Form() {
		super();
	}

	public static enum EventType {
		UNICOURSE(0.8f), SEMINAR(0.6f), PREPCERT(0.75f), CERTIF(1f), TECHTRAIN(0.9f), OTHER(0.3f);

		private final float percent;

		private EventType(float percent) {
			this.percent = percent;
		}

		public float getPercent() {
			return percent;
		}
	}

	public static enum Status {
		URGENT, SUBMITTED, PENDING, CANCELLED, AWARDED
	}
	
	public static enum GradingFormat {
		LETTER, PASSFAIL, PRESENTATION 
	}

	private EventType type;
	private Status status;
	private GradingFormat grade;

	private int formId;
	private int employeeId;
	private String employeeFirstName;
	private String employeeLastName;
	private String employeeEmail;

	private String applicationTimeZone;
	private ZonedDateTime applicationDateTime;

	private String eventLocation;
	private String eventDescription;
	private String eventTimeZone;
	private ZonedDateTime eventDateTime;
	private float eventCost;
	private String justification;	// employee reason for attending event

	private boolean superApproved;	// supervisor approval on request
	private boolean deptApproved;	// dept head approval on request
	private boolean benCoApproved;	// benefits coordinator approval on request

	private float projectedAward;	// read only
	private float adjustedAward;
	private float awardValue;

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public GradingFormat getGrade() {
		return grade;
	}

	public void setGrade(GradingFormat grade) {
		this.grade = grade;
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

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getApplicationTimeZone() {
		return applicationTimeZone;
	}

	public void setApplicationTimeZone(String applicationTimeZone) {
		this.applicationTimeZone = applicationTimeZone;
	}

	public ZonedDateTime getApplicationDateTime() {
		return applicationDateTime;
	}

	public void setApplicationDateTime(ZonedDateTime applicationDateTime) {
		this.applicationDateTime = applicationDateTime;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventTimeZone() {
		return eventTimeZone;
	}

	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}

	public ZonedDateTime getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(ZonedDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public float getEventCost() {
		return eventCost;
	}

	public void setEventCost(float eventCost) {
		this.eventCost = eventCost;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public boolean isSuperApproved() {
		return superApproved;
	}

	public void setSuperApproved(boolean superApproved) {
		this.superApproved = superApproved;
	}

	public boolean isDeptApproved() {
		return deptApproved;
	}

	public void setDeptApproved(boolean deptApproved) {
		this.deptApproved = deptApproved;
	}

	public boolean isBenCoApproved() {
		return benCoApproved;
	}

	public void setBenCoApproved(boolean benCoApproved) {
		this.benCoApproved = benCoApproved;
	}
	
	public float getProjectedAward() {
		return this.type.getPercent() * this.eventCost;
	}

	public void setProjectedAward() {
		this.projectedAward = this.type.getPercent() * this.eventCost;
	}
	
	public float getAdjustedAward() {
		return adjustedAward;
	}

	public void setAdjustedAward(float adjustedAward) {
		this.adjustedAward = adjustedAward;
	}

	public float getAwardValue() {
		return awardValue;
	}

	public void setAwardValue(float awardValue) {
		this.awardValue = awardValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(adjustedAward);
		result = prime * result + ((applicationDateTime == null) ? 0 : applicationDateTime.hashCode());
		result = prime * result + ((applicationTimeZone == null) ? 0 : applicationTimeZone.hashCode());
		result = prime * result + Float.floatToIntBits(awardValue);
		result = prime * result + (benCoApproved ? 1231 : 1237);
		result = prime * result + (deptApproved ? 1231 : 1237);
		result = prime * result + ((employeeEmail == null) ? 0 : employeeEmail.hashCode());
		result = prime * result + ((employeeFirstName == null) ? 0 : employeeFirstName.hashCode());
		result = prime * result + employeeId;
		result = prime * result + ((employeeLastName == null) ? 0 : employeeLastName.hashCode());
		result = prime * result + Float.floatToIntBits(eventCost);
		result = prime * result + ((eventDateTime == null) ? 0 : eventDateTime.hashCode());
		result = prime * result + ((eventDescription == null) ? 0 : eventDescription.hashCode());
		result = prime * result + ((eventLocation == null) ? 0 : eventLocation.hashCode());
		result = prime * result + ((eventTimeZone == null) ? 0 : eventTimeZone.hashCode());
		result = prime * result + formId;
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + ((justification == null) ? 0 : justification.hashCode());
		result = prime * result + Float.floatToIntBits(projectedAward);
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + (superApproved ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Form other = (Form) obj;
		if (Float.floatToIntBits(adjustedAward) != Float.floatToIntBits(other.adjustedAward))
			return false;
		if (applicationDateTime == null) {
			if (other.applicationDateTime != null)
				return false;
		} else if (!applicationDateTime.equals(other.applicationDateTime))
			return false;
		if (applicationTimeZone == null) {
			if (other.applicationTimeZone != null)
				return false;
		} else if (!applicationTimeZone.equals(other.applicationTimeZone))
			return false;
		if (Float.floatToIntBits(awardValue) != Float.floatToIntBits(other.awardValue))
			return false;
		if (benCoApproved != other.benCoApproved)
			return false;
		if (deptApproved != other.deptApproved)
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
		if (Float.floatToIntBits(eventCost) != Float.floatToIntBits(other.eventCost))
			return false;
		if (eventDateTime == null) {
			if (other.eventDateTime != null)
				return false;
		} else if (!eventDateTime.equals(other.eventDateTime))
			return false;
		if (eventDescription == null) {
			if (other.eventDescription != null)
				return false;
		} else if (!eventDescription.equals(other.eventDescription))
			return false;
		if (eventLocation == null) {
			if (other.eventLocation != null)
				return false;
		} else if (!eventLocation.equals(other.eventLocation))
			return false;
		if (eventTimeZone == null) {
			if (other.eventTimeZone != null)
				return false;
		} else if (!eventTimeZone.equals(other.eventTimeZone))
			return false;
		if (formId != other.formId)
			return false;
		if (grade != other.grade)
			return false;
		if (justification == null) {
			if (other.justification != null)
				return false;
		} else if (!justification.equals(other.justification))
			return false;
		if (Float.floatToIntBits(projectedAward) != Float.floatToIntBits(other.projectedAward))
			return false;
		if (status != other.status)
			return false;
		if (superApproved != other.superApproved)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Form [type=" + type + ", status=" + status + ", grade=" + grade + ", formId=" + formId + ", employeeId="
				+ employeeId + ", employeeFirstName=" + employeeFirstName + ", employeeLastName=" + employeeLastName
				+ ", employeeEmail=" + employeeEmail + ", applicationTimeZone=" + applicationTimeZone
				+ ", applicationDateTime=" + applicationDateTime + ", eventLocation=" + eventLocation
				+ ", eventDescription=" + eventDescription + ", eventTimeZone=" + eventTimeZone + ", eventDateTime="
				+ eventDateTime + ", eventCost=" + eventCost + ", justification=" + justification + ", superApproved="
				+ superApproved + ", deptApproved=" + deptApproved + ", benCoApproved=" + benCoApproved
				+ ", projectedAward=" + projectedAward + ", adjustedAward=" + adjustedAward + ", awardValue="
				+ awardValue + "]";
	}

	
}
