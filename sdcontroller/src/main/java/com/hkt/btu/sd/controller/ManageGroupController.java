package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.facade.SdUserGroupFacade;
import com.hkt.btu.sd.facade.data.SdUserGroupData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/admin/manage-group")
public class ManageGroupController {

    @Resource(name = "userGroupFacade")
    SdUserGroupFacade sdUserGroupFacade;

    @GetMapping("/list-user-grp")
    public String listUserGrp() {
        return "admin/manageGroup/listUserGrp";
    }

    @GetMapping("/ajax-list-user-grp")
    public ResponseEntity<?> ajaxListUserGrp() {
        List<SdUserGroupData> sdUserGroupDataList = sdUserGroupFacade.listAllUserGroup();
        return ResponseEntity.ok(sdUserGroupDataList);
    }

}
