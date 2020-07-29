package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.impl.BtuApiClientServiceImpl;
import com.hkt.btu.sd.core.service.SdApiClientService;
import com.hkt.btu.sds.spring.security.constant.SdsTokenAuthorizationConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

public class SdApiClientServiceImpl extends BtuApiClientServiceImpl implements SdApiClientService {

	@Override
	protected BtuUserBean getApiUserTokenByBearerAuthorization(String header) {
		// servicedesk bearer method = basic method
		String serializeToken = StringUtils.substringAfter(header, SdsTokenAuthorizationConstant.AUTH_HEADER_PREFIX_BEARER);
		String deserializeToken = new String(Base64Utils.decodeFromString(serializeToken));
		String[] splitStrs = StringUtils.split(deserializeToken, SdsTokenAuthorizationConstant.AUTH_HEADER_SEPARATOR);
		if (splitStrs.length < 2)
			return null;
		BtuUserBean result = new BtuUserBean();
		result.setUsername(splitStrs[0]);
		result.setPassword(splitStrs[1]);
		return result;
	}
}
