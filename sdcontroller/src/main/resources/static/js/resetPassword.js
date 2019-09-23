$(document).ready(function () {
    $("#request-pwd-otp-form").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        ajaxRequestPwdOtp();
    });
});

function ajaxRequestPwdOtp() {
    $("#btn-request-pwd-otp").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/reset-password-otp?name=" + $("#name").val(),
        dataType: 'json',
        cache: false,
        success: function (data) {
            $("#btn-request-pwd-otp").prop("disabled", false);

            clearAllMsg();
            if(data.success){
                showInfoMsg("Please check your email.");
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Request failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
            $("#btn-request-pwd-otp").prop("disabled", false);
        }
    });
}


$(document).ready(function () {
    $("#reset-pwd-form").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        ajaxResetPwdWithOtp();
    });
});

function ajaxResetPwdWithOtp() {

    $("#btn-reset-pwd").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/reset-password",
        data: {
            resetOtp: $("#resetOtp").val(),
            newPassword: $("#newPassword").val(),
            newPasswordRe: $("#newPasswordRe").val()
        },
        dataType: 'json',
        cache: false,
        success: function (data) {
            $("#reset-pwd-form")[0].reset();
            $("#btn-reset-pwd").prop("disabled", false);

            clearAllMsg();
            if(data.success){
                showInfoMsg("Updated password.");
                $("#btn-reset-pwd").prop("disabled", true);

                // display only login button
                $("#reset-pwd-form").slideUp(200);
                $("#go-login-page").slideDown(200);
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Update failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
            $("#btn-reset-pwd").prop("disabled", false);
        }
    });
}