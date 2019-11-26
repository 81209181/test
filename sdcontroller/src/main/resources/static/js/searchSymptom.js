$(document).ready(function() {
    $('#searchSymptomTable').DataTable({
        processing: true,
        serverSide: true,
        searching: false,
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/symptom/ajax-search-symptom",
            dataSrc: 'data',
            data: function(d){
                let {dirList, sortList} = getSortData(d);
                d.dirList = dirList;
                d.sortList = sortList;
                // search input
                d.symptomGroupCode = $("#symptomGroupCode").val();
                d.symptomDescription = $("#symptomDescription").val();
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
            {data: 'symptomCode', name: 'symptomCode'},
            {data: 'symptomDescription', name: 'symptomDescription'},
            {data: 'symptomGroupName', name: 'symptomGroup'},
            {data: 'modifydate', name: 'modifydate'},
            {data: 'modifyby', name: 'modifyby'}
        ],
        columnDefs: [
        {
            targets: 3,
            data: "modifydate",
            render: function (modifydate, type, row, meta) {
                return modifydate==null ? null : modifydate.replace('T', ' ');
            }
        },
        {
            targets: 5,
            data: "symptomCode",
            render: function ( symptomCode, type, row, meta ) {
                var ctx = $("meta[name='_ctx']").attr("content");
                var link = ctx + "/symptom/edit-symptom-mapping?symptomCode=" + symptomCode;
                return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Edit</a>';
            }
        } ]
    });

    // search button
    $("#search-symptom-form").submit(function (event) {
        event.preventDefault();
        clearAllMsg();
        $('#searchSymptomTable').DataTable().ajax.reload();
    });
});

function getSortData(d) {
    let dirList = ''
    let sortList = '';
    const separator = ',';
    let orderList = d.order;
    for (let i = 0; i < orderList.length; i++) {
        sortList = sortList + d.columns[orderList[i].column].name;
        dirList = dirList + orderList[i].dir;
        if (i !== orderList.length - 1) {
            sortList = sortList + separator;
            dirList = dirList + separator;
        }
    }
    return {dirList, sortList};
}