$(document).ready(function() {

    $('.selectpicker').selectpicker({});

    getServiceTypeList();

    $('#btnCreateOfferName').on('click', function () {
       $('.offerName').modal('show');
   })

    $('.offerName').on('hide.bs.modal', function () {
        clearSelectPicker();
        clearFormContent($('.needs-validation').get(0));
    })

    $('#btnOffNameSubmit').on('click', function () {
        createServiceTypeMapping();
    })

    getAjaxServiceTypeMappingDataTable();
})


function clearSelectPicker() {
    document.getElementById('serviceTypeList').options.length = 0;
    document.getElementById('editServiceTypeList').options.length = 0;
    $('.selectpicker').selectpicker('refresh');
    getServiceTypeList();
}

function clearFormContent(form) {
    $(form).find('input[name=offerName]').val('');
}

function getServiceTypeList() {
    $.get('/system/service-type-mapping/service-type-list', function (res) {
        appendSelectOption(res);
    }).fail(function(e){
        var responseError = e.responseText ? e.responseText : "Get failed.";
        console.log("ERROR : ", responseError);
        showErrorMsg(responseError);
    })
}

function appendSelectOption(res) {
    for (serviceType of res) {
        $('.selectpicker').append("<option value="+serviceType.serviceTypeCode+">"+serviceType.serviceTypeName+"</option>");
    }
    $('.selectpicker').selectpicker('refresh');
}

function createServiceTypeMapping() {
    let form = $('.needs-validation').get(0);
    if (form.checkValidity()) {
        $.post('/system/service-type-mapping/create-offer-name', {
            offerName: $(form).find('input[name=offerName]').val(),
            serviceTypeCode: $(form).find('select[name=serviceType]').val()
        }, function (res) {
            if (res.success) {
                $(".offerName").modal('hide');
                showInfoMsg("create success.");
                setTimeout(function () {
                    location.reload();
                }, 1000)
            }
        }).fail(function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            $('.offerName').modal('hide');
            showErrorMsg(responseError);
        })
    }
    $(form).addClass("was-validated");
}

function updateServiceTypeMapping() {
    let form = $('#updateOfferNameForm').get(0);
    if (form.checkValidity()) {
        let offerName = $(form).find('input[name=offerName]').val();
        let oldOfferName = $(form).find('input[name=oldOfferName]').val()
        let serviceTypeCode = $(form).find('select[name=serviceType]').val();
        let oldServiceTypeCode = $(form).find('input[name=oldServiceTypeCode]').val();

        if (checkServiceTypeOfferMappingDiff(offerName, oldOfferName, serviceTypeCode, oldServiceTypeCode)) {
            $.post('/system/service-type-mapping/update-service-type-mapping', {
                offerName: offerName,
                oldOfferName: oldOfferName,
                serviceTypeCode: serviceTypeCode,
                oldServiceTypeCode: oldServiceTypeCode
            }, function (res) {
                if (res.success) {
                    $(".updateOfferName").modal('hide');
                    showInfoMsg("update success.");
                    setTimeout(function () {
                        location.reload();
                    }, 1000)
                }
            }).fail(function (e) {
                var responseError = e.responseText ? e.responseText : "Get failed.";
                console.log("ERROR : ", responseError);
                $(".updateOfferName").modal('hide');
                showErrorMsg(responseError);
            })
        }
        return;
    }
    $(form).addClass("was-validated");
}

function showUpdateOfferNameModal(serviceTypeName, offerName) {
    $('.updateOfferName').modal('show');
    
    $('#btnUpdateOfferName').on('click', function () {
        updateServiceTypeMapping();
    })

    let form = $('#updateOfferNameForm').get(0);
    $(form).find('input[name=offerName]').val(offerName);
    $(form).find('input[name=oldOfferName]').val(offerName);

    let serviceTypeCode = getServiceTypeCodeByServiceTypeName(serviceTypeName);
    $(form).find('input[name=oldServiceTypeCode]').val(serviceTypeCode);
    $('#editServiceTypeList').find('option[value=' + serviceTypeCode + ']').attr('selected', 'selected');
    $('.selectpicker').selectpicker('refresh');
}

function deleteServiceTypeOfferMapping(serviceTypeName, offerName) {
    if (confirm("Are you sure you want to delete this record?")) {
        let serviceTypeCode = getServiceTypeCodeByServiceTypeName(serviceTypeName);
        $.post('/system/service-type-mapping/delete-service-type-mapping', {
            offerName: offerName,
            serviceTypeCode: serviceTypeCode,
        }, function (res) {
            if (res.success) {
                showInfoMsg("delete success.");
                setTimeout(function () {
                    location.reload();
                }, 1000)
            }
        }).fail(function (e) {
            var responseError = e.responseText ? e.responseText : "Get failed.";
            console.log("ERROR : ", responseError);
            showErrorMsg(responseError);
        })
    }
}

function getServiceTypeCodeByServiceTypeName(serviceTypeName) {
    if (!serviceTypeName) {
        return;
    }

    let serviceTypeCode = '';

    $("#editServiceTypeList option").each(function(){
        let text = $(this).text();
        if (text) {
            if (serviceTypeName == text) {
                serviceTypeCode = $(this).val();
            }
        }
    })
    return serviceTypeCode;
}

function checkServiceTypeOfferMappingDiff(offerName, oldOfferName, serviceTypeCode, oldServiceTypeCode) {
    let isChange = true;
    if (offerName === oldOfferName && serviceTypeCode === oldServiceTypeCode) {
        isChange = false;
    }
    return isChange;
}


function getAjaxServiceTypeMappingDataTable() {
    $('#serviceTypeMpTable').DataTable({
        "order": [[ 0, "asc" ],[1,"asc"]],
        ajax: {
            type: "GET",
            contentType: "application/json",
            url: "/system/service-type-mapping/service-type-mapping-list",
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
            {data: 'serviceTypeName', name: 'serviceTypeName'},
            {data: 'offerName', name: 'offerName'},
        ],
        columnDefs: [
            {
                targets: 2,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-info' onclick='showUpdateOfferNameModal(\"" + row['serviceTypeName'] + "\",\"" + row['offerName'] +"\")' ><i class='fas fa-edit'></i>Edit</button>";
                }
            },
            {
                targets: 3,
                render: function (data, type, row, meta) {
                    return "<button class='btn btn-danger' onclick='deleteServiceTypeOfferMapping(\"" + row['serviceTypeName'] + "\",\"" + row['offerName'] +"\")' ><i class='fas fa-edit'></i>Delete</button>";
                }
            }
        ]
    });
}
