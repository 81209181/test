$().ready(function(){
    $('#btnEditSqlRoeport').on("click",function(){
        clearAllMsg();
        $.post('/system/report/updateSqlReport',$('form').serialize(),function(res){
            showInfoMsg(res);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
})