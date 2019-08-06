package com.hkt.btu.sd.controller.advice;

import com.hkt.btu.sd.facade.SdSiteConfigFacade;
import com.hkt.btu.sd.facade.data.SdSiteConfigData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;

@ControllerAdvice(basePackages="com.hkt.btu.sd.controller")
public class BasicCommonAttribute {
	
	private static final String PROJECT_BUILDTIME = "projectBuildtime";
	private static final String SITE_CONFIG_APP_NAME = "siteConfigAppName";

	@Value("${project.buildtime}")
	public String buildTime = "N/A";

	@Resource(name = "siteConfigFacade")
    SdSiteConfigFacade sdSiteConfigFacade;

	@ModelAttribute
    public void populateVersion(Model model) {
        model.addAttribute(PROJECT_BUILDTIME, buildTime);
    }

    @ModelAttribute
	public void populateSiteConfigInfo(Model model){
		SdSiteConfigData data = sdSiteConfigFacade.getSiteInstance();
		model.addAttribute(SITE_CONFIG_APP_NAME, data.getAppName());
	}
}
