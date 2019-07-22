$(document).ready(function() {
    createJobInstTable();
    createJobProfileTable();
});

function createJobInstTable() {
    $('#jobInstTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/job/ajax-list-job-inst",
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load job list.");
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
                        return "<button type='button' class='btn btn-success' onclick='ajaxResumeJob(\"" + row['keyGroup'] + "\",\"" + row['keyName'] +"\")' ><i class=\"fas fa-play\"></i> Resume</button>";
                    } else {
                        return "<button type='button' class='btn btn-danger' onclick='ajaxPauseJob(\"" + row['keyGroup'] + "\",\"" + row['keyName'] +"\")' ><i class=\"fas fa-pause\"></i> Pause</button>";
                    }
                }
            },
            {
                targets: 7,
                render: function (keyName, type, row, meta) {
                    return "<button type='button' class='btn btn-info' onclick='ajaxTriggerJob(\"" + row['keyGroup'] + "\",\"" + row['keyName'] +"\")' ><i class=\"fas fa-stopwatch\"></i> Trigger</button>";
                }
            }
        ]
    });
}

function ajaxPauseJob( keyGroup, keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/job/ajax-pause-job?keyGroup=" + keyGroup + "&keyName=" + keyName,
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

function ajaxResumeJob( keyGroup, keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/job/ajax-resume-job?keyGroup=" + keyGroup + "&keyName=" + keyName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Resumed job: " + keyName);
                $('#jobInstTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot resume job.");
        }
    });
}

function ajaxTriggerJob( keyGroup, keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/job/ajax-trigger-job?keyGroup=" + keyGroup + "&keyName=" + keyName,
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




function createJobProfileTable() {
    $('#jobProfileTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/job/ajax-list-job-profile",
            dataSrc: '',
            error: function () {
                showErrorMsg("Cannot load job list.");
            }
        },
        columns: [
            {data: 'keyGroup'},
            {data: 'keyName'},
            {data: 'jobClass'},
            {data: 'active'},
            {data: 'mandatory'},
            {data: 'cronExp'},
            {data: 'keyName'},
            {data: 'keyName'}
        ],
        columnDefs: [
            {   targets: 0, orderData: [0,1,2]  },
            {   targets: 1, orderData: [0,1,2]  },
            {   targets: 2, orderData: [0,1,2]  },
            {
                targets: 6,
                render: function (keyName, type, row, meta) {
                    if (row['active']) {
                        return "<button type='button' class='btn btn-danger' onclick='ajaxDeactivateJobProfile(\"" + row['keyGroup'] + "\",\"" + row['keyName'] + "\")' ><i class=\"fas fa-times-circle\"></i> Deactivate</button>";
                    } else {
                        return "<button type='button' class='btn btn-success' onclick='ajaxActivateJobProfile(\"" + row['keyGroup'] + "\",\"" + row['keyName'] + "\")' ><i class=\"fas fa-check-circle\"></i> Activate</button>";
                    }
                }
            },
            {
                targets: 7,
                render: function (keyName, type, row, meta) {
                    return "<button type='button' class='btn btn-info' onclick='ajaxSyncJobProfile(\"" + row['keyGroup'] + "\",\"" + row['keyName'] + "\")' ><i class=\"fas fa-sync\"></i> Sync</button>";
                }
            }
        ]
    });
}

function ajaxActivateJobProfile( keyGroup, keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/job/ajax-activate-job?keyGroup=" + keyGroup + "&keyName=" + keyName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Activated job: " + keyName);
                $('#jobProfileTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot activate job.");
        }
    });
}

function ajaxDeactivateJobProfile( keyGroup, keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/job/ajax-deactivate-job?keyGroup=" + keyGroup + "&keyName=" + keyName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Deactivated job: " + keyName);
                $('#jobProfileTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot deactivate job.");
        }
    });
}

function ajaxSyncJobProfile( keyGroup, keyName ) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/job/ajax-sync-job?keyGroup=" + keyGroup + "&keyName=" + keyName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Sync job: " + keyName);
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
