$(document).ready(function() {
    // load content
    ajaxGetSiteInstance();

    // click event
    $("#btnReloadSiteConfig").on("click", function (){
        ajaxReload();
    });
});

function ajaxGetSiteInstance(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/system/site-config/ajax-get-site-instance",
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();

            // fill content
            $("#server-address").val(data.serverAddress);
            $("#server-hostname").val(data.serverHostname);
            $("#server-type").val(data.serverType);
            $("#cronjob-hostname").val(data.cronjobHostname);

            $("#app-name").val(data.appName);
            $("#app-http-url").val(data.httpUrl);
            $("#app-https-url").val(data.httpsUrl);

            $("#given-domain").val(data.givenDomain);
            $("#app-context-path").val(data.contextPath);

            $("#login-tried-limit").val(data.loginTriedLimit);
            $("#password-lifespan").val(data.passwordLifespanInDay);
            $("#password-reset-otp-lifespan").val(data.passwordResetOtpLifespanInMin);

            $("#mail-host").val(data.mailHost);
            $("#mail-port").val(data.mailPort);
            $("#mail-username").val(data.mailUsername);
        },
        error: function (e) {
            showErrorMsg("Cannot reload site instance.");
        }
    });
}

function ajaxReload() {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/site-config/reload",
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            showInfoMsg("Reloaded site config.");
        },
        error: function (e) {
            showErrorMsg("Cannot reload site config.");
        }
    });
}


