$(document).ready(function() {
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
            { data: 'parentRoleId' }
        ]
    });
});