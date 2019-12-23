$(document).ready(function() {
    // search button
    $("#search-public-holiday-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#publicHolidayTable').DataTable().ajax.reload();
    });

    exportPublicHoliday();

    copyToClipBoard();

    showImportPublicHoliday();

    // search result
    $('#publicHolidayTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/public-holiday/ajax-page-public-holiday",
            dataSrc: 'data',
            data: function(d){
                d.year = $("#public-holiday-search-year").val();
            },
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                } else {
                    showErrorMsg("Cannot load result.");
                }
            }
        },
        columns: [
            { data: 'publicHoliday' },
            { data: 'description' }
        ]
    });
});

function copyToClipBoard() {
    const btn = document.querySelector('#btnPbExportCp');
    btn.addEventListener('click', () => {
        const input = document.querySelector('#textareaExport');
        input.select();
        if (document.execCommand('copy')) {
            document.execCommand('copy');
            alert('Copy success.');
        }
    })
}

function exportPublicHoliday() {
    $("#btnPbExport").on('click', function () {
        $('.export').on('hide.bs.modal', function () {
            $("#textareaExport").val("");
        })
        getAllPhList();
        $(".export").modal('show');
    })
}

function getAllPhList() {
    $.ajax({
        type: 'GET',
        url: '/system/public-holiday/ajax-list-public-holiday',
        dataType: 'text',
        success: function (res) {
            var jsonPretty = JSON.stringify(JSON.parse(res), null, 2);
            $('#textareaExport').val(jsonPretty);
        },
    }).fail(function (e) {
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        $('.export').modal('hide');
        showErrorMsg(responseError);
    })
}

function showImportPublicHoliday() {
    $("#btnPbImport").on('click', function () {
        $('.import').on('hide.bs.modal', function () {
            $("#textareaImport").val("");
        })
        importPublicHoliday();
        $('.import').modal('show');
    })
}

function importPublicHoliday() {
    $("#btnPbImportSb").on('click', function () {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json;charset=utf-8',
            url : '/system/public-holiday/ajax-add-public-holiday',
            data: JSON.stringify(JSON.parse($("#textareaImport").val())),
            success: function (res) {
                if (res.success) {
                    $('.import').modal('hide');
                    showInfoMsg(res.feedback);
                }
            }
        }).fail(function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            $('.import').modal('hide');
            showErrorMsg(responseError);
        })
    })
}