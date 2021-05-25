package com.tcl.dias.l2oworkflowutils.service.v1;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.delegate.DelegateTask;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.l2oworkflow.entity.repository.ActivityPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ActivityRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstActivityDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstProcessDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstStageDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstStatusRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.l2oworkflow.entity.repository.StagePlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.StageRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.objectcreator.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * this class is used testing workflowservice 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkFlowServiceTest {

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	MstProcessDefRepository mstProcessDefRepository;
	
	@MockBean
	TaskAssignmentRepository taskAssignmentRepository;

	@MockBean
	StageRepository stageRepository;

	@MockBean
	MstStatusRepository mstStatusRepository;

	@MockBean
	ProcessRepository processRepository;

	@MockBean
	DelegateExecution execution;

	@MockBean
	MstStageDefRepository mstStageDefRepository;
	
	@MockBean
	MstActivityDefRepository mstActivityDefRepository;
	
	@MockBean
	ActivityRepository activityRepository;
	
	@MockBean
	MstTaskDefRepository mstTaskDefRepository;
	
	@MockBean
	TaskRepository taskRepository;
	
	@MockBean
	TATService tatservice;
	
	@MockBean
	ProcessTaskLogRepository processTaskLogRepository;
	
	@MockBean
	TaskCacheService taskCacheService;
	
	@MockBean
	ProcessPlanRepository processPlanRepository;
	
	@MockBean
	StagePlanRepository stagePlanRepository;
	
	@MockBean
	ActivityPlanRepository activityPlanRepository;

	/**
	 * @author diksha garg
	 * @return
	 */
	@Test
	public void testProcessStage() throws TclCommonException {

		/*
		 * execution--> variables,getCurrentActivityId mock
		 * mstStageDefRepository,mstStatusRepository
		 * OPEN,L2O_INPUT,OPTIMUS_STAGE_VARIABLE
		 */

		DelegateExecution execution = mock(DelegateExecution.class);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CurrentActivityId", "1");
		map.putAll(objectCreator.getMap());
		execution.setVariables(map);
		map.put("serviceId",288);
		when(execution.getVariables()).thenReturn(map);
		when(execution.getCurrentActivityId()).thenReturn("1");

		Mockito.when(mstStageDefRepository.findByKey(Mockito.anyString())).thenReturn(objectCreator.getMstStageDef());
		Mockito.when(mstStatusRepository.findByCode(Mockito.anyString())).thenReturn(objectCreator.getMstStatus());

//		Mockito.doThrow(new RuntimeException()).when(stageRepository)
//		.save(new com.tcl.dias.servicefulfillment.entity.entities.Stage());

		Mockito.when(stageRepository.save(Mockito.any())).thenReturn(objectCreator.getStage());
		Mockito.when(stagePlanRepository.findByServiceIdAndMstStageDefKey(Mockito.anyInt(),Mockito.any()))
				.thenReturn(objectCreator.getStagePlan());
		workFlowService.processStage(execution);

	}
	
	
	/**
	 * @author diksha garg
	 * @return
	 */
	@Test
	public void testInitiateProcess() throws TclCommonException {

		DelegateExecution execution = mock(DelegateExecution.class);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CurrentActivityId", "1");
		map.putAll(objectCreator.getMap());	
		execution.setVariables(map);
		map.put("serviceId",288);
	//	map.put("key", objectCreator.getMstProcessDef().getMstStageDef().getKey() );
		when(execution.getVariables()).thenReturn(map);
		when(execution.getCurrentActivityId()).thenReturn("1");
		Mockito.when(mstProcessDefRepository.findByKey(Mockito.anyString()))
				.thenReturn(objectCreator.getMstProcessDef());

		Mockito.when(stageRepository.findById(Mockito.any())).thenReturn(Optional.of(objectCreator.getStage()));

		//Mockito.when(mstStatusRepository.findByCode(Mockito.anyString())).thenReturn(objectCreator.getMstStatus());
		Mockito.when(taskCacheService.getMstStatus(Mockito.any())).thenReturn(objectCreator.getMstStatus());		
		Mockito.when(processRepository.save(Mockito.any())).thenReturn(objectCreator.getProcess());
		Mockito.when(processPlanRepository.findByServiceIdAndMstProcessDefKey(Mockito.anyInt(), Mockito.any())).thenReturn(objectCreator.getProcessPlan());
		//Mockito.when(processPlanRepository.findByServiceIdAndMstProcessDefKey(Mockito.anyInt(),Mockito.any())).thenReturn(objectCreator.getProcessPlan());
		workFlowService.initiateProcess(execution);
		// Map<String, Object> ma=
		// workFlowService.initiateProcess(objectCreator.getMap());

	}

	/**
	 * @author diksha garg
	 * @return
	 */
	@Test
	public void testProcessActivity() throws TclCommonException {

		
		/* Execution -->getCurrentActivityId
		 * mstActivityDefRepository
		 * processRepository
		 * mstStatusRepository
		 * activityRepository
		 * OPTIMUS_PROCESSVARIABLE,OPEN,L2O_INPUT,OPTIMUS_ACTIVITYVARIABLE
		 */
	
		DelegateExecution execution = mock(DelegateExecution.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("TaskDefinitionKey", "1");
		map.put("CurrentActivityId", "1");
		map.putAll(objectCreator.getMap());
		map.put("serviceId",288);
		map.put("id",288);
		//map.put("key", objectCreator.getMstActivityDef().getMstProcessDef().getKey());
		execution.setVariables(map);
		when(execution.getVariables()).thenReturn(map);
		when(execution.getCurrentActivityId()).thenReturn("1");
		Mockito.when(mstActivityDefRepository.findByKey(Mockito.anyString())).thenReturn(objectCreator.getMstActivityDef());
		Mockito.when(mstStatusRepository.findByCode(Mockito.anyString())).thenReturn(objectCreator.getMstStatus());
		Mockito.when(processRepository.findById(Mockito.any())).thenReturn(Optional.of(objectCreator.getProcessRep()));
		Mockito.when(activityRepository.save(Mockito.any())).thenReturn(objectCreator.getActivity());
		Mockito.when(activityPlanRepository.findByServiceIdAndMstActivityDefKey(Mockito.anyInt(),Mockito.any()))
		.thenReturn(objectCreator.getActivityPlan());
		workFlowService.processActivity(execution);
		
	}
	
	/**
	 * @author diksha garg
	 * @return
	 */
	@Test
	public void testProcessManulTask() throws TclCommonException {
		
		
		DelegateTask execution = mock(DelegateTask.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("TaskDefinitionKey", "1");
		map.putAll(objectCreator.getMap());
		execution.setVariables(map);
		when(execution.getVariables()).thenReturn(map);
		when(execution.getTaskDefinitionKey()).thenReturn("1");
		
		Mockito.when(mstTaskDefRepository.findByKey(Mockito.anyString())).thenReturn(objectCreator.getMstTaskDef());
		Mockito.when(activityRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getActivity()));
		Mockito.when(mstStatusRepository.findByCode(Mockito.anyString())).thenReturn(objectCreator.getMstStatus());
				
		Mockito.when(taskRepository.save(Mockito.any())).thenReturn(objectCreator.getTask());
		Mockito.when(taskAssignmentRepository.save(Mockito.any())).thenReturn(objectCreator.getTaskAssignment());
		Mockito.when(processRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getProcessRep()));
		Mockito.when(processTaskLogRepository.save(Mockito.any())).thenReturn(objectCreator.getProcessTaskLog());
		
		workFlowService.processManulTask(execution);
		
	}
	
	/**
	 * @author diksha garg
	 * @return
	 */
	@Test
	public void testProcessTask() throws TclCommonException {
		
		DelegateExecution execution = mock(DelegateExecution.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("TaskDefinitionKey", "1");
		map.putAll(objectCreator.getMap());
		execution.setVariables(map);
		when(execution.getVariables()).thenReturn(map);
		when(execution.getCurrentActivityId()).thenReturn("1");
		
		Mockito.when(mstTaskDefRepository.findByKey(Mockito.any())).thenReturn(objectCreator.getMstTaskDef());
		Mockito.when(activityRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getActivity()));
		Mockito.when(mstStatusRepository.findByCode(Mockito.anyString())).thenReturn(objectCreator.getMstStatus());
		Mockito.when(processRepository.findById(Mockito.any())).thenReturn(Optional.of(objectCreator.getProcessRep()));
		
		Mockito.when(tatservice.calculateDueDate(Mockito.anyInt(),Mockito.any(),Mockito.any())).thenReturn(new Timestamp(new Date().getTime()));


		Mockito.when(taskRepository.save(Mockito.any())).thenReturn(objectCreator.getTask());
		Mockito.when(taskAssignmentRepository.save(Mockito.any())).thenReturn(objectCreator.getTaskAssignment());
		//Mockito.when(processRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getProcessRep()));
		Mockito.when(processTaskLogRepository.save(Mockito.any())).thenReturn(objectCreator.getProcessTaskLog());
		
		
		workFlowService.processServiceTask(execution);
	}

}
