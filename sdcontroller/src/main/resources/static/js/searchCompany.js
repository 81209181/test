$(document).ready(function() {
    // search button
    $("#search-company-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#searchCompanyTable').DataTable().ajax.reload();
    });

    // search result
    $('#searchCompanyTable').DataTable({
        processing: true,
        serverSide: true,
        ordering: false,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-company/ajax-search-company",
            dataSrc: 'data',
            data: function(d){
                // search input
                d.companyId = $("#company-search-company-id").val();
                d.name = $("#company-search-name").val();
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
            { data: 'companyId' },
            { data: 'name' },
            { data: 'remarks' },
            { data: 'companyId' }
        ],
        columnDefs: [ {
            targets: 3,
            data: "companyId",
            render: function ( companyId, type, row, meta ) {
                var ctx = $("meta[name='_ctx']").attr("content");
                var link = ctx + "/admin/manage-company/edit-company?companyId=" + companyId;
                return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Edit</a>';
            }
        } ]
    });
});