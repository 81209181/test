package com.hkt.btu.sd.controller.response;

import com.hkt.btu.common.facade.data.DataInterface;

public class SimpleAjaxResponse implements DataInterface {
    private boolean success;
    private String feedback;


    private SimpleAjaxResponse(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public static SimpleAjaxResponse of(){
        return of(true, null);
    }
    public static SimpleAjaxResponse of(boolean success, String feedback){
        return new SimpleAjaxResponse(success, feedback);
    }



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
