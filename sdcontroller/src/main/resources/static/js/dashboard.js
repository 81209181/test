let outstandingFaultEchart = echarts.init($('#outstanding_fault').get(0));
let outstandingFaultYAxisData = [];
let outstandingFaultData = [];

$.parseJSON(outstandingFault).forEach(item => {
    outstandingFaultYAxisData.push(item.serviceTypeCode);
    switch (item.serviceTypeCode) {
        case "UNKNOWN":
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'grey'}});
            break;
        case "GMB":
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'cyan'}});
            break;
        case "E_CLOUD":
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'lightblue'}});
            break;
        case 'VOIP':
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'violet'}});
            break;
        case 'EC_365':
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'purple'}});
            break;
        case 'BN':
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'green'}});
            break;
        case 'FN':
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'lightgreen'}});
            break;
        case 'METER':
            outstandingFaultData.push({value: item.count, itemStyle: {color: 'orange'}});
            break;
    }
})

outstandingFaultEchart.setOption({
    title: {
        left: '45%',
        text: 'Outstanding fault'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    grid: {
        left: '2%',
        right: '5%',
        bottom: '5%',
        containLabel: true
    },
    xAxis: {
        axisLine: {
            show: true
        },
        name: 'The number of tickets',
        nameLocation: 'center',
        nameGap: 30,
        type: 'value',
        boundaryGap: [0, 0.01]
    },
    yAxis: {
        name: 'Produce type',
        nameLocation: 'center',
        nameGap: 80,
        type: 'category',
        data: outstandingFaultYAxisData
    },
    series: [
        {
            type: 'bar',
            data: outstandingFaultData
        },
    ]
});

let ticketTimePeriodSummary = echarts.init($('#ticket_timeperiod_summary').get(0));
ticketTimePeriodSummary.setOption({
    title: {
        left: '45%',
        text: 'Ticket summary'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    grid: {
        left: '4%',
        right: '4%',
        bottom: '5%',
        containLabel: true
    },
    xAxis: {
        name: 'Time(minutes)',
        nameLocation: 'center',
        nameGap: 30,
        type: 'category',
    },
    yAxis: {
        axisLine: {
            show: true
        },
        name: 'The number of tickets',
        nameLocation: 'center',
        nameGap: 45,
        type: 'value'
    },
    series: [{
        type: 'bar'
    }],
    dataset: {
        dimensions: ['timePeriod', 'count'],
        source: $.parseJSON(ticketTimePeriodSummaryList)
    }
});