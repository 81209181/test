package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdSymptomData implements DataInterface {

    private String symptomCode;
    private String symptomDescription;
    private String symptomGroupCode;
    private String symptomGroupName;

    public String getSymptomCode() {
        return symptomCode;
    }

    public void setSymptomCode(String symptomCode) {
        this.symptomCode = symptomCode;
    }

    public String getSymptomDescription() {
        return symptomDescription;
    }

    public void setSymptomDescription(String symptomDescription) {
        this.symptomDescription = symptomDescription;
    }

    public String getSymptomGroupCode() {
        return symptomGroupCode;
    }

    public void setSymptomGroupCode(String symptomGroupCode) {
        this.symptomGroupCode = symptomGroupCode;
    }

    public String getSymptomGroupName() {
        return symptomGroupName;
    }

    public void setSymptomGroupName(String symptomGroupName) {
        this.symptomGroupName = symptomGroupName;
    }
}
