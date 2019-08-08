function ajaxGetUser(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/manage-user/edit-user/get-user?userId="+$("#edit-user-id").val(),
        success: function (data) {
            // fill in form
            $("#edit-email").val(data.email);
            $("#edit-name").val(data.name);
            $("#edit-mobile").val(data.mobile);

            $("#edit-status").val(data.status);
            $("#edit-login-tried").val(data.loginTried);
            $("#edit-password-modify-date").val(data.passwordModifyDate);
            $("#edit-ldap-domain").val(data.ldapDomain);

            $("#edit-company-name").val(data.companyName);
            $("#edit-company-id").val(data.companyId);
            $("#edit-staff-id").val(data.staffId);

            if($("#edit-user-group-admin").val() && $.inArray("ADMIN", data.userGroupList)>=0){
                $("#edit-user-group-admin").prop('checked', true);
            }
            if($("#edit-user-group-user").val() && $.inArray("USER", data.userGroupList)>=0){
                $("#edit-user-group-user").prop('checked', true);
            }
            if($("#edit-user-group-cadmin").val() && $.inArray("C_ADMIN", data.userGroupList)>=0){
                $("#edit-user-group-cadmin").prop('checked', true);
            }
            if($("#edit-user-group-cuser").val() && $.inArray("C_USER", data.userGroupList)>=0 ){
                $("#edit-user-group-cuser").prop('checked', true);
            }

            // populateSelectOptions($("#my-user-group"), data.userGroupList);
            // $("input[name='userGroupId']:checked").each(function (){
            //    data['userGroupId'].push($(this).val());
            // });

        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }
    });
}

function ajaxUpdateUser() {
    var input = {};
    input["userId"] = $("#edit-user-id").val();
    input["name"] = $("#edit-name").val();
    input["mobile"] = $("#edit-mobile").val();
    input["staffId"] = $("#edit-staff-id").val();

    input["userGroupAdmin"] = $("#edit-user-group-admin").val() ? ($("#edit-user-group-admin").prop("checked") ? true : false) : null;
    input["userGroupUser"] = $("#edit-user-group-user").val() ? ($("#edit-user-group-user").prop("checked") ? true : false) : null;
    input["userGroupCAdmin"] = $("#edit-user-group-cadmin").val() ? ($("#edit-user-group-cadmin").prop("checked") ? true : false) : null;
    input["userGroupCUser"] = $("#edit-user-group-cuser").val() ? ($("#edit-user-group-cuser").prop("checked") ? true : false) : null;

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/admin/manage-user/edit-user",
        data: JSON.stringify(input),
        dataType: 'json',
        cache: false,
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Updated user.");
                ajaxGetUser();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg(e);
        }
    });
}

function ajaxActivateUser() {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/admin/manage-user/edit-user/activate?userId="+$("#edit-user-id").val(),
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Activated user.");
                ajaxGetUser();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (message) {
            if(message){
                showErrorMsg(message);
            }else {
                showErrorMsg("Cannot activate user.");
            }
        }
    });
}

function ajaxDeactivateUser() {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/admin/manage-user/edit-user/deactivate?userId="+$("#edit-user-id").val(),
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Deactivated user.");
                ajaxGetUser();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (message) {
            if (message) {
                showErrorMsg(message);
            } else {
                showErrorMsg("Cannot deactivate user.");
            }
        }
    });
}

$(document).ready(function() {
    // click event
    $("#btnActivateUser").on("click", function (){
        ajaxActivateUser();
    });
    $("#btnDeactivateUser").on("click", function (){
        ajaxDeactivateUser();
    });
    $("#btnUpdateUser").on("click", function (){
        ajaxUpdateUser();
    });

    // load user
    ajaxGetUser();
});