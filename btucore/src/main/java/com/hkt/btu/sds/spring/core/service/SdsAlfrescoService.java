package com.hkt.btu.sds.spring.core.service;

import com.hkt.btu.sds.spring.core.service.bean.SdsAlfrescoFileBean;
import com.hkt.btu.sds.spring.core.service.bean.SdsAlfrescoFileProfileBean;

import java.io.File;

public interface SdsAlfrescoService {

	String login();


	SdsAlfrescoFileProfileBean uploadFile(String folderId, String fileName, String fileNodeType, boolean isMajorVersion, File uploadFile);

	SdsAlfrescoFileProfileBean updateFile(String fileId, String fileName, boolean isMajorVersion, File uploadFile);

	SdsAlfrescoFileProfileBean getFileInfo(String fileId, Double version);

	SdsAlfrescoFileBean downloadFile(String fileId, Double version);

	SdsAlfrescoFileProfileBean getOrCreateFolder(String folderId, String path);

	SdsAlfrescoFileProfileBean moveNode(String nodeId, String rename, String targetNodeId);
}
