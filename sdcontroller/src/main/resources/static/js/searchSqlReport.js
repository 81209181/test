$(document).ready(function() {
    createSqlReportTable();
});

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
            { data: 'emailTo' }
        ],
        columnDefs: [
            {
                targets: 5,
                data: "reportId",
                render: function (reportId, type, row, meta) {
                    var ctx = $("meta[name='_ctx']").attr("content");
                    var link = ctx + "/system/report/edit-sql-report?reportId=" + reportId;
                    return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Edit</a>';
                }
            },
            {
                targets: 6,
                data: "reportId",
                render: function (reportId, type, row, meta) {
                    return "<button type='button' class='btn btn-info' onclick='ajaxDeleteReport(\"" + reportId + "\")' ><i class=\"fas fa-stopwatch\"></i> Delete</button>";
                }
            }
        ]
    });
}

function ajaxDeleteReport(reportName) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/system/report/ajax-delete-report?reportName=" + reportName,
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            if(data.success){
                showInfoMsg("Deleted report: " + reportName);
                $('#sqlReportTable').DataTable().ajax.reload();
            }else{
                showErrorMsg(data.feedback);
            }
        },
        error: function (e) {
            showErrorMsg("Cannot delete report.");
        }
    });
}

