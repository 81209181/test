$(document).ready(function() {
    // click event
    $("#btn-add-visitor").on("click", function (){
        addVisitorInput();
    });
    $("#btn-add-equip").on("click", function (){
        addEquipInput();
    });
});


function addVisitorInput() {
    var inputTableBody = $("#table-visitor-list > tbody");

    var currentInputCount = inputTableBody.children().length;
    var newRowId = currentInputCount + 1;

    // construct new input row
    var newTableRow = "<tr>" + $("#input-template .visitor-tr").clone().html() + "</tr>";
    newTableRow = newTableRow.replace(/__inputCounter__/g, newRowId);
    newTableRow = newTableRow.replace(/__inputArrayCounter__/g, newRowId-1);

    // add new input row
    inputTableBody.append(newTableRow);
}

function addEquipInput() {
    var inputTableBody = $("#table-equip-list > tbody");

    var currentInputCount = inputTableBody.children().length;
    var newRowId = currentInputCount + 1;

    // construct new input row
    var newTableRow = "<tr>" + $("#input-template .equip-tr").clone().html() + "</tr>";
    newTableRow = newTableRow.replace(/__inputCounter__/g, newRowId);
    newTableRow = newTableRow.replace(/__inputArrayCounter__/g, newRowId-1);

    // add new input row
    inputTableBody.append(newTableRow);
}