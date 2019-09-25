$().ready(function(){

    $('#btnCreateSymptomMapping').on('click',function(){
        clearAllMsg();
        let ctx = $("meta[name='_ctx']").attr("content");
        $.post('/symptom/post-create-symptom-mapping',$('form').serialize(),function(res){
            if (res.success) {
                $(location).attr('href',ctx+'/symptom/edit-symptom-mapping?'+$('form').serialize());
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