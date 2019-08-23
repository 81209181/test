package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.RequestCreateSearchResultsData;
import org.springframework.http.ResponseEntity;

public interface SdRequestCreateFacade {
    RequestCreateSearchResultsData searchProductList(String searchKey, String searchValue);
}
