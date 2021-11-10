package com.hkt.btu.common.core.service.populator;


import com.hkt.btu.common.core.dao.entity.BaseEntity;
import com.hkt.btu.common.core.service.bean.BaseBean;


public interface BeanPopulator<B extends BaseBean> {

    void populate(BaseEntity source, B target);
    void populate(BaseBean source, B target);

}
