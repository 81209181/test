$(document).ready(function() {
    // click event
    $('#btnEditServiceType').on("click",function(){
        clearAllMsg();
        ajaxEditServiceType();
    })
});


function ajaxEditServiceType(){
    let userRoleId = $('input[name=userRoleId]:checked');
    if (userRoleId.length < 1) {
        showErrorMsg("Empty user role.");
        return;
    }

    $.post('/system/service-type/edit-service-type-mapping',$('form').serialize(),function(data){
        if(data.success){
            showInfoMsg("Updated service type mapping.");
        }else{
            showErrorMsg(data.feedback);
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });
}
