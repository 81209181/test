$(document).ready(function() {
    // load request
    ajaxGetBasicRequestInfo();
    createVisitorDataTable();
    createEquipmentDataTable();
    createOptHistDataTable();
});

function getCurrentHashedRequestId(){
    return $("#target-request-id").val();
}

function ajaxGetBasicRequestInfo(){
    $.ajax({
        type: "GET",
        async: false,
        contentType: "application/json",
        url: "/user/access-request/process/ajax-get-basic-info?hashedRequestId=" + getCurrentHashedRequestId(),
        success: function (data) {
            // fill in info
            $("#request-status").val(data.status);

            $("#requestor-id").val(data.requesterId);
            $("#requestor-name").val(data.requesterName);
            $("#requestor-company-name").val(data.companyName);
            $("#requestor-email").val(data.mobile);
            $("#requestor-mobile").val(data.email);

            $("#request-visit-reason").val(data.visitReason);
            $("#request-visit-date").val(data.visitDate);
            $("#request-visit-time-from").val(data.visitTimeFrom);
            $("#request-visit-time-to").val(data.visitTimeTo);
            $("#request-visit-loc").val(data.visitLocation);
            $("#request-visit-rack").val(data.visitRackNum);

            // approve reject button
            if(data.showApproveAndReject){
                $("#btn-request-approve").attr("disabled", false);
                $("#btn-request-reject").attr("disabled", false);
            } else {
                $("#btn-request-approve").attr("disabled", true);
                $("#btn-request-reject").attr("disabled", true);
            }
        },
        error: function (e) {
            var responseError = e.responseText ? e.responseText : "Fail to load.";
            showErrorMsg(responseError);
        }
    });
}

function createVisitorDataTable(){
    $('#request-visitor-table').DataTable({
        paging:   false,
        searching:false,
        ajax: {
            type: "GET",
            async: false,
            contentType: "application/json",
            url: "/user/access-request/process/ajax-get-visitor-list?hashedRequestId=" + getCurrentHashedRequestId(),
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load visitor list.");
            }
        },
        columns: [
            { data: 'visitorAccessId' },
            { data: 'name' },
            { data: 'company' },
            { data: 'staffId' },
            { data: 'mobile' },
            { data: 'visitorCardNum' },
            { data: 'timeIn' },
            { data: 'timeOut' }
        ],
        columnDefs: [
            {
                targets: 0,
                data: "visitorAccessId",
                visible: false
            },
            {
                targets: 5,
                data: "visitorCardNum",
                render: function ( visitorCardNum, type, row ) {
                    if(visitorCardNum) {
                        return visitorCardNum;
                    } else if (row['canCheckInOut']) {
                        return "<input id='visitor-card-num-" + row['visitorAccessId'] +
                            "' type='text' class='form-control'>";
                    } else {
                        return "<input id='visitor-card-num-" + row['visitorAccessId'] +
                            "' type='text' class='form-control' readonly>";
                    }
                }
            },
            {
                targets: 6,
                data: "timeIn",
                render: function ( timeIn, type, row ) {
                    if(timeIn) {
                        return timeIn;
                    } else if (row['canCheckInOut']) {
                        return "<button type='button' class='btn btn-info' onclick='ajaxVisitorCheckIn(" + row['visitorAccessId'] + ")' >check-in</button>";
                    } else {
                        return "<button type='button' class='btn btn-info' disabled='disabled' >check-in</button>";
                    }
                }
            },
            {
                targets: 7,
                data: "timeOut",
                render: function ( timeOut, type, row ) {
                    if(timeOut) {
                        return timeOut;
                    } else if (row['timeIn'] && row['canCheckInOut']) {
                        return "<button type='button' class='btn btn-info' onclick='ajaxVisitorCheckOut(" + row['visitorAccessId'] + ")' >check-out</button>";
                    } else {
                        return "<button type='button' class='btn btn-info' disabled='disabled' >check-out</button>";
                    }
                }
            }
        ]
    });
}

function createEquipmentDataTable(){
    $('#request-equip-table').DataTable({
        paging:   false,
        searching:false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/user/access-request/process/ajax-get-equip-list?hashedRequestId=" + getCurrentHashedRequestId(),
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load equipment list.");
            }
        },
        columns: [
            { data: 'eqBrand' },
            { data: 'eqType' },
            { data: 'eqModel' },
            { data: 'eqSerialNum' },
            { data: 'eqRackNum' },
            { data: 'eqUNum' },
            { data: 'action' }
        ]
    });
}
function createOptHistDataTable(){
    $('#request-opt-hist-table').DataTable({
        paging:   false,
        searching:false,
        ordering: false,
        ajax: {
            type: "GET",
            async: false,
            contentType: "application/json",
            url: "/user/access-request/ajax-get-opt-hist?hashedRequestId=" + getCurrentHashedRequestId(),
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load operation history.");
            }
        },
        columns: [
            { width: '30%', data: 'createdate' },
            { width: '15%', data: 'userId' },
            { width: '55%', data: 'detail' }
        ],
        columnDefs: [
            {
                targets: 0,
                data: "createdate",
                render: function ( createdate, type, row, meta ) {
                    return createdate==null ? null : createdate.replace('T', ' ');
                }
            }
        ]
    });
}

function ajaxVisitorCheckIn( visitorAccessId ) {
    var visitorCardNum = $('#visitor-card-num-' + visitorAccessId ).val();
    if(!visitorCardNum){
        showErrorMsg("Missing visitor card number for check-in.");
        return;
    }

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/user/access-request/process/check-in?visitorAccessId=" + visitorAccessId + "&visitorCardNum=" + visitorCardNum,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                // showInfoMsg("Done check-in visitor.");
                $('#request-visitor-table').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function () {
            showErrorMsg("Cannot check-in visitor.");
        }
    });
}

function ajaxVisitorCheckOut( visitorAccessId ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/user/access-request/process/check-out?visitorAccessId=" + visitorAccessId,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                // showInfoMsg("Done check-out visitor.");
                $('#request-visitor-table').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function () {
            showErrorMsg("Cannot check-out visitor.");
        }
    });
}

