package com.hkt.btu.common.facade;

import com.hkt.btu.common.facade.data.BtuCronJobInstData;
import com.hkt.btu.common.facade.data.BtuReportFileData;
import com.hkt.btu.common.facade.data.BtuReportProfileData;
import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import org.springframework.core.io.Resource;

import java.util.List;

public interface BtuReportFacade {
    // job instance (running on server)
    List<BtuCronJobInstData> getAllReportJobInstance();

    String resumeReport(String reportId);
    String pauseReport(String reportId);
    String triggerReport(String reportId);


    // job profile (saved on db)
    List<BtuReportProfileData> getAllReportProfiles();
    BtuReportProfileData getReportProfileById(String reportId);

    BtuSimpleResponseData createReport(BtuReportProfileData data);
    BtuSimpleResponseData deleteReportProfile(String reportId);
    BtuSimpleResponseData updateReportProfile(BtuReportProfileData data);

    String activateReportProfile(String reportId);
    String deactivateReportProfile(String reportId);
    String syncReport(String reportId);

    List<BtuReportFileData> getFileList(String reportId);
    Resource downloadReportFile(String reportId, String reportFilename);

}
