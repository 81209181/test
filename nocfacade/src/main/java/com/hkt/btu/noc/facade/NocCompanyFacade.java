package com.hkt.btu.noc.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.facade.data.CreateResultData;
import com.hkt.btu.noc.facade.data.NocCompanyData;
import org.springframework.data.domain.Pageable;

public interface NocCompanyFacade {

    NocCompanyData getCompanyId(Integer companyId);
    PageData<NocCompanyData> searchCompany(Pageable pageable, Integer companyId, String name);

    CreateResultData createCompany(NocCompanyData nocCompanyData);

    String editCompany(NocCompanyData nocCompanyData);
}
