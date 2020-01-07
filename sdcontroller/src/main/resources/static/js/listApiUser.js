$(document).ready(function() {
    copyToClipBoard();

    $('#userGroupTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/manage-api/ajax-list-api-user",
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
            { data: 'name' }
        ],
        columnDefs: [
            {
                targets: 1,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-info' onclick='ajaxGetApiKey(\"" + row['name'] + "\")' ><i class='fas fa-edit'></i>Show</button>";
                }
            },
            {
                targets: 2,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-info' onclick='ajaxRegenerate(\"" + row['name'] + "\")' ><i class='fas fa-edit'></i>Re-generate</button>";
                }
            }
        ]
    });
});

function ajaxGetApiKey(apiName) {
    $('.apiKey').on('hide.bs.modal', function () {
        $("#textareaApiKey").val("");
    })

    $.post('/system/manage-api/getAuthorization',{
        apiName : apiName,
    },function(data){
        $('#textareaApiKey').val(data);
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        $('.apiKey').modal('hide');
        showErrorMsg(responseError);
    });

    $(".apiKey").modal('show');
}

function ajaxRegenerate(apiName) {
    if (confirm("Are you sure you want to re-generate?")) {
        $.post('/system/manage-api/regenerateKey',{
            apiName : apiName,
        }, function (res) {
            if (res.success) {
                showInfoMsg("Re-generate success.");
            }
        }).fail(function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    }
}

function copyToClipBoard() {
    const btn = document.querySelector('#btnCopy');
    btn.addEventListener('click', () => {
        const input = document.querySelector('#textareaApiKey');
        input.select();
        if (document.execCommand('copy')) {
            document.execCommand('copy');
            $('.apiKey').modal('hide');
            showInfoMsg("Copied successfully.");
        }
    })
}