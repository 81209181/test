let input = {};
$().ready(function(){

    $('#btnCreateSymptom').on('click',function(){
        clearAllMsg();
        let ctx = $("meta[name='_ctx']").attr("content");
        input['symptomCode'] = $('input[name="symptomCode"]').val();
        input['symptomGroupCode'] = $("#symptomGroupCode").val();
        input['symptomDescription'] = $('input[name="symptomDescription"]').val();
        input['voiceLineTest'] = $("#voiceLineTest").val();
        input['apptMode'] = $("#apptMode").val();
        let serviceTypes = new Array();
        $('input[name="serviceTypeCode"]:checked').each(function (j) {
            if (j >= 0) {
                serviceTypes.push($(this).val());
            }
        });
        input["serviceTypeList"] = serviceTypes;

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/symptom/ifSymptomDescExist',
            data: JSON.stringify(input),
            dataType: 'json',
            cache: false,
            success: function (res) {
                if (res.success) {
                    if (res.feedback === 'warningMsg') {
                        $('.continue').modal('show');
                    } else {
                        createSymptom(input);
                    }
                } else {
                    showErrorMsg(res.feedback);
                }
            },
            error: function (e) {
                var responseError = e.responseText ? e.responseText : "Create failed.";
                console.log("ERROR : ", responseError);
                showErrorMsg(responseError);
            }
        })
    })

})

$('.continue-btn').on('click', function (){
    $('.continue').modal('hide');
    createSymptom(input);
})

function createSymptom(input) {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/symptom/post-create-symptom',
        data: JSON.stringify(input),
        dataType: 'json',
        cache: false,
        success: function (res) {
            if (res.success) {
                showInfoMsg(res.feedback);
            } else {
                showErrorMsg(res.feedback);
            }
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Create failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }
    })
}