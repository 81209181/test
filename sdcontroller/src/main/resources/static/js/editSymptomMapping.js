$().ready(function(){

    ajaxGetSymptom();

    $('#btnSubmit').on('click',function(){
        clearAllMsg();
        ajaxUpdateSymptom();
    });
})

function ajaxGetSymptom() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/symptom/edit-symptom-mapping/get-symptom?symptomCode="+$("#symptomCode").val(),
        success: function (data) {
            // fill in form
            $("#symptomDescription").val(data.symptomDescription);
            $("#symptomGroupCode").val(data.symptomGroupCode);
            $("#voiceLineTest").val(data.voiceLineTest);
            $("#allowAppt").val(data.allowAppt);
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }
    });
}

function ajaxUpdateSymptom() {
    clearAllMsg();
    var input = {};
    input["oldSymptomCode"] = $("#oldSymptomCode").val();
    input["symptomCode"] = $("#symptomCode").val();
    input["symptomGroupCode"] = $("#symptomGroupCode").val();
    input["symptomDescription"] = $("#symptomDescription").val();
    input['voiceLineTest'] = $("#voiceLineTest").val();
    input['allowAppt'] = $("#allowAppt").val();

    let serviceTypes = new Array();
    $("input[name='serviceTypeCode']:checked").each(function (j) {
        if (j >= 0) {
            serviceTypes.push($(this).val());
        }
    });
    input["serviceTypeList"] = serviceTypes;

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/symptom/post-edit-symptom-mapping",
        data: JSON.stringify(input),
        dataType: 'json',
        cache: false,
        success: function (data) {
            if(data.success){
                showInfoMsg("Updated Symptom Mapping.");
                ajaxGetSymptom();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg(e);
        }
    });
}