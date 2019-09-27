$().ready(function(){

    let ctx = $("meta[name='_ctx']").attr("content");

    $('#btnSearchReset').on('click',function(){
        $('#searchKey').val('');
        $('#searchValue').val('');
        $('form').get(0).reset();
    })

    $('#btnSearchCustomerNext').on('click',function(){
        clearAllMsg();
        let custCode =$('input[name=custCode]').val();
        if(custCode.length < 1){
            showErrorMsg('Please input customer code.');
            return;
        }
        $.post('/ticket/query/create',$('form').serialize(),function(res){
            $(location).attr('href',ctx+'/ticket/'+ res);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

    $('#btnSearchInfo').on('click',function(){
        let searchKey=$('#searchKey');
        let searchValue=$('#searchValue');
        clearAllMsg();
        $('tbody').empty();
        $('form').get(0).reset();
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
        $.post('/ticket/searchCustomer',{
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
                })
                $('.modal').modal('hide');
            });
        })


    })

})