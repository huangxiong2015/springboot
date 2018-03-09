(function () {
    var PAY_MENT_MAP = {
        'ALIPAY': '支付宝',
        'EXT_OFFLINE': '银行汇款',
        'UNIONPAY_B2B': '企业网银',
        'UNIONPAY_B2C': '银联',
        'WECHATPAY': '微信',
        'TEST': '测试支付',
        'CREDITPAY': '账期'
    }

    var ORDER_STATUS_MAP = {
        'WAIT_PAID': '待支付',
        'WAIT_GOODS_APPROVE': '物料审核',
        'WAIT_CREDIT_APPROVE': '账期审核',
        'WAIT_APPROVE': '待审核',
        'REJECTED': '审核不通过',
        'WAIT_SHIP': '待发货',
        'WAIT_RECEIVE': '待收货',
        'COMPLETED': '交易完成',
        'CANCELLED': '交易关闭',
        'PSENT_RT_FAIL': '平台确认失败',
        'PAY_FAILED': '支付失败',
        'REFUNDED': '已退款',
        'WAIT_CON_PSENT': '平台确认中'
    }

    var elDialog = {
        name: 'ElDialog',
        template: `
        <Modal
            :mask-closable="false"
            v-model="showDialog"
            :loading="loading"
            title="汇款异常"
            @on-ok="onClickOk"
        >
            <Form v-if="showDialog" ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
                <Form-item label="异常原因" prop="desc">
                    <Input v-model="formValidate.desc" type="textarea" :autosize="{minRows: 5,maxRows: 10}" placeholder="请输入..."></Input>
                </Form-item>
            </Form>
        </Modal>
        `,
        data: function () {
            return {
                loading: true,
                showDialog: false,
                paymentPreferenceId: '',
                formValidate: {
                    desc: ''
                },
                ruleValidate: {
                    desc: [
                        { required: true, message: '请输入异常原因', trigger: 'blur, change' },
                        { type: 'string', max: 200, message: '介绍不能多于200字', trigger: 'blur, change' }
                    ]
                }
            }
        },
        methods: {
            show: function (id) {
                this.paymentPreferenceId = id
                this.formValidate.desc = ''
                this.showDialog = true
            },
            hide: function () {
                this.showDialog = false
            },
            onClickOk: function () {
                var self = this
                var param = {
                    paymentPreferenceId: this.paymentPreferenceId,
                    result: 0,
                    reason: this.formValidate.desc
                }
                this.$refs.formValidate.validate(function (valid) {
                    if (valid) {
                        $.aAjax({
                            url:  ykyUrl.pay + "/v1/payment?" + $.param(param),
                            type:"PUT",
                            complete: function(){
                                self.$emit('put-ok')
                                self.showDialog = false
                            }
                        });

                    } else {
                        self.loading = false
                        self.$nextTick(function () {
                            self.loading = true
                        })
                    }
                })
            }
        }
    }

    var UploadDialog = {
        template: `
        <Modal
            :mask-closable="false"
            v-model="showDialog"
            :loading="loading"
            title="汇款异常"
            @on-ok="onClickOk"
        >
            <div>
                <Form v-if="showDialog" ref="form" :model="form" :rules="ruleValidate" :label-width="90">
                    <Form-item label="汇款人" prop="corpName">
                        <Input v-show="showInput" v-model="form.corpName"></Input>
                        <span v-show="showDetail">{{form.corpName}}</span>
                    </Form-item>
                    
                    <Form-item label="汇款金额" prop="amount">
                        <Input v-show="showInput" v-model="form.amount"></Input>
                        <span v-show="showDetail">{{form.amount}}</span>
                    </Form-item>
                    
                    <Form-item label="备注信息" prop="remarks">
                        <Input v-show="showInput" v-model="form.remarks"></Input>
                        <span v-show="showDetail">{{form.remarks}}</span>
                    </Form-item>
                    
                    <Form-item label="上传转账凭证" prop="xxx">
                        <img style="width: 50px; height: 50px; cursor: pointer;" v-if="imgUrl" :src="imgUrl" alt="" @click="cacheShow = showImg = true">
                        <div v-if="cacheShow">
                            <Modal width="900" title="查看图片" v-model="showImg">
                                <img :src="imgUrl" v-if="showImg" style="width: 100%">
                            </Modal>           
                        </div>
                        
                        <Upload 
                            v-show="showInput"
                            :show-upload-list="false"
                            :action="postUrl"
                            :before-upload="beforeUpload"
                            :data="postData"
                            :on-success="onUploadSuccess"
                        >
                            <Button type="ghost" icon="ios-cloud-upload-outline">上传文件</Button>
                        </Upload>
                    </Form-item>
                </Form>
            </div>
        </Modal>
        `,
        name: 'UploadDialog',
        data: function () {
            return {
                imgUrl: '',
                cacheImgUrl: '',
                showDialog: false,
                showImg: false,
                cacheShow: false,
                loading: true,
                postUrl: '',
                postData: {},
                rowData: {},
                type: '',
                form: {
                    corpName: '',
                    amount: '',
                    remarks: '',
                    xxx: ''
                },
                ruleValidate: {
                    corpName: [
                        { required: true, message: '请输入汇款人', trigger: 'blur, change' }
                    ],
                    amount: [
                        { required: true, message: '请输入汇款金额', trigger: 'blur, change' }
                    ]
                }
            }
        },
        computed: {
            showInput: function () {
                return this.type === '上传凭证' || this.type === '重新上传'
            },
            showDetail: function () {
                return this.type === '查看凭证'
            }
        },
        methods: {
            getSubmitData: function () {
                return {
                    "amount": this.form.amount,  //总金额
                    "bankName": this.form.corpName,   //汇款人
                    "comments": this.form.remarks,    //
                    "currencyUomId": this.rowData.currency,   //币种
                    "partyIdFrom": this.rowData.purchaserId,  //买方
                    "partyIdTo": this.rowData.vendorId,	  //卖方
                    "paymentExternalUrlThumb": this.imgUrl.split('?')[0],  //缩略图片地址
                    "paymentExternalUrl": this.imgUrl.split('?')[0],  //原图片地址
                    "paymentMethodId": this.rowData.orderPay.paymentMethodId,  //支付方式
                    "paymentPreferenceId": this.rowData.orderPay.id,      //用来说明这个支付是对应哪个订单的 ,
                    "paymentTypeId": "CUSTOMER_PAYMENT"        //支付类型（客户的付款收入）
                };
            },
            show: function (rowData, type) {
                this.type = type
                this.rowData = rowData
                this.showDialog = true
                this.imgUrl = this.cacheImgUrl = ''
                var form = this.form
                for (var key in form) {
                    form[key] = ''
                }

                if (type !== '上传凭证') {
                    this.beforeShowDialog(rowData)
                }
            },
            beforeShowDialog: function (rowData) {
                var self = this
                $.aAjax({
                    url: ykyUrl.pay + '/v1/payment',
                    data: {
                        ids: rowData.orderPay.id
                    },
                    type:"GET",
                    success: function(resp){
                        self.imgUrl = resp.paymentExternalUrl

                        self.form.corpName = resp.bankName
                        self.form.amount = resp.amount + ''
                        self.form.remarks = resp.comments

                    }
                });
            },
            onClickOk: function () {
                var self = this
                var param = this.getSubmitData()

                if (this.type === '查看凭证') {
                    self.showDialog = false
                    return
                }

                this.$refs.form.validate(function (valid) {
                    if (valid) {
                        $.aAjax({
                            url: ykyUrl.pay + "/v1/payment",
                            type: "POST",
                            data: JSON.stringify(param),
                            complete: function () {
                                self.showDialog = false
                                self.$emit('put-ok')
                            }

                        });

                    } else {
                        self.loading = false
                        self.$nextTick(function () {
                            self.loading = true
                        })
                    }
                })
            },
            beforeUpload: function (file) {
                var self = this
                return new Promise(function (resolve, reject) {
                    var data = {
                        uploadType: 'payment.voucher',
                        key: file.name,
                        isImage: true
                    }

                    $.aAjax({
                        type: 'GET',
                        url: ykyUrl.database + '/v1/oss/upload',
                        dataType: 'json',
                        data: data,
                        async: true,
                        contentType: "application/json",
                        xhrFields: {
                            withCredentials: true
                        },
                        crossDomain: true,
                        success: function(data) {
                            self.nextToUploadImg(data, file)
                            self.cacheImgUrl = data.image
                            resolve()
                        },
                        error: function() {
                            reject()
                        }
                    });
                })
            },

            nextToUploadImg: function (data, file) {
                var postData = $.extend({}, data);

                postData["x-oss-security-token"] = data.securityToken;
                postData["success_action_status"] = "200";

                this.postUrl = data.host
                this.postData = postData
            },
            onUploadSuccess: function () {
                this.imgUrl = this.cacheImgUrl
            }
        }
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

	new Vue({
		el: '#order-list',
        components: {
            'ElDialog': elDialog,
            'UploadDialog': UploadDialog
        },
		data: function () {
		    var self = this
			return {
                url: ykyUrl.transaction + '/v1/orders/search',
                queryParams: {
                    size: 10,
                    page: 1,
                    defaultStatus: true
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
						keyname: '订单编号',
						type: 'text',
						key: 'orderNumber',
						name: 'orderNumber'
                	},{
                        keyname: '状态',
                        type: 'select',
                        key: 'status',
                        name: 'status',
                        options: mapToArray({
							'': '全部',
                            'WAIT_PAID': '待支付',
                            'WAIT_APPROVE': '待审核',
                            'WAIT_SHIP': '待发货',
							'WAIT_RECEIVE': '待收货',
							'WAIT_CON_PSENT': '平台确认中',
                            'PSENT_RT_FAIL': '平台确认失败',
							'PAY_FAILED': '支付失败',
							'COMPLETED': '交易完成',
							'CANCELLED': '交易关闭',
							'REFUNDED': '已退款'
						})
                    },{
                        keyname: '总价',
                        type: 'range',
                        startkey: 'totalPriceBegin',
                        startname: 'totalPriceBegin',
                        endkey: 'totalPriceEnd',
                        endname: 'totalPriceEnd'
                    },{
                        keyname: '创建时间',
                        type: 'daterange',
                        startkey: 'createDateBegin',  //star id
                        endkey: 'createDateEnd',        //end id
                        startname: 'createDateBegin',    //star name
                        endname: 'createDateEnd'
                    },{
                        keyname: '支付号',
                        type: 'text',
                        key: 'paymentNo',
                        name: 'paymentNo'
                    },{
                        keyname: '账号',
                        type: 'text',
                        key: 'account',
                        name: 'account'
                    },{
                        keyname: '支付平台',
                        type: 'select',
                        key: 'payment',
                        name: 'payment',
                        options: mapToArray({
                            '': '全部',
                            'WECHATPAY': '微信支付',
                            'ALIPAY': '支付宝',
                            'UNIONPAY_B2B': '在线支付（企业）',
                            'UNIONPAY_B2C': '在线支付',
                            'EXT_OFFLINE': '银行汇款',
                            'CREDITPAY': '账期支付'
                        })
                    },{
                        keyname: '币种',
                        type: 'select',
                        key: 'currency',
                        name: 'currency',
                        options: mapToArray({
                            '': '全部',
                            'CNY': 'RMB',
                            'USD': 'USD'
                        })
                    },{
                        keyname: '支付时间',
                        type: 'daterange',
                        startkey: 'paymentBegin',
                        endkey: 'paymentEnd',
                        startname: 'paymentBegin',
                        endname: 'paymentEnd'
                    },{
                        keyname: '订单来源',
                        type: 'select',
                        key: 'from',
                        name: 'from',
                        options: mapToArray({
                            '': '全部',
                            'inquiry': '询报价',
                            'cart': '购物车'
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
                    width: '20px'
                    // checkflag: true
                },
                {
                    key: 'index',
                    name: '序号',
                    width: '50px',
                    align: 'center'
                },{
                    key: 'id',
                    name: '订单编号',
                    width: '50px',
                    align: 'center',
                    cutstring: true
                },{
                    key: 'orderPay',
                    name: '支付号',
                    width: '50px',
                    align: 'center',
                    cutstring: true,
                    formatter: function (rowData, value) {
                        return value.id
                    }
                },{
                    key: 'orderDate',
                    name: '创建时间',
                    width: '50px',
                    align: 'center'
                },{
                    key: 'createdDate',
                    name: '支付时间',
                    width: '50px',
                    align: 'center',
                    formatter: function (rowData) {
                        return rowData.orderPay.createdDate
                    }
                },{
                    key: 'amount',
                    name: '总价',
                    width: '50px',
                    align: 'center',
                    formatter: function (rowData) {
                        return rowData.orderPay.amount
                    }
                },{
                    key: 'currency',
                    name: '币种',
                    width: '50px',
                    align: 'center',
                    formatter: function (rowData, value) {
                        return value === 'CNY' ? 'RMB' : 'USD'
                    }
                },{
                    key: 'purchaserName',
                    name: '账号',
                    width: '50px',
                    align: 'center',
                    cutstring: true
                },{
                    key: 'paymentMethodId',
                    name: '支付平台',
                    width: '50px',
                    align: 'center',
                    formatter: function (rowData) {
                        return PAY_MENT_MAP[rowData.orderPay.paymentMethodId] || ''
                    }
                },{
                    key: 'orderStatus',
                    name: '状态',
                    width: '50px',
                    align: 'center',
                    formatter: function (rowData, val) {
                        return ORDER_STATUS_MAP[val] || ''
                    }
                },{
                    key: 'uploadCred',
                    name: '转账凭证',
                    width: '50px',
                    align: 'center',
                    render: function (h, params) {
                        var rowData = params.row, label = '', renderBtn = []
                        if (rowData.orderStatus === 'WAIT_PAID' && rowData.orderPay.paymentMethodId === 'EXT_OFFLINE') {
                            label = '上传凭证'
                        }

                        if (rowData.orderStatus !== 'WAIT_PAID' && rowData.orderStatus !== 'CANCELLED' && rowData.orderPay.paymentMethodId === 'EXT_OFFLINE') {
                            label = '查看凭证'
                        }

                        if (rowData.orderStatus === 'PSENT_RT_FAIL') {
                            label = '重新上传'
                        }
                        if (label) {
                            renderBtn = [
                                h('Button', {
                                    props: {
                                        type: 'text'
                                    },
                                    on: {
                                        click: function () {
                                            self.$refs.uploadDialog.show(rowData, label)
                                            // return self.onUploadCredClick(params)
                                        }
                                    }
                                }, label)
                            ]
                        }

                        return h('div', renderBtn);
                    }
                },{
                    key: 'noteInfo',
                    name: '客户备注',
                    width: '50px',
                    align: 'center',
                    cutstring: true,
                    formatter: function (rowData) {
                        return rowData.orderNote && rowData.orderNote.noteInfo
                    }
                },{
                    key: 'orderSource',
                    name: '订单来源',
                    width: '50px',
                    align: 'center',
                    cutstring: true,
                    formatter: function (rowData) {
                        return rowData.isInquiryOrder === '1' ? '询报价' : '购物车'
                    }
                },{
                    key: 'operation',
                    name: '操作',
                    width: '50px',
                    align: 'center',
                    render: function (h, params) {
                        var rowData = params.row, label = '', renderBtn = [
                            h('Button', {
                                props: {
                                    type: 'text'
                                },
                                on: {
                                    click: function () {
                                        window.open(ykyUrl._this + '/order.htm?action=detail&id=' + rowData.id)
                                    }
                                }
                            }, '详情')
                        ]


                        if (rowData.orderStatus === 'WAIT_CON_PSENT') {
                            renderBtn.unshift(
                                h('Button', {
                                    props: {
                                        type: 'text'
                                    },
                                    on: {
                                        click: function () {
                                            return self.onReceiptFaild(rowData)
                                        }
                                    }
                                }, '收款失败')
                            )

                            renderBtn.unshift(
                                h('Button', {
                                    props: {
                                        type: 'text'
                                    },
                                    on: {
                                        click: function () {
                                            return self.onReceiptSuccess(rowData)
                                        }
                                    }
                                }, '收款成功')
                            )
                        }

                        return h('div', renderBtn)

                    }
                }]
			}
		},
		methods: {
            exportData: function (type) {
                var params = {}
                if (type === 'all') {
                    params = Object.assign({}, this.queryParams)
                } else {
                    var selectedDatas = this.$refs.lemoGrid.getChecked()
                    params.orderIds = selectedDatas.map(function (data) {
                        return data.id
                    }).join(',')
                    if (!params.orderIds) {
                        alert('请至少勾选一行')
                        return
                    }
                }

                $.aAjax({
                    url: ykyUrl.transaction + "/v1/bills/new",
                    type:"GET",
                    data: params,
                    success: function(url) {
                        window.location.href = url
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
		        this.$refs.dialog.show(rowData.orderPay.id)
            },
            onReceiptSuccess: function (rowdata) {
                var self = this
                var param = {
                    paymentPreferenceId: rowdata.orderPay.id,
                    result: 1,
                    reason: ''
                }

                this.$Modal.confirm({
                    title: '确认收到汇款吗？',
                    content: '请确认财务部已经收到相应转账汇款再进行本操作',
                    onOk: function () {

                        $.aAjax({
                            url:  ykyUrl.pay + "/v1/payment?" + $.param(param),
                            type:"PUT",
                            complete: function(){
                                self.updateGrid()
                            }
                        });

                        try {
                            //调用ga函数
                            var prodList = [];
                            var orderInfo = {                      //操作类型设置为购买
                                'id': rowdata.id,                //订单号(string类型).  必填
                                'revenue': rowdata.orderPay.amount,            //价格（总价）(string类型).
                            };

                            purchaseClick(prodList,orderInfo);
                        } catch (e) {}
                    }
                })
            }
		}
	})
})()









