package com.globalrelay.servicemonitor.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * A domain class to store the details of a task to be executed by the Scheduled
 * job.
 * <p>
 * This domain class also acts as a entity for database table "task" and
 * participates in ORM mapping.
 * <p>
 * This class extends {@linkplain AbstractDomainObject} and has auditing
 * features to track records created and updated timestamps
 * <p>
 * This class has many to one relationship with
 * {@linkplain ServiceMonitorStatus}
 * 
 * @author Ravikiran Butti
 *
 */
@Entity
@Table(name = "task")
public class ServiceMonitorTask extends AbstractDomainObject {

	private static final long serialVersionUID = -8630782409450154412L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "jobclass")
	private String jobClass;

	@Transient
	private boolean active;

	@Column(name = "email")
	private String email;

	@Column(name = "grace_time")
	private int graceTime;

	@Column(name = "outage_from")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm", iso = ISO.DATE_TIME)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
	private LocalDateTime outageFrom;

	@Column(name = "outage_to")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm", iso = ISO.DATE_TIME)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm")
	private LocalDateTime outageTo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "task_status_id", nullable = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ServiceMonitorStatus status;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public ServiceMonitorStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceMonitorStatus status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGraceTime() {
		return graceTime;
	}

	public void setGraceTime(int graceTime) {
		this.graceTime = graceTime;
	}

	public LocalDateTime getOutageFrom() {
		return outageFrom;
	}

	public void setOutageFrom(LocalDateTime outageFrom) {
		this.outageFrom = outageFrom;
	}

	public LocalDateTime getOutageTo() {
		return outageTo;
	}

	public void setOutageTo(LocalDateTime outageTo) {
		this.outageTo = outageTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + graceTime;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jobClass == null) ? 0 : jobClass.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((outageFrom == null) ? 0 : outageFrom.hashCode());
		result = prime * result + ((outageTo == null) ? 0 : outageTo.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ServiceMonitorTask other = (ServiceMonitorTask) obj;
		if (active != other.active)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (graceTime != other.graceTime)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (jobClass == null) {
			if (other.jobClass != null)
				return false;
		} else if (!jobClass.equals(other.jobClass))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (outageFrom == null) {
			if (other.outageFrom != null)
				return false;
		} else if (!outageFrom.equals(other.outageFrom))
			return false;
		if (outageTo == null) {
			if (other.outageTo != null)
				return false;
		} else if (!outageTo.equals(other.outageTo))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceMonitorTask [id=" + id + ", name=" + name + ", jobClass=" + jobClass + ", active=" + active
				+ ", email=" + email + ", graceTime=" + graceTime + ", outageFrom=" + outageFrom + ", outageTo="
				+ outageTo + ", status=" + status + "]";
	}

}
