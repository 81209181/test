package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketUploadFileEntity;
import com.hkt.btu.sd.core.service.bean.SdTicketUploadFileBean;

public class SdTicketUploadFileBeanPopulator extends AbstractBeanPopulator<SdTicketUploadFileBean> {

    public void populate(SdTicketUploadFileEntity source, SdTicketUploadFileBean target) {
        target.setFile(source.getFile());
        target.setFileName(source.getFileName());
    }
}
