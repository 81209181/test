package com.hkt.btu.noc.facade;


import com.hkt.btu.noc.facade.data.NocCronJobInstData;
import com.hkt.btu.noc.facade.data.NocCronJobProfileData;

import java.util.List;

public interface NocJobFacade {
    List<NocCronJobInstData> getAllJobInstance();
    String resumeJob(String keyGroup, String keyName);
    String pauseJob(String keyGroup, String keyName);
    String triggerJob(String keyGroup, String keyName);

    List<NocCronJobProfileData> getAllJobProfile();
    String activateJob(String keyGroup, String keyName);
    String deactivateJob(String keyGroup, String keyName);
    String syncJob(String keyGroup, String keyName);
}
