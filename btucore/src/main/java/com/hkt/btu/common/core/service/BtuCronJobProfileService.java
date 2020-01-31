package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;

import java.util.List;

public interface BtuCronJobProfileService {

    List<BtuCronJobProfileBean> getAllJobProfile();

    BtuCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName);

    boolean isRunnable(BtuCronJobProfileBean sdCronJobProfileBean);
    boolean isRunnable(String jobGroup, String jobName);

    void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException;
    void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException;
}
