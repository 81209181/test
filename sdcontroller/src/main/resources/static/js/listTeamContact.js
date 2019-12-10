$(document).ready(function() {
    $('#teamContactTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-user/ajax-search-user",
            dataSrc: 'data',
            data: function(d){
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
            { data: 'userId' },
            { data: 'name' },
            { data: 'email' },
            { data: 'primaryRoleId' },
        ]
    });
});