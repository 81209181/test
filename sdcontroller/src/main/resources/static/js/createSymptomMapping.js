$().ready(function(){

    $('#btnCreateSymptomMapping').on('click',function(){
        clearAllMsg();
        $.post('/symptom/post-create-symptom-mapping',$('form').serialize(),function(res){
            if (res.success) {
                showInfoMsg("Create success.");
            } else {
                showErrorMsg(res.feedback);
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Create failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    })

})