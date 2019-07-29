$(document).ready(function () {
    $("#edit-company-form").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        ajaxUpdateCompany();
    });
});

function ajaxUpdateCompany() {
    var input = {}
    input["companyId"] = $("#companyId").val();
    input["name"] = $("#companyName").val();
    input["remarks"] = $("#companyRemarks").val();

    $("#btn-update-company").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/admin/manage-company/edit-company",
        data: JSON.stringify(input),
        dataType: 'json',
        cache: false,
        success: function (data) {
            $("#btn-update-company").prop("disabled", false);

            clearAllMsg();
            if(data.success){
                showInfoMsg("Updated company info.");
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Update failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
            $("#btn-update-company").prop("disabled", false);
        }
    });

}