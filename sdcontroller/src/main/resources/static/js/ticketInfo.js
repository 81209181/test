$().ready(function(){

    var ticketDetId = "";

    $('.selectpicker').selectpicker({});

    if(ticketStatus === "OPEN"){
        $("#ticketStatus").css("color","blue");
    } else if(ticketStatus === "WORKING"){
         $("#ticketStatus").css("color","orange");
    } else if(ticketStatus === "COMPLETE"){
         $("#ticketStatus").css("color","green");
    } else if(ticketStatus === "CANCEL"){
         $("#ticketStatus").css("color","red");
    }

    $.get('/ticket/service/symptom?ticketMasId='+ticketMasId, function (res) {
        for (item of res) {
            $('.selectpicker').append("<option value="+item.symptomCode+">"+item.symptomCode+"---"+item.symptomDescription+"</option>");
        }
        $('.selectpicker').selectpicker('refresh');

        $.get('/ticket/service?ticketMasId='+ticketMasId,function(res){
            if (res.length > 0) {
                $.each(res,function(index,j){
                    let service =$('#service');
                    $.each(j,function(key,value){
//                    service.find('input[name='+key+']').val(value);    // by Dennis  ref jira 179
                        if (key === 'faultsList') {
                            if (value === '') {
                                $('#btnMakeAppointment').attr('disabled', true);
                            }
                            for (item of value) {
                                $('#symptomList').find('option[value='+item.symptomCode+']').attr('selected','selected');
                            }

                        }
                        if (key === 'ticketDetId') {
                            ticketDetId = value;
                        }
                    })
                })
                $('.selectpicker').selectpicker('refresh');
                $('.selectpicker').selectpicker('render');
            } else {
                $('#btnMakeAppointment').attr('disabled', true);
            }
        });
    })


    ajaxGetDataTable();

    $('#btnUpdateService').on('click',function () {
        let arr = new Array();
        $('#service').each(function(index,form){
            let form_arr =$(form).serializeArray();
            let faults = new Array();
            let form_json = {};
            $.map(form_arr, function (n, i) {
                if (n['name'] === 'symptom') {
                    faults.push(n['value']);
                }
                form_json[n['name']] = n['value'];
            });
            form_json['faults'] = faults;
            form_json['ticketMasId']=ticketMasId;
            arr.push(form_json);
        });
        $.ajax({
            url:'/ticket/service/update',
            type : 'POST',
            dataType: 'text',
            contentType: "application/json",
            data: JSON.stringify(arr),
            success:function(res){
                showInfoMsg(res);
                window.location.reload();
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        });
    });

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
        if (ticketMasId === '') {
            showErrorMsg('No ticket mas Id');
            return;
        }
        if (ticketDetId === '') {
            showErrorMsg('No ticket detail Id.');
            return;
        }
        makeAppointment(ticketMasId, ticketDetId);
    });

    getAppointmentInfo(ticketMasId);

    //contact
    $.get('/ticket/contact?ticketMasId='+ticketMasId,function(res){
        if (res.length === 0) {
            if(ticketStatus !== "COMPLETE"){
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
                if (res === "") {
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

    // submit button
    $('#btnTicketSubmit').on('click',function(){
        let ticket ={};
        let ticket_input =$('.card-body').find('input');
        $.each(ticket_input,function(index,input){
            ticket[$(input).attr('name')] =$(input).val();
        })
        ticket['ticketMasId'] =ticketMasId;
        clearAllMsg();
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
    });

    $('#btnResetNGN3PWD').on('click',function(){
        let accountSelect = $('select[name=ngn3Account]');
        accountSelect.find('option:not(:first)').remove();
        $('input[name=ngn3pwd]').val('');
        $.get('/ticket/getNgn3AccountList/'+bsn,function(res){
            $.each(res,function(k,v){
                accountSelect.append('<option>'+v+'</option>');
            })
        }).then(function(){
            $('.ngn3').modal({backdrop: 'static', keyboard: false});
        })
    })

    $('#btnNgn3Reset').on('click',function(){
        let account =$('select[name=ngn3Account]').val();
        if(account){
            $.get('/ticket/resetNgn3Pwd/'+account,function(res){
                $('input[name=ngn3pwd]').val(res);
            }).fail(function(e){
                let responseError = e.responseText ? e.responseText : "Get failed.";
                alert('ERROR : ' + responseError);
            })
        }else{
            alert("Please select one account.");
        }
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

    $.ajax({
        type: 'GET',
        url: '/ticket/makeAppointment',
        dataType: 'text',
        success: function (res) {
            let inventoryWindow = window.open('', 'Inventory', 'scrollbars=yes,width=800, height=800');
            inventoryWindow.document.write(res);
            inventoryWindow.focus();
        }
    })

    /*let window = AppointmentSDObj.make({
        data : {
            ticketMasId : ticketMasId,
            ticketDetId : ticketDetId,
            serviceType: "Broadband", // todo [SERVDESK-182]: ticket service type
            symptomCode: "ABC123", // todo [SERVDESK-182]: ticket symptom code
            userName : "sd",
            password : "Ki6=rEDs47*^5" // todo [SERVDESK-182]: need to use config param, and hide in backend
        }
    }, "https://10.252.15.158/wfm"); // todo [SERVDESK-182]: need to use config param link
    checkWindowClose(window);*/
}

function getInventory(bsn) {
    if (bsn === '') {
        showErrorMsg('No service number!');
    }else {
        let relatedBsn =  $("#relatedBsn").val();
        let key = relatedBsn=== '' ? bsn : relatedBsn;

        $.ajax({
            url: '/ticket/getInventory',
            type: 'POST',
            data: {bsn: key},
            dataType: 'text',
            success: function (res) {
                let inventoryWindow = window.open('', 'Inventory', 'scrollbars=yes,width=800, height=800');
                inventoryWindow.document.write(res);
                inventoryWindow.focus();
            }
        }).fail(function (e) {
            let responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    }
}

function getRelatedOfferInfo(bsn) {
    if (bsn === '') {
        showErrorMsg('No service number!');
    }else {
        let ctx = $("meta[name='_ctx']").attr("content");
        let relatedBsn =  $("#relatedBsn").val();
        let key = relatedBsn=== '' ? bsn : relatedBsn;
        window.open(ctx+'/ticket/offer-info?bsn='+key,'RelatedOfferInfo','scrollbars=yes,height=800,width=1200');
    }

}

function getOfferDetailList(bsn) {
    if (bsn === '') {
        showErrorMsg('No service number!');
    }else {
        let ctx = $("meta[name='_ctx']").attr("content");
        let relatedBsn =  $("#relatedBsn").val();
        let key = relatedBsn=== '' ? bsn : relatedBsn;
        window.open(ctx+'/ticket/offer-detail?bsn='+key,'OfferDetailList','scrollbars=yes,height=800,width=1200');
    }
}

function getNGN3OneDayAdminAccount(bsn) {
        clearAllMsg();
        $.get('/ticket/getNGN3OneDayAdminAccount/'+ bsn,function(res){
            $.each(res,function(k,v){
                $('.ngn3-one-day-account').find('input[name='+ k +']').val(v);
            })
        }).fail(function(e){
            let responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }).then(function(){
            $('.ngn3-one-day-account').modal({backdrop: 'static', keyboard: false});
        })
}

function getAppointmentInfo(ticketMasId) {
    $.ajax({
        url: '/ticket/getAppointmentInfo',
        type: 'GET',
        dataType: 'json',
        data: {ticketMasId:ticketMasId},
        success: function (res) {
            $('#appointment').find('input[name=appointmentDate]').val(res.appointmentDate);
            $('#appointment').find('input[name=appointmentStartDateTime]').val(res.appointmentStartDateTime);
            $('#appointment').find('input[name=appointmentEndDateTime]').val(res.appointmentEndDateTime);
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}


function ajaxGetJobInfo(ticketMasId){
    $.ajax({
        url:'/ticket/getJobInfo',
        type : 'POST',
        dataType: 'json',
        data: {ticketMasId:ticketMasId},
        success:function(res){
            for (jobInfo of res) {
                let jobItem = $('.jobItem').children().clone();
                jobItem.find('input[name=jobId]').val(jobInfo.jobId);
                jobItem.find('input[name=deptId]').val(jobInfo.deptId);
                jobItem.find('input[name=sysId]').val(jobInfo.sysId);
                jobItem.find('input[name=status]').val(jobInfo.status);
                jobItem.find('input[name=assignTech]').val(jobInfo.assignTech);
                jobItem.find('input[name=actionDate]').val(jobInfo.actionDate);
                jobItem.find('input[name=srDate]').val(jobInfo.srDate);
                jobItem.find('input[name=apptDate]').val(jobInfo.apptDate);
                jobItem.find('input[name=apptSTime]').val(jobInfo.apptSTime);
                jobItem.find('input[name=apptETime]').val(jobInfo.apptETime);

                jobItem.find('input[name=lastUpdateDate]').val(jobInfo.lastUpdateDate);
                jobItem.find('input[name=fieldInd]').val(jobInfo.fieldInd);
                jobItem.find('input[name=lastJobInd]').val(jobInfo.lastJobInd);
                jobItem.find('input[name=alertFieldRemark]').val(jobInfo.alertFieldRemark);
                jobItem.find('input[name=workgroup]').val(jobInfo.workgroup);
                jobItem.find('input[name=resolver]').val(jobInfo.resolver);
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

function checkWindowClose(winObj) {
    let loop = setInterval(function() {
        if(winObj.closed) {
            clearInterval(loop);
            window.location.reload();
        }
    }, 1000);
}