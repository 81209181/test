package com.hkt.btu.sds.spring.factory;

import com.hkt.btu.common.core.service.BtuCacheService;
import org.springframework.beans.factory.SmartInitializingSingleton;

import javax.annotation.Resource;

public class SdsSmartInitializingSingletonFactory implements SmartInitializingSingleton {

	@Resource(name = "cacheService")
	private BtuCacheService cacheService;
	
	@Override
	public void afterSingletonsInstantiated() {
		cacheService.initCacheObjectIntoMap();
	}
}
