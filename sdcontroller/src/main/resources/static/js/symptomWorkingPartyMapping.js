$(document).ready(function() {
    initTable();
})

function initTable() {
    $('#symptomWorkingpartymapTable').dataTable({
        "order": [[ 0, "asc" ]],
        ajax: {
            type: "GET",
            url: "/symptom/symptom-workingparty-mapping/list",
            dataSrc: '',
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                } else {
                    showErrorMsg("Cannot load result.");
                }
            }
        },
        columns: [
            {data: 'symptomCode', name: 'symptomCode'},
            {data: 'workingParty', name: 'workingParty'},
            {data: 'serviceTypeCode', name: 'serviceTypeCode'},
        ],
        columnDefs: [
            {
                targets: 3,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-info' onclick='showUpdateModal(\"" + row['symptomCode'] + "\")' ><i class='fas fa-edit'></i>Edit</button>";
                }
            },
            {
                targets: 4,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-danger' onclick='delSymptomGroup(\"" + row['symptomCode'] + "\")' ><i class='fa fa-trash' aria-hidden='true'></i>Delete</button>";
                }
            }
        ]
    });
}

function submit(type){
    clearAllMsg();
    if ($('#dialogbox form').get(0).checkValidity()) {
        if (type === 'create') {
            $.post('/symptom/symptom-workingparty-mapping/create',$('#dialogbox form').serialize(),function(res){
                if (res.success){
                    location.reload();
                } else {
                    showErrorMsg(res.feedback);
                }
            }).fail(function (e){
                $('#dialogbox').modal('hide');
                let responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
                showErrorMsg(responseError);
            });
        } else if (type === 'update') {
            $.post('/symptom/symptom-workingparty-mapping/edit',{
                symptomCode: $("#symptomCode option:selected").val(),
                workingParty: $('#dialogbox form input[name=workingParty]').val(),
                serviceTypeCode: $('#dialogbox form input[name=serviceTypeCode]:checked').val()
            },function(res){
                if (res.success){
                    $('#symptomWorkingpartymapTable').DataTable().ajax.reload();
                    showInfoMsg(res.feedback);
                } else {
                    showErrorMsg(res.feedback);
                }
            }).fail(function (e){
                $('#dialogbox').modal('hide');
                let responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
                showErrorMsg(responseError);
            });
        }
        $('#dialogbox').modal('hide');
    }
    $('#dialogbox form').addClass("was-validated");
}

$('#createMapping').on('click', function() {
    $('#dialogbox').modal('show');
    $('#dialogbox h6').text('Create Mapping');
    $('#symptomCode').attr('disabled', false);
    $('#submit').attr('data-type', 'create');
})

$('#dialogbox').on('hidden.bs.modal', function() {
    $('#dialogbox form').get(0).reset();
})

function showUpdateModal(symptomCode) {
    $.ajax({
        url: '/symptom/symptom-workingparty-mapping/get',
        data: {symptomCode: symptomCode},
        type: 'GET',
        contentType: 'application/json',
        success: function (res){
            $('#dialogbox').modal('show');
            $('#dialogbox h6').text('Update Mapping');
            $("#symptomCode").val(res.symptomCode);
            $('#symptomCode').attr('disabled', true);
            $('#dialogbox form').find('input[name=workingParty]').val(res.workingParty);
            $('#dialogbox form').find('input[value='+res.serviceTypeCode+']').prop('checked', true);
        }
    }).fail(function (e){
        let responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });

    $('#dialogbox form').find('input[name=symptomCode]').prop('readonly', true);
    $('#submit').attr('data-type', 'update');
    $('#dialogbox form').removeClass('was-validated');
}

function delSymptomGroup(symptomCode) {
    $.post('/symptom/symptom-workingparty-mapping/delete', {'symptomCode': symptomCode}, function(res){
        if (res.success){
            location.reload();
        } else {
            showErrorMsg(res.feedback);
        }
    }).fail(function (e){
        let responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });
}