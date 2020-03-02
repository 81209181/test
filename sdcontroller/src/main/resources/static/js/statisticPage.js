$(document).ready(function() {
    google.charts.load('current', {packages: ['corechart', 'line','bar']});
    google.charts.setOnLoadCallback(drawCharts);

});

function drawCharts() {
    drawLoginCount90DaysChart();
    drawLoginCountTwoWeeksChart();
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
        title: 'The number of login per month',
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
        title: 'The number of login per hours',
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

function drawTicketTypeCountOwnerGroupOverLast90days() {
    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Days');
    data.addColumn('number', 'Query');
    data.addColumn('number', 'Job');

    data.addRows([
        [new Date('2019/12/01'), 0, 0],
        [new Date('2019/12/02'), 10, 5],
        [new Date('2019/12/03'), 23, 15],
        [new Date('2019/12/04'), 17, 9],
        [new Date('2019/12/05'), 18, 10],
        [new Date('2019/12/06'), 9, 5]
    ]);

    var options = {
        title: 'The number of ticket type per month',
        height: 300,
        width: 1100,
        explorer: {
            axis: 'horizontal',
            action: ['dragToZoom', 'rightClickToReset'],
            keepInBounds: true,
            maxZoomIn: .1,
            maxZoomOut: 8
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
            title: 'The number of ticket type',
            viewWindow: {
                max: 30
            }
        }
    };

    var chart = new google.visualization.LineChart(document.getElementById('statisticTicketType'));
    chart.draw(data, options);
}

function drawTicketStatusCountOwnerGroupOverLast90days() {
    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Days');
    data.addColumn('number', 'W');
    data.addColumn('number', 'CP');
    data.addColumn('number', 'O');

    data.addRows([
        [new Date('2019/12/01'), 0, 0, 1],
        [new Date('2019/12/02'), 10, 5, 3],
        [new Date('2019/12/03'), 23, 15, 23],
        [new Date('2019/12/04'), 17, 9, 24],
        [new Date('2019/12/05'), 18, 10,5],
        [new Date('2019/12/06'), 9, 5,6]
    ]);

    var options = {
        title: 'The number of ticket status per month',
        height: 300,
        width: 1100,
        explorer: {
            axis: 'horizontal',
            action: ['dragToZoom', 'rightClickToReset'],
            keepInBounds: true,
            maxZoomIn: .1,
            maxZoomOut: 8
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
            title: 'The number of ticket status',
            viewWindow: {
                max: 30
            }
        }
    };

    var chart = new google.visualization.LineChart(document.getElementById('statisticTicketStatus'));
    chart.draw(data, options);
}