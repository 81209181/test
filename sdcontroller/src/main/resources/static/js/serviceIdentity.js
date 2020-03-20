let ctx = $("meta[name='_ctx']").attr("content");
var voIpBtn = $('.voIpBtn'),
    bbBtn = $('.bbBtn'),
    inventoryBtn = $('.inventoryBtn'),
    eCloudBtn =$('.eCloudBtn');

$().ready(function(){

    $(".dummy").hide();
    $('#searchKey').change(function(){
        $('#searchValue').attr('disabled',false);
        $('#btnSearchInfo').attr('disabled',false);
        let selected = $(this).children('option:selected').val();
        if(selected =='poleId'){
            $(".dummy").show();
            if($("#chkDummy").is(":checked") == true){
                $('#searchValue').val('');
                $('#searchValue').attr('disabled',true);
                $('#btnSearchInfo').attr('disabled',true);
            }
        }else{
            $(".dummy").hide();
        }
    })

    $("#chkDummy").click(function() {
        $('input[name=searchKey]').val($('#searchKey').val());
        if ($(this).is(":checked") == true) {
            $('#searchValue').val('');
            $('#searchValue').attr('disabled',true);
            $('#btnSearchInfo').attr('disabled',true);
        } else {
            $('#searchValue').attr('disabled',false);
            $('#btnSearchInfo').attr('disabled',false);
        }
    });

    $(document).keydown(function(event){
        if(event.keyCode==13){
           $('#btnSearchInfo').click();
        }
    })

    $('#btnSearchCustomerNext').on('click',function(){
        clearAllMsg();
        if ($('#searchKey').val() === "poleId") {
            createTicket();
            return;
        }
        $.post('/ticket/service-identity/checkPendingOrder',$('form').serialize(),function(res){
            if (res.success) {
                createTicket();
            } else {
                showErrorMsg("There is pending order (" + res.data + ") of the service in WFM. Continue to create ticket?<button class='btn btn-primary mt-1 ml-2' onclick='createTicket()'>Yes</button><button class='btn btn-primary mt-1 ml-2' onclick='clearAllMsg()'>No</button>");
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        });
    })

    $('#btnSearchInfo').on('click',function(){
        let searchKey=$('#searchKey');
        let searchValue=$('#searchValue');
        clearAllMsg();
        $('tbody').empty();
        $('form').get(0).reset();
        $('.eCloudBtn').removeData('url');
        $('.serviceButtons').find('button').attr('disabled',true);
        if(searchKey.val().trim().length <1){
            searchKey.attr('class','custom-select is-invalid');
            searchKey.focus();
            return;
        }else{
            searchKey.attr('class','custom-select');
        }
        if(searchValue.val().trim().length<1){
            searchValue.attr('class','form-control is-invalid');
            searchValue.focus();
            return;
        }else{
            searchValue.attr('class','form-control');
        }
        $.post('/ticket/search-service',{
            searchKey : searchKey.val().trim(),
            searchValue : searchValue.val().trim()
        }, function(response){
            let res = response.list;
            if(res.length === 1){
                let info = res[0];
                bnButtonCtrl(info.bnCtrl);
                voIpButtonCtrl(info.voIpCtrl);
                eCloudButtonCtrl(info.cloudCtrl);
                $('.eCloudBtn').data('url',info.url);
                $.each(info,function(key,val){
                    $('form').find('input[name='+ key +']').val(val);
                });
            } else {
                $.each(res, function (i, val) {
                    let tr = '<tr class="text-center"></tr>';
                    $('tbody').append(tr);
                    $('tbody tr:last').data('info', val);
                    $('tbody tr:last').append('<td><input type="radio" name="service"></td>')
                        .append('<td>' + val.custCode + '</td>')
                        .append('<td>' + val.custName + '</td>')
                        .append('<td>' + val.serviceType + '</td>')
                        .append('<td>' + val.serviceNo + '</td>')
                });
                $('#products_modal').modal('show');
            }

            if(response.warningMsg){
                showWarningMsg(response.warningMsg);
            }
        }).fail(function(e){
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        }).then(function(){
            $('input[name=searchKey]').val(searchKey.val());
            $('input[name=searchValue]').val(searchValue.val());
            $('#btnApplyProduct').on('click',function(){
                let info = $('tbody').find('input:checked').parent().parent().data('info');
                bnButtonCtrl(info.bnCtrl);
                voIpButtonCtrl(info.voIpCtrl);
                eCloudButtonCtrl(info.cloudCtrl);
                $('.eCloudBtn').data('url',info.url);
                $.each(info,function(i,val){
                    $('form').find('input[name='+i+']').val(val);
                })
                $('#products_modal').modal('hide');
            })
        })
    });

    //button action
    $('.eCloudBtn').on('click',function(){
        window.open($(this).data('url'),'Profile','scrollbars=yes,height=600,width=800');
    })

    $('.inventoryBtn').on('click', function () {
        let finalBsn = getFinalBsn();
        if(finalBsn.length > 0){
            $.ajax({
                url: '/ticket/getInventory',
                type: 'POST',
                data: {bsn: finalBsn},
                dataType: 'text',
                success: function (res) {
                    let inventoryWindow = window.open('', 'Inventory', 'scrollbars=yes,width=800, height=800');
                    inventoryWindow.document.write(res);
                    inventoryWindow.focus();
                }
            }).fail(function (e) {
                let responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
                showErrorMsg(responseError);
            })
        }
    })

    $('.voIpBtn').on('click', function () {
        let finalBsn = getFinalBsn();
        if(finalBsn.length > 0){
            window.open(ctx+'/ticket/offer-info?bsn='+finalBsn,'RelatedOfferInfo','scrollbars=yes,height=800,width=1200');
        }
    })

    $('.bbBtn').on('click', function () {
        let finalBsn = getFinalBsn();
        if(finalBsn.length > 0){
            window.open(ctx+'/ticket/offer-detail?bsn='+finalBsn,'OfferDetailList','scrollbars=yes,height=800,width=1200');
        }
    })

    //related table
    var relatedTicketTable;
    $("#relatedTicketTable").hide();
    $('.panel').find('.panel-body').slideUp();
    $('.panel').find('.panel-heading').find('span').addClass('panel-collapsed');

    $(document).on('click', '.panel-heading span.clickable', function(e){
        if ($('#relatedTicketTable').hasClass('dataTable')) {
            relatedTicketTable = $('#relatedTicketTable').dataTable();
            relatedTicketTable.fnClearTable();
            relatedTicketTable.fnDestroy();
        }
        var $this = $(this);
        if(!$this.hasClass('panel-collapsed')) {
            $this.parents('.panel').find('.panel-body').slideUp();
            $this.addClass('panel-collapsed');
            $this.find('i').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
            $('#relatedTicketTable').hide();
        } else {
            $this.parents('.panel').find('.panel-body').slideDown();
            $this.removeClass('panel-collapsed');
            $this.find('i').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-down');
            let serviceType = $('input[name=serviceType]').val();
            let serviceNo = $('input[name=serviceNo]').val();
            if (serviceNo === '') {
                $('#relatedTicketTable').hide();
            } else {
                $('#relatedTicketTable').show();
                relatedTicketTable = $('#relatedTicketTable').DataTable({
                    processing: true,
                    serverSide: true,
                    ordering: false,
                    searching: false,
                    ajax: {
                        type: "GET",
                        contentType: "application/json",
                        url: "/ticket/searchTicket",
                        dataSrc: 'data',
                        data: function(d){
                            d.serviceType = serviceType;
                            d.serviceNumberExact = serviceNo;
                        },
                        error: function (e) {
                            if(e.responseText){
                                showErrorMsg(e.responseText);
                            } else {
                                showErrorMsg("Cannot load result.");
                            }
                        }
                    },
                    columns: [
                        { data: 'ticketMasId' },
                        { data: 'ticketType' },
                        { data: 'statusDesc' },
                        { data: 'callInCount'},
                        { data: 'createDate' },
                        { data: 'completeDate'}
                    ],
                    columnDefs: [
                        {
                            targets: 4,
                            data: "createDate",
                            render: function (nextRunTime, type, row, meta) {
                                return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                            }
                        },
                        {
                            targets: 5,
                            data: "completeDate",
                            render: function (nextRunTime, type, row, meta) {
                                return nextRunTime==null ? null : nextRunTime.replace('T', ' ');
                            }
                        },
                        {
                            targets: 6,
                            data: "ticketMasId",
                            render: function ( ticketMasId, type, row, meta ) {
                                var ctx = $("meta[name='_ctx']").attr("content");
                                var link = ctx + "/ticket?ticketMasId=" + ticketMasId;
                                return '<a class="btn btn-info" href=' + link + ' role="button">Detail</a>';
                            }
                        }
                    ]
                });
            }
        }
    })
});

$(document).ready(function () {
    $.get('/public/keepSession',function(res){
        if(res.success){
            // console.log("User login");
        }
    });
});

function createTicket(){
    $.post('/ticket/service-identity/createQueryTicket',$('form').serialize(),function(res){
        if (res.success) {
            $(location).attr('href',ctx+'/ticket?ticketMasId='+ res.data);
        } else {
            var responseError = "The service number already exists in Ticket -";
            let ticketMasIds = res.data;
            $.each(ticketMasIds,function(index,j){
                if (index > 0) {
                    responseError += "/ ";
                }
                responseError += "<a href='"+ctx+"/ticket?ticketMasId="+j.ticketMasId+"'>"+j.ticketMasId+"</a> ";
            });
            showErrorMsg(responseError);
        }
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}
function getFinalBsn(){
    let serviceNo=$('input[name=serviceNo]').val().trim();
    let relatedBsn=$('input[name=relatedBsn]').val().trim();
    if(relatedBsn.length < 1){
        if(serviceNo.length < 1){
            showErrorMsg('No service number!');
            return;
        }else{
            return serviceNo;
        }
    } else {
        return relatedBsn;
    }

}
function reset(){
    clearAllMsg();
    $('#searchKey').val('');
    $('#searchValue').val('');
    $('form').get(0).reset();
    $('.eCloudBtn').removeData('url');
    $('.serviceButtons').find('button').attr('disabled',true);
    $("#relatedTicketTable").hide();
    $("#chkDummy").prop('checked', false);
    $('#searchValue').attr('disabled',false);
    $('#btnSearchInfo').attr('disabled',false);
    $(".dummy").hide();
    $('.panel').find('.panel-body').slideUp();
    $('.panel').find('.panel-heading').find('span').addClass('panel-collapsed');
}
function eCloudButtonCtrl(flag) {
    if (flag) {
        eCloudBtn.attr('disabled', false);
        inventoryBtn.attr('disabled', true);
        bbBtn.attr('disabled', true);
        voIpBtn.attr('disabled', true);
    }
}

function bnButtonCtrl(val){
    if(val){
        eCloudBtn.attr('disabled', true);
        inventoryBtn.attr('disabled', false);
        bbBtn.attr('disabled', false);
        voIpBtn.attr('disabled', true);
    }
}

function voIpButtonCtrl(val){
    if(val){
        eCloudBtn.attr('disabled', true);
        inventoryBtn.attr('disabled', false);
        bbBtn.attr('disabled', false);
        voIpBtn.attr('disabled', false);
    }
}
