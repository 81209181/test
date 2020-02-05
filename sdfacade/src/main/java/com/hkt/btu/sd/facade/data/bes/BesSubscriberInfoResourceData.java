package com.hkt.btu.sd.facade.data.bes;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class BesSubscriberInfoResourceData implements DataInterface {


    private BesSubscriberBasicData subInfo;
    private List<BesOfferingInstDetailInfoData> primaryOfferingList;
    private List<BesOfferingInstDetailInfoData> supplementaryOfferingList;



    public BesSubscriberBasicData getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(BesSubscriberBasicData subInfo) {
        this.subInfo = subInfo;
    }

    public List<BesOfferingInstDetailInfoData> getPrimaryOfferingList() {
        return primaryOfferingList;
    }

    public void setPrimaryOfferingList(List<BesOfferingInstDetailInfoData> primaryOfferingList) {
        this.primaryOfferingList = primaryOfferingList;
    }

    public List<BesOfferingInstDetailInfoData> getSupplementaryOfferingList() {
        return supplementaryOfferingList;
    }

    public void setSupplementaryOfferingList(List<BesOfferingInstDetailInfoData> supplementaryOfferingList) {
        this.supplementaryOfferingList = supplementaryOfferingList;
    }

}
