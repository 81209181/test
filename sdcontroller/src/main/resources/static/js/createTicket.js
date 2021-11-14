let ctx = $("meta[name='_ctx']").attr("content");

$().ready(function(){
    $('#btnAddContact').on('click',function(){
        clearAllMsg();
        if($(this).prev('select').val().length <1){
            showErrorMsg('Please select one contact type.');
            return;
        }
        let contact =$('#tempContact').children().clone();
        contact.find('input[name=contactType]').val($(this).prev('select').find('option:selected').val());
        contact.find('input[name=contactTypeDesc]').val($(this).prev('select').find('option:selected').text());
        contact.appendTo($('#contact_list'));
    });

    // submit button
    $('#btnTicketSubmit').on('click',function(){
        clearAllMsg();

        let contactList = new Array() ;
        $('#contact_list').find('form').each(function(index,form){
            let form_arr =$(form).serializeArray();
            let form_json = {};
            $.map(form_arr, function (n, i) {
              form_json[n['name']] = n['value'];
            });
            contactList.push(form_json);
        });

        let serviceNumber = $("#ticket").find('input[name=serviceNumber]').val();
        let ticketType = $("#ticket").find('select[name=ticketType]').find("option:selected").val();
        let priority = $("#ticket").find('select[name=priority]').find("option:selected").val();
        let remarks = $("#remark").find('textarea[name=remarks]').val();

        let arr = new Array();
        let param_json = {};
        param_json['serviceNumber'] = serviceNumber;
        param_json['ticketType'] = ticketType;
        param_json['priority'] = priority;
        param_json['contactList'] = contactList;
        param_json['remarks'] = remarks;
        arr.push(param_json);

        $.ajax({
            url:'/ticket/submit',
            type : 'POST',
            dataType: 'text',
            contentType: "application/json",
            data: JSON.stringify(arr),
            cache: false,
            success:function(res){
                window.location.href = ctx + "/ticket?ticketMasId=" + res;
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        });
    });
});

function removeContact(btn){
    $(btn).parents('form').remove();
}