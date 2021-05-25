package com.tcl.dias.servicefulfillmentutils.delegates;

import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.common.engine.api.delegate.event.AbstractFlowableEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MyProcessListener extends AbstractFlowableEventListener implements ExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(MyProcessListener.class);

    @Override
    public void notify(DelegateExecution delegateExecution) {
        logger.info("Myprocesslistenre: " + delegateExecution.getEventName() + ", " + delegateExecution.getCurrentActivityId());
        if (delegateExecution.getCurrentFlowElement() != null) {
            logger.info("MPL:: " + delegateExecution.getCurrentFlowElement().getName());
            logger.info("MPL: " + delegateExecution.getCurrentFlowElement().getId());
            Map<String, List<ExtensionAttribute>> attrs = delegateExecution.getCurrentFlowElement().getAttributes();
            logger.info("MPL attrs: " + attrs);

            logger.info("MPL::" + delegateExecution.getCurrentFlowElement().getSubProcess());
        }
    }


    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        logger.info("Myprocesslistenre: " + flowableEvent.getType());

    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
