$(document).ready(function() {
    // load user role
    ajaxGetUserRole();
    ajaxGetRolePath();

    // click event
    $('#btnUpdateUserRole').on("click",function(){
        clearAllMsg();
        ajaxUpdateUserRole();
    })
});

function ajaxGetUserRole(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/manage-role/edit-user-role/getUserRole?roleId="+$("#roleId").val(),
        success: function (data) {
            // fill in form
            $("#parentRoleId").val(data.parentRoleId);
            $("#roleDesc").val(data.roleDesc);
            $("#status").val(data.status);
            $("#abstractFlag").val(data.abstractFlag);
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }
    });
}

function ajaxGetRolePath(){
    $('#pathCtrlTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-role/edit-user-role/getParentRolePathByRoleId?roleId="+$("#roleId").val(),
            dataSrc: '',
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                }
            }
        },
        columns: [
            { data: 'roleId' },
            { data: 'path' },
            { data: 'description' }
        ],
        columnDefs: [
            {
                targets: 3,
                width: "110px",
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-danger' onclick='delPathCtrl(\""+row['roleId']+"\","+row['pathCtrlId']+")'><i class='fa fa-trash' aria-hidden='true'></i>Delete</button>";
                }
            }
        ]
    });
}

function ajaxUpdateUserRole(){
    $.post('/admin/manage-role/edit-user-role',$('form').serialize(),function(data){
        if(data.success){
            showInfoMsg("Updated user role.");
        }else{
            showErrorMsg(data.feedback);
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });
}

$('#addPathCtrlBtn').on('click', function() {
    $('#dialogbox').modal('show');
});

function submit(){
    clearAllMsg();
    if ($('#dialogbox form').get(0).checkValidity()) {
        $.post('/admin/manage-role/edit-user-role/createUserRoleCtrl',$('#dialogbox form').serialize(),function(res){
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
        $('#dialogbox').modal('hide');
    }
    $('#dialogbox form').addClass("was-validated");
    $('.was-validated .bootstrap-select + div.invalid-feedback').addClass('display-feedBack');
}

function delPathCtrl(roleId, pathCtrlId) {
    clearAllMsg();
    if (confirm("Are you sure you want to delete this record?")) {
        $.post('/admin/manage-role/edit-user-role/delUserRoleCtrl', {'roleId': roleId, 'pathCtrlId': pathCtrlId}, function(res) {
            if (res.success) {
                location.reload();
            } else {
                showErrorMsg(res.feedback);
            }
        }).fail(function (e) {
            let responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    }
}

$('#dialogbox').on('hidden.bs.modal', function() {
    $('#dialogbox form').get(0).reset();
    $('#dialogbox form .selectpicker').selectpicker('deselectAll');
    $('.bootstrap-select + div.invalid-feedback').removeClass('display-feedBack');
    $('#dialogbox form').removeClass("was-validated");
})

$('.selectpicker').on('changed.bs.select', function () {
    $('.was-validated .bootstrap-select + div.invalid-feedback').removeClass('display-feedBack');
})