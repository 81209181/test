$().ready(function(){
    /******************************************** Load and render each part *******************************************/
    if(ticketStatusDesc === "OPEN"){
        $("#ticketStatusDesc").css("color","blue");
        $("#btnTicketComplete").hide();
    } else if(ticketStatusDesc === "WORKING"){
        $("#ticketStatusDesc").css("color","orange");
        $("#btnTicketAssign").hide();
    } else if(ticketStatusDesc === "COMPLETE"){
        $("#ticketStatusDesc").css("color","green");
        $("#btnUpdateContact").hide();
        $("#btnTicketClose").hide();
        $("#btnTicketAssign").hide();
        $("#btnTicketComplete").hide();
    } else if (ticketStatusDesc === "CLOSE") {
        $("#ticketStatusDesc").css("color","red");
        $("#btnUpdateContact").hide();
        $("#btnTicketClose").hide();
        $("#btnTicketAssign").hide();
        $("#btnTicketComplete").hide();
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

    $('#btnUpdateContact').on('click',function(){
        clearAllMsg();
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
                if (res === "") {
                    showInfoMsg("Updated contact info.", false);
                } else {
                    showErrorMsg(res);
                }
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    });

    // remark
    getRemarksTableData();

    // close button
    $('#btnTicketClose').on('click',function(){
        $('.reason').modal('show');
    });

    $('#btnReasonSubmit').on('click',function(){
        let form =$('.reason-form').get(0);
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

    // assign button
    $('#btnTicketAssign').on('click',function(){
        $('.assign').modal('show');
    });

    $('.workGroup').change(function(){
        let selected = $(this).children('option:selected').val();
        $.get('/ticket/getAssignEngineer?roleId='+selected, function (res) {
            if (res != null) {
                for (item of res) {
                    $('.engineer').append("<option value=" + item.userId + ">" + item.staffId + "--" + item.name + "</option>");
                }
            }
        });
    })

    $('#btnAssignSubmit').on('click',function(){
        let form =$('.assign-form').get(0);
        if(form.checkValidity()){
            $.post('/ticket/assign',{
                ticketMasId:ticketMasId,
                engineer:$(form).find('select[name=engineer]').val(),
                remark:$(form).find('textarea[name=remark]').val()
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

    // close button
    $('#btnTicketComplete').on('click',function(){
        $('.complete').modal('show');
    });

    $('#btnCompleteSubmit').on('click',function(){
        let form =$('.complete-form').get(0);
        if(form.checkValidity()){
            $.post('/ticket/complete',{
                ticketMasId:ticketMasId,
                remark:$(form).find('textarea[name=remark]').val(),
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
