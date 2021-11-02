package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.BtuConfigParamData;
import com.hkt.btu.common.facade.data.BtuCronJobInstData;
import com.hkt.btu.common.facade.data.BtuCronJobProfileData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdConfigParamFacade;
import com.hkt.btu.sd.facade.SdJobFacade;
import com.hkt.btu.sd.facade.SdSiteConfigFacade;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.data.SdSiteConfigData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/system")
public class SystemController {

    @Resource(name = "configParamFacade")
    SdConfigParamFacade sdConfigParamFacade;
    @Resource(name = "siteConfigFacade")
    SdSiteConfigFacade sdSiteConfigFacade;
    @Resource(name = "jobFacade")
    SdJobFacade sdJobFacade;
    @Resource(name = "userFacade")
    SdUserFacade userFacade;

    @GetMapping({"", "/", "/index"})
    public String index() {
        return "redirect:/system/config-param/search";
    }

    @GetMapping({"/config-param/search"})
    public String searchConfigParam() {
        return "system/configParam/searchConfigParam";
    }

    @GetMapping({"/config-param/ajax-list"})
    public ResponseEntity<?> listConfigParam() {
        List<BtuConfigParamData> dataList = sdConfigParamFacade.getAllConfigParam();

        if (dataList == null) {
            return ResponseEntity.badRequest().body("Config param list not found.");
        } else {
            return ResponseEntity.ok(dataList);
        }
    }

    @GetMapping("config-param/create")
    public String showCreateConfigParam(Model model) {
        model.addAttribute("configTypeList", sdConfigParamFacade.getConfigTypeList());
        model.addAttribute("configGroupList", sdConfigParamFacade.getConfigGroupList());
        return "system/configParam/createConfigParam";
    }

    @PostMapping("config-param/createConfigParam")
    public ResponseEntity<?> createConfigParam(String configGroup, String configKey,String configValue,String configValueType,String encrypt) throws GeneralSecurityException {
        if (StringUtils.isEmpty(configKey)) {
            return ResponseEntity.badRequest().body("Please input config key.");
        }
        if (StringUtils.isEmpty(configValue)) {
            return ResponseEntity.badRequest().body("Please input config value.");
        }
        if (!sdConfigParamFacade.checkConfigParam(configGroup, configKey, configValue, configValueType)) {
            return ResponseEntity.badRequest().body("config value not match config value type!");
        }
        if (sdConfigParamFacade.checkConfigKey(configGroup, configKey)) {
            return ResponseEntity.badRequest().body(StringUtils.join("Config Group : ",configGroup,", Config Key : ",configKey," already exists."));
        }
        if (sdConfigParamFacade.createConfigParam(configGroup,configKey,configValue,configValueType,encrypt)) {
            return ResponseEntity.ok().body("Config param create success.");
        }
        return ResponseEntity.badRequest().body("Cannot create config param.");

    }

    @GetMapping("config-param/edit")
    public String showEditConfigParam(@RequestParam String configGroup, @RequestParam String configKey, Model model) {
        model.addAttribute("configGroup", configGroup);
        model.addAttribute("configKey", configKey);
        model.addAttribute("configGroupList", sdConfigParamFacade.getConfigGroupList());
        model.addAttribute("configTypeList", sdConfigParamFacade.getConfigTypeList());
        return "system/configParam/editConfigParam";
    }

    @GetMapping("config-param/getConfigParam")
    public ResponseEntity<?> getConfigParam(String configGroup, String configKey) {
        Optional<BtuConfigParamData> configParamData = sdConfigParamFacade.getConfigParamByGroupAndKey(configGroup, configKey);
        if (configParamData.isPresent()) {
            return ResponseEntity.ok(configParamData.get());
        }
        return ResponseEntity.badRequest().body("Cannot load config param sitting.");
    }

    @GetMapping({"/site-config", "/site-config/", "/site-config/index"})
    public String siteConfig() {
        return "system/siteConfig/siteInstance";
    }

    @PostMapping("config-param/updateConfigParam")
    public ResponseEntity<?> updateConfigParam(String configGroup, String configKey,String configValue,String configValueType,String encrypt) throws GeneralSecurityException {
        if (StringUtils.isEmpty(configValue)) {
            return ResponseEntity.badRequest().body("Please input config value.");
        }
        if (!sdConfigParamFacade.checkConfigParam(configGroup, configKey, configValue, configValueType)) {
            return ResponseEntity.badRequest().body("config value not match config value type!");
        }
        if (sdConfigParamFacade.updateConfigParam(configGroup, configKey, configValue, configValueType, encrypt)) {
            return ResponseEntity.ok().body("Config param update success.");
        }
        return ResponseEntity.badRequest().body("Cannot update config param.");
    }

    @GetMapping("/site-config/ajax-get-site-instance")
    public ResponseEntity<?> ajaxGetSiteInstance() {
        SdSiteConfigData sdSiteInstanceData = sdSiteConfigFacade.getSiteInstance();
        if (sdSiteInstanceData == null) {
            return ResponseEntity.badRequest().body("Cannot load site instance.");
        } else {
            return ResponseEntity.ok(sdSiteInstanceData);
        }
    }

    @PostMapping("/site-config/reload")
    public ResponseEntity<?> reloadSiteConfig() {
        sdSiteConfigFacade.reload();
        return ResponseEntity.ok("done");
    }

    @GetMapping("/job/list")
    public String listJob() {
        return "system/job/listJob";
    }

    @GetMapping("/job/ajax-list-job-inst")
    public ResponseEntity<?> ajaxListJobInst() {
        List<BtuCronJobInstData> jobDataList = sdJobFacade.getAllJobInstance();

        if (jobDataList == null) {
            return ResponseEntity.badRequest().body("Job inst list not found.");
        } else {
            return ResponseEntity.ok(jobDataList);
        }
    }

    @PostMapping("/job/ajax-resume-job")
    public ResponseEntity<?> ajaxResumeJob(@RequestParam String keyGroup,
                                           @RequestParam String keyName) {
        String errorMsg = sdJobFacade.resumeJob(keyGroup, keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-pause-job")
    public ResponseEntity<?> ajaxPauseJob(@RequestParam String keyGroup,
                                          @RequestParam String keyName) {
        String errorMsg = sdJobFacade.pauseJob(keyGroup, keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-trigger-job")
    public ResponseEntity<?> ajaxTriggerJob(@RequestParam String keyGroup,
                                            @RequestParam String keyName) {
        String errorMsg = sdJobFacade.triggerJob(keyGroup, keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/job/ajax-list-job-profile")
    public ResponseEntity<?> ajaxListJobProfile() {
        List<BtuCronJobProfileData> jobDataList = sdJobFacade.getAllJobProfile();

        if (jobDataList == null) {
            return ResponseEntity.badRequest().body("Job profile list not found.");
        } else {
            return ResponseEntity.ok(jobDataList);
        }
    }

    @PostMapping("/job/ajax-activate-job")
    public ResponseEntity<?> ajaxActivateJob(@RequestParam String keyGroup,
                                             @RequestParam String keyName) {
        String errorMsg = sdJobFacade.activateJob(keyGroup, keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-deactivate-job")
    public ResponseEntity<?> ajaxDeactivateJob(@RequestParam String keyGroup,
                                               @RequestParam String keyName) {
        String errorMsg = sdJobFacade.deactivateJob(keyGroup, keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/job/ajax-sync-job")
    public ResponseEntity<?> ajaxSyncJob(@RequestParam String keyGroup,
                                         @RequestParam String keyName) {
        String errorMsg = sdJobFacade.syncJob(keyGroup, keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
}
