$(document).ready(function() {
    getAjaxUTCallRecordDataTable();
});

function getAjaxUTCallRecordDataTable() {
    $('#utCallRecordTable').DataTable({
        "order": [[ 0, "asc" ],[1,"asc"],[2,"asc"],[3,"asc"]],
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
        ],
        columnDefs: [
            {
                targets: 4,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-info' onclick='reTriggerUTCall(\"" + row['bsnNum'] +"\")' ><i class='fas fa-stopwatch'></i> Re-Trigger</button>";
                }
            },
            {
                targets: 5,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-success' onclick='getUTCallRequestResult(\"" + row['utCallId'] + "\",\"" + row['serviceCode'] +"\")' ><i class='fas fa-play'></i> Get Result</button>";
                }
            }
        ]
    });
}

function reTriggerUTCall(bsnNum) {
    $.post('/system/ut-call/ajax-re-trigger-ut-call', {
        bsnNum: bsnNum
    }, function (res) {
        if (res.success) {
            showInfoMsg("re-trigger success.");
            setTimeout(function () {
                location.reload();
            }, 1000)
        }
    }).fail(function (e) {
        var responseError = e.responseText ? e.responseText : "re-trigger failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });
}

function getUTCallRequestResult(utCallId, serviceCode) {
    $.post('/system/ut-call//ut-call-get-request-result', {
        utCallId: utCallId,
        serviceCode: serviceCode
    }, function (res) {
        if (res.success) {
            showInfoMsg("get result success.");
            setTimeout(function () {
                location.reload();
            }, 1000)
        }
    }).fail(function (e) {
        var responseError = e.responseText ? e.responseText : "get result failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });
}