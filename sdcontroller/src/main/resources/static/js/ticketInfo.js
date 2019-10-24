$().ready(function(){

    var ticketDetId = "";

    $('.selectpicker').selectpicker({});

    if(ticketStatus == "OPEN"){
        $("#ticketStatus").css("color","blue");
    } else if(ticketStatus == "WORKING"){
         $("#ticketStatus").css("color","orange");
    } else if(ticketStatus == "COMPLETE"){
         $("#ticketStatus").css("color","green");
    } else if(ticketStatus == "CANCEL"){
         $("#ticketStatus").css("color","red");
    }

    $.get('/ticket/service/symptom?ticketMasId='+ticketMasId, function (res) {
        for (item of res) {
            $('.selectpicker').append("<option value="+item.symptomCode+">"+item.symptomCode+"---"+item.symptomDescription+"</option>");
        }
        $('.selectpicker').selectpicker('refresh');
    })

    $.get('/ticket/service?ticketMasId='+ticketMasId,function(res){
        if (res.length > 0) {
            $.each(res,function(index,j){
                let service =$('#service');
                $.each(j,function(key,value){
                    service.find('input[name='+key+']').val(value);
                    if (key == 'faultsList') {
                        for (item of value) {
                            $('#symptomList').find('option[value='+item.symptomCode+']').attr('selected','selected');
                        }
                    }
                    if (key == 'ticketDetId') {
                        ticketDetId = value;
                    }
                })
            })
            $('.selectpicker').selectpicker('refresh');
            $('.selectpicker').selectpicker('render');
        }
    });

    ajaxGetDataTable();

    $('#btnUpdateService').on('click',function () {
        let arr = new Array();
        $('#service').each(function(index,form){
            let form_arr =$(form).serializeArray();
            let faults = new Array();
            let form_json = {};
            $.map(form_arr, function (n, i) {
                if (n['name'] == 'symptom') {
                    faults.push(n['value']);
                }
                form_json[n['name']] = n['value'];
            });
            form_json['faults'] = faults;
            form_json['ticketMasId']=ticketMasId;
            arr.push(form_json);
        })
        $.ajax({
            url:'/ticket/service/update',
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

    // call in count
    $('#btnCallInCount').on('click', function(){
        $.post('/ticket/callInCount', {
            ticketMasId:ticketMasId
        }, function (res) {
            if (res.success) {
                location.reload();
            } else {
                showErrorMsg(res.feedback);
            }
        })
    })

    // make appointment
    $('#btnMakeAppointment').on('click', function () {
        if (ticketMasId == '') {
            showErrorMsg('No ticket mas Id');
            return;
        }
        if (ticketDetId == '') {
            showErrorMsg('No ticket detail Id.');
            return;
        }
        makeAppointment(ticketMasId, ticketDetId);
    })

    //contact
    $.get('/ticket/contact?ticketMasId='+ticketMasId,function(res){
        if (res.length == 0) {
            if(ticketStatus != "COMPLETE"){
                let contact =$('#tempContact').children().clone();
                contact.find('input[name=contactType]').val("On-site Contact");
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
        contact.find('input[name=contactType]').val($(this).prev('select').find('option:selected').text());
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
                if (res == "") {
                    showInfoMsg("Update contact info success");
                } else {
                    showErrorMsg(res);
                }
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

    $('#btnCreateTicketRemarks').on('click',function(){
        clearAllMsg();
        let form_arr = $('#remark').find('form').serialize();
        form_arr += "&ticketMasId="+ticketMasId;
        $.post('/ticket/post-create-ticket-remarks',form_arr,function(res){
            if (res.success) {
                $('#remark').find('form')[0].reset();
                $('#searchTicketRemarksTable').DataTable().ajax.reload();
            } else {
                showErrorMsg(res.feedback);
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Create failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

    readyForTicketService();

    // service link button
    $('#btnServiceLink').on('click',function(){
        window.open($(this).data('url'),'Profile','scrollbars=yes,height=600,width=800');
    })

    // appointment
    $('#asap_checkbox').change(function(){
        let input =$('input[name=appointmentDate]');
        if(this.checked){
            let tzoffset = (new Date()).getTimezoneOffset() * 60000;
            let localISOTime = (new Date(Date.now() - tzoffset)).toISOString().slice(0, 16);
            input.val(localISOTime);
            input.attr('disabled',true);
        }else{
            input.val('');
            input.attr('disabled',false);
        }
    })

    $('#btnUpdateAppointment').on('click',function(){
        clearAllMsg();
        let appointment =$('input[name=appointmentDate]').val();
        if(!appointment){
            showErrorMsg('please input appointment date.');
            return ;
        }
        $.post('/ticket/appointment/update',{
            appointmentDate:appointment,
            asap:$(this).parents('form').find('input[type=checkbox]').get(0).checked,
            ticketMasId:ticketMasId
        },function(res){
            showInfoMsg(res);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

    // submit button
    $('#btnTicketSubmit').on('click',function(){
        let ticket ={};
        let ticket_input =$('.card-body').find('input');
        $.each(ticket_input,function(index,input){
            ticket[$(input).attr('name')] =$(input).val();
        })
        ticket['ticketMasId'] =ticketMasId;
        $.ajax({
            url:'/ticket/submit',
            type : 'POST',
            dataType: 'json',
            data: ticket,
            success:function(res){
                if(res.success){
                    location.reload();
                }
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
    // close button
    $('#btnTicketClose').on('click',function(){
        $('.modal').modal('show');
    })

    $('#btnReasonSubmit').on('click',function(){
        let form =$('.needs-validation').get(0);
        if(form.checkValidity()){
            $.post('/ticket/close',{
                ticketMasId:ticketMasId,
                reasonType:$('select[name=reasonType]').val(),
                reasonContent:$('textarea[name=reasonContent]').val()
            },function(res){
                if(res.success){
                    location.reload();
                }
            }).fail(function(e){
                var responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
                showErrorMsg(responseError);
            })
        }
        $(form).addClass("was-validated");
    })

    ajaxGetJobInfo(ticketMasId);
})


function readyForTicketService() {
    $('#btnAddService').on('click',function(){
        let service =$('#tempService').children().clone();
        service.appendTo($('#service_list'));
        $('#btnUpdateService').attr('disabled',false);
    })
}

function addFaults(btn) {
    $('#btnUpdateService').attr('disabled',false);
    let service =$('#tempFaults').children().clone();
    service.appendTo($(btn).parent().next('.faults_list'));
}

function removeService(btn){
    $(btn).parents('form').remove();
    $('#btnUpdateService').attr('disabled',false);
    if($('#service_list').find('form').length <1){
        $('#btnUpdateService').attr('disabled',true);
    }
}

function removeFaults(btn){
    $(btn).parent().remove();
}


function removeContact(btn){
    $(btn).parents('form').remove();
    if($('#contact_list').find('form').length < 1){
        $('#btnUpdateContact').attr('disabled',true);
    }else{
        $('#btnUpdateContact').attr('disabled',false);
    }
}

function makeAppointment(ticketMasId, ticketDetId) {
    AppointmentSDObj.make({
        data : {
            ticketMasId : ticketMasId,
            ticketDetId : ticketDetId,
            serviceType: "Broadband",
            userName : "sd",
            password : "Ki6=rEDs47*^5"
        }
        //}, 'https://10.252.15.158/wfm');
    }, "https://10.252.15.158/wfm");
}

function getInventory(bsn) {
    $.ajax({
        url: '/ticket/getInventory',
        type: 'POST',
        data: {bsn:bsn},
        dataType: 'text',
        success: function (res) {
            let inventoryWindow= window.open('','Inventory','scrollbars=yes,width=800, height=800');
            inventoryWindow.document.write(res);
            inventoryWindow.focus();
        }
    }).fail(function (e) {
        let responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}

function getOfferDetail(bsn) {
    if (bsn === '') {
        showErrorMsg('No service number!');
        return;
    }

    let ctx = $("meta[name='_ctx']").attr("content");
    window.open(ctx+'/ticket/offer-detail?bsn='+bsn,'OfferDetail','scrollbars=yes,height=400,width=500');
}


function ajaxGetJobInfo(ticketMasId){
    $.ajax({
        url:'/ticket/getJobInfo',
        type : 'POST',
        dataType: 'json',
        data: {ticketMasId:ticketMasId},
        success:function(res){
            let jobList = res.wfmJobInfoDataList;
            for (jobInfo of jobList) {
                let jobItem = $('.jobItem').children().clone();
                jobItem.find('input[name=jobId]').val(jobInfo.jobId);
                jobItem.find('input[name=handler]').val(jobInfo.handler);
                jobItem.find('input[name=jobStatus]').val(jobInfo.jobStatus);
                jobItem.appendTo($('#jobList'));
            }
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}


function ajaxGetDataTable(){
    $('#searchTicketRemarksTable').DataTable({
        searching: false,
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
            { width: '55%', data: 'remarks' },
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