package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.tcl.dias.networkaugment.entity.entities.NwaEorEquipDetails;
import com.tcl.dias.networkaugment.entity.entities.ScOrder;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.repository.ScOrderRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.MfTaskTrailBean;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

@Component
public class NotifyOriginatorDelegate implements JavaDelegate {
    @Autowired
    NotificationService notificationService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @Autowired
    ScOrderRepository scOrderRepository;

    @Autowired
    NetworkAugmentationWorkFlowService workFlowService;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        String errorMessage = "";

        Task task = workFlowService.processServiceTask(delegateExecution);
       // System.out.println("======task id is " +task.getId());

        Map<String, Object> processMap = delegateExecution.getVariables();
        System.out.println(" ===== NotifyOriginatorDelegate started ");
        System.out.println("======process Map is ...."+ processMap);

        Integer serviceId = (Integer) processMap.get(SERVICE_ID);
        Optional<ScServiceDetail> optionalService=scServiceDetailRepository.findById(serviceId);
        ScServiceDetail scServiceDetail = optionalService.get();
        ScOrder scOrder =  scServiceDetail.getScOrder();
        Set<NwaEorEquipDetails> nwaEorEquipDetails = scOrder.getNwaEorEquipDetails();

        List<MfTaskTrailBean> trail = taskService.getTaskTrails(task.getId());

       notificationService.notifyOriginator(scServiceDetail,scOrder,nwaEorEquipDetails,trail);

       workFlowService.processServiceTaskCompletion(delegateExecution ,errorMessage);

        scOrder.setOrderStatus(TaskStatusConstants.COMMISSIONED);
        scServiceDetail.setServiceStatus(TaskStatusConstants.COMMISSIONED);
        scOrderRepository.save(scOrder);
        scServiceDetailRepository.save(scServiceDetail);

    }


}