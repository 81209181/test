package com.hkt.btu.noc.core.service;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.noc.core.exception.CompanyNotFoundException;
import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NocCompanyService {
    NocCompanyBean getCompanyById(Integer companyId) throws CompanyNotFoundException, InvalidInputException;
    List<NocCompanyBean> getAllCompany();

    Page<NocCompanyBean> searchCompany(Pageable pageable, Integer companyId, String name);


    void updateCompany(Integer companyId, String name, String remarks)
            throws CompanyNotFoundException, DuplicateKeyException, UserNotFoundException;

    Integer createCompany(String name, String remarks)
            throws DuplicateKeyException, UserNotFoundException;

}
