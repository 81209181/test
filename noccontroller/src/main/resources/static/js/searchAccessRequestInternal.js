$(document).ready(function() {
    // search button
    $("#search-access-request").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#searchAccessRequestTable').DataTable().ajax.reload();
    });

    // search result
    // noinspection JSUnusedLocalSymbols
    $('#searchAccessRequestTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/user/access-request/ajax-search-request",
            dataSrc: 'data',
            data: function(d){
                // search input
                d.hashedRequestId = $("#search-hashed-request-id").val();
                d.companyName = $("#search-access-company-name").val();
                d.status = $("#search-access-status").val();
                d.visitDateFrom = $("#search-access-visit-from").val();
                d.visitDateTo = $("#search-access-visit-to").val();
                d.visitLocation = $("#search-access-visit-loc").val();
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
            { data: 'hashedRequestId' },
            { data: 'status' },
            { data: 'visitDate' },
            { data: 'visitTimeFrom' },
            { data: 'visitTimeTo' },
            { data: 'visitLocation' },
            { data: 'createdate' },
            { data: 'visitorCount' },
            { data: 'companyName' },
            { data: 'hashedRequestId' }
        ],
        columnDefs: [
            {
                targets: 6,
                data: "createdate",
                render: function ( createdate, type, row, meta ) {
                    return createdate==null ? null : createdate.replace('T', ' ');
                }
            },{
                targets: 9,
                data: "hashedRequestId",
                render: function ( hashedRequestId, type, row, meta ) {
                    let ctx = $("meta[name='_ctx']").attr("content");
                    let link = ctx + "/user/access-request/process?hashedRequestId=" + hashedRequestId;
                    return '<a class="btn btn-info" href=' + link + ' role="button"><i class="far fa-calendar-check"> Visit</i></a>';
                }
            }
        ]
    });
});