$().ready(function(){

    $('#new_group').on('click',function(){
        if($(this).is(':checked')){
            $('#config_group_select').attr("disabled",true);
            $('#config_group_select').attr("hidden",true);
            $('#config_group_input').attr("disabled",false);
            $('#config_group_input').attr("hidden",false);
        }else{
            $('#config_group_select').attr("disabled",false);
            $('#config_group_select').attr("hidden",false);
            $('#config_group_input').attr("disabled",true);
            $('#config_group_input').attr("hidden",true);
        }
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

    $('#btnCreateConfigParam').on('click',function(){
        clearAllMsg();
        let configValue = $('#configValue').val();
        if(configValue.length < 1){
            showErrorMsg("Please input Config Value.");
            return;
        }
        if(!checkConfigValue(configValue,$('#configValueType').val())){
            return;
        }
        $.post('/system/config-param/createConfigParam',$('form').serialize(),function(res){
            showInfoMsg(res);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
})