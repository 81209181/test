$(document).ready(function() {
    // search button
    $("#btnSearch").on('click', function(event) {
        event.preventDefault();
        clearAllMsg();
        $('#publicHolidayTable').DataTable().ajax.reload();
    });

    // create button
    $('#btnCreate').on('click', function(){
        $('.reason').modal('show');
    });

    $('#btnSubmit').on('click',function(){
        let form =$('.needs-validation').get(0);
        if(form.checkValidity()){
            $.post('/system/public-holiday/create-public-holiday',{
                publicHoliday : $(form).find('input[name=publicHoliday]').val(),
                description : $(form).find('textarea[name=description]').val(),
            },function(res){
                if(res.success){
                    location.reload();
                }
            }).fail(function(e){
                var responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
                alert(responseError);
            })
        }
        $(form).addClass("was-validated");
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
        ],
        columnDefs: [
            {
                targets: 2,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-danger' onclick='deletePublicHoliday(\"" + row['publicHoliday'] + "\",\"" + row['description'] +"\")' ><i class='fas fa-edit'></i>Delete</button>";
                }
            }
        ]
    });
});

// delete button
function deletePublicHoliday(publicHoliday, description) {
    if (confirm("Are you sure you want to delete this record?")) {
        $.post('/system/public-holiday/delete-public-holiday', {
            publicHoliday: publicHoliday,
            description: description,
        }, function (res) {
            if (res.success) {
                showInfoMsg("delete success.");
                setTimeout(function () {
                    location.reload();
                }, 1000)
            }
        }).fail(function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    }
}

function copyToClipBoard() {
    const btn = document.querySelector('#btnPbExportCp');
    btn.addEventListener('click', () => {
        const input = document.querySelector('#textareaExport');
        input.select();
        if (document.execCommand('copy')) {
            document.execCommand('copy');
            $('.export').modal('hide');
            showInfoMsg('Copied successfully.');
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
        let array = '';
        try {
            array = JSON.parse($("#textareaImport").val());
            for (item of array) {
                    if (JSON.stringify(item) === '{}') {
                        showErrorMsg('can not import empty data.');
                        $('.import').modal('hide');
                        return;
                    }
                }
        } catch (e) {
            showErrorMsg('Cannot import invalid data.');
            $('.import').modal('hide');
            return;
        }
        let textarea = JSON.stringify(array);
        $.ajax({
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json;charset=utf-8',
            url : '/system/public-holiday/ajax-add-public-holiday',
            data: textarea,
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