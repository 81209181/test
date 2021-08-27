var outstandingFaultEchart = echarts.init($('#outstanding_fault').get(0));
let outstandingFaultYAxisData = new Array();
let outstandingFaultData = new Array();

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
        text: 'Outstanding fault',
        subtext: 'group by produce type'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'value',
        boundaryGap: [0, 0.01]
    },
    yAxis: {
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