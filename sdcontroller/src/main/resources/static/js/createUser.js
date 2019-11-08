$(document).ready(function () {

    $("#nonLoginId").val('X');

    $('#hktLoginId').val('T');
});


function addNonHktPrefix(prefix) {
    let loginId = $('#nonLoginId').val();
    if (loginId.indexOf(prefix,0) !== 0) {
        $('#nonLoginId').val(prefix + loginId);
    }
}

function addHktPrefix(prefix) {
    let loginId = $('#hktLoginId').val();
    if (loginId.indexOf(prefix, 0) !== 0) {
        $('#hktLoginId').val(prefix + loginId);
    }
}
