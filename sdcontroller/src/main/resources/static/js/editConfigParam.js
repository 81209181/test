$().ready(function(){
    let configGroup =$('#configGroup').val(),
        configKey = $('#configKey').val();
    $.get('/system/config-param/getConfigParam',{configGroup:configGroup,configKey:configKey},function(r){
        $.each(r,function(key,value){
            $('input[name='+key+']').val(value);
            $('select[name='+key+']').val(value);
            $('input[type=checkbox][name='+key+']').prop("checked",value);
            if(value == 'Boolean'){
                $('#config_value_select').attr("disabled",false);
                $('#config_value_select').attr("hidden",false);
                $('#config_value_input').attr("disabled",true);
                $('#config_value_input').attr("hidden",true);
            }
        })
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })

    $('#configValueType').change(function(){
        let selected = $(this).children('option:selected').val();
        $('#config_value_input').val('');
        if(selected =='LocalDateTime'){
            $('#config_value_input').attr('type','datetime-local');
            $('#config_value_input').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}');
            $('#config_value_input').attr('placeholder','1900-01-01T01:00');
        }else{
            $('#config_value_input').attr('type','text');
            $('#config_value_input').removeAttr('pattern');
            $('#config_value_input').removeAttr('placeholder');
        }
        if(selected == 'Boolean'){
            $('#config_value_select').attr("disabled",false);
            $('#config_value_select').attr("hidden",false);
            $('#config_value_input').attr("disabled",true);
            $('#config_value_input').attr("hidden",true);
        }else{
            $('#config_value_select').attr("disabled",true);
            $('#config_value_select').attr("hidden",true);
            $('#config_value_input').attr("disabled",false);
            $('#config_value_input').attr("hidden",false);
        }
    })

    $('#btnUpdateConfigParam').on("click",function(){
        clearAllMsg();
        $.post('/system/config-param/updateConfigParam',$('form').serialize(),function(res){
            showInfoMsg(res);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
})