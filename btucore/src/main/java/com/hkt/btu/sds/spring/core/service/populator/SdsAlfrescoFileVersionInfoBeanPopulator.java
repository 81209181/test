package com.hkt.btu.sds.spring.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sds.spring.core.service.bean.SdsAlfrescoFileInfoStandardBean;
import com.hkt.btu.sds.spring.core.service.bean.SdsAlfrescoFileVersionInfoBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SdsAlfrescoFileVersionInfoBeanPopulator extends AbstractBeanPopulator<SdsAlfrescoFileVersionInfoBean> {
	private static final Logger LOG = LogManager.getLogger(SdsAlfrescoFileVersionInfoBeanPopulator.class);

	public void populate(SdsAlfrescoFileInfoStandardBean source, SdsAlfrescoFileVersionInfoBean target) {
		target.setFileName(source.getName());
		target.setMimeType(source.getNodeType());
		if (source.getContent() != null) {
			target.setMimeType(source.getContent().getMimeType());
			target.setSize(source.getContent().getSizeInBytes());
		}
		Double version = null;
		try {
			version = Double.valueOf(source.getId());
		} catch (NumberFormatException e) {
			LOG.warn("Cannot get version from '{}'", source.getId());
		}
		target.setVersion(version == null? 0d : version);
		if (source.getModifiedByUser() != null)
			target.setModifyBy(source.getModifiedByUser().getId());
		target.setModifyDate(source.getModifiedAt());
	}
}
