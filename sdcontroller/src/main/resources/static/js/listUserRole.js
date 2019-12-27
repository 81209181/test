$(document).ready(function() {
    // click event
    $("#btnReloadUserRole").on("click", function (){
        ajaxReload();
    });

    $('#userGroupTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-role/ajax-list-user-role",
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
            { data: 'roleId' },
            { data: 'roleDesc' },
            { data: 'parentRoleId' },
            { data: 'status' },
            { data: 'abstractFlag' }
        ],
        columnDefs: [ {
            targets: 5,
            data: "roleId",
            render: function ( roleId, type, row, meta ) {
                var ctx = $("meta[name='_ctx']").attr("content");
                var link = ctx + "/admin/manage-role/edit-user-role?roleId=" + roleId;
                return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Edit</a>';
            }
        } ]
    });
});
google.charts.load('current', {packages:["orgchart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var chartDate = $.ajax({
        url:'/admin/manage-role/getRole4Chart',
        async: false
    }).responseText;
    var data = new google.visualization.DataTable(chartDate);
    data.addColumn('string', 'name');
    data.addColumn('string', 'parent');
    data.addRows(JSON.parse(chartDate));
    var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
    chart.draw(data,{'allowHtml':true});
    google.charts.load('current', {packages:["orgchart"]});
    google.charts.setOnLoadCallback(drawChart());
});


function ajaxReload() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/manage-role/reloadRole",
        dataSrc: 'data',
        success: function (data) {
            clearAllMsg();
            showInfoMsg("Reloaded user role.");
        },
        error: function (e) {
            showErrorMsg("Cannot reload user role.");
        }
    });
}