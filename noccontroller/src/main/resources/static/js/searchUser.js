$(document).ready(function() {
    // search button
    $("#search-user-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#searchUserTable').DataTable().ajax.reload();
    });

    // search result
    $('#searchUserTable').DataTable({
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
                // search input
                d.userId = $("#user-search-user-id").val();
                d.name = $("#user-search-name").val();
                d.email = $("#user-search-email").val();
                d.userGroupId = $("#user-search-user-group").val();
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
            { data: 'email' },
            { data: 'name' },
            { data: 'status' },
            { data: 'userId' }
        ],
        columnDefs: [ {
            targets: 3,
            data: "userId",
            render: function ( userId, type, row, meta ) {
                var ctx = $("meta[name='_ctx']").attr("content");
                var link = ctx + "/admin/manage-user/edit-user?userId=" + userId;
                return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Edit</a>';
            }
        } ]
    });
});