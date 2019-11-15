$(document).ready(function () {
    $('#cachedObjectTable').DataTable();
    /*$('#cachedObjectTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/cached/list",
            dataSrc: '',
            error: function (e) {
                showErrorMsg("Cannot load result.");
            }
        },
        columnDefs: [{
            targets: 0,
            render: function (type, row, meta ) {
                let cachedObjectName = meta;
                return cachedObjectName;
            }
        } ,{
            targets: 1,
            render: function (type, row, meta ) {
                let cachedObjectName = meta;
                let ctx = $("meta[name='_ctx']").attr("content");
                let link = ctx + "/system/cachedObject/content?cachedObjectName=" + cachedObjectName;
                return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-edit"></i> Detail</a>';
            }
        }, {
            targets: 2,
            render: function (type, row, meta ) {
                let cachedObjectName = meta;
                let ctx = $("meta[name='_ctx']").attr("content");
                let link = ctx + "/system/cachedObject/reload?cachedObjectName=" + cachedObjectName;
                return '<a class="btn btn-info" href=' + link + ' role="button"><i class="fas fa-sync"></i>Reload</a>';
            }
        }]
    });*/
})

function reloadSiteConfig() {
    clearAllMsg();
    $.get('/cached/reloadSiteConfig', function (res) {
        if (res.success) {
            showInfoMsg(res.feedback);
        } else {
            showErrorMsg(res.feedback)
        }
    })
}

function reloadServiceTypeOfferMapping() {
    clearAllMsg();
    $.get('/cached/reloadServiceTypeOfferMapping', function (res) {
        if (res.success) {
            showInfoMsg(res.feedback);
        } else {
            showErrorMsg(res.feedback)
        }
    })
}

function reloadServiceTypeList() {
    clearAllMsg();
    $.get('/cached/reloadServiceTypeList', function (res) {
        if (res.success) {
            showInfoMsg(res.feedback);
        } else {
            showErrorMsg(res.feedback)
        }
    })
}

function reloadUserRole() {
    clearAllMsg();
    $.get('/cached/reloadCachedRoleTree', function (res) {
        if (res.success) {
            showInfoMsg(res.feedback);
        } else {
            showErrorMsg(res.feedback)
        }
    })
}