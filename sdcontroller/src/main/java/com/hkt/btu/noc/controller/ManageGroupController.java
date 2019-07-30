package com.hkt.btu.noc.controller;


import com.hkt.btu.noc.facade.NocUserGroupFacade;
import com.hkt.btu.noc.facade.data.NocUserGroupData;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    NocUserGroupFacade nocUserGroupFacade;

    @GetMapping("/list-user-grp")
    public String listUserGrp() {
        return "admin/manageGroup/listUserGrp";
    }

    @GetMapping("/ajax-list-user-grp")
    public ResponseEntity<?> ajaxListUserGrp() {
        List<NocUserGroupData> nocUserGroupDataList = nocUserGroupFacade.listAllUserGroup();
        return ResponseEntity.ok(nocUserGroupDataList);
    }

}
