let ctx = $("meta[name='_ctx']").attr("content");

$().ready(function(){

    $(document).keydown(function(event){
        if(event.keyCode==13){
           $('#btnSearchInfo').click();
        }
    })

    let bsn = "";

    $('#btnSearchReset').on('click',function(){
        $('#searchKey').val('');
        $('#searchValue').val('');
        $('form').get(0).reset();
    })

    $('#btnSearchCustomerNext').on('click',function(){
        console.log($('form').serialize());
        clearAllMsg();
        let custCode =$('input[name=custCode]').val();
        if(custCode.length < 1){
            showErrorMsg('Please input customer code.');
            return;
        }
        $.post('/ticket/service-identity/checkPendingOrder',$('form').serialize(),function(res){
            if (res.success) {
                createTicket();
            } else {
                showErrorMsg("There is pending order (" + res.data + ") of the service in WFM. Continue to create ticket?<button class='btn btn-primary mt-1 ml-2' onclick='createTicket()'>Yes</button><button class='btn btn-primary mt-1 ml-2' onclick='clearAllMsg()'>No</button>");
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        });
    })


    $('.itsm').on('click',function(){
        window.open($(this).data('url'),'Profile','scrollbars=yes,height=600,width=800');
    })

    $('#btnGetInventory').on('click', function () {
        if (bsn === '') {
            showErrorMsg('No service number!');
        } else {
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
    })

    $('#btnRelatedOfferInfo').on('click', function () {
        if (bsn === '') {
            showErrorMsg('No service number!');
            return;
        }else {
            let relatedBsn =  $("#relatedBsn").val();
            let key = relatedBsn=== '' ? bsn : relatedBsn;
            window.open(ctx+'/ticket/offer-info?bsn='+key,'RelatedOfferInfo','scrollbars=yes,height=800,width=1200');
        }
    })

    $('#btnOfferDetailList').on('click', function () {
        if (bsn === '') {
            showErrorMsg('No service number!');
        } else {
            let relatedBsn =  $("#relatedBsn").val();
            let key = relatedBsn=== '' ? bsn : relatedBsn;
            window.open(ctx+'/ticket/offer-detail?bsn='+key,'OfferDetailList','scrollbars=yes,height=800,width=1200');
        }
    })

    $('#btnSearchInfo').on('click',function(){
        let searchKey=$('#searchKey');
        let searchValue=$('#searchValue');
        clearAllMsg();
        $('tbody').empty();
        $('form').get(0).reset();
        $('input[name=searchKey]').val(searchKey.val());
        $('input[name=searchValue]').val(searchValue.val());
        $('.itsm').attr('disabled',true);
        if(searchKey.val().trim().length <1){
            searchKey.attr('class','custom-select is-invalid');
            searchKey.focus();
            return;
        }else{
            searchKey.attr('class','custom-select');
        }
        if(searchValue.val().trim().length<1){
            searchValue.attr('class','form-control is-invalid');
            searchValue.focus();
            return;
        }else{
            searchValue.attr('class','form-control');
        }
        $.post('/ticket/search-service',{
            searchKey : searchKey.val().trim(),
            searchValue : searchValue.val().trim()
        },function(res){
            let service_list = res.list;
            if(service_list.length > 1){
                $.each(service_list,function(i,val){
                    let tr ='<tr class="text-center"></tr>';
                    $('tbody').append(tr);
                    $('tbody tr:last').data('info',val);
                    $('tbody tr:last').append('<td><input type="radio"></td>')
                        .append('<td>'+val.custCode+'</td>')
                        .append('<td>'+val.custName+'</td>')
                        .append('<td>'+val.serviceType+'</td>')
                        .append('<td>'+val.serviceNo+'</td>')
                });
                $('.modal').modal('show');
            }else{
                $.each(service_list.pop(),function(key,val){
                     $('form').find('input[name='+ key +']').val(val);
                     if (key == 'serviceNo') {
                         if (val != null) {
                             bsn = val;
                             $('.nora').removeAttr('disabled');
                         }
                     }
                })
            }
            if(res.errorMsg){
               showErrorMsg(res.errorMsg);
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }).then(function(){
            $('#btnApplyProduct').on('click',function(){
                $.each($('tbody').find('input:checked').parent().parent().data('info'),function(i,val){
                    $('form').find('input[name='+i+']').val(val);
                    if(i == 'url'){
                        if(val !=null){
                            $('.itsm').data('url',val);
                            $('.itsm').removeAttr('disabled');
                        }
                    }
                    if(i == 'serviceNo') {
                        if(val !=null) {
                            $('.nora').removeAttr('disabled');
                        }
                    }
                })
                $('.modal').modal('hide');
            })
        })
    })

})

function createTicket(){
    $.post('/ticket/service-identity/createQueryTicket',$('form').serialize(),function(res){
        if (res.success) {
            $(location).attr('href',ctx+'/ticket?ticketMasId='+ res.data);
        } else {
            var responseError = "The service number already exists in Ticket-";
            let ticketMasIds = res.data;
            $.each(ticketMasIds,function(index,j){
                if (index > 0) {
                    responseError += "/ ";
                }
                responseError += "<a href='"+ctx+"/ticket?ticketMasId="+j.ticketMasId+"'>"+j.ticketMasId+"</a> ";
            });
            showErrorMsg(responseError);
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}