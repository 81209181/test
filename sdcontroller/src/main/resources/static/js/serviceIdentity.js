$().ready(function(){

    let ctx = $("meta[name='_ctx']").attr("content");

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
        $.post('/ticket/query/create',$('form').serialize(),function(res){
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
    })


    $('.itsm').on('click',function(){
        window.open($(this).data('url'),'Profile','scrollbars=yes,height=600,width=800');
    })

    $('#btnGetInventory').on('click', function () {
        if (bsn === '') {
            showErrorMsg('No service number!');
            return;
        }
        getInventory(bsn);
    })

    $('#btnRelatedOfferInfo').on('click', function () {
        if (bsn === '') {
            showErrorMsg('No service number!');
            return;
        }

        let ctx = $("meta[name='_ctx']").attr("content");
        window.open(ctx+'/ticket/offer-info?bsn='+bsn,'RelatedOfferInfo','scrollbars=yes,height=400,width=500');
    })

    $('#btnOfferDetailList').on('click', function () {
        if (bsn === '') {
            showErrorMsg('No service number!');
            return;
        }

        let ctx = $("meta[name='_ctx']").attr("content");
        window.open(ctx+'/ticket/offer-detail?bsn='+bsn,'Offer Detail','scrollbars=yes,height=800,width=1200');
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
            if(res.length > 1){
                $.each(res,function(i,val){
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
                $.each(res.pop(),function(key,val){
                     $('form').find('input[name='+ key +']').val(val);
                     if (key == 'serviceNo') {
                         if (val != null) {
                             bsn = val;
                             $('.nora').removeAttr('disabled');
                         }
                     }
                })
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
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}