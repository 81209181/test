$(document).ready(function() {
    // load request
    createOptHistDataTable();
});

function getCurrentHashedRequestId(){
    return $("#target-request-id").val();
}

function createOptHistDataTable(){
    $('#request-opt-hist-table').DataTable({
        paging:   false,
        searching:false,
        ajax: {
            type: "GET",
            async: false,
            contentType: "application/json",
            url: "/user/access-request/ajax-get-opt-hist?hashedRequestId=" + getCurrentHashedRequestId(),
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load operation history.");
            }
        },
        columns: [
            { width: '30%', data: 'createdate' },
            { width: '20%', data: 'userId' },
            { width: '50%', data: 'detail' }
        ],
        columnDefs: [
            {
                targets: 0,
                data: "createdate",
                render: function ( createdate, type, row, meta ) {
                    return createdate==null ? null : createdate.replace('T', ' ');
                }
            }
        ]
    });
}

