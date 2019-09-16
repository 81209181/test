$().ready(function(){

    $.get('/ticket/contact/'+ticketMasId,function(res){
    })

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

})

function removeContact(btn){
    $(btn).parents('form').remove();
    if($('#contact_list').find('form').length <1){
        $('#btnUpdateContact').attr('disabled',true);
    }
}