package com.hkt.btu.common.facade.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Object for DataTable request on searching
 * format please follow the documentation of DataTable : https://datatables.net/
 * searching criteria should form a new Object extends from this and request through <code>POST</code> method
 * support backend paging only since 0.0.4-SNAPSHOT
 */
public class BtuGeneralSearchCriteriaData {
	public static final Integer DEFAULT_DRAW = 0;
	public static final Integer DEFAULT_START = 0;
	public static final Integer DEFAULT_LENGTH = 10;

	private Integer draw = DEFAULT_DRAW;
	private Integer start = DEFAULT_START;
	private Integer length = DEFAULT_LENGTH;
	private List<BtuGeneralSearchColumnData> columns;
	private List<BtuGeneralOrderingData> order;

	private Pageable pageable;

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public List<BtuGeneralSearchColumnData> getColumns() {
		return columns;
	}

	public void setColumns(List<BtuGeneralSearchColumnData> columns) {
		this.columns = columns;
	}

	public List<BtuGeneralOrderingData> getOrder() {
		return order;
	}

	public void setOrder(List<BtuGeneralOrderingData> order) {
		this.order = order;
	}

	public Pageable getPageable() {
		if (pageable != null)
			return pageable;
		// if encounter divided by 0 problem, allow throw exception as it is front end problem and return to controller
		int page = start/length;
		List<Sort.Order> orders = new ArrayList<>();
		if (!CollectionUtils.isEmpty(columns) && !CollectionUtils.isEmpty(order)) {
			for (BtuGeneralOrderingData ordering : order) {
				if (ordering.getColumn() >= 0 && ordering.getColumn() < columns.size()) {
					Sort.Order orderObj = new Sort.Order(Sort.Direction.fromString(ordering.getDir()), columns.get(ordering.getColumn()).getName());
					orders.add(orderObj);
				}
			}
		}
		Sort sort = Sort.by(orders);
		pageable = PageRequest.of(page, length, sort);
		return pageable;
	}
}
