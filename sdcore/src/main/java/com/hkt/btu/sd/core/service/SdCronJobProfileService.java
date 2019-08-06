package com.hkt.btu.sd.core.service;


import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;

import java.util.List;

public interface SdCronJobProfileService {

    List<SdCronJobProfileBean> getAll();
    SdCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName);

    boolean isRunnable(SdCronJobProfileBean sdCronJobProfileBean);
    boolean isRunnable(String jobGroup, String jobName);

    void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException;
    void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException;
}
