$(document).ready(function() {
    $('#userGroupTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/config-param/ajax-list",
            dataSrc: '',
            error: function (e) {
                showErrorMsg("Cannot load config param list.");
            }
        },
        columns: [
            { data: 'configGroup' },
            { data: 'configKey' },
            { data: 'configValue' },
            { data: 'configValueType' },
            { data: 'modifydate' }
        ],
        columnDefs: [
            {
                targets: 4,
                data: "modifydate",
                render: function (modifydate, type, row, meta) {
                    return modifydate==null ? null : modifydate.replace('T', ' ');
                }
            }
        ]
    });
});