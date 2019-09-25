$().ready(function(){

    $('#serviceTypeCode').val($('#oldServiceTypeCode').val());
    $('#symptomGroupCode').val($('#oldSymptomGroupCode').val());

    $('#btnSubmit').hide();

    $('#btnEdit').on('click',function(){
        $('#btnEdit').hide();
        $('#btnSubmit').show();
        $('#serviceTypeCode').attr("disabled", false);
        $('#symptomGroupCode').attr("disabled", false);
        $('#serviceTypeCode').removeAttr("readonly");
        $('#symptomGroupCode').removeAttr("readonly");
    });

    $('#btnSubmit').on('click',function(){
        clearAllMsg();
        $.post('/symptom/post-edit-symptom-mapping',$('form').serialize(),function(res){
            if (res.success) {
                $('#oldServiceTypeCode').val($('#serviceTypeCode').val());
                $('#oldSymptomGroupCode').val($('#symptomGroupCode').val());
                showInfoMsg("Update success.");
            } else {
                showErrorMsg(res.feedback);
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Update failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    });
})