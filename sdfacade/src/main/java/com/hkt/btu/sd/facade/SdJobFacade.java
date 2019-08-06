package com.hkt.btu.sd.facade;


import com.hkt.btu.sd.facade.data.SdCronJobInstData;
import com.hkt.btu.sd.facade.data.SdCronJobProfileData;

import java.util.List;

public interface SdJobFacade {
    List<SdCronJobInstData> getAllJobInstance();
    String resumeJob(String keyGroup, String keyName);
    String pauseJob(String keyGroup, String keyName);
    String triggerJob(String keyGroup, String keyName);

    List<SdCronJobProfileData> getAllJobProfile();
    String activateJob(String keyGroup, String keyName);
    String deactivateJob(String keyGroup, String keyName);
    String syncJob(String keyGroup, String keyName);
}
