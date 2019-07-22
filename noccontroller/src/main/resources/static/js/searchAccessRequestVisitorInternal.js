$(document).ready(function() {
    // search button
    // noinspection JSUnusedLocalSymbols
    $("#btn-search-visitor").on("click", function (event) {
        clearAllMsg();
        $('#search-visitor-table').DataTable().ajax.reload();
    });

    // search result
    // noinspection JSUnusedLocalSymbols
    $('#search-visitor-table').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/user/access-request/ajax-search-visitor",
            dataSrc: 'data',
            data: function(d){
                // search input
                d.visitorName = $("#search-access-visitor-name").val();
                d.companyName = $("#search-access-visitor-company").val();
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
            { data: 'visitDate' },
            { data: 'visitLocation' },
            { data: 'name' },
            { data: 'company' },
            { data: 'hashedRequestId' }
        ],
        columnDefs: [
            {
                targets: 4,
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