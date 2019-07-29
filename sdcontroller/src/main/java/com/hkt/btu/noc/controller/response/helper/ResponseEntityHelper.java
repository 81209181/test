package com.hkt.btu.noc.controller.response.helper;

import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.controller.response.DataTablesResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;

public class ResponseEntityHelper {

    public static <D extends DataInterface> ResponseEntity buildDataTablesResponse(int draw, PageData<D> pageData){
        if ( pageData == null ) {
            return ResponseEntity.badRequest().body("Null page data.");
        } else if ( ! StringUtils.isEmpty(pageData.getErrorMsg()) ) {
            return ResponseEntity.badRequest().body(pageData.getErrorMsg());
        } else {
            // build response with datatable.js format
            DataTablesResponse dataTablesResponse = DataTablesResponse.of(
                    draw, pageData.getTotalElements(), pageData.getContent() );
            return ResponseEntity.ok(dataTablesResponse);
        }
    }
}
