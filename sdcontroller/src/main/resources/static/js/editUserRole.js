$(document).ready(function() {
    // load user
    ajaxGetUser();

    // click event
    $('#btnUpdateUserRole').on("click",function(){
        clearAllMsg();
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
        })
    })
});

function ajaxGetUser(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/manage-role/edit-user-role/getUserRole?roleId="+$("#roleId").val(),
        success: function (data) {
            // fill in form
            $("#parentRoleId").val(data.parentRoleId);
            $("#roleDesc").val(data.roleDesc);
            $("#status").val(data.status);
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }
    });
}

