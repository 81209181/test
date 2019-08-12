$(document).ready(function () {
    prepareAjax();
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
        }
    });

    // add context path
    var ctx = $("meta[name='_ctx']").attr("content");
    $.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
        if (!options.crossDomain && ctx) {
            options.url = ctx + options.url;
        }
    });
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

function checkConfigValue(configValue,configValueType){
    if(configValueType == 'Boolean'){
        if(configValue!='true' && configValue!='false'){
            showErrorMsg("config value not match config value type!");
            return false;
        }
    }else if(configValueType == 'Integer'){
        if(!configValue.match(/^[1-9]\d*$/)){
            showErrorMsg("config value not match config value type!");
            return false;
        }
    }else if(configValueType == 'Double'){
        if(!configValue.match(/^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/)){
            showErrorMsg("config value not match config value type!");
            return false;
        }
    }else if(configValueType == 'LocalDateTime'){
        if(!configValue.match(/^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}/)){
            showErrorMsg("config value not match config value type!");
            return false;
        }
    }else {
//        if(!configValue.match(/^[\u4E00-\u9FA5A-Za-z0-9_]+$/)){
//            showErrorMsg("config value not match config value type!");
//            return false;
//        }
    }
    return true;
}