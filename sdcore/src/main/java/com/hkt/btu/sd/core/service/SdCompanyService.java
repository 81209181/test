package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.exception.CompanyNotFoundException;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SdCompanyService {
    SdCompanyBean getCompanyById(Integer companyId) throws CompanyNotFoundException, InvalidInputException;
    List<SdCompanyBean> getAllCompany();

    Page<SdCompanyBean> searchCompany(Pageable pageable, Integer companyId, String name);


    void updateCompany(Integer companyId, String name, String remarks)
            throws CompanyNotFoundException, DuplicateKeyException, UserNotFoundException;

    Integer createCompany(String name, String remarks)
            throws DuplicateKeyException, UserNotFoundException;

}
