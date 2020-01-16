let time2reload, countdown, timeOut, currentURL, isLoginPage;

$(document).ready(function () {
    prepareAjax();

    getCurrentURL();

    addTimeOutCookie();

    $('#session-expire-warning-modal').find('button').on('click',function(){
        $.get('/public/keepSession',function(res){
            if(res.success){
                clearTimeout(time2reload);
                clearInterval(countdown);
                $('#session-expire-warning-modal').modal('hide');
                $('mark').text(30);
            }
        });
    });

    checkTimeout();

    $('#session-expire-warning-modal').on('show.bs.modal',function(){
        time2reload = setTimeout(function(){
            setTimeout(function () {
                location.reload();
            }, 5000)
        },30000);
        countdown = setInterval(function(){
                if ($('mark').text() > 0) {
                    $('mark').text($('mark').text() - 1);
                }
        },1000);
    });
});

function getCurrentURL() {
    currentURL = window.location.href;

    if (currentURL.endsWith("/servicedesk/")||currentURL.includes("/servicedesk/login")){
        isLoginPage = true;
        console.log("it's login page")
    }
    else {
        isLoginPage = false;
        console.log("it's not login page");
    }
}

function prepareAjax(){
    // add spring csrf token
    let header = $("meta[name='_csrf_header']").attr("content");
    let token =$("meta[name='_csrf']").attr("content");
    $.ajaxSetup({
        beforeSend: function (xhr) {
            if (header && token) {
                xhr.setRequestHeader(header, token);
            }
            addTimeOutCookie();
        }
    });

    // add context path
    let ctx = $("meta[name='_ctx']").attr("content");
    $.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
        if (!options.crossDomain && ctx) {
            options.url = ctx + options.url;
        }
    });

    $(document).ajaxComplete(function () {
        checkTimeout();
    })
}

function populateSelectOptions(select, jsonList){
    if(!select.is("select")){
        console.log("Invalid select element. Cannot populate select options. ");
        return;
    } else if(! jsonList.length>0) {
        console.log("Invalid data. Cannot populate select options. ");
    }

    // empty select options
    select.empty();

    // add data
    for(let i=0; i<jsonList.length; i++){
        let data = jsonList[i];
        let optionText = data.optionText==null ? data : data.optionText;
        let optionValue = data.optionValue==null ? optionText : data.optionValue;
        let isDefaultSelected = data.isDefaultSelected==null ? false : data.isDefaultSelected;
        let isSelected = data.isSelected==null ? false : data.isSelected;

        let option = new Option(optionText, optionValue, isDefaultSelected, isSelected);
        select.append(option);
    }
}

function safeParseJson(input){
    if(typeof input == "object"){
        return input;
    }

    try{
        return JSON.parse(input);
    } catch (e) {
        // console.log(e);
        return null;
    }
}

function calculateTimeDiff (oldTime, newTime) {
    oldTime = new Date(oldTime);
    newTime = new Date(newTime);
    let time = newTime.getTime() - oldTime.getTime();
    let minute = time % (24 * 3600 * 1000) % (3600 * 1000) / (60 * 1000);
    let timeOutMillisecond = (60000 * 14 + 30900) - time;
    return {minute: minute, timeOutMillisecond: timeOutMillisecond};
}

function checkTimeout() {
    if (!isLoginPage) {
        let timeOutCookie = getCookie("timeOut");
        let {minute, timeOutMillisecond} = calculateTimeDiff(timeOutCookie, new Date());
        if (minute >= 14.5) {
            $('#session-expire-warning-modal').modal('show');
        } else {
            clearTimeout(timeOut);
            timeOut = setTimeout('checkTimeout()', timeOutMillisecond);
        }
    }
}

function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for(let i=0; i<ca.length; i++) {
        let c = ca[i].trim();
        if (c.indexOf(name)==0) return c.substring(name.length,c.length);
    }
    return "";
}

function addTimeOutCookie() {
    let ctx = $("meta[name='_ctx']").attr("content");
    document.cookie = "timeOut=" + new Date() + ";path="+ ctx;
}