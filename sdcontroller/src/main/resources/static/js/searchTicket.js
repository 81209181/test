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

    // export button
    $('#btn-export-form').on('click',function() {
        var xhr = new XMLHttpRequest();
        let ctx = $("meta[name='_ctx']").attr("content");
        let input = "";
        input += "createDateFrom=" + $("#createDateFrom").val();
        input += "&createDateTo=" + $("#createDateTo").val();
        input += "&status=" + $("#search-status").val();
        input += "&completeDateFrom=" + $("#completeDateFrom").val();
        input += "&completeDateTo=" + $("#completeDateTo").val();
        input += "&createBy=" + $("#createBy").val();
        input += "&ticketMasId=" + $("#ticket_mas_id").val();
        input += "&custCode=" + $("#customer_code").val();
        input += "&serviceNumber=" + $("#serviceNumber").val();
        input += "&ticketType=" + $("#ticketType").val();
        input += "&serviceType=" + $("#serviceType").val();
        input += "&owningRole=" + $("#owningRole").val();

        xhr.open("GET", ctx + "/ticket/exportExcel?" + input, true);
        xhr.responseType = 'arraybuffer';
        xhr.onload = function () {
            if (this.status === 200) {
                // Get response header mainly to get attachment name
                var contentDisposition = xhr.getResponseHeader('content-disposition');
                // Get type type and encoding
                var contentType = xhr.getResponseHeader('content-type');
                // Construct a blob object, depending on the link address provided in the header
                var blob = new Blob([xhr.response], {
                    type: contentType
                });
                var url = window.URL.createObjectURL(blob);
                // Get folder name
                var regex = /filename=[^;]*/;
                var matchs = contentDisposition.match(regex);
                if (matchs) {
                    filename = decodeURIComponent(matchs[0].split("=\"")[1]);
                } else {
                    filename += Date.now() + ".xls";
                }
                var a = document.createElement('a');
                a.href = url;
                a.download = filename;
                a.click();
                window.URL.revokeObjectURL(url);
                $(a).remove();
            } else {
                showErrorMsg(String.fromCharCode.apply(null, new Uint8Array(xhr.response)));
            }
        };
        xhr.send();
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
                d.owningRole = $("#owningRole").val();
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
            { data: 'serviceNumber'},
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