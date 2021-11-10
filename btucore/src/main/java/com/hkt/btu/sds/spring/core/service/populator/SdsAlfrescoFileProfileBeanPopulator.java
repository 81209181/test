package com.hkt.btu.sds.spring.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sds.spring.core.service.bean.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SdsAlfrescoFileProfileBeanPopulator extends AbstractBeanPopulator<SdsAlfrescoFileProfileBean> {

	private static final Logger LOG = LogManager.getLogger(SdsAlfrescoFileProfileBeanPopulator.class);

	@Resource(name = "alfrescoFileVersionInfoBeanPopulator")
	SdsAlfrescoFileVersionInfoBeanPopulator alfrescoFileVersionInfoBeanPopulator;

	public void populate(SdsAlfrescoFileInfoStandardBean source, SdsAlfrescoFileProfileBean target) {
		target.setAspects(source.getAspectNames());
		target.setFile(source.isFile());
		target.setFileId(source.getId());
		target.setFileName(source.getName());
		target.setFolder(source.isFolder());
		target.setNodeType(source.getNodeType());
		target.setParentId(source.getParentId());
		if (MapUtils.isNotEmpty(source.getProperties())) {
			Map<String, Object> properties = source.getProperties();
			String versionStr = (String) properties.get(SdsAlfrescoConstantsBean.PROPERTY_VERSION_LABEL);
			String versionType = (String) properties.get(SdsAlfrescoConstantsBean.PROPERTY_VERSION_TYPE);
			Double version = null;
			if (StringUtils.isNotEmpty(versionStr)) {
				try {
					version = Double.valueOf(versionStr);
				} catch (NumberFormatException e) {
					LOG.warn("File {} version str {} cannot be converted to double value", source.getId(), versionStr);
				}
			}
			target.setVersion(version);
			target.setVersionType(versionType);
		}
		if (source.getContent() != null) {
			target.setMimeType(source.getContent().getMimeType());
			target.setSize(source.getContent().getSizeInBytes());
		}
		if (source.getCreatedByUser() != null)
			target.setCreateBy(source.getCreatedByUser().getId());
		target.setCreateDate(source.getCreatedAt());
		if (source.getModifiedByUser() != null)
			target.setModifyBy(source.getModifiedByUser().getId());
		target.setModifyDate(source.getModifiedAt());
	}

	public void populate(List<SdsAlfrescoFileSearchListDetailBean> source, SdsAlfrescoFileProfileBean target) {
		List<SdsAlfrescoFileVersionInfoBean> versionList = new ArrayList<>();
		source.forEach(detailBean -> {
			if (detailBean.getEntry() != null) {
				SdsAlfrescoFileVersionInfoBean bean = new SdsAlfrescoFileVersionInfoBean();
				alfrescoFileVersionInfoBeanPopulator.populate(detailBean.getEntry(), bean);
				versionList.add(bean);
			}
		});
		target.setVersions(versionList);
	}
}
