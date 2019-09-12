$().ready(function(){

    $('#btnCreateSqlRoeport').on('click',function(){
        clearAllMsg();
        var form = $('form').serializeArray();
        var reportName = $('#reportName').val();
        let ctx = $("meta[name='_ctx']").attr("content");
        $.post('/system/report/createSqlReport',$('form').serialize(),function(res){
            $(location).attr('href',ctx+'/system/report/edit-sql-report?reportId='+res.reportId);
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })
})