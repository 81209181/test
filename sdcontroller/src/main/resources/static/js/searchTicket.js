$(document).ready(function() {
    $('#search-date-from').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#search-date-from').attr('placeholder','1900-01-01');
    $('#search-date-to').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#search-date-to').attr('placeholder','1900-01-01');

    createMyTicketDataTable();
    createSearchTicketDataTable();

    // search button
    $("#search-ticket-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#searchTicketTable').DataTable().ajax.reload();
    });
});

function createMyTicketDataTable(){
    $('#myTicketTable').DataTable({
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/ticket/my-ticket",
            dataSrc: '',
            error: function (e) {
                showErrorMsg("Cannot load my ticket list.");
            }
        },
        columns: [
            { data: 'ticketMasId' },
            { data: 'ticketType' },
            { data: 'custCode' },
            { data: 'status' },
            { data: 'createDate' },
            { data: 'createBy' }
        ],
        columnDefs: [
            {
                targets: 4,
                data: "createDate",
                render: function (nextRunTime, type, row, meta) {
                    return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
        ]
    });
}

function createSearchTicketDataTable(){
    $('#searchTicketTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/ticket/search-ticket",
            dataSrc: 'data',
            data: function(d){
                // search input
                d.dateFrom = $("#search-date-from").val();
                d.dateTo = $("#search-date-to").val();
                d.status = $("#search-status").val();
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
            { data: 'ticketMasId' },
            { data: 'ticketType' },
            { data: 'custCode' },
            { data: 'status' },
            { data: 'createDate' },
            { data: 'createBy' }
        ],
        columnDefs: [
            {
                targets: 4,
                data: "createDate",
                render: function (nextRunTime, type, row, meta) {
                     return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
        ]
    });
}