package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketUploadFileBean;
import com.hkt.btu.sd.facade.data.SdTicketUploadFileData;

public class SdTicketUploadFileDataPopulator extends AbstractDataPopulator<SdTicketUploadFileData> {

    public void populate(SdTicketUploadFileBean source, SdTicketUploadFileData target) {
        target.setContent(source.getContent());
        target.setFileName(source.getFileName());
        target.setCreatedate(source.getCreateDate());
        target.setCreateby(source.getCreateBy());
    }
}
