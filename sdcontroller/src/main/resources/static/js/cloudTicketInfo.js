let ticketMasId ;
$(function(){
    ticketMasId = $('#ticketMasId').text();
//  contact
    showContact();
    $('#btnUpdateContact').on('click',function(){ updateContact() });
//  remark
    showRemark();
    $('#btnCreateTicketRemarks').on('click',function(){ addRemark() });
//  upload file
    showUploadFile();
//  close button
    $('#btnTicketClose').on('click',function(){$('.reason').modal('show')});
    $('#btnReasonSubmit').on('click',function(){ closeTicket() });
//  submit ticket
    $('#btnTicketSubmit').on('click',function(){ submitTicket() });
})
function submitTicket(){
    clearAllMsg();
    $.post('/ticket/submit',{'ticketMasId':ticketMasId},function(res){
        if(res.success){
            location.reload();
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}


function closeTicket(){
    let form =$('#close_ticket_form').get(0);
    if(form.checkValidity()){
        $.post('/ticket/close',{
            ticketMasId:ticketMasId,
            reasonType:$(form).find('select[name=reasonType]').val(),
            reasonContent:$(form).find('textarea[name=reasonContent]').val(),
            contactName:$(form).find('input[name=contactName]').val(),
            contactNumber:$(form).find('input[name=contactNumber]').val(),
        },function(res){
            if(res.success){
                location.reload();
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            alert(responseError);
        })
    }
    $(form).addClass("was-validated");
}

function showUploadFile(){
    clearAllMsg();
    $.post('/ticket/get-upload-files',{ticketMasId:ticketMasId},function(res){
        let div = $('#div_upload_file');
        $.each(res,function(index,val){
            let filename = val.fileName;
            let base64Context = val.content;
            let file_blob = _base64ToBlob(base64Context);
            let file_div = div.clone();
            file_div.find('input').val(filename);
            file_div.find('a').attr({href:URL.createObjectURL(file_blob),download:filename});
            file_div.appendTo(div.parent());
            file_div.show('slow');
        })
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Create failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}
function _base64ToBlob(base64) {
    var binary_string = window.atob(base64);
    var len = binary_string.length;
    var bytes = new Uint8Array(len);
    for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return new Blob([bytes.buffer]);
}


function addRemark(){
    clearAllMsg();
    let form_arr = $('#remark').find('form').serialize();
    form_arr += "&ticketMasId="+ticketMasId;
    $.post('/ticket/post-create-ticket-remarks',form_arr,function(res){
        if (res.success) {
            $('#remark').find('form')[0].reset();
            $('#searchTicketRemarksTable').DataTable().ajax.reload();
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Create failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}


function showRemark(){
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


function showContact(){
    $.get('/ticket/contact?ticketMasId='+ticketMasId,function(res){
        $.each(res[0],function(key,value){
            $('#contact').find('input[name='+key+']').val(value);
        })
    });
}

function updateContact(){
    clearAllMsg();
    let arr =new Array() ;
    $('#contact').find('form').each(function(index,form){
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
}