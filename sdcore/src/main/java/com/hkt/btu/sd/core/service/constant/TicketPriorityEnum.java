package com.hkt.btu.sd.core.service.constant;

public enum TicketPriorityEnum {
    General(1, "General"),
    Minor(2, "Minor"),
    Major(3, "Major"),
    Urgent(4, "Urgent");

    TicketPriorityEnum(Integer priorityCode, String priorityDesc){
        this.priorityCode = priorityCode;
        this.priorityDesc = priorityDesc;
    }

    public static TicketPriorityEnum getEnum(Integer priorityCode) {
        for(TicketPriorityEnum ticketPriorityEnum : values()){
            if(priorityCode == ticketPriorityEnum.priorityCode){
                return ticketPriorityEnum;
            }
        }
        return null;
    }

    private Integer priorityCode;
    private String priorityDesc;

    public Integer getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(Integer priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getPriorityDesc() {
        return priorityDesc;
    }

    public void setPriorityDesc(String priorityDesc) {
        this.priorityDesc = priorityDesc;
    }
}
