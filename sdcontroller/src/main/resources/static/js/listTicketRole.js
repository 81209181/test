$().ready(function() {
    ajaxGetUserOwnerAuthRole();
    ajaxGetUserRoleWorkgroup();
})

function ajaxGetUserOwnerAuthRole(){
    $('#userOwnerAuthRoleTable').DataTable({
        "order": [[ 0, "asc" ],[2,"asc"]],
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-role/ticket-role-mapping/getUserOwnerAuthRoleList",
            dataSrc: '',
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                }
            }
        },
        columns: [
            { data: 'ownerId' },
            { data: 'authRoleId' },
            { data: 'serviceTypeCode' }
        ],
        columnDefs: [
            {
                targets: 3,
                width: "110px",
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-danger' onclick='delUserOwnerAuthRole(\""+row['ownerId']+"\",\""+row['authRoleId']+"\",\""+row['serviceTypeCode']+"\")'><i class='fa fa-trash' aria-hidden='true'></i>Delete</button>";
                }
            }
        ]
    });
}

function delUserOwnerAuthRole(ownerId, authRoleId, serviceTypeCode) {
    clearAllMsg();
    if (confirm("Are you sure you want to delete this record?")) {
        $.post('/admin/manage-role/ticket-role-mapping/delUserOwnerAuthRole', {'ownerId': ownerId, 'authRoleId': authRoleId, 'serviceTypeCode': serviceTypeCode}, function(res) {
            if (res.success) {
                $('#userOwnerAuthRoleTable').DataTable().ajax.reload();
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

function submit(el) {
    clearAllMsg();
    let dialogbox = $(el).parents('.modal.fade.show');
    let form = $(dialogbox).find('form');
    if (form.get(0).checkValidity()) {
        $.post('/admin/manage-role/ticket-role-mapping/'+dialogbox.attr('id'), form.serialize(), function(res){
            if (res.success){
                $('#'+dialogbox.data('ref')).DataTable().ajax.reload();
            } else {
                showErrorMsg(res.feedback);
            }
        }).fail(function (e){
            dialogbox.modal('hide');
            let responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        });
        dialogbox.modal('hide');
    }
    form.addClass("was-validated");
}

$('.addBtn').on('click', function (){
    $('#'+$(this).data('ref')).modal('show');
})

$('.modal.fade').on('hidden.bs.modal', function() {
    if ($(this).find('form').length > 0) {
        $(this).find('form').get(0).reset();
        $(this).find('form').removeClass("was-validated");
    }
})

function ajaxGetUserRoleWorkgroup(){
    $('#userRoleWorkgroupTable').DataTable({
        "order": [[ 0, "asc" ],[1,"asc"]],
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-role/ticket-role-mapping/getUserRoleWorkgroupList",
            dataSrc: '',
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                }
            }
        },
        columns: [
            { data: 'roleId' },
            { data: 'workgroup' }
        ],
        columnDefs: [
            {
                targets: 2,
                width: "110px",
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-danger' onclick='delUserRoleWorkgroup(\""+row['roleId']+"\",\""+row['workgroup']+"\")'><i class='fa fa-trash' aria-hidden='true'></i>Delete</button>";
                }
            }
        ]
    });
}

function delUserRoleWorkgroup(roleId, workgroup) {
    clearAllMsg();
    if (confirm("Are you sure you want to delete this record?")) {
        $.post('/admin/manage-role/ticket-role-mapping/delUserRoleWorkgroup', {'roleId': roleId, 'workgroup': workgroup}, function(res) {
            if (res.success) {
                $('#userRoleWorkgroupTable').DataTable().ajax.reload();
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