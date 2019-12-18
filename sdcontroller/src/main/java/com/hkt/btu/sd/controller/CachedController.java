package com.hkt.btu.sd.controller;

import com.hkt.btu.common.core.service.bean.BtuCacheInfoBean;
import com.hkt.btu.sd.facade.SdCachedFacade;
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
    SdCachedFacade cachedFacade;

    @GetMapping({"","/"})
    public String forwardToCachedPage(Model model) {
        List<BtuCacheInfoBean> cacheInfoList = cachedFacade.getCacheInfoList();
        model.addAttribute("cacheInfoList", cacheInfoList);
        return "system/cachedObject/cachedObjectList";
    }

    @GetMapping("info/{cacheId}")
    public String getCacheList(@PathVariable String cacheId,Model model) {
        model.addAttribute("cacheId", cacheId);
        return "system/cachedObject/cachedObjectInfo";
    }

    @GetMapping("getCacheInfo/{cacheId}")
    public ResponseEntity<?> getCacheInfo(@PathVariable String cacheId){
        try {
            return ResponseEntity.ok(cachedFacade.getCacheInfo(cacheId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get cache fail.");
        }
    }

    @GetMapping("reloadCache/{cacheId}")
    public ResponseEntity<?> reloadCache(@PathVariable String cacheId) {
        try {
            cachedFacade.reloadCache(cacheId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Reload cache fail.");
        }
    }

    @GetMapping("getReloadedCacheInfo/{cacheId}")
    public ResponseEntity<?> getReloadedCacheInfo(@PathVariable String cacheId) {
        try {
            cachedFacade.reloadCache(cacheId);
            return ResponseEntity.ok(cachedFacade.getCacheInfo(cacheId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Reload cache fail.");
        }
    }

}
