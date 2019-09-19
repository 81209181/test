$().ready(function(){

    $.get('/ticket/contact/'+ticketMasId,function(res){
        $.each(res,function(index,j){
            let contact =$('#tempContact').children().clone();
            $.each(j,function(key,value){
                contact.find('input[name='+key+']').val(value);
            })
            contact.appendTo($('#contact_list'));
        })
    });

    var ticketDetId = "";

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
            })
            remark.appendTo($('#remark_list'));
        })
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
        remark.find('input[name=remarksType]').val($(this).prev('select').find('option:selected').val());
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

    $('#btnSearchService').on('click',function(){
        let modal_table_body =$('.modal').find('tbody');
        modal_table_body.empty();
        $.post('/ticket/searchCustomer',{
            searchKey : 'bes_custCode',
            searchValue : customerCode
        },function(res){
            $.each(res,function(i,val){
                let tr ='<tr class="text-center"></tr>';
                modal_table_body.append(tr);
                modal_table_body.find('tr:last').data('info',val);
                modal_table_body.find('tr:last')
                                .append('<td style="width:6%"><input type="radio"></td>')
                                .append('<td>'+val.custName+'</td>')
                                .append('<td>'+val.statusDesc+'</td>');
            })
            $('.modal').modal('show');
        }).fail(function(e){
          var responseError = e.responseText ? e.responseText : "Get failed.";
          console.log("ERROR : ", responseError);
          modal_table_body.append('<tr class="text-center"><td></td><td colspan=2 >no record found.</td></tr>')
          $('.modal').modal('show');
        })
    })

    $('#btnApplyProduct').on('click',function(){
        let tr =$(this).parent().find('input:checked').parents('tr');
        if(tr.length<1){
            alert('Please select one product.');
            return;
        }
        let product_info = tr.data('info');
        $('#service').find('input[name=serviceCode]').val(product_info.serviceNo);
        $('#service').find('input[name=serviceType]').val(product_info.serviceType);
        if(product_info.url){
            $('.itsm_link').data('url',product_info.url);
            $('.itsm_link').show();
        }
        $('.modal').modal('hide');
    })

    $('.itsm_link').on('click',function(){
        window.open($('.itsm_link').data('url'),'Profile','scrollbars=yes,height=600,width=800');
    })

    $.post('/ticket/searchCustomer',{
        searchKey : 'bes_custCode',
        searchValue : customerCode
    },function(res){
        if(res.length == 1){
            $.each(res[0],function(key,value){
                if(key =='serviceNo'){
                    $('#service').find('input[name=serviceCode]').val(value);
                }else{
                    $('#service').find('input[name='+key+']').val(value);
                }
            })
        }
    }).fail(function(e){
      var responseError = e.responseText ? e.responseText : "Get failed.";
      console.log("ERROR : ", responseError);
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