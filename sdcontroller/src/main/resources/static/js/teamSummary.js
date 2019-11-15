$(function(){
    $('a[aria-controls=tab-ticket-myTeam]').on('click',function(){
        getMyTeamTicketData();
    })
})

function getMyTeamTicketData(){
    $('#tab-ticket-myTeam').find('table').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        retrieve: true,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/ticket/searchTicket",
            dataSrc: 'data',
            data: function(d){
                d.createDateFrom = getCurrentMonthFirst();
                d.isReport = true;
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
            { data: 'serviceType' },
            { data: 'searchValue'},
            { data: 'statusDesc' },
            { data: 'callInCount'},
            { data: 'completeDate'},
            { data: 'createDate' },
            { data: 'createBy' }
        ],
        columnDefs: [
            {
                targets: 7,
                data: "completeDate",
                render: function (nextRunTime, type, row, meta) {
                    return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
            {
                targets: 8,
                data: "createDate",
                render: function (nextRunTime, type, row, meta) {
                     return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
            {
                targets: 10,
                data: "ticketMasId",
                render: function ( ticketMasId, type, row, meta ) {
                    var ctx = $("meta[name='_ctx']").attr("content");
                    var link = ctx + "/ticket?ticketMasId=" + ticketMasId;
                    return '<a class="btn btn-info" href=' + link + ' role="button">Detail</a>';
                }
            }
        ]
    });
}

function getCurrentMonthFirst(){
    let now = new Date();
    return now.getFullYear() + '-' + (now.getMonth()+1) + '-01';
}
