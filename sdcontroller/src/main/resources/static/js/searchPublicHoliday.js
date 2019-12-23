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

    // search result
    $('#publicHolidayTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/public-holiday/ajax-list-public-holiday",
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