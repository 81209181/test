$(document).ready(function() {
    createJobInstTable();
    createSqlReportTable();
});

function createJobInstTable() {
    $('#jobInstTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/report/ajax-list-report-job-inst",
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load  report job list.");
            }
        },
        columns: [
            { data: 'keyGroup' },
            { data: 'keyName' },
            { data: 'jobClass' },
            { data: 'cronExpression' },
            { data: 'lastRunTime' },
            { data: 'nextRunTime' },
            { data: 'keyName' },
            { data: 'keyName' }
        ],
        columnDefs: [
            {   targets: 0, orderData: [0,1,2]  },
            {   targets: 1, orderData: [0,1,2]  },
            {   targets: 2, orderData: [0,1,2]  },

            {
                targets: 4,
                data: "lastRunTime",
                render: function (lastRunTime, type, row, meta) {
                    return lastRunTime==null ? null : lastRunTime.replace('T', ' ');
                }
            },
            {
                targets: 5,
                data: "createdate",
                render: function (nextRunTime, type, row, meta) {
                    return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                }
            },
            {
                targets: 6,
                render: function (keyName, type, row, meta) {
                    if (row['paused']){
                        return "<button type='button' class='btn btn-success' onclick='ajaxResumeReport(\"" + row['keyName'] +"\")' ><i class=\"fas fa-play\"></i> Resume</button>";
                    } else {
                        return "<button type='button' class='btn btn-danger' onclick='ajaxPauseReport(\"" + row['keyName'] +"\")' ><i class=\"fas fa-pause\"></i> Pause</button>";
                    }
                }
            },
            {
                targets: 7,
                render: function (keyName, type, row, meta) {
                    return "<button type='button' class='btn btn-info' onclick='ajaxTriggerReport(\"" + row['keyName'] +"\")' ><i class=\"fas fa-stopwatch\"></i> Trigger</button>";
                }
            }
        ]
    });
}

function ajaxPauseReport(  keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-pause-report?keyName=" + keyName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Paused job: " + keyName);
                $('#jobInstTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot pause job.");
        }
    });
}

function ajaxResumeReport(  keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-resume-report?keyName=" + keyName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Resumed job: " + keyName);
                setTimeout('$(\'#jobInstTable\').DataTable().ajax.reload();', 200);
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot resume job.");
        }
    });
}

function ajaxTriggerReport( keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-trigger-report?keyName=" + keyName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Triggered job: " + keyName);
                $('#jobInstTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot trigger job.");
        }
    });
}

function createSqlReportTable() {
    $('#sqlReportTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/report/ajax-list-sql-report",
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load report list.");
            }
        },
        columns: [
            { data: 'reportName' },
            { data: 'cronExp' },
            { data: 'sql' },
            { data: 'exportTo' },
            { data: 'emailTo' },
            { data: 'active' }
        ],
        columnDefs: [
            {
                targets: 6,
                data: "reportId",
                render: function (reportId, type, row, meta) {
                    if (row['active']) {
                        return "<button type='button' class='btn btn-danger' onclick='ajaxDeactivateJobProfile(\"" + reportId + "\")' ><i class=\"fas fa-times-circle\"></i> Deactivate</button>";
                    } else {
                        return "<button type='button' class='btn btn-success' onclick='ajaxActivateJobProfile(\"" + reportId + "\")' ><i class=\"fas fa-check-circle\"></i> Activate</button>";
                    }
                }
            },
            {
                targets: 7,
                render: function (reportName, type, row, meta) {
                    return "<button type='button' class='btn btn-info' onclick='ajaxSyncJobProfile(\"" + row['reportName'] + "\")' ><i class=\"fas fa-sync\"></i> Sync</button>";
                }
            },
            {
                targets: 8,
                data: "reportId",
                render: function (reportId, type, row, meta) {
                    var ctx = $("meta[name='_ctx']").attr("content");
                    var link = ctx + "/system/report/edit-sql-report?reportId=" + reportId;
                    return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Edit</a>';
                }
            },
            {
                targets: 9,
                data: "reportId",
                render: function (reportId, type, row, meta) {
                    return "<button type='button' class='btn btn-danger' onclick='ajaxDeleteReport(\"" + reportId + "\")' ><i class=\"fas fa-times-circle\"></i> Delete</button>";
                }
            }
        ]
    });
}

function ajaxActivateJobProfile(reportName) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-active-report?reportName=" + reportName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Activated job: " + reportName);
                $('#sqlReportTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot activate job.");
        }
    });
}

function ajaxDeactivateJobProfile(reportName) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-deactivate-report?reportName=" + reportName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Deactivated job: " + reportName);
                $('#sqlReportTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot deactivate job.");
        }
    });
}

function ajaxSyncJobProfile(reportName) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-sync-report?reportName=" + reportName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Sync job: " + reportName);
                $('#jobInstTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot sync job.");
        }
    });
}

function ajaxDeleteReport(reportId) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-delete-report?reportId=" + reportId,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Deleted report");
                $('#sqlReportTable').DataTable().ajax.reload();
                $('#jobInstTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot delete report.");
        }
    });
}