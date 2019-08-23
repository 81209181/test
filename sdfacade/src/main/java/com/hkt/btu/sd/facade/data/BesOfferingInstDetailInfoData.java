package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class BesOfferingInstDetailInfoData implements DataInterface {

    private BesOfferingInstData offeringBasic;
    private List<BesOfferingInstAttrData> offeringPropList;
    private List<BesContactData> contractList;



    public BesOfferingInstData getOfferingBasic() {
        return offeringBasic;
    }

    public void setOfferingBasic(BesOfferingInstData offeringBasic) {
        this.offeringBasic = offeringBasic;
    }

    public List<BesOfferingInstAttrData> getOfferingPropList() {
        return offeringPropList;
    }

    public void setOfferingPropList(List<BesOfferingInstAttrData> offeringPropList) {
        this.offeringPropList = offeringPropList;
    }

    public List<BesContactData> getContractList() {
        return contractList;
    }

    public void setContractList(List<BesContactData> contractList) {
        this.contractList = contractList;
    }


}
