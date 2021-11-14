$(document).ready(function() {
    google.charts.load('current', {packages: ['corechart', 'line','bar']});
    google.charts.setOnLoadCallback(drawCharts);

});

function drawCharts() {
//    drawLoginCount90DaysChart();
//    drawLoginCountTwoWeeksChart();
    drawTicketTypeCountOwnerGroupOverLast90days();
    drawTicketStatusCountOwnerGroupOverLast90days();
}

function ajaxGetLoginCount90Days() {
    let array = new Array();
    let maxCount = 0;
    $.ajax({
        type: 'GET',
        async: false,
        url: '/system/statistic/getLoginCountLast90Days',
        success: function (res) {
            maxCount = res.maxTotal;
            for (item of res.data) {
                let temp = new Array();
                let statisticDate = item.statisticDate.replace('-', '/');
                temp[0] = new Date(statisticDate);
                temp[1] = item.total;
                array.push(temp);
            }
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });

    return {array: array, maxCount: maxCount};
}

function ajaxGetLoginCountTwoWeeks() {
    let array = new Array();
    let maxCount = 0;
    $.ajax({
        type: 'GET',
        async: false,
        url: '/system/statistic/getLoginCountLastTwoWeeks',
        success: function (res) {
            maxCount = res.maxTotal;
            for (item of res.data) {
                let temp = new Array();
                let statisticDate = parseInt(item.statisticDate);
                temp[0] = [statisticDate, 0, 0];
                temp[1] = item.total;
                array.push(temp);
            }
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });

    return {array: array, maxCount: maxCount};
}

function drawLoginCount90DaysChart() {
    let data = new google.visualization.DataTable();
    data.addColumn('date', 'days');
    data.addColumn('number', 'total');

    let {array, maxCount} = ajaxGetLoginCount90Days();
    data.addRows(array);

    let options = {
        explorer: {
            axis: 'horizontal',
            action: ['dragToZoom', 'rightClickToReset'],
            keepInBounds: true,
            maxZoomIn: .1,
            maxZoomOut: 8
        },
        title: 'The number of login per month over last 90 days',
        height: 300,
        width: 1100,
        colors: ['#76A7FA'],
        titleTextStyle: {
            bold: true,
            color: '#757575',
            fontSize: 16
        },
        legend: {
            textStyle: {
                color: '#757575',
                fontSize: 15
            }
        },
        hAxis: {
            titleTextStyle: {
                color: '#757575'
            },
            title: 'Statistics-Date',

        },
        vAxis: {
            titleTextStyle: {
                color: '#757575'
            },
            title: 'The number of login',
            viewWindow: {
                max: maxCount
            }
        }
    };

    let chart = new google.visualization.LineChart(document.getElementById('loginCountLast90Days'));
    chart.draw(data, options);
}

function drawLoginCountTwoWeeksChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('timeofday', 'Time of Day');
    data.addColumn('number', 'total');

    let {array, maxCount} = ajaxGetLoginCountTwoWeeks();
    data.addRows(array);

    var options = {
        title: 'The number of login per hours over last two weeks',
        height: 300,
        width: 1100,
        colors: ['#76A7FA'],
        titleTextStyle: {
            bold: true,
            color: '#757575',
            fontSize: 16
        },
        legend: {
            textStyle: {
                color: '#757575',
                fontSize: 15
            }
        },
        hAxis: {
            titleTextStyle: {
                color: '#757575'
            },
            title: 'Time of Day',
            format: 'h:mm a',
            viewWindow: {
                min: [0, 0, 0],
                max: [24, 0, 0]
            }
        },
        vAxis: {
            titleTextStyle: {
                color: '#757575'
            },
            title: 'The number of login',
            viewWindow: {
                max: maxCount
            }
        }
    };

    var chart = new google.visualization.ColumnChart(document.getElementById('loginCountLastTwoWeeks'));
    chart.draw(data, options);
}

function getTicketTypeCountOwnerGroupOverLast90days() {
    let array = new Array();
    let header = new Array();
    let maxCount = 0;
    $.ajax({
        type: 'GET',
        async: false,
        url: '/system/statistic/getTicketTypeCountPerOwnerGroup',
        success: function (res) {
            maxCount = res.maxTotal;
            for (item of res.data) {
                let statisticDate = item[0].replace(/-/g, '/');
                item[0] = new Date(statisticDate);
                for (i = 1 ; i< res.header.length ; i++) {
                    if (!item[i]) {
                        item[i] = 0;
                    } else {
                        item[i] = parseInt(item[i]);
                    }
                }
            }
            array = res.data;
            header = res.header;
        }
    }).fail(function (e) {
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });

    return {array: array, maxCount: maxCount, header: header};
}

function drawTicketTypeCountOwnerGroupOverLast90days() {
    let {array, maxCount, header} = getTicketTypeCountOwnerGroupOverLast90days();
    var data = new google.visualization.DataTable();
    for (item of header) {
        if (item === 'Days') {
            data.addColumn('date', item);
        } else {
            data.addColumn('number', item);
        }
    }

    data.addRows(array);

    var options = {
        title: 'The number of ticket type per group over last 30 days',
        height: 300,
        width: 1100,
        seriesType: 'bars',
        explorer: {
            axis: 'horizontal',
            action: ['dragToZoom', 'rightClickToReset'],
            keepInBounds: true,
            maxZoomIn: .1,
            maxZoomOut: 5
        },
        titleTextStyle: {
            bold: true,
            color: '#757575',
            fontSize: 16
        },
        legend: {
            textStyle: {
                color: '#757575',
                fontSize: 15
            }
        },
        hAxis: {
            title: 'Statistics-Date',

        },
        vAxis: {
            title: 'The number of ticket type per group',
            viewWindow: {
                max: maxCount
            }
        }
    };

    var chart = new google.visualization.ComboChart(document.getElementById('statisticTicketType'));
    chart.draw(data, options);
}

function getTicketStatusCountOwnerGroupOverLast90days() {
    let array = new Array();
    let header = new Array();
    let maxCount = 0;
    $.ajax({
        type: 'GET',
        async: false,
        url: '/system/statistic/getTicketStatusCountPerOwnerGroup',
        success: function (res) {
            maxCount = res.maxTotal;
            for (item of res.data) {
                let statisticDate = item[0].replace(/-/g, '/');
                item[0] = new Date(statisticDate);
                for (i = 1 ; i< res.header.length ; i++) {
                    if (!item[i]) {
                        item[i] = 0;
                    } else {
                        item[i] = parseInt(item[i]);
                    }
                }
            }
            array = res.data;
            header = res.header;
        }
    }).fail(function (e) {
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    });

    return {array: array, maxCount: maxCount, header: header};
}


function drawTicketStatusCountOwnerGroupOverLast90days() {
    let {array, maxCount, header} = getTicketStatusCountOwnerGroupOverLast90days();
    var data = new google.visualization.DataTable();
    for (item of header) {
        if (item === 'Days') {
            data.addColumn('date', item);
        } else {
            data.addColumn('number', item);
        }
    }

    data.addRows(array);

    var options = {
        title: 'The number of ticket status per group over last 30 days',
        height: 300,
        width: 1100,
        seriesType: 'bars',
        explorer: {
            axis: 'horizontal',
            action: ['dragToZoom', 'rightClickToReset'],
            keepInBounds: true,
            maxZoomIn: .1,
            maxZoomOut: 5
        },
        titleTextStyle: {
            bold: true,
            color: '#757575',
            fontSize: 16
        },
        legend: {
            textStyle: {
                color: '#757575',
                fontSize: 15
            }
        },
        hAxis: {
            title: 'Statistics-Date',

        },
        vAxis: {
            title: 'The number of ticket status per group',
            viewWindow: {
                max: maxCount
            }
        }
    };

    var chart = new google.visualization.ComboChart(document.getElementById('statisticTicketStatus'));
    chart.draw(data, options);
}