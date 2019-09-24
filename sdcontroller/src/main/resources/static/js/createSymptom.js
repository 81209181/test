$().ready(function(){

    $('#btnCreateSymptom').on('click',function(){
        clearAllMsg();
        $.post('/symptom/createSymptom',$('form').serialize(),function(res){
            if (res.success) {
                showInfoMsg("Create success.");
            } else {
                showErrorMsg(res);
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Create failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

})