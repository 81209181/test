$(function(){
    $('a[aria-controls=tab-ticket-myTeam]').on('click',function(){
        getMyTeamTicketData();
    })

    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);

})
function drawChart() {
    var query_data = new google.visualization.DataTable();
    query_data.addColumn('string', 'status');
    query_data.addColumn('number', 'Slices');
    query_data.addRows([
        ['Open', Number(query_open)],
        ['Work', Number(query_work)],
        ['Complete', Number(query_complete)],
    ]);

    var job_data = new google.visualization.DataTable();
    job_data.addColumn('string', 'status');
    job_data.addColumn('number', 'Slices');
    job_data.addRows([
        ['Open', Number(job_open)],
        ['Work', Number(job_work)],
        ['Complete', Number(job_complete)],
    ]);

    var query_options = {
        'title':'Query Ticket',
        'height':300,
        is3D:true,
        slices: {
            0: { color: 'dodgerblue' },
            1: { color: 'orange' },
            2: { color: 'mediumseagreen'}
        },
    };
    var job_options = {
        'title':'Job Ticket',
        'height':300,
        is3D: true,
        slices: {
            0: { color: 'dodgerblue' },
            1: { color: 'orange' },
            2: { color: 'mediumseagreen'}
        },
    };

    var query_chart = new google.visualization.PieChart(document.getElementById('query_ticket'));
    var job_chart = new google.visualization.PieChart(document.getElementById('job_ticket'));
    query_chart.draw(query_data, query_options);
    job_chart.draw(job_data, job_options);
}

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
            { data: 'statusDesc' },
            { data: 'custCode' },
            { data: 'serviceType' },
            { data: 'searchValue'},
            { data: 'callInCount'},
            { data: 'completeDate'},
            { data: 'createDate' },
            { data: 'createBy' },
            { data: 'owningRole'}
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
                targets: 11,
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
