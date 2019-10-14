$(document).ready(function() {
    var table = $('#myTicketTable').DataTable({
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
            { data: 'createBy' },
        ],
        columnDefs: [
            {
                targets: 4,
                data: "createDate",
                render: function (nextRunTime, type, row, meta) {
                    return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },{
                targets:6,
                data:null,
                defaultContent:'<button class="btn btn-info">Detail</button>'
            }
        ]
    });
    let ctx = $("meta[name='_ctx']").attr("content");
    $('#myTicketTable tbody').on('click','button',function(){
        var data = table.row( $(this).parents('tr') ).data();
         $(location).attr('href',ctx+'/ticket?ticketMasId='+data.ticketMasId);
    })
});
