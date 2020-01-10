package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.BtuCacheProfileData;
import com.hkt.btu.sd.facade.SdCacheFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("cached")
public class CachedController {

    @Resource(name = "cachedFacade")
    SdCacheFacade cachedFacade;

    @GetMapping({"","/"})
    public String forwardToCachedPage(Model model) {
        List<BtuCacheProfileData> cacheProfileDataList = cachedFacade.getCacheProfileDataList();
        model.addAttribute("cacheProfileDataList", cacheProfileDataList);
        return "system/cachedObject/cachedObjectList";
    }

    @GetMapping("info/{cacheName}")
    public String getCacheList(@PathVariable String cacheName, Model model) {
        model.addAttribute("cacheProfile", cachedFacade.getCacheProfileDataByCacheName(cacheName));
        return "system/cachedObject/cachedObjectInfo";
    }

    @GetMapping("getCacheInfo/{cacheName}")
    public ResponseEntity<?> getCacheInfo(@PathVariable String cacheName){
        try {
            return ResponseEntity.ok(List.of(cachedFacade.getCachedObjectJson(cacheName), cachedFacade.getSourceObjectJson(cacheName)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get cache fail.");
        }
    }

    @GetMapping("reloadCache/{cacheName}")
    public ResponseEntity<?> reloadCache(@PathVariable String cacheName) {
        try {
            cachedFacade.reloadCacheByCacheName(cacheName);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Reload cache fail.");
        }
    }

}
