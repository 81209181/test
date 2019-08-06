package com.hkt.btu.sd.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.CreateResultData;
import com.hkt.btu.sd.facade.data.SdCompanyData;
import org.springframework.data.domain.Pageable;

public interface SdCompanyFacade {

    SdCompanyData getCompanyId(Integer companyId);
    PageData<SdCompanyData> searchCompany(Pageable pageable, Integer companyId, String name);

    CreateResultData createCompany(SdCompanyData sdCompanyData);

    String editCompany(SdCompanyData sdCompanyData);
}
