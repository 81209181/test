package com.hkt.btu.common.facade.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Deprecated // replaced by BtuPageData
public class PageData<T extends DataInterface> extends PageImpl<T> implements PageDataInterface<T> {

    private String errorMsg;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageData(@JsonProperty("content") List<T> content, @JsonProperty("number") int number, @JsonProperty("size") int size,
                    @JsonProperty("totalElements") Long totalElements, @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                    @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort, @JsonProperty("first") boolean first,
                    @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public PageData(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageData(List<T> content) {
        super(content);
    }

    public PageData() {
        super(List.of());
    }

    public PageData(String errorMsg){
        super(List.of());
        this.errorMsg = errorMsg;
    }



    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
