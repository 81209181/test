package com.hkt.btu.noc.controller.response;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class DataTablesResponse <D extends DataInterface> {
    private Integer draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<D> data;


    private DataTablesResponse(Integer draw, Long recordsTotal, Long recordsFiltered, List<D> data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public static <D extends DataInterface> DataTablesResponse<D> of(Integer draw, Long recordsTotal, List<D> data){
        return of(draw, recordsTotal, recordsTotal, data);
    }
    public static <D extends DataInterface> DataTablesResponse<D> of(Integer draw, Long recordsTotal, Long recordsFiltered, List<D> data){
        return new DataTablesResponse<>(draw, recordsTotal, recordsFiltered, data);
    }



    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        this.data = data;
    }
}
