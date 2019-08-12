$().ready(function(){
    let configGroup =$('#configGroup').val(),
        configKey = $('#configKey').val();
    $.get('/system/config-param/getConfigParam',{configGroup:configGroup,configKey:configKey},function(r){
        $.each(r,function(key,value){
            $('#'+key).val(value);
        })
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })

    $('#configValueType').change(function(){
        let selected = $(this).children('option:selected').val();
        $('#configValue').val('');
        if(selected =='LocalDateTime'){
            $('#configValue').attr('type','datetime-local');
            $('#configValue').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}');
            $('#configValue').attr('placeholder','1900-01-01T01:00');
        }else{
            $('#configValue').attr('type','text');
            $('#configValue').removeAttr('pattern');
            $('#configValue').removeAttr('placeholder');
        }
    })

    $('#btnUpdateConfigParam').on("click",function(){
        let configValue =$('#configValue').val().trim();
        let configValueType =$('#configValueType').val().trim();
//        console.log(configValue);
//        console.log(configValueType);
        clearAllMsg();
        if(configValue.length < 1){
            showErrorMsg("Please input Config Value.");
            return;
        }
        if(!checkConfigValue(configValue,configValueType)){
            return;
        }
        $.post('/system/config-param/updateConfigParam',{
                configGroup:configGroup,
                configKey:configKey,
                configValue:configValue,
                configValueType:configValueType
        },function(res){
            showInfoMsg(res);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
})