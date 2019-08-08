$(document).ready(function () {
    $("#update-pwd-form").submit(function (event) {
        event.preventDefault();
        ajaxUpdatePwd();
    });

    ajaxGetCurrentUser();
});

function ajaxUpdatePwd() {
    let input = {};
    input["oldPassword"] = $("#oldPassword").val();
    input["newPassword"] = $("#newPassword").val();
    input["newPasswordRe"] = $("#newPasswordRe").val();

    $("#btn-update-pwd").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/user/my-account/update-pwd",
        data: JSON.stringify(input),
        dataType: 'json',
        cache: false,
        success: function (data) {
            $("#update-pwd-form")[0].reset();
            $("#btn-update-pwd").prop("disabled", false);

            clearAllMsg();
            if(data.success){
                ajaxGetCurrentUser();
                showInfoMsg("Updated password.");
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            let responseError = e.responseText ? e.responseText : "Update failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
            $("#btn-update-pwd").prop("disabled", false);
        }
    });
}

function ajaxGetCurrentUser(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/user/my-account/get-current-user",
        success: function (data) {
            // fill in form
            $("#my-user-id").val(data.userId);
            $("#my-email").val(data.email);
            $("#my-name").val(data.name);
            $("#my-mobile").val(data.mobile);

            $("#my-status").val(data.status);
            $("#my-login-tried").val(data.loginTried);
            $("#my-password-modify-date").val(data.passwordModifyDate);
            $("#my-ldap-domain").val(data.ldapDomain);

            $("#my-company-id").val(data.companyId);
            $("#my-company-name").val(data.companyName);
            $("#my-staff-id").val(data.staffId);


            populateSelectOptions($("#my-user-group"), data.userGroupList);
        },
        error: function (e) {
            let responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }
    });
}