$(document).ready(function() {
    let table = $('#myTicketTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/ticket/myTicket",
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
            { data: 'ticketMasId' },
            { data: 'ticketType' },
            { data: 'custCode' },
            { data: 'statusDesc' },
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
            },{
                targets: 6,
                data: "ticketMasId",
                render: function ( ticketMasId, type, row, meta ) {
                    var ctx = $("meta[name='_ctx']").attr("content");
                    var link = ctx + "/ticket?ticketMasId=" + ticketMasId;
                    return '<a class="btn btn-info" href=' + link + ' role="button">Detail</a>';
                }
            }
        ]
    });
});
