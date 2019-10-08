package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.WfmJobDetailsData;
import com.hkt.btu.sd.facade.data.WfmRequestDetailsBeanDate;

public interface WfmApiFacade {

    Integer createJob(WfmRequestDetailsBeanDate wfmRequestDetailsBeanDate, String createdBy);
    Integer completeJob(Integer jobId, String completeDateTime, String remark, String createdBy);
    WfmJobDetailsData getJobDetails(Integer jobId);
}
