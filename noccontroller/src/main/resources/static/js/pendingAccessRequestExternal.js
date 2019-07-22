$(document).ready(function() {

    $("#btn-save-target-loc").on("click", function () {
        saveTargetLoc();
        $('#pendingAccessRequestTable').DataTable().ajax.reload();
        $('#completedAccessRequestTable').DataTable().ajax.reload();
    });

    loadTargetLoc();

    createPendingDataTable();
    createCompletedDataTable();
});

function createPendingDataTable(){
    $('#pendingAccessRequestTable').DataTable({
        order: [[ 2,"asc"],[ 3,"asc"]],
        ajax: {
            processing: true,
            serverSide: true,
            type: "GET",
            contentType: "application/json",
            url: "/user/access-request/ajax-list-pending",
            dataSrc: "",
            data: function(d){
                // search input
                d.targetLocation = getTargetLoc();
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
            { data: 'hashedRequestId' },
            { data: 'status' },
            { data: 'visitDate' },
            { data: 'visitTimeFrom' },
            { data: 'visitTimeTo' },
            { data: 'visitLocation' },
            { data: 'createdate' },
            { data: 'visitorCount' },
            { data: 'hashedRequestId' }
        ],
        columnDefs: [
            {   targets: 0, orderData: [0,2,3]  },
            {   targets: 1, orderable: false  },
            {   targets: 2, orderData: [2,3,0]  },
            {   targets: 3, orderData: [2,3,0]  },
            {   targets: 4, orderData: [2,4,0]  },
            {   targets: 5, orderData: [5,2,3,0]  },
            {   targets: 6, orderData: [6,2,3,0],
                data: "createdate",
                render: function (createdate) {
                    return createdate==null ? null : createdate.replace('T', ' ');
                }
            },
            {   targets: 7, orderData: [7,2,3,0]  },
            {
                targets: 8,
                data: "hashedRequestId",
                orderable: false,
                render: function ( hashedRequestId ) {
                    let ctx = $("meta[name='_ctx']").attr("content");
                    let link = ctx + "/user/access-request/view?hashedRequestId=" + hashedRequestId;
                    return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-list"></i> View</a>';
                }
            }
        ]
    });
}

function createCompletedDataTable() {
    $('#completedAccessRequestTable').DataTable({
        order: [[ 2,"desc"],[ 3,"desc"]],
        ajax: {
            processing: true,
            serverSide: true,
            type: "GET",
            contentType: "application/json",
            url: "/user/access-request/ajax-list-recent-completed",
            dataSrc: "",
            data: function(d){
                // search input
                d.targetLocation = getTargetLoc();
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
            { data: 'hashedRequestId' },
            { data: 'status' },
            { data: 'visitDate' },
            { data: 'visitTimeFrom' },
            { data: 'visitTimeTo' },
            { data: 'visitLocation' },
            { data: 'createdate' },
            { data: 'visitorCount' },
            { data: 'hashedRequestId' },
        ],
        columnDefs: [
            {   targets: 0, orderData: [0,2,3]  },
            {   targets: 1, orderable: false  },
            {   targets: 2, orderData: [2,3,0]  },
            {   targets: 3, orderData: [2,3,0]  },
            {   targets: 4, orderData: [2,4,0]  },
            {   targets: 5, orderData: [5,2,3,0]  },
            {   targets: 6, orderData: [6,2,3,0],
                data: "createdate",
                render: function (createdate) {
                    return createdate==null ? null : createdate.replace('T', ' ');
                }
            },
            {   targets: 7, orderData: [7,2,3,0]  },
            {
                targets: 8,
                data: "hashedRequestId",
                orderable: false,
                render: function ( hashedRequestId ) {
                    let ctx = $("meta[name='_ctx']").attr("content");
                    let link = ctx + "/user/access-request/view?hashedRequestId=" + hashedRequestId;
                    return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-list"></i> View</a>';
                }
            }
        ]
    });
}

function loadTargetLoc() {
    let targetLoc = $.cookie("access-summary-target-loc");
    let isValidOption = $("#access-summary-target-loc option[value='" + targetLoc + "']").length !== 0;

    if( targetLoc && isValidOption){
        $("#access-summary-target-loc").val(targetLoc);
    }
}

function saveTargetLoc(){
    let targetLoc = $("#access-summary-target-loc").val();
    $.cookie("access-summary-target-loc", targetLoc);
}

function getTargetLoc() {
    return $("#access-summary-target-loc").val();
}
