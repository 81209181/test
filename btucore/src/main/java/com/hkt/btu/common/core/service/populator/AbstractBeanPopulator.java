package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BaseEntity;
import com.hkt.btu.common.core.service.bean.BaseBean;

public abstract class AbstractBeanPopulator<B extends BaseBean> implements BeanPopulator<B> {
    @Override
    public void populate(BaseEntity source, B target) {
        target.setCreateby(source.getCreateby());
        target.setCreatedate(source.getCreatedate());
        target.setModifyby(source.getModifyby());
        target.setModifydate(source.getModifydate());
        target.setRemarks(source.getRemarks());
    }
}
