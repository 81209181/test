$(document).ready(function() {
    // search button
    $("#search-team-head-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#teamContactTable').DataTable().ajax.reload();
    });

    // search result
    $('#teamContactTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-user/ajax-list-team-contact",
            dataSrc: 'data',
            data: function(d){
                d.teamHead = $("#teamHead").val();
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
            { data: 'teamHead' },
            { data: 'userId' },
            { data: 'name' },
            { data: 'email' },
            { data: 'domainEmail' }
        ]
    });
});