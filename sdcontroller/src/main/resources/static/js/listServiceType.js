$(document).ready(function() {
    getAjaxServiceTypeDataTable();
})

function getAjaxServiceTypeDataTable() {
    $('#serviceTypeTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/service-type-mapping/service-type-list",
            dataSrc: '',
            error: function (e) {
                if(e.responseText){
                    showErrorMsg(e.responseText);
                } else {
                    showErrorMsg("Cannot load result.");
                }
            }
        },
        columns: [
            {data: 'serviceTypeCode'},
            {data: 'serviceTypeName'},
        ],
        columnDefs: [
            {
                targets: 2,
                data: "serviceTypeCode",
                render: function ( serviceTypeCode, type, row, meta ) {
                    var ctx = $("meta[name='_ctx']").attr("content");
                    var link = ctx + "/system/service-type/edit-service-type?serviceTypeCode=" + serviceTypeCode;
                    return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Edit</a>';
                }
            }
        ]
    });
}
