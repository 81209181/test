package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketUploadFileBean;
import com.hkt.btu.sd.facade.data.SdTicketUploadFileData;

public class SdTicketUploadFileDataPopulator extends AbstractDataPopulator<SdTicketUploadFileData> {

    public void populate(SdTicketUploadFileBean source, SdTicketUploadFileData target) {
        target.setFile(source.getFile());
        target.setFileName(source.getFileName());
    }
}
