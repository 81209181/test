$().ready(function(){

    $('#btnCreateSqlRoeport').on('click',function(){
        clearAllMsg();
        var form = $('form').serializeArray();
        var reportName = $('#reportName').val();
        let ctx = $("meta[name='_ctx']").attr("content");
        $.post('/report/createSqlReport',$('form').serialize(),function(res){
            $(location).attr('href',ctx+'/report/edit-sql-report?reportId='+reportId);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
})