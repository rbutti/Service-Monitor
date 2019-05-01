package com.globalrelay.servicemonitor.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * A domain class to store the current status and execution details of a service
 * monitoring job.
 * <p>
 * This domain class also acts as a entity for database table "task_status" and
 * participates in ORM mapping.
 * <p>
 * This class extends {@linkplain AbstractDomainObject} and has auditing
 * features to track records created and updated timestamps
 * <p>
 * This class has many to one relationship with {@linkplain ServiceMonitorTask}
 * 
 * @author Ravikiran Butti
 *
 */
@Entity
@Table(name = "task_status")
public class ServiceMonitorStatus extends AbstractDomainObject {

	private static final long serialVersionUID = -1301831279801111878L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "host_name")
	private String hostName;

	@Column(name = "port")
	private int port;

	@Column(name = "status_cd")
	private String statusCd;

	@Column(name = "last_failed_time")
	private LocalDateTime lastFailedTime;

	@Column(name = "cronexpression")
	private String cronExpression;

	@Transient
	private String cronHumanExpression;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public LocalDateTime getLastFailedTime() {
		return lastFailedTime;
	}

	public void setLastFailedTime(LocalDateTime lastFailedTime) {
		this.lastFailedTime = lastFailedTime;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getCronHumanExpression() {
		return cronHumanExpression;
	}

	public void setCronHumanExpression(String cronHumanExpression) {
		this.cronHumanExpression = cronHumanExpression;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cronExpression == null) ? 0 : cronExpression.hashCode());
		result = prime * result + ((cronHumanExpression == null) ? 0 : cronHumanExpression.hashCode());
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastFailedTime == null) ? 0 : lastFailedTime.hashCode());
		result = prime * result + port;
		result = prime * result + ((statusCd == null) ? 0 : statusCd.hashCode());
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
		ServiceMonitorStatus other = (ServiceMonitorStatus) obj;
		if (cronExpression == null) {
			if (other.cronExpression != null)
				return false;
		} else if (!cronExpression.equals(other.cronExpression))
			return false;
		if (cronHumanExpression == null) {
			if (other.cronHumanExpression != null)
				return false;
		} else if (!cronHumanExpression.equals(other.cronHumanExpression))
			return false;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastFailedTime == null) {
			if (other.lastFailedTime != null)
				return false;
		} else if (!lastFailedTime.equals(other.lastFailedTime))
			return false;
		if (port != other.port)
			return false;
		if (statusCd == null) {
			if (other.statusCd != null)
				return false;
		} else if (!statusCd.equals(other.statusCd))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceMonitorStatus [id=" + id + ", hostName=" + hostName + ", port=" + port + ", statusCd=" + statusCd
				+ ", lastFailedTime=" + lastFailedTime + ", cronExpression=" + cronExpression + ", cronHumanExpression="
				+ cronHumanExpression + "]";
	}

}