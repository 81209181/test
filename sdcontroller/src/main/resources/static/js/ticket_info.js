$().ready(function(){

    $.get('/ticket/contact/'+ticketMasId,function(res){
        $.each(res,function(index,j){
            let contact =$('#tempContact').children().clone();
            $.each(j,function(key,value){
                contact.find('input[name='+key+']').val(value);
            })
            contact.appendTo($('#contact_list'));
        })
    })

    /*$.get('/ticket/service/'+ticketMasId,function(res){
        $.each(res,function(index,j){
            let service =$('#tempService').children().clone();
            $.each(j,function(key,value){
                service.find('input[name='+key+']').val(value);
                if (value instanceof Array) {
                    for (item of value) {
                        let faults = $('#tempFaults').children().clone();
                        faults.find('input[name=faults]').val(item.faults);
                        faults.appendTo($('#faults_list'));
                    }
                }
            })
            service.appendTo($('#service_list'));
        })
    })*/


    $('#btnAddContact').on('click',function(){
        clearAllMsg();
        let contactType =$(this).prev('select').val();
        if(contactType.length <1){
            showErrorMsg('Please one contact type.');
            return;
        }
        let contact =$('#tempContact').children().clone();
        contact.find('input[name=contactType]').val(contactType);
        contact.appendTo($('#contact_list'));
        $('#btnUpdateContact').attr('disabled',false);
    })

    $('#btnUpdateContact').on('click',function(){
        let arr =new Array() ;
        $('#contact_list').find('form').each(function(index,form){
            let form_arr =$(form).serializeArray();
            let form_json = {};
            $.map(form_arr, function (n, i) {
              form_json[n['name']] = n['value'];
            });
            form_json['ticketMasId']=ticketMasId;
            arr.push(form_json);
        })
        $.ajax({
            url:'/ticket/contact/update',
            type : 'POST',
            dataType: 'text',
            contentType: "application/json",
            data: JSON.stringify(arr),
            success:function(res){
                showInfoMsg(res);
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

    readyForTicketService();
})


function readyForTicketService() {
    $('#btnAddService').on('click',function(){
        let service =$('#tempService').children().clone();
        service.appendTo($('#service_list'));
        $('#btnUpdateService').attr('disabled',false);
    })
}

function addFaults(btn) {
    let service =$('#tempFaults').children().clone();
    service.appendTo($(btn).parent().find('.faults_list'));
}

function removeService(btn){
    $(btn).parents('form').remove();
    if($('#service_list').find('form').length <1){
        $('#btnUpdateService').attr('disabled',true);
    }
}

function removeFaults(btn){
    console.log($(btn).parent());
    $(btn).parent().remove();
}


function removeContact(btn){
    $(btn).parents('form').remove();
    if($('#contact_list').find('form').length <1){
        $('#btnUpdateContact').attr('disabled',true);
    }
}