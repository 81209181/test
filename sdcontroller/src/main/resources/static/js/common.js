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