var cacheName = $('#cacheName').val();

$(function(){
    $.get('/cached/getCacheInfo/'+cacheName,function(res){
        var currentObject = "";
        var newObject = "";
        if (typeof res[0] == 'string' && typeof res[1] == 'string') {
            try {
                currentObject = JSON.stringify(JSON.parse(res[0]), null, 2);
            } catch(e) {
                currentObject = res[0];
            }
            try{
                newObject = JSON.stringify(JSON.parse(res[1]), null, 2);
            } catch(e) {
                newObject = res[1];
            }
        } else {
            currentObject = res[0];
            newObject = res[1];
        }
        $('#current_object').text(currentObject);
        $('#new_object').text(newObject);
        leftBlockId = $('#current_object').attr('id');
        rightBlockId = $('#new_object').attr('id');
        $(document).jdd(leftBlockId, rightBlockId);
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
})

function refresh(){
    clearAllMsg();
    $.get('/cached/reloadCache/'+cacheName,function(res){
        if(res == true){
            location.reload();
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        showErrorMsg(responseError);
    })
}

function startCompare() {
    var a = $.trim($("#current_object").val());
    var b = $.trim($("#new_object").val());

    var result = getHighLightDifferent(a, b);

    if (result[0].length > 0) {
        $("#current_object").highlightTextarea({
            words: result[0]
        });
    }

    if (result[1].length > 0) {
        $("#new_object").highlightTextarea({
            words: result[1]
        });
    }

    return false;
}

function getHighLightDifferent(a, b) {
    var temp = getDiffArray(a, b);
    var a1 = getHighLightArray(temp[0]);
    var a2 = getHighLightArray(temp[1]);
    return new Array(a1,a2);
}

function getHighLightArray(source){
    var result = new Array();
    var sourceChars = source.split("");
    for (var i = 0; i < sourceChars.length; i++) {
        if (sourceChars[i] != ' ') {
            result.push(sourceChars[i]);
        }
    }
    return result;
}

function getDiffArray(a, b) {
    var result = new Array();
    // Pick smaller strings for exhaustive substrings
    if (a.length < b.length) {
        var start = 0;
        var end = a.length;
        result = getDiff(a, b, start, end);
    } else {
        var start = 0;
        var end = b.length;
        result = getDiff(b, a, 0, b.length);
        result = new Array(result[1], result[0]);
    }
    return result;

}

// Compare the specified part of a with b to generate the comparison result
function getDiff(a, b, start, end) {
    var result = new Array(a, b);
    var len = result[0].length;
    while (len > 0) {
        for (var i = start; i < end - len + 1; i++) {
            var sub = result[0].substring(i, i + len);
            var idx = -1;
            if ((idx = result[1].indexOf(sub)) != -1) {
                result[0] = setEmpty(result[0], i, i + len);
                result[1] = setEmpty(result[1], idx, idx + len);
                if (i > 0) {
                    // Recursively get the difference on the left of the blank area
                    result = getDiff(result[0], result[1], start, i);
                }
                if (i + len < end) {
                    // Recursively get the difference on the right of the blank area
                    result = getDiff(result[0], result[1], i + len, end);
                }
                len = 0;// Exit the while loop
                break;
            }
        }
        len = parseInt(len / 2);
    }
    return result;
}

// Set the area specified by the string s to spaces
function setEmpty(s, start, end) {
    var array = s.split("");
    for (var i = start; i < end; i++) {
        array[i] = ' ';
    }
    return array.join("");
}