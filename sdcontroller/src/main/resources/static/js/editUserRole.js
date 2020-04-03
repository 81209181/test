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
        ]
    });
}

function ajaxUpdateUserRole(){
    let serviceTypeList = $('input[name=serviceTypeList]:checked');
    if (serviceTypeList.length < 1) {
        showErrorMsg("Empty service type.");
        return;
    }

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
