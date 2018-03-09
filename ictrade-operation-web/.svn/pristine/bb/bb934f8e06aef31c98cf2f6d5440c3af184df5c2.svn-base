(function () {
    Date.prototype.getWeek = function(){
        var a = ['日', '一', '二', '三', '四', '五', '六'];
        var week = this.getDay();
        return '周'+ a[week];
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
        'REFUNDED': '已退款'
    }

    var EXPRESS_MAP = {
        "SF_CARRIER": "顺丰快递",
        "KY_CARRIER": "跨越快递",
        "DHL_CARRIER": "DHL快递",
        "OTHER": "其他",
        "DB_CARRIER": "德邦快递"
    }

    var PAYMENT_MAP = {
        'ALIPAY': '支付宝支付',
        'EXT_OFFLINE': '银行汇款',
        'UNIONPAY_B2B': '企业网银支付',
        'UNIONPAY_B2C': '银联支付',
        'WECHATPAY': '微信支付',
        'TEST': '测试支付',
        'CREDITPAY': '账期'
    }

    var PARTY_ID_TO_CODE = {
        'SF_CARRIER':'SF',   //顺丰
        'KY_CARRIER':'KYSY',  //跨越速运
        'DB_CARRIER': 'DBL' //德邦
    }

    var RESTRICT_MATERIAL_MAP = {
        'P': '该产品需要特殊包装，请咨询YKY客服',
        'F': '该产品进出口需要特殊文件，请咨询YKY客服',
        'S': '限制物料'
    }

    $.aAjax({
        url: ykyUrl.transaction + "/v1/orders/" + window.location.search.split("id=")[1],
        // url: 'http://operation-sit.yikuyi.com/services-transaction/v1/orders/812345270724460544',
        success: function(data) {
            render(data)
        }
    })

    function render (data) {
        window.vm = new Vue({
            el: '#root',
            mounted: function () {
                this.$nextTick(function () {
                    document.querySelector('#root').style.display = 'block'
                })

                this.fetchExpressMessage()
            },
            data: function () {
                return {
                    order: data,
                    showInnerNoteInfo: false,
                    expressMessageArray: []
                }
            },
            computed: {
                // 计算公司名称
                companyName: function () {
                    try {
                        return this.order.shippingContactMech.contactMech.contactMechAttributes.usdCompany.value
                    }  catch (e) {
                        return ' -- '
                    }
                },

                // 计算联系人
                toName: function () {
                    try {
                        return this.order.shippingContactMech.contactMech.postalAddress.toName
                    }  catch (e) {
                        return ' -- '
                    }
                },

                // 计算收货地址
                address: function () {
                    try {
                        var address = this.order.shippingContactMech.contactMech.postalAddress;
                        return (address.countryGeoName || '') + address.provinceGeoName + address.cityGeoName + address.countyGeoName + address.address1
                    }  catch (e) {
                        return ' -- '
                    }
                },

                // 计算联系人号码
                contactNumber: function () {
                    try {
                        return this.order.selfContactMech.contactMech.telecomNumber.mobileTelecomNumber.contactNumber
                    } catch (e) {
                        return ' -- '
                    }
                },

                // 计算手机号码
                contactNumber2: function () {
                    try {
                        return this.order.shippingContactMech.contactMech.telecomNumber.mobileTelecomNumber.contactNumber
                    }  catch (e) {
                        return ' -- '
                    }
                },

                // 计算电子邮箱
                email: function () {
                    try {
                        return this.order.shippingContactMech.contactMech.email
                    }  catch (e) {
                        return ' -- '
                    }
                },

                // 计算联系人名称
                askForName: function () {
                    try {
                        return this.order.selfContactMech.contactMech.telecomNumber.mobileTelecomNumber.askForName
                    } catch (e) {
                        return ' -- '
                    }
                },

                // 计算收款类型
                paymentType: function () {
                    return PAYMENT_MAP[this.order.orderPay.paymentMethodId] || ''
                },

                // 计算快递单号
                expressId: function () {
                    try {
                        return this.order.orderShipmentPreference.trackingNumber ||  ' -- '
                    } catch (e) {
                        return ' -- '
                    }
                },

                // 计算收货单号
                deliveryId: function () {
                    try {
                        return this.order.orderShipmentPreference.shipmentNumber ||  ' -- '
                    } catch (e) {
                        return ' -- '
                    }
                },

                // 计算收货备注
                expressRemark: function () {
                    try {
                        return this.order.orderShipmentPreference.comments ||  ' -- '
                    } catch (e) {
                        return ' -- '
                    }
                },

                // 计算快递公司
                expressCompany: function () {
                    try {
                        return EXPRESS_MAP[this.order.orderShipmentPreference.carrierPartyId] ||  ' -- '
                    } catch (e) {
                        return ' -- '
                    }
                },

                // 计算是人民币还是美金
                currencyType: function () {
                    return this.order.currency === 'CNY' ? '￥' : '$'
                },

                // 计算是否是快递
                isExpress: function () {
                    return !!this.order.shippingContactMech
                },

                // 计算流程的长度
                // orderStatusHistoriesLength: function () {
                //     var orderStatusHistories = this.order.orderStatusHistories
                //     if (orderStatusHistories) {
                //         return orderStatusHistories.length
                //     }
                //
                //     return 0
                // },





                calculateOrderStatusHistories: function () {
                    // var result = false
                    var array = JSON.parse(JSON.stringify(this.order.orderStatusHistories))
                    // this.order.orderStatusHistories.forEach(function (item) {
                    //     if (item.orderStatus === 'WAIT_GOODS_APPROVE' || item.orderStatus === 'WAIT_CREDIT_APPROVE') {
                    //         result = true
                    //     }
                    // })
                    // if (result) {
                    array.shift()
                    // }

                    // 如果是账期支付就需要删除待支付的状态
                    if (this.isCreditPay) {
                        array = array.filter(function (item) {
                            return item.orderStatus !== 'WAIT_PAID'
                        })
                    }

                    // if (this.order.orderStatus === 'REFUNDED') {
                    //     array.push(array[array.length - 1])
                    // }
                    return array
                },

                // 计算流程的长度
                orderStatusHistoriesLength: function () {
                    var orderStatusHistories = this.calculateOrderStatusHistories
                    if (orderStatusHistories) {
                        return orderStatusHistories.length
                    }

                    return 0
                },





                // 是否未支付
                isNotPay: function () {
                    var status = this.order.orderStatus;
                    var result = true;
                    // Array.prototype.forEach.call(['WAIT_PAID', 'WAIT_CON_PSENT', 'PSENT_RT_FAIL', 'PAY_FAILED', 'WAIT_GOODS_APPROVE', 'WAIT_CREDIT_APPROVE'], function (item) {
                    //     if (status === item) result = true
                    // })

                    this.order.orderStatusHistories.forEach(function (item) {
                        if (item.orderStatus === 'WAIT_SHIP') result = false
                    })
                    return result
                },

                // 询报价订单
                inquiryHref: function () {
                    return ykyUrl.quotation + '/inquiries.htm?action=itemListFromOrder&ids=' + this.order.inquiryItemIds
                },

                // 状态formatter
                orderStatusFormat: function () {
                    return ORDER_STATUS_MAP[this.order.orderStatus] || ''
                },

                // 是否是物料订单
                isGoodsOrder: function () {
                    if (this.order.orderStatus === 'WAIT_GOODS_APPROVE') {
                        return true
                    }

                    var result = false

                    this.order.orderStatusHistories.forEach(function (item) {
                        if (item.orderStatus === 'WAIT_GOODS_APPROVE') {
                            result = true
                        }
                    })

                    return result
                },

                // 是否是账期订单
                isCreditPay: function () {
                   // return this.order.orderPay.paymentMethodId === 'CREDITPAY'
                   return  false
                },

                // 状态数组
                statusMap: function () {
                    var order = this.order, payStatus = this.isNotPay ? '待支付' : '已支付'
                    var result = []

                    if (order.orderStatus === 'CANCELLED') {
                        result = [
                            { className: 'icon-pending_payment', label: payStatus, orderStatus: 'WAIT_PAID' },
                            { className: 'icon-canc', label: '交易关闭', orderStatus: 'CANCELLED'}
                        ]
                    } else if (order.orderStatus === 'REFUNDED') {
                        result = [
                            { className: 'icon-pending_payment', label: payStatus, orderStatus: 'WAIT_PAID' },
                            // { className: 'icon-to_be_shipped', label: '待发货', orderStatus: 'WAIT_SHIP' },
                            { className: 'icon-refund_money', label: '已退款', orderStatus: 'REFUNDED' }
                        ]
                    } else {
                        result = [
                            { className: 'icon-pending_payment', label: payStatus, orderStatus: 'WAIT_PAID' },
                            { className: 'icon-to_be_shipped', label: '待发货', orderStatus: 'WAIT_SHIP' },
                            { className: 'icon-already_shipped', label: '待收货', orderStatus: 'WAIT_RECEIVE' },
                            { className: 'icon-complete_ok', label: '交易完成', orderStatus: 'COMPLETED' }
                        ]
                    }

                    // if (this.isNotPay) {
                    this.formatterCredit(result)
                    // }
                    this.formatterGoods(result)

                    return result
                }
            },
            methods: {
                formatterLtTime: function (item) {
                    // item.ltTime ? item.ltTime + '个工作日' : '--'
                    return item.ltTime ? item.ltTimeType === 1 ? item.ltTime + '周' : item.ltTime + '个工作日' : '--'
                },
                isOrderItemDisable: function (item) {
                    return item.approveResult === 0 || item.isInvalid === 1
                },
                restrictMaterialFormatter: function (type) {
                    return RESTRICT_MATERIAL_MAP[type] || ''
                },
                // 是否为限制物料
                isRestrictMaterial: function (type) {
                    return type === 'P' || type === 'F' || type === 'S'
                },

                getDashClass: function (item, index) {
                    var result = []
                    if (index < this.orderStatusHistoriesLength) {
                        result.push('dash-border')
                    }

                    if (this.statusMap.length >= 5) {
                        result.push('width10')
                    }

                    return result
                },
                
                formatterCredit: function (array) {
                    // 如果是账期
                    if (this.isCreditPay) {
                        array[0] = {
                            className: 'icon-already_shipped',
                            label: '账期审核',
                            orderStatus: 'WAIT_CREDIT_APPROVE'
                        }
                    }

                    return array
                },
                
                formatterGoods: function (array) {
                    // 如果是物料
                    if (this.isGoodsOrder) {

                        // 如果订单是取消的状态 并且不是账期订单,就需要删除第一个
                        // if (this.order.orderStatus === 'CANCELLED' && !this.isCreditPay) {
                        if (this.shouldShiftStatus()) {
                            array.shift()
                        }

                        array.unshift({
                            className: 'icon-already_shipped',
                            label: '物料审核',
                            orderStatus: 'WAIT_GOODS_APPROVE'
                        })
                    }

                    return array
                },
                
                shouldShiftStatus: function () {
                    // 如果不是cancel状态直接return false
                    if (this.order.orderStatus !== 'CANCELLED') {
                        return false
                    }

                    // 在交易关闭的状态，如果不是账期订单就需要删除第一个
                    if (!this.isCreditPay) {
                        return true
                    }

                    // 在交易关闭的状态，如果是账期订单，但是却在物料审核的时候就审核不通过也需要删除第一个
                    if (this.isCanceledInGoods()) {
                        return true
                    }
                },

                isCanceledInGoods: function () {
                    var result = false
                    this.order.orderStatusHistories.forEach(function (item) {
                        if (item.orderStatus === 'GOODS_APPROVE_FAIL') {
                            result = true
                        }
                    })

                    return result
                },
                
                isShowStatus: function (statusItem) {
                    return this.order.orderStatusHistories.find(function (item) {
                        return item.orderStatus === statusItem.orderStatus
                    })
                },
                
                // 转为link
                toLink:function(id){
                    return ykyUrl.portal + "/product/detail.htm?id=" + id;
                },

                fetchExpressMessage: function () {
                    var carrierPartyId = '', code = ''

                    try {
                        carrierPartyId = this.order.orderShipmentPreference.carrierPartyId
                    } catch (e) {}

                    code = PARTY_ID_TO_CODE[carrierPartyId]

                    if (!code) {
                        return
                    }

                    var self = this


                    $.aAjax({
                        url: ykyUrl.shipment+'/v1/shipment/queryShipment',
                        type:"POST",
                        data: JSON.stringify({
                            logisticCode: this.expressId,
                            shipperCode: code
                            // logisticCode: '5391923012',
                            // shipperCode: 'DBL'
                        }),
                        success: function(data) {

                            var array = data.traces || [], tmp = ''

                            self.expressMessageArray = array.map(function (item) {
                                var day = new Date(item.acceptTime.replace(/-/g,'/'))

                                var resultData = {
                                    formatDate: day.Format("yyyy-MM-dd"),
                                    formatWeek: day.getWeek(),
                                    formatTime: day.Format("hh:mm:ss"),
                                    acceptStation: item.acceptStation,
                                    dateShowTag: tmp !== day.Format("yyyy-MM-dd")
                                }

                                tmp = day.Format("yyyy-MM-dd")

                                return resultData
                            }).reverse()

                        }
                    })
                }
            }
        })
    }
})()