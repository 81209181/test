$().ready(function(){
    /******************************************** Load and render each part *******************************************/
    if(ticketStatusDesc === "OPEN"){
        $("#ticketStatusDesc").css("color","blue");
    } else if(ticketStatusDesc === "WORKING"){
        $("#ticketStatusDesc").css("color","orange");
    } else if(ticketStatusDesc === "COMPLETE"){
        $("#ticketStatusDesc").css("color","green");
        $("#btnTicketSubmit").hide();
    }

    // contact
    $.get('/ticket/contact?ticketMasId='+ticketMasId,function(res){
        if (res.length === 0) {
            if(ticketStatusDesc !== "COMPLETE"){
                let contact =$('#tempContact').children().clone();
                contact.find('input[name=contactType]').val("SITE");
                contact.find('input[name=contactTypeDesc]').val("On-site Contact");
                contact.appendTo($('#contact_list'));
                $('#btnUpdateContact').attr('disabled',false);
            }
        } else {
            $.each(res,function(index,j){
                let contact =$('#tempContact').children().clone();
                $.each(j,function(key,value){
                    contact.find('input[name='+key+']').val(value);
                })
                contact.appendTo($('#contact_list'));
            })
        }
    });


    // remark
    getRemarksTableData();

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
        $('#btnUpdateContact').attr('disabled',false);
    });

    // close button
    $('#btnTicketClose').on('click',function(){
        $('.reason').modal('show');
    });

    $('#btnReasonSubmit').on('click',function(){
        let form =$('.needs-validation').get(0);
        if(form.checkValidity()){
            $.post('/ticket/close',{
                ticketMasId:ticketMasId,
                reasonContent:$(form).find('textarea[name=reasonContent]').val(),
            },function(res){
                if(res.success){
                    location.reload();
                }
            }).fail(function(e){
                var responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
            })
        }
        $(form).addClass("was-validated");
    });
})

/*********************************************** functions **********************************************************/
function removeContact(btn){
    $(btn).parents('form').remove();
    if($('#contact_list').find('form').length < 1){
        $('#btnUpdateContact').attr('disabled',true);
    }else{
        $('#btnUpdateContact').attr('disabled',false);
    }
}


function getRemarksTableData(){
    $('#searchTicketRemarksTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/ticket/ajax-search-ticket-remarks?ticketMasId="+ticketMasId,
            dataSrc: '',
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                }
            }
        },
        columns: [
            { width: '15%', data: 'createdate' },
            { width: '15%', data: 'remarksType' },
            { width: '55%', data: 'remarks' , render: function(data,type,row,meta){
                if(data.match(/\r\n/g)){
                    return '<pre>'+data+'</pre>';
                }else{
                    return data;
                }
            }},
            { width: '15%', data: 'createby' }
        ],
        columnDefs: [
            {
                targets: 0,
                data: "createdate",
                render: function (nextRunTime, type, row, meta) {
                     return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
        ],
        order: [[ 0, "desc" ]]
    });
}

function checkWindowClose(winObj) {
    let loop = setInterval(function() {
        if(winObj.closed) {
            clearInterval(loop);
            window.location.reload();
        }
    }, 1000);
}
