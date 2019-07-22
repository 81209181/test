package com.hkt.btu.noc.controller.advice;

import com.hkt.btu.noc.facade.NocSiteConfigFacade;
import com.hkt.btu.noc.facade.data.NocSiteConfigData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;

@ControllerAdvice(basePackages="com.hkt.btu.noc.controller")
public class BasicCommonAttribute {
	
	private static final String PROJECT_BUILDTIME = "projectBuildtime";
	private static final String SITE_CONFIG_APP_NAME = "siteConfigAppName";

	@Value("${project.buildtime}")
	public String buildTime = "N/A";

	@Resource(name = "siteConfigFacade")
    NocSiteConfigFacade nocSiteConfigFacade;

	@ModelAttribute
    public void populateVersion(Model model) {
        model.addAttribute(PROJECT_BUILDTIME, buildTime);
    }

    @ModelAttribute
	public void populateSiteConfigInfo(Model model){
		NocSiteConfigData data = nocSiteConfigFacade.getSiteInstance();
		model.addAttribute(SITE_CONFIG_APP_NAME, data.getAppName());
	}
}
