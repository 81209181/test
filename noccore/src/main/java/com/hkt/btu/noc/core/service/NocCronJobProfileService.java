package com.hkt.btu.noc.core.service;


import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.bean.NocCronJobProfileBean;

import java.util.List;

public interface NocCronJobProfileService {

    List<NocCronJobProfileBean> getAll();
    NocCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName);

    boolean isRunnable(NocCronJobProfileBean nocCronJobProfileBean);
    boolean isRunnable(String jobGroup, String jobName);

    void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException;
    void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException;
}
