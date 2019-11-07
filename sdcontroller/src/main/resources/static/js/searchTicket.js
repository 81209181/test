$(document).ready(function() {
    $('#createDateFrom').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#createDateTo').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#completeDateFrom').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#completeDateTo').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#createDateFrom').attr('placeholder','1900-01-01');
    $('#createDateTo').attr('placeholder','1900-01-01');
    $('#completeDateFrom').attr('placeholder','1900-01-01');
    $('#completeDateTo').attr('placeholder','1900-01-01');
    let date = new Date();
    let day = ("0" + date.getDate()).slice(-2);
    let month = ("0" + (date.getMonth() + 1)).slice(-2);
    let localeDate = date.getFullYear() + "-" + (month) + "-" + (day);
    $('#createDateFrom').val(localeDate);
    $('#createDateTo').val(localeDate);

    createSearchTicketDataTable();

    // search button
    $("#search-ticket-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#searchTicketTable').DataTable().ajax.reload();
    });

    $('#btnSearchReset').on('click',function(){
        $("#search-ticket-form")[0].reset();
    });
});

function createSearchTicketDataTable(){
    let table = $('#searchTicketTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/ticket/searchTicket",
            dataSrc: 'data',
            data: function(d){
                // search input
                d.createDateFrom = $("#createDateFrom").val();
                d.createDateTo = $("#createDateTo").val();
                d.status = $("#search-status").val();
                d.completeDateFrom = $("#completeDateFrom").val();
                d.completeDateTo = $("#completeDateTo").val();
                d.createBy = $("#createBy").val();
                d.ticketMasId = $('#ticket_mas_id').val();
                d.custCode = $('#customer_code').val();
                d.serviceNumber = $("#serviceNumber").val();
                d.ticketType = $('#ticketType').val();
                d.serviceType = $("#serviceType").val();
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