package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.BesCustomerData;
import com.hkt.btu.sd.facade.data.BesSubFaultData;
import com.hkt.btu.sd.facade.data.BesSubscriberData;

public interface BesApiFacade {

    BesCustomerData queryCustomerByCustomerCode(String CustomerCode);
    BesCustomerData queryCustomerByServiceCode(String serviceCode);
    BesCustomerData queryCustomerByBusinessRegNum(String businessRegNum);
    BesCustomerData queryCustomerByPassport(String passport);
    BesCustomerData queryCustomerByIdCard(String idCard);
    BesCustomerData queryCustomerBySchoolCert(String schoolCert);
    BesCustomerData queryCustomerByCertIncorp(String certIncorp);

    BesSubscriberData querySubscriberByServiceNumber(String serviceNumber);
    BesSubscriberData querySubscriberByCustomerCode(String customerCode);

    String getBesCustomerCode (BesCustomerData besCustomerData);

    BesSubFaultData getSubFaultBySubscriberId(String subscriberId);
}
