var ngn3Btn = $('.ngn3Btn'),
    voIpBtn = $('.voIpBtn'),
    bbBtn = $('.bbBtn'),
    inventoryBtn = $('.inventoryBtn'),
    eCloudBtn =$('.eCloudBtn');

$().ready(function(){

    var ticketDetId = "";

    let symptomCode = "";

    let serviceType = "";

    $('.selectpicker').selectpicker({});

    if(ticketStatusDesc === "OPEN"){
        $("#ticketStatusDesc").css("color","blue");
    } else if(ticketStatusDesc === "WORKING"){
         $("#ticketStatusDesc").css("color","orange");
    } else if(ticketStatusDesc === "COMPLETE"){
         $("#ticketStatusDesc").css("color","green");
    } else if(ticketStatusDesc === "CANCEL"){
         $("#ticketStatusDesc").css("color","red");
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
                        service.find('input[name='+key+']').val(value);
                        if (key === 'couldUrl'){
                            $('.eCloudBtn').data('url',value);
                        }
                        if (key === 'faultsList') {
                            if (value == '') {
                                $('#btnMakeAppointment').attr('disabled', true);
                            }
                            for (item of value) {
                                $('#symptomList').find('option[value=' + item.symptomCode + ']').attr('selected', 'selected');
                                symptomCode = item.symptomCode;
                            }

                        }
                        if (key === 'ticketDetId') {
                            ticketDetId = value;
                        }
                        if (key === 'serviceType') {
                            serviceType = value
                        }
                        // button control
                        if (key === 'bnCtrl') {
                            bnButtonCtrl(value);
                        }
                        if (key === 'voIpCtrl'){
                            voIpButtonCtrl(value);
                        }
                        if (key === 'eCloudCtrl'){
                            eCloudButtonCtrl(value);
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

    getRemarksTableData();

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
        makeAppointment(ticketMasId, ticketDetId, symptomCode,serviceType);
    });

    getAppointmentInfo(ticketMasId);

    //contact
    $.get('/ticket/contact?ticketMasId='+ticketMasId,function(res){
        if (res.length === 0) {
            if(ticketStatusDesc !== "COMPLETE"){
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
    });

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
    });

    readyForTicketService();

    // service link button
    $('.eCloudBtn').on('click',function(){
        window.open($(this).data('url'),'Profile','scrollbars=yes,height=600,width=800');
    });

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
    });


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

    $('#relatedTicketTable').hide();
    $('.panel').find('.panel-body').slideUp();
    $('.panel').find('.panel-heading').find('span').addClass('panel-collapsed');

    $(document).on('click', '.panel-heading span.clickable', function(e){
        $('#relatedTicketTable').dataTable().fnClearTable(false);
        $('#relatedTicketTable').dataTable().fnDestroy();
        var $this = $(this);
        if(!$this.hasClass('panel-collapsed')) {
            $this.parents('.panel').find('.panel-body').slideUp();
            $this.addClass('panel-collapsed');
            $this.find('i').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
            $('#relatedTicketTable').hide();
        } else {
            $this.parents('.panel').find('.panel-body').slideDown();
            $this.removeClass('panel-collapsed');
            $this.find('i').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-down');
            let serviceNo = $('input[name=serviceCode]').val();
            if (serviceNo === '') {
                $('#relatedTicketTable').hide();
            } else {
                $('#relatedTicketTable').show();
                $('#relatedTicketTable').DataTable({
                    processing: true,
                    serverSide: true,
                    ordering: false,
                    searching: false,
                    ajax: {
                        type: "GET",
                        contentType: "application/json",
                        url: "/ticket/searchTicket",
                        dataSrc: 'data',
                        data: function(d){
                            d.serviceNumber = serviceNo
                        },
                        error: function (e) {
                            if(e.responseText){
                                showErrorMsg(e.responseText);
                            } else {
                                showErrorMsg("Cannot load result.");
                            }
                        }
                    },
                    columns: [
                        { data: 'ticketMasId' },
                        { data: 'ticketType' },
                        { data: 'statusDesc' },
                        { data: 'callInCount'},
                        { data: 'createDate' },
                        { data: 'completeDate'}
                    ],
                    columnDefs: [
                        {
                            targets: 4,
                            data: "createDate",
                            render: function (nextRunTime, type, row, meta) {
                                return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                            }
                        },
                        {
                            targets: 5,
                            data: "completeDate",
                            render: function (nextRunTime, type, row, meta) {
                                return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                            }
                        },
                        {
                            targets: 6,
                            data: "ticketMasId",
                            render: function ( ticketMasId, type, row, meta ) {
                                var ctx = $("meta[name='_ctx']").attr("content");
                                var link = ctx + "/ticket?ticketMasId=" + ticketMasId;
                                return '<a class="btn btn-info" href=' + link + ' role="button">Detail</a>';
                            }
                        }
                    ]
                });
            }
        }
    })
})


function readyForTicketService() {
    $('#btnAddService').on('click',function(){
        let service =$('#tempService').children().clone();
        service.appendTo($('#service_list'));
        $('#btnUpdateService').attr('disabled',false);
    })
}

function removeContact(btn){
    $(btn).parents('form').remove();
    if($('#contact_list').find('form').length < 1){
        $('#btnUpdateContact').attr('disabled',true);
    }else{
        $('#btnUpdateContact').attr('disabled',false);
    }
}

function makeAppointment(ticketMasId, ticketDetId, symptomCode, serviceType) {

    let bsn = $("#service").find('input[name=relatedBsn]').val();
    if (bsn === '') {
        bsn = $("#service").find('input[name=serviceCode]').val();
    }

    $.get('/ticket/token', {
        bsn : bsn,
        ticketMasId: ticketMasId,
        ticketDetId: ticketDetId,
        symptomCode: symptomCode,
        serviceType: serviceType
    }, function (res) {
        let window = AppointmentSDObj.make({
            data: {
                sdToken: res.jwt,
            }
        }, res.url);
        checkWindowClose(window);
    }).fail(function (e) {
        let responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
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
            $('#appointment').find('input[name=appointmentDateStr]').val(res.appointmentDateStr);
            let hasAppointment = !(res.appointmentDateStr === null || res.appointmentDateStr === "");
            controlSymptomUpdateUi(hasAppointment);
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
        controlSymptomUpdateUi(true);
    });
}

function controlSymptomUpdateUi(disable){
    $("#symptomList").siblings().attr("disabled", disable);
    $("#btnUpdateService").attr("disabled", disable);
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

function goResetNgn3Pwd(){
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
}

function eCloudButtonCtrl(flag) {
    if (flag) {
        inventoryBtn.attr('disabled', true);
        bbBtn.attr('disabled', true);
        voIpBtn.attr('disabled', true);
        ngn3Btn.hide();
    }
}

function bnButtonCtrl(val){
    if(val){
        eCloudBtn.attr('disabled', true);
        inventoryBtn.attr('disabled', false);
        bbBtn.attr('disabled', false);
        voIpBtn.attr('disabled', true);
        ngn3Btn.hide();
    }
}

function voIpButtonCtrl(val){
    if(val){
        eCloudBtn.attr('disabled', true);
        inventoryBtn.attr('disabled', false);
        bbBtn.attr('disabled', false);
        voIpBtn.attr('disabled', false);
    }
}