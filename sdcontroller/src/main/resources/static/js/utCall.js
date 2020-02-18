$(document).ready(function() {
    getAjaxUTCallRecordDataTable();

    $('#btnTriggerNewBSNNum').on('click',function(){
        triggerUTCall($('#triggerNewBSNNum').val());
    });
});

function getAjaxUTCallRecordDataTable() {
    $('#utCallRecordTable').DataTable({
        "order": [[0,"asc"],[1,"asc"],[2,"asc"],[3,"asc"],[4,"asc"],[5,"asc"],[6,"asc"],[7,"asc"]],
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/ut-call/ut-call-record-list",
            dataSrc: '',
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                } else {
                    showErrorMsg("Cannot load UT Call Request");
                }
            }
        },
        columns: [
            {data: 'utCallId'},
            {data: 'serviceCode'},
            {data: 'bsnNum'},
            {data: 'createDate'},
            {data: 'msg'},
            {data: 'testStatus'},
            {data: 'lastCheckDate'},
            {data: 'ticketDetId'}
        ],
        columnDefs: [
            {
                targets: 8,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-info' type='button' onclick='triggerUTCall(\"" + row['bsnNum'] +"\")' ><i class='fas fa-stopwatch'></i> Re-Trigger</button>";
                }
            },
            {
                targets: 9,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-success' type='button' onclick='getUTCallRequestResult(\"" + row['utCallId'] + "\",\"" + row['serviceCode'] +"\")' ><i class='fas fa-play'></i> Get Result</button>";
                }
            }
        ]
    });
}

function triggerUTCall(bsnNum) {
    clearAllMsg();
    $.post('/system/ut-call/ajax-trigger-ut-call', {
        bsnNum: bsnNum
    }, function (res) {
        if (res.success) {
            showInfoMsg("trigger success.");
            $('#utCallRecordTable').DataTable().ajax.reload();
        }
    }).fail(function (e) {
        var responseError = e.responseText ? e.responseText : "trigger failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });
}

function getUTCallRequestResult(utCallId, serviceCode) {
    clearAllMsg();
    $.post('/system/ut-call/ajax-ut-call-get-request-result', {
        utCallId: utCallId,
        serviceCode: serviceCode
    }, function (res) {
        if (res.success) {
            showInfoMsg("get result success.");
            $('#utCallRecordTable').DataTable().ajax.reload();
        }
    }).fail(function (e) {
        var responseError = e.responseText ? e.responseText : "get result failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });
}