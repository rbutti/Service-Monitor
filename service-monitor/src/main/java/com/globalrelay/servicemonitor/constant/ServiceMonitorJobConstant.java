package com.globalrelay.servicemonitor.constant;

/**
 * A file to hold the constants used by all the Job classes in the application
 * 
 * @author Ravikiran Butti
 *
 */
public interface ServiceMonitorJobConstant {
	/**
	 * The key that will be used to store and retrieve the name of the Job class that
	 * needs to be executed by the scheduler
	 */
	String JOB_CLASS_KEY = "jobClass";
	
	/**
	 * The key that will be used to store and retrieve the Id of the task that
	 * needs to be executed by while running a job by the scheduler
	 */
	String TASK_ID_KEY = "taskId";
}
