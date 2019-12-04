var time2reload, countdown, timeOut;

$(document).ready(function () {
    prepareAjax();

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
    })

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
    })
});

function prepareAjax(){
    // add spring csrf token
    var header = $("meta[name='_csrf_header']").attr("content");
    var token =$("meta[name='_csrf']").attr("content");
    $.ajaxSetup({
        beforeSend: function (xhr) {
            if (header && token) {
                xhr.setRequestHeader(header, token);
            }
            addTimeOutCookie();
        }
    });

    // add context path
    var ctx = $("meta[name='_ctx']").attr("content");
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
    for(var i=0; i<jsonList.length; i++){
        var data = jsonList[i];
        var optionText = data.optionText==null ? data : data.optionText;
        var optionValue = data.optionValue==null ? optionText : data.optionValue;
        var isDefaultSelected = data.isDefaultSelected==null ? false : data.isDefaultSelected;
        var isSelected = data.isSelected==null ? false : data.isSelected;

        var option = new Option(optionText, optionValue, isDefaultSelected, isSelected);
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
    return minute;
}

function checkTimeout() {
    let timeOutCookie = getCookie("timeOut");
    let minute = calculateTimeDiff(timeOutCookie, new Date());
    if (minute >= 14.5) {
          $('#session-expire-warning-modal').modal('show');
    } else {
          clearTimeout(timeOut);
          setTimeout('checkTimeout()', 60000 * 14 + 30000);
    }
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name)==0) return c.substring(name.length,c.length);
    }
    return "";
}

function addTimeOutCookie() {
    let ctx = $("meta[name='_ctx']").attr("content");
    document.cookie = "timeOut=" + new Date() + ";path="+ ctx;
}