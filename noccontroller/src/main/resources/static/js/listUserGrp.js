$(document).ready(function() {
    $('#userGroupTable').DataTable({
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/admin/manage-group/ajax-list-user-grp",
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
            { data: 'groupId' },
            { data: 'groupName' },
            { data: 'groupDesc' },
            { data: 'parentGroup' }
        ]
    });
});