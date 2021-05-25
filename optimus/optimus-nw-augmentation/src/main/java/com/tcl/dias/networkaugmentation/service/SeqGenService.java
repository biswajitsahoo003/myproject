package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.networkaugment.entity.entities.SeqGen;
import com.tcl.dias.networkaugment.entity.repository.SeqGenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false,isolation= Isolation.READ_COMMITTED)
public class SeqGenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NwaMstMasterDataService.class);

    @Autowired
    SeqGenRepository seqGenRepository;

    public Integer getSequenceNo(String applicationId){
        SeqGen seqgen = seqGenRepository.findByApplicationId(applicationId);
        return seqgen.getSeqNo();

    }

    public void updateSequenceNo(String applicationId, Integer seqNo){
        SeqGen seqgen = seqGenRepository.findByApplicationId(applicationId);
        seqgen.setSeqNo(seqNo);
        seqGenRepository.save(seqgen);
    }

}
