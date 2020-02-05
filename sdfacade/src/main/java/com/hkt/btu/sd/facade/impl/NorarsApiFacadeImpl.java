package com.hkt.btu.sd.facade.impl;

import com.google.gson.Gson;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.core.exception.ApiException;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdApiProfileBean;
import com.hkt.btu.sd.facade.NorarsApiFacade;
import com.hkt.btu.sd.facade.SdAuditTrailFacade;
import com.hkt.btu.sd.facade.data.SdDnGroupData;
import com.hkt.btu.sd.facade.data.SdDnPlanData;
import com.hkt.btu.sd.facade.data.ServiceAddressData;
import com.hkt.btu.sd.facade.data.nora.*;
import com.hkt.btu.sd.facade.populator.SdDnGroupDataPopulator;
import com.hkt.btu.sd.facade.populator.SdDnPlanDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.*;

public class NorarsApiFacadeImpl extends AbstractRestfulApiFacade implements NorarsApiFacade {
    private static final Logger LOG = LogManager.getLogger(NorarsApiFacadeImpl.class);

    private final static String STATUS = "W";

    @Resource(name = "apiService")
    SdApiService apiService;
    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "auditTrailFacade")
    SdAuditTrailFacade auditTrailFacade;

    @Resource(name = "dnGroupDataPopulator")
    SdDnGroupDataPopulator dnGroupDataPopulator;
    @Resource(name = "dnPlanDataPopulator")
    SdDnPlanDataPopulator dnPlanDataPopulator;


    @Override
    protected SdApiProfileBean getTargetApiProfile() {
        return apiService.getNorarsApiProfileBean();
    }

    @Override
    public String getBsnByDn(String dn) {
        String apiPath = "/norars/api/v1/onecomm/bsn/" + dn;
        return getData(apiPath, null);
    }

    @Override
    public NoraBroadbandInfoData getOfferDetailListByBsn(String bsn) {
        if (StringUtils.isEmpty(bsn)) {
            throw new InvalidInputException("Empty BSN.");
        }

        String apiPath = "/norars/api/v1/onecomm/bsn/" + bsn + "/detail";
        return getData(apiPath, NoraBroadbandInfoData.class, null);
    }

    @Override
    public String getInventory(String bsn) {
        if (StringUtils.isEmpty(bsn)) {
            throw new InvalidInputException("Empty BSN.");
        }

        String apiPath = "nora/wfm/Profile.action";

        Map<String, String> queryParam = new HashMap<>(2);
        queryParam.put("bsn", bsn);
        queryParam.put("status", STATUS);

        return Optional.ofNullable(getData(apiPath, queryParam)).orElse("<h1>Test</h1>");
    }

    @Override
    public NoraDnGroupData getRelatedOfferInfoListByBsn(String bsn) {
        if (StringUtils.isEmpty(bsn)) {
            throw new InvalidInputException("Empty BSN.");
        }

        String apiPath = "/norars/api/v1/ec/groupids/sr/" + bsn;
        String responseString = getData(apiPath, null);
        if (StringUtils.isEmpty(responseString)) {
            return null;
        } else {
            return new Gson().fromJson(responseString, NoraDnGroupData.class);
        }
    }

    @Override
    public SdDnGroupData getDnGroupData(String bsn) {
        NoraDnGroupData noraDnGroupData = getRelatedOfferInfoListByBsn(bsn);
        if(noraDnGroupData==null){
            return null;
        }

        SdDnGroupData dnGroupData = new SdDnGroupData();
        dnGroupDataPopulator.populate(noraDnGroupData, dnGroupData);

        List<NoraDnPlanData> noraDnPlanDataList = noraDnGroupData.getUserPlanList();
        if(CollectionUtils.isNotEmpty(noraDnPlanDataList)){
            List<SdDnPlanData> dnPlanDataList = new ArrayList<>();
            dnGroupData.setUserPlanList(dnPlanDataList);

            for(NoraDnPlanData noraDnPlanData : noraDnPlanDataList){
                SdDnPlanData dnPlanData = new SdDnPlanData();
                dnPlanDataPopulator.populate(noraDnPlanData, dnPlanData);
                dnPlanDataList.add(dnPlanData);
            }
        }

        return dnGroupData;
    }

    @Override
    public NoraAccountData getNgn3OneDayAdminAccount(String bsn) throws InvalidInputException {
        String requestorId = userService.getCurrentUserBean().getUserId();

        // get company id
        LOG.info("Getting company id...");
        NoraBroadbandInfoData noraBroadbandInfoData = getOfferDetailListByBsn(bsn);
        String companyId = noraBroadbandInfoData==null ? null : noraBroadbandInfoData.getCompanyId();
        if( StringUtils.isEmpty(companyId) ){
            String errorMsg = String.format("Cannot find company Id. (bsn: %s)", bsn);
            throw new InvalidInputException(errorMsg);
        }

        // get one-day admin account
        LOG.info("Getting one-day admin account...");
        String apiPath = "/norars/api/v1/osb/accounts/" + companyId + "/" + requestorId;
        NoraAccountData accountData = getData(apiPath, NoraAccountData.class, null);
        if(accountData!=null && StringUtils.isNotEmpty(accountData.getPassword())){
            auditTrailFacade.insertGetNgn3OneDayAdmin(bsn, companyId); // add audit
        }

        return accountData;
    }

    @Override
    public String resetNgn3Account(String dn) throws ApiException {
        LOG.info("Resetting NGN3 Account...");
        String apiPath = "/norars/api/v1/osb/complexpwd/" + dn;
        String complexPwd = putData(apiPath, null, null);
        if(StringUtils.isNotEmpty(complexPwd)){
            auditTrailFacade.insertResetNgn3Account(dn); // add audit
        }

        return complexPwd;
    }

    @Override
    public ServiceAddressData getServiceAddressByBsn(String bsn) {
        String apiPath = "/norars/api/v1/onecomm/address/" + bsn;
        NoraAddressInfoData noraAddressInfoData = getData(apiPath, NoraAddressInfoData.class, null);
        if (noraAddressInfoData != null) {
            ServiceAddressData data = new ServiceAddressData();
            data.setServiceAddress(getAddressString(noraAddressInfoData));
            data.setGridId(noraAddressInfoData.getAddr14());
            data.setExchangeBuildingId(noraAddressInfoData.getAddr15());
            return data;
        }
        return null;
    }

    @Override
    public String getL1InfoByBsn(String bsn) {
        String apiPath = "/norars/api/v1/onecomm/pid/" + bsn;
        NoraPidInfoData noraPidInfoData = getData(apiPath, NoraPidInfoData.class, null);
        if (noraPidInfoData != null) {
            return noraPidInfoData.getPid() + "/" + noraPidInfoData.getStb() + "/" + noraPidInfoData.getDescription();
        }
        return null;
    }

    private String getAddressString(NoraAddressInfoData noraAddressInfoData) {
        String result = appendStringWithDelimiter("", noraAddressInfoData.getAddr1(), ", ", "FLAT %s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr2(), ", ", "LOT %s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr3(), ", ", "%s/F");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr4(), ", ", "BLOCK %s");

        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr5(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr6(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr7(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr8(), ", ", "%s");

        // Street number and name
        String street = appendStringWithDelimiter("", noraAddressInfoData.getAddr9(), "", "%s");
        street = appendStringWithDelimiter(street, noraAddressInfoData.getAddr10(), " ", "- %s");
        street = appendStringWithDelimiter(street, noraAddressInfoData.getAddr11(), " ", "%s");
        result = appendStringWithDelimiter(result, street, ", ", "%s");

        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr12(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr13(), ", ", "%s");

        return result;
    }

    private String appendStringWithDelimiter(String result, String addr, String str1, String str2) {
        if (StringUtils.isEmpty(result)) {
            if (StringUtils.isNotEmpty(addr)) {
                result = String.format(result + str2, addr);
            }
        } else {
            if (StringUtils.isNotEmpty(addr)) {
                result = String.format(result + str1 + str2, addr);
            }
        }
        return result;
    }
}
