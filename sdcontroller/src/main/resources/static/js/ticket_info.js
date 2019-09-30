$().ready(function(){

    var ticketDetId = "";

    if($('input[name=jobId]').val()){
        $.each($('.card button'),function(){
            $(this).attr('disabled',true);
        })
    }

    $.get('/ticket/service/'+ticketMasId,function(res){
        $.each(res,function(index,j){
            let service =$('#tempService').children().clone();
            $.each(j,function(key,value){
                service.find('input[name='+key+']').val(value);
                if (value instanceof Array) {
                    for (item of value) {
                        let faults = $('#tempFaults').children().clone();
                        faults.find('input[name=faults]').val(item.faults);
                        faults.appendTo(service.find('.faults_list'));
                    }
                }
            })
            service.appendTo($('#service_list'));
        })
    });

    $.get('/ticket/remark/'+ticketMasId,function(res){
        $.each(res,function(index,j){
            let remark =$('#tempRemark').children().clone();
            $.each(j,function(key,value){
                remark.find('input[name='+key+']').val(value);
                if(key == "remarksType" && value == "System"){
                    remark.find('input[name='+key+']').attr("readonly","readonly");
                    remark.find('#btnRemoveRemark').attr("disabled",true);
                }
                if(key == "system" && value == true){
                    remark.find('input[name="remarks"]').attr("readonly","readonly");
                }
            })
            remark.appendTo($('#remark_list'));
        })
    });

     $('#remarksTypeSelect').change(function(){
        let selected = $(this).children('option:selected').val();
        if(selected == 'SYS'){
            $('#btnAddRemark').attr("disabled",true);
        }else{
            $('#btnAddRemark').attr("disabled",false);
        }
    });

    $('#btnUpdateService').on('click',function () {
        console.log(ticketDetId);
        let arr = new Array();
        $('#service_list').find('form').each(function(index,form){
            let form_arr =$(form).serializeArray();
            let faults = new Array();
            let form_json = {};
            $.map(form_arr, function (n, i) {
                if (n['name'] == 'faults') {
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


    //contact
    $.get('/ticket/contact/'+ticketMasId,function(res){
        $.each(res,function(index,j){
            let contact =$('#tempContact').children().clone();
            $.each(j,function(key,value){
                contact.find('input[name='+key+']').val(value);
            })
            contact.appendTo($('#contact_list'));
        })
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

    $('#btnAddRemark').on('click',function(){
        clearAllMsg();
        if($(this).prev('select').val().length <1){
            showErrorMsg('Please one remark type.');
            return;
        }
        let remark =$('#tempRemark').children().clone();
        remark.find('input[name=remarksType]').val($(this).prev('select').find('option:selected').text());
        remark.appendTo($('#remark_list'));
        $('#btnUpdateRemark').attr('disabled',false);
    });

    $('#btnUpdateRemark').on('click',function(){
        let arr =new Array() ;
        $('#remark_list').find('form').each(function(index,form){
            let form_arr =$(form).serializeArray();
            let form_json = {};
            $.map(form_arr, function (n, i) {
              form_json[n['name']] = n['value'];
            });
            form_json['ticketMasId']=ticketMasId;
            arr.push(form_json);
        })
        $.ajax({
            url:'/ticket/remark/update',
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
    });

    $('.itsm_link').on('click',function(){
//        window.open($(this).data('url'),'Profile','scrollbars=yes,height=600,width=800');
        window.open('https://10.111.7.32/itsm/info/ResourcePoolTab.action?resourceId=309033','Profile','scrollbars=yes,height=600,width=800');
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
            console.log(res);
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
                $.each(res,function(key,val){
                    $('input[name='+ key +']').val(val);
                })
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

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
    service.appendTo($(btn).parent().parent().next('.faults_list'));
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

function removeRemark(btn){
    $(btn).parents('form').remove();
    if($('#remark_list').find('form').length < 1){
        $('#btnUpdateRemark').attr('disabled',true);
    }else{
        $('#btnUpdateRemark').attr('disabled',false);
    }
}