package com.hkt.btu.sd.facade.data.wfm;

import com.hkt.btu.common.facade.data.DataInterface;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class WfmMakeApptData implements DataInterface {

    @Min(1)
    private Integer ticketMasId;
    @Min(1)
    private Integer ticketDetId;
    @NotEmpty
    private String symptomCode;

    public Integer getTicketMasId() {
        return ticketMasId;
    }

    public void setTicketMasId(Integer ticketMasId) {
        this.ticketMasId = ticketMasId;
    }

    public Integer getTicketDetId() {
        return ticketDetId;
    }

    public void setTicketDetId(Integer ticketDetId) {
        this.ticketDetId = ticketDetId;
    }

    public String getSymptomCode() {
        return symptomCode;
    }

    public void setSymptomCode(String symptomCode) {
        this.symptomCode = symptomCode;
    }
}
