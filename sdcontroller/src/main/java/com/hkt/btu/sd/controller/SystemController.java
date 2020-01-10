package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @Resource(name = "publicHolidayFacade")
    SdPublicHolidayFacade sdPublicHolidayFacade;
    @Resource(name = "userFacade")
    SdUserFacade userFacade;
    @Resource(name = "apiClientFacade")
    SdApiClientFacade sdApiClientFacade;

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
        List<SdConfigParamData> dataList = sdConfigParamFacade.getAllConfigParam();

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
        Optional<SdConfigParamData> configParamData = sdConfigParamFacade.getConfigParamByGroupAndKey(configGroup, configKey);
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
        List<SdCronJobInstData> jobDataList = sdJobFacade.getAllJobInstance();

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
        List<SdCronJobProfileData> jobDataList = sdJobFacade.getAllJobProfile();

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

    @GetMapping("/public-holiday")
    public String ListPublicHoliday() {
        return "system/publicHoliday/searchPublicHoliday";
    }

    @GetMapping("/public-holiday/ajax-page-public-holiday")
    public ResponseEntity<?> ajaxListPublicHoliday(@RequestParam(defaultValue = "0") int draw,
                                                   @RequestParam(defaultValue = "0") int start,
                                                   @RequestParam(defaultValue = "10") int length,
                                                   @RequestParam(required = false) String year) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdPublicHolidayData> pageData = sdPublicHolidayFacade.getPublicHolidayList(pageable, year);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @ResponseBody
    @GetMapping("/public-holiday/ajax-list-public-holiday")
    public List<SdPublicHolidayData> ajaxListPublicHoliday() {
        List<SdPublicHolidayData> allPublicHolidayList = sdPublicHolidayFacade.getAllPublicHolidayList();
        return allPublicHolidayList;
    }

    @PostMapping("/public-holiday/ajax-add-public-holiday")
    public ResponseEntity<?> ajaxAddPublicHoliday(@RequestBody List<SdPublicHolidayData> data) {
        boolean result = sdPublicHolidayFacade.addPublicHoliday(data);
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(true, "insert successful."));
        } else {
            return ResponseEntity.badRequest().body("Cannot import duplicate/invalid date.");
        }
    }

    @PostMapping("/public-holiday/delete-public-holiday")
    public ResponseEntity<?> deletePublicHoliday(@RequestParam String publicHoliday) {
        boolean result = sdPublicHolidayFacade.deletePublicHoliday(publicHoliday);
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body("delete failed.");
        }
    }

    @PostMapping("/public-holiday/create-public-holiday")
    public ResponseEntity<?> createPublicHoliday(@RequestParam String publicHoliday,
                                                 @RequestParam String description) {
        try {
            sdPublicHolidayFacade.createPublicHoliday(publicHoliday, description);
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } catch (Exception e) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, e.getMessage()));
        }
    }

    @GetMapping("/manage-api")
    public String ListApiUser(){
        return "system/manageApi/listApiUser";
    }

    @GetMapping("/manage-api/ajax-list-api-user")
    public ResponseEntity<?> ajaxListApiUser(){
        List<SdUserData> dataList = userFacade.getApiUser();

        if (dataList == null) {
            return ResponseEntity.badRequest().body("user list not found.");
        } else {
            return ResponseEntity.ok(dataList);
        }
    }

    @PostMapping("/manage-api/getAuthorization")
    public ResponseEntity<?> getAuthorization(String apiName) {
        String authPlainText = String.format("%s:%s", apiName, sdApiClientFacade.getApiClientBean(apiName));

        if (StringUtils.isEmpty(authPlainText)) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, "Get Authorization failed."));
        }

        String encodedAuth = Base64.getEncoder().encodeToString(authPlainText.getBytes());
        String apiKey = String.format("Bearer %s", encodedAuth);
        return ResponseEntity.ok(SimpleAjaxResponse.of(true, apiKey));
    }

    @PostMapping("/manage-api/regenerateKey")
    public ResponseEntity<?> regenerateKey(String apiName) {
        try {
            sdApiClientFacade.reloadCached(apiName);
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Re-generate failed.");
        }
    }
}
