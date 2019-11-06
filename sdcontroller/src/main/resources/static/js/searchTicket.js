$(document).ready(function() {
    $('#createDateFrom').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#createDateFrom').attr('placeholder','1900-01-01');
    $('#createDateTo').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#createDateTo').attr('placeholder','1900-01-01');
    $('#completeDateFrom').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#completeDateFrom').attr('placeholder','1900-01-01');
    $('#completeDateTo').attr('pattern','[0-9]{4}-[0-9]{2}-[0-9]{2}');
    $('#completeDateTo').attr('placeholder','1900-01-01');

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
            { data: 'searchValue'},
            { data: 'status' },
            { data: 'callInCount'},
            { data: 'completeDate'},
            { data: 'createDate' },
            { data: 'createBy' }
        ],
        columnDefs: [
            {
                targets: 6,
                data: "completeDate",
                render: function (nextRunTime, type, row, meta) {
                    return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
            {
                targets: 7,
                data: "createDate",
                render: function (nextRunTime, type, row, meta) {
                     return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
            {
                  targets:9,
                  data:null,
                  defaultContent:'<button class="btn btn-info">Detail</button>'
              }
        ]
    });
    let ctx = $("meta[name='_ctx']").attr("content");
    $('#searchTicketTable tbody').on('click','button',function(){
        var data = table.row( $(this).parents('tr') ).data();
         $(location).attr('href',ctx+'/ticket?ticketMasId='+data.ticketMasId);
    })
}