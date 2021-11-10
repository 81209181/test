package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BaseEntity;
import com.hkt.btu.common.core.service.bean.BaseBean;

public abstract class AbstractBeanPopulator<B extends BaseBean> implements BeanPopulator<B> {
    @Override
    public void populate(BaseEntity source, B target) {
        target.setCreateBy(source.getCreateBy());
        target.setCreateDate(source.getCreateDate());
        target.setModifyBy(source.getModifyBy());
        target.setModifyDate(source.getModifyDate());
        target.setRemarks(source.getRemarks());
    }

    @Override
    public void populate(BaseBean source, B target) {
        target.setCreateBy(source.getCreateBy());
        target.setCreateDate(source.getCreateDate());
        target.setModifyBy(source.getModifyBy());
        target.setModifyDate(source.getModifyDate());
        target.setRemarks(source.getRemarks());
    }
}
