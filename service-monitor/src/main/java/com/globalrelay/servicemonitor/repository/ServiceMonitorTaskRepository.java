package com.globalrelay.servicemonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;

/**
 * An JPA repository interface to perform CRUD operations on the TASK
 * table. This class extends {@linkplain JpaRepository} and provides ORM mapping
 * between database TASK table and ServiceMOnitorTask java class.
 * <p>
 * During compile time Spring-JPA will automatically generates an implementation
 * of this interface.
 * 
 * @author Ravikiran Butti
 *
 */
public interface ServiceMonitorTaskRepository extends JpaRepository<ServiceMonitorTask, Long> {

	@Query("SELECT t FROM ServiceMonitorTask t  WHERE t.status.hostName = :hostName and t.status.port = :port")
	public List<ServiceMonitorTask> findByHostNameAndPort(@Param("hostName") String hostName, @Param("port") int port);

}
