$().ready(function(){

    $('#btnSearchReset').on('click',function(){
        $('#searchKey').val('');
        $('#searchValue').val('');
        $('form').get(0).reset();
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
        $.post('/fault/search',{
            searchKey : searchKey.val().trim(),
            searchValue : searchValue.val().trim()
        },function(res){
            $.each(res,function(i,val){
                let tr ='<tr class="text-center"></tr>';
                $('tbody').append(tr);
                $('tbody>tr:last').data('info',val);
                $('tbody>tr:last').append('<th><input type="radio" name="product"></th>')
                    .append('<th>'+val.tenantId+'</th>')
                    .append('<th>'+val.serviceNo+'</th>')
                    .append('<th>'+val.custName+'</th>')
                    .append('<th>'+val.offerName+'</th>')
                    .append('<th>'+val.statusDesc+'</th>')
                    .append('<th><a class="text-secondary goNewWin" style="cursor:pointer"><i class="fa fa-table" aria-hidden="true"></i></a></th>');
            });
            $('.modal').modal('show');
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }).then(function(){
            $('.goNewWin').on('click',function(){
                window.open($(this).parent().parent().data('info').url,'Profile','scrollbars=yes,height=600,width=800');
            })
            $('#btnApplyProduct').on('click',function(){
                $.each($('tbody').find('input:checked').parent().parent().data('info'),function(i,val){
                    $('form').find('input[name='+i+']').val(val);
                })
                $('.modal').modal('hide');
            });
        })


    })

})