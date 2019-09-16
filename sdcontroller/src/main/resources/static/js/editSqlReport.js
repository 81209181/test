$().ready(function(){
    $('#btnEditSqlRoeport').on("click",function(){
        clearAllMsg();
        $.post('/system/report/updateSqlReport',$('form').serialize(),function(res){
            if (res.success) {
                showInfoMsg("Update success.");
                setTimeout("location.reload()",800);
            } else {
                let responseError = res.feedback ? res.feedback : "Update failed.";
                showErrorMsg(responseError);
            }
        }).fail(function(e){
            let responseError = e.responseText ? e.responseText : "Get failed.";
            showErrorMsg(responseError);
        })
    })
})