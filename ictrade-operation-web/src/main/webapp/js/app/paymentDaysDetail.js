(function () {
    var ORDER_STATUS_MAP = {
        'WAIT_SHIP': '待发货',
        'WAIT_RECEIVE': '待收货',
        'COMPLETED': '交易完成'
    }
    //发票状态
    var INVOICE_STATUS_MAP = {
    		'INVOICE_APPROVED': '待开票',
    		'INVOICE_CANCELLED': '待开票',
    		'INVOICE_IN_PROCESS': '待开票',
    		'INVOICE_PAID': '待开票',
    		'INVOICE_READY': '待开票',
    		'INVOICE_WRITEOFF': '待开票',
    		'INVOICE_WAIT_APPLY': '待开票',
    		'INVOICE_APPLIED': '待开票',
    		
    		'INVOICE_WAIT_SENT': '待寄送',
    		
    		'INVOICE_SENT': '已寄送',
    		'INVOICE_RECEIVED': '已寄送',
    		'ELECTRONIC': '--'
    		
    }
    
    //支付状态
    var PAY_STATUS_MAP = {
    		'PAY': '已支付',
    		'UNPAY': '未支付'
    }

	function mapToArray (obj) {
		var result = []
		for (var key in obj) {
			result.push({
				value: key,
				text: obj[key]
			})
		}

		return result
	}

	window.vm = new Vue({
		el: '#order-list',
      /*  components: {
            'ElDialog': elDialog,
            'UploadDialog': UploadDialog
        },*/
		data: function () {
		    var self = this
			return {
                btnType: true,
                url: ykyUrl.transaction + '/v1/orders/terms',
                queryParams: {
                	defaultStatus: false,
                    searchType:'OPERATE',
                    partyId: $('#enterpriseId').val()
                },
                pageflag: true,
                refresh: false,
                showTotal: true,
                showReceiptFaild: false,
                checkflag: true,
                formData: {
					id: 'search-form',
					data: '',
                    columnCount: 3,
					fields: [{
                        keyname: '交易时间',
                        type: 'daterange',
                        startkey: 'orderCreateDateBegin',  //star id
                        endkey: 'orderCreateDateEnd',        //end id
                        startname: 'orderCreateDateBegin',    //star name
                        endname: 'orderCreateDateEnd'
                    },{
						keyname: '订单编号',
						type: 'text',
						key: 'orderIds',
						name: 'orderIds'
                	},{
                        keyname: '账号',
                        type: 'text',
                        key: 'account',
                        name: 'account'
                    },{
                        keyname: '发货时间',
                        type: 'daterange',
                        startkey: 'deliveryDateBegin',  //star id
                        endkey: 'deliveryDateEnd',        //end id
                        startname: 'deliveryDateBegin',    //star name
                        endname: 'deliveryDateEnd'
                    },{
                        keyname: '状态',
                        type: 'select',
                        key: 'status',
                        name: 'status',
                        options: mapToArray({
							'': '全部',
                            'WAIT_SHIP': '待发货',
							'WAIT_RECEIVE': '待收货',
							'COMPLETED': '交易完成'
						})
                    }],
                    buttons:[{
                        text: '查询',
                        type: 'submit',
                        id: '',
                        name: '',
                        inputClass: 'btn-danger',
                        callback: {
                            action: 'on-search'
                        }
                    }]
				},
                gridColumns: [
                {
                    key: 'selected',
                    name: '',
                    cutstring: true,
                    width: '20px'
                    // checkflag: true
                },
                {
                    key: 'index',
                    name: '序号',
                    width: '50px',
                    cutstring: true,
                    align: 'center'
                },{
                    key: 'createdDate',
                    name: '交易时间',
                    width: '50px',
                    cutstring: true,
                    align: 'center'
                },{
                    key: 'purchaserName',
                    name: '交易账号',
                    width: '50px',
                    cutstring: true,
                    align: 'center'
                },{
                	key: 'id',
                	name: '订单编号',
                	width: '50px',
                	align: 'center',
                	cutstring: true
                },{
                    key: 'orderShipmentCreatedDate',
                    name: '发货时间',
                    width: '50px',
                    cutstring: true,
                    align: 'center',
                    formatter: function (rowData) {
                    	var payDate=rowData.orderShipmentPreference;
                        if(payDate){
                        	return payDate.createdDate;
                        }
                    }
                },{
                    key: 'remainingSubTotal',
                    name: '订单金额',
                    width: '50px',
                    align: 'center',
                    cutstring: true,
                    formatter: function (rowData) {
                    	var remainingSubTotal = rowData.remainingSubTotal,
                    	 	currency=rowData.currency;
                        if(remainingSubTotal && currency){
                        	return remainingSubTotal+"("+(currency=='CNY'?"RMB":currency)+")";
                        }
                    }
                },{
                    key: 'orderStatus',
                    name: '订单状态',
                    width: '50px',
                    align: 'center',
                    cutstring: true,
                    formatter: function (rowData, val) {
                        return ORDER_STATUS_MAP[val] || ''
                    }
                },{
                    key: 'invoiceStatus',
                    name: '开票状态',
                    width: '50px',
                    align: 'center',
                    cutstring: true,
                    formatter: function (rowData, val) {
                        return INVOICE_STATUS_MAP[val] || '已开票'
                    }
                },{
                    key: 'havePay',
                    name: '是否付款',
                    width: '50px',
                    align: 'center',
                    cutstring: true,
                    formatter: function (rowData, val) {
                        return PAY_STATUS_MAP[val]
                    }
                },{
                    key : 'operate',
                    name : '操作',
                    width: '100px',
                    align : 'center',
                    items : [ {
                        className : 'btn-edit',
                        text : '详情',
                        show : true,
                        href : ykyUrl._this+'/order.htm?action=detail&id={id}',
                        target : '_blank'
                    }, {
                        className : 'btn-delete',
                        show : "'{havePay}'=='UNPAY'",
                        text : '销账',
                        callback : {
                            confirm : {
                                title : '销账',
                                content : '确认销账？'
                            },
                            action : 'doWriteOff',
                            params : ['{id}']
                        }
                    }]
                }]
			}
		},
		methods: {
            onSaveModifyClick: function () {
                if (!this.btnType) {
                    var creditQuota = parseFloat($("#creditQuota").text())
                    var modifyValue = parseFloat($("#realtimeCreditQuotaInput").val())
                    if (isNaN(modifyValue)) {
                        layer.msg("请输入授信余额", {icon: 2});
                        return
                    }
                    if (modifyValue > creditQuota) {
                        layer.msg("账户余额不能大于授信余额", {icon: 2});
                        return
                    }
                    this.changeCreditQuota();
                }
                this.btnType = !this.btnType
            },
            changeCreditQuota: function () {
                  var realtimeCreditQuota = $("#realtimeCreditQuotaInput").val();
                  var partyId= $('#enterpriseId').val();
                  var currency = $('#currency').val();
                  $.aAjax({
            		  type: "PUT",
            		  url: ykyUrl.party + '/v1/person/credit',
            		  data:JSON.stringify({"realtimeCreditQuota":realtimeCreditQuota,"partyId":partyId,"currency":currency}),
            		  contentType:'application/json',
            		  success: function(data){
            			  $("#realtimeCreditQuota").html(realtimeCreditQuota);
            			  layer.msg("修改成功", {icon: 1});
            		  },
            		  error:function(e){
            			  layer.msg(JSON.parse(e.responseText).message, {icon: 2});
            		  }
            	});
                  
            },
            exportData: function (type) {
                    var params = {},orderIds;
                    var selectedDatas = this.$refs.lemoGrid.getChecked()
                    params.orderIds = selectedDatas.map(function (data) {
					                        return data.id
					                  }).join(',')
					 
                    if (!params.orderIds) {
                        layer.msg('请至少勾选一行', {time: 2000})
                        return
                    }
                    orderIds = params.orderIds;              
                $.aAjax({
                    url:ykyUrl.transaction + "/v1/bills/term/export?searchType=OPERATE&orderIds="+orderIds,
                    type:"GET",
                    success: function(url) {
                        window.location.href = url
                    },
                    error:function(e) {
                        console.log(e);
                    }
                })
            },
            onPutOk: function () {
                this.updateGrid()
            },
		    updateGrid: function () {
                this.refresh = !this.refresh
            },
            onUploadCredClick: function (data) {
                alert(data.index)
            },
            onSearchClick: function (searchParam) {
                var queryParams = this.queryParams
                queryParams.pageSize = 10
                queryParams.page = 1
                queryParams.defaultStatus = !queryParams.defaultStatus

                for (var key in searchParam) {
                    queryParams[key] = searchParam[key] ? searchParam[key] : undefined
                }
			},
            onReceiptFaild: function (rowData) {
                var self = this
		        this.$refs.dialog.show(rowData.id)
            },
            onReceiptSuccess: function (rowdata) {
                var self = this
                var param = {
                    paymentPreferenceId: rowdata.id,
                    result: 1,
                    reason: ''
                }

                this.$Modal.confirm({
                    title: '确认收到汇款吗？',
                    content: '请确认财务部已经收到相应转账汇款再进行本操作',
                    onOk: function () {
                    	//调用ga函数
                    	var prodList = [];
                    	var orderInfo = {                      //操作类型设置为购买     
                      		  'id': rowdata.id,                //订单号(string类型).  必填  
                      		  'revenue': rowdata.orderPay.amount,            //价格（总价）(string类型).     
                      		};
                    	
                    	purchaseClick(prodList,orderInfo);
                    	
                    }
                })
            }
		}
	})
	
	$.aAjax({
		  type: "GET",
		  url: ykyUrl.party + '/v1/enterprises/' + $('#enterpriseId').val() + '/credit',
//		  url: ykyUrl.party + '/v1/enterprises/890037494542761984/credit',
		  contentType:'application/json',
		  success: function(data){
			  if(data){
				  $("#creditQuota").text(data.creditQuota); //授信额度
				  $("#currency").text(data.currency); //币种
				  $("#realtimeCreditQuota").text(data.realtimeCreditQuota);// 授信余额
				  $("#creditDeadline").text(data.creditDeadline);// 授信期限
				  $("#checkDate").text(data.checkDate);// 对账日期
				  $("#checkCycle").text(data.checkCycle);// 对账周期
				  $("#payDate").text(data.payDate);// 付款日期
				  $("#groupName").text(data.groupName);// 公司名称
				  var tempStatus=data.accountPeriodStatus;
				  if(tempStatus=='PERIOD_VERIFIED'){
					  $("#accountPeriodStatus").text("正常");// 状态
					  $("#accountPeriodStatus").addClass("normal")
				  }else if(tempStatus=='PERIOD_DISABLED'){
					  $("#accountPeriodStatus").text("冻结");// 状态
					  $("#accountPeriodStatus").addClass("frozen")
				  }
			  }
		  },
		  error:function(e){
			  layer.msg(JSON.parse(e.responseText).message, {icon: 2});
		  }
	});
	
     
     
})();
//销账
function doWriteOff(index, params) {//销账
	syncData(ykyUrl.pay + "/v1/accountperiod/writeOff", 'PUT',params, function(res, err) {//页面加载前调用方法
				console.log(res);
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;//重载
				}.bind(vm), 400);
				
				layer.close(index);
			});
	console.log(params);
}
