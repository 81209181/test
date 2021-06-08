package com.hkt.btu.sd.facade.data.gmb;

public class Parameter {

    String parameterName;
    String parameterValue;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public Parameter(String parameterName, String parameterValue) {
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public static Parameter of(String parameterName, String parameterValue) {
        return new Parameter(parameterName, parameterValue);
    }
}
