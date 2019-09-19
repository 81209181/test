$(document).ready(function() {
    $('#myTicketTable').DataTable({
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/ticket/myTicket",
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
});
