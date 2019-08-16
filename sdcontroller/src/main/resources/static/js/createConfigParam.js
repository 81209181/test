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

    $('#btnCreateConfigParam').on('click',function(){
        clearAllMsg();
        var form = $('form').serializeArray();
        var group_key;
        let ctx = $("meta[name='_ctx']").attr("content");
        $.each(form,function(i,val){
            if(val.name =='configGroup'){
                group_key = val.value + '/';
            }
            if(val.name =='configKey'){
                group_key += val.value;
            }
        })
        $.post('/system/config-param/createConfigParam',$('form').serialize(),function(res){
//            showInfoMsg(res);
            $(location).attr('href',ctx+'/system/config-param/'+group_key);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
})