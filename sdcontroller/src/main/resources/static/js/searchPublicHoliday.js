$(document).ready(function() {
    // search button
    $("#btnSearch").on('click', function (event) {
        searchPublicHolidayTableByYear(event);
    });

    // create button
    $('#btnCreate').on('click', function(){
        $('.reason').modal('show');
    });

    $('#btnSubmit').on('click',function(){
        let form =$('.needs-validation').get(0);
        if(form.checkValidity()){
            clearAllMsg();
            $.post('/system/public-holiday/create-public-holiday',{
                publicHoliday : $(form).find('input[name=publicHoliday]').val(),
                description : $(form).find('textarea[name=description]').val(),
            },function(res){
                if (res.success) {
                    $('#publicHolidayTable').DataTable().ajax.reload();
                } else {
                    showErrorMsg(res.feedback);
                }
            }).fail(function(e){
                var responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
                showErrorMsg(responseError);
            })
            $('.reason').modal('hide');
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
        pageLength: 50,
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
                    let description = row['description'].replace("'", "&#39;");
                    return "<button class='btn btn-danger' onclick='deletePublicHoliday(\"" + row['publicHoliday'] + "\",\"" + description +"\")' ><i class='fas fa-edit'></i>Delete</button>";
                }
            }
        ]
    });
});

function searchPublicHolidayTableByYear(event) {
    event.preventDefault();
    clearAllMsg();
    $('#publicHolidayTable').DataTable().ajax.reload();
}

function triggerSearch(event) {
    if (event.keyCode === 13) {
        searchPublicHolidayTableByYear(event);
    }
}

// delete button
function deletePublicHoliday(publicHoliday, description) {
    if (confirm("Are you sure you want to delete this record?")) {
        clearAllMsg();
        $.post('/system/public-holiday/delete-public-holiday', {
            publicHoliday: publicHoliday,
            description: description,
        }, function (res) {
            if (res.success) {
                showInfoMsg("delete success.");
                $('#publicHolidayTable').DataTable().ajax.reload();
            }
        }).fail(function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    }
}

function copyToClipBoard() {
    clearAllMsg();
    const btn = document.querySelector('#btnPbExportCp');
    btn.addEventListener('click', () => {
        const input = document.querySelector('#textareaExport');
        input.select();
        if (document.execCommand('copy')) {
            document.execCommand('copy');
            $('.export').modal('hide');
            showInfoMsg('Copied successfully.', true);
        }
    })
}

function exportPublicHoliday() {
    $("#btnPbExport").on('click', function () {
        clearAllMsg();
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
    clearAllMsg();
    $("#btnPbImportSb").one('click', function () {
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
                    setTimeout(function () {
                        $('#publicHolidayTable').DataTable().ajax.reload();
                    }, 1000)
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