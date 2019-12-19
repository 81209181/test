$(document).ready(function() {
    // search button
    $("#search-public-holiday-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#publicHolidayTable').DataTable().ajax.reload();
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
        ]
    });
});