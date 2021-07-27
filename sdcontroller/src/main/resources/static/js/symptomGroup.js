$(document).ready(function (){
    initTable();
})

function initTable() {
    $('#symptomGroupTable').DataTable({
        "order": [[ 0, "asc" ]],
        ajax: {
            type: "GET",
            url: "/symptom/symptom-group/list",
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
            {data: 'symptomGroupCode', name: 'symptomGroupCode'},
            {data: 'symptomGroupName', name: 'symptomGroupName'},
        ],
        columnDefs: [
            {
                targets: 2,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-info' onclick='showUpdateModal(\"" + row['symptomGroupCode'] + "\")' ><i class='fas fa-edit'></i>Edit</button>";
                }
            },
            {
                targets: 3,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-danger' onclick='delSymptomGroup(\"" + row['symptomGroupCode'] + "\")' ><i class='fa fa-trash' aria-hidden='true'></i>Delete</button>";
                }
            }
        ]
    });
}

function submit(type){
    clearAllMsg();
    if ($('#dialogbox form').get(0).checkValidity()) {
        if (type === 'create') {
            $.post('/symptom/symptom-group/create',$('#dialogbox form').serialize(),function(res){
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
            $.post('/symptom/symptom-group/edit',$('#dialogbox form').serialize(),function(res){
                if (res.success){
                    $('#symptomGroupTable').DataTable().ajax.reload();
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

$('#createSymptomGroup').on('click', function() {
    $('#dialogbox').modal('show');
    $('#dialogbox h6').text('Create Symptom Group');
    $('#dialogbox form').find('input[name=symptomGroupCode]').prop('readonly', false);
    $('#submit').attr('data-type', 'create');
})

function showUpdateModal(symptomGroupCode) {
    $.ajax({
        url: '/symptom/symptom-group/get',
        data: {symptomGroupCode: symptomGroupCode},
        type: 'GET',
        contentType: 'application/json',
        success: function (res){
            $('#dialogbox').modal('show');
            $('#dialogbox h6').text('Update Symptom Group');
            $('#dialogbox form').find('input[name=symptomGroupCode]').val(res.symptomGroupCode);
            $('#dialogbox form').find('input[name=symptomGroupName]').val(res.symptomGroupName);
            res.roleList.forEach(role => {
                $('#'+role+'').prop('checked', true);
            })
        }
    }).fail(function (e){
        let responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })

    $('#dialogbox form').find('input[name=symptomGroupCode]').prop('readonly', true);
    $('#submit').attr('data-type', 'update');
}

$('#dialogbox').on('hidden.bs.modal', function() {
    $('#dialogbox form').get(0).reset();
})

function delSymptomGroup(symptomGroupCode) {
    $.post('/symptom/symptom-group/delete', {'symptomGroupCode': symptomGroupCode}, function(res){
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