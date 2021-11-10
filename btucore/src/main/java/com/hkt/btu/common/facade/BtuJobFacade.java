package com.hkt.btu.common.facade;

import com.hkt.btu.common.facade.data.BtuCronJobInstData;
import com.hkt.btu.common.facade.data.BtuCronJobProfileData;

import java.util.List;

public interface BtuJobFacade {
    // job instance (running on server)
    List<BtuCronJobInstData> getAllJobInstance();
    String resumeJob(String keyGroup, String keyName);
    String pauseJob(String keyGroup, String keyName);
    String triggerJob(String keyGroup, String keyName);

    // job profile (saved on db)
    List<BtuCronJobProfileData> getAllJobProfile();
    String activateJob(String keyGroup, String keyName);
    String deactivateJob(String keyGroup, String keyName);
    String syncJob(String keyGroup, String keyName);
}
