var uploader;
var uploadLayer = null;
var succLayer = null;
var url0 = "", url1 = "", url2 =  "";
var id = "";
var item = [];
var cny = "",flag = "";
var amoutTotal = 0;
var currency = "";
var hg = 555;
$(function() {
	$(document).on('click', '.cred_up_btn', function(event) {
		
		event.preventDefault();
		/* Act on the event */
		//通过小标得到参数
		var el = $(event.target);
		var i = el.data("index");
		flag =  el.data("isupload");
		
		var item = showList.list.list[i];
		amoutTotal = item.orderPay.amount;
		
		if(item.currency == "￥" || item.currency == "CNY"){
			currency = "RMB";
		}else{
			currency = "USD";
		}
		
		var _html = "",data="",data2="",data3="";
		
		if(flag == "0"){
			hg=435;
		}
		
		if(flag == "0" || flag == "-1"){ //上传凭证  || 重新上传
			_html = '<input type="hidden" id="idx" value="'+i+'">' + 
		  			'<div class="upload_b" id="upload_file"><p class="g9">请上传付款凭证</p>	<form action="" class="credence_frm">'+
		  			'<ul class="input_list"><li><span class="label"><i class="reqd_tag">*</i>汇款人：</span><div class="iput_wrap">'+
		  			'<input type="text" id="corpName" name="corpName" class="same_input" placeholder="" maxlength="50" ></div></li>'+
		  			'<li><span class="label"><i class="reqd_tag">*</i>汇款金额：</span><div class="iput_wrap">'+
		  			'<input type="text" id="amount" name="amount" class="input_w241 l" placeholder="" maxlength="13" ><span class="unit">'+
		  			currency+'</span><i id="amountTotal" class="error" style="left: 450px; top: 2.33333px;">汇款金额不能小于订单金额</i></div></li>'+
		  			'<li><span class="label">备注信息：</span><div class="iput_wrap">'+
		  			'<input type="text" id="remarks" name="remarks" class="same_input" placeholder="注明订单号等信息" maxlength="50" >'+
		  			'</div></li><li><span class="label"><i class="reqd_tag">*</i>上传转账凭证：</span>'+
		  			'<div class="iput_wrap" id="imgWrap">'+
		  			'<img src="" class="dn" id="voucherImg" alt="" />'+
		  			'<img src="" class="dn" id="voucherImg1" alt="" onclick="showVoucherPic(\'voucherImg2\')" />'+
		  			'<div class="dn"><img src="" id="voucherImg2" alt="" onload="reSizeImg(this);" /></div><a href="#" class="upload_btn">选择文件<input type="file" name="credenceUpload" id="credFile" class="up_file">'+
		  			'</a><span class="ml5">图片格式jpg、jpeg、png、bmp，大小不超过5MB</span><i id="credFileContent" class="error" style="left: 550px; top: 2.33333px;">请上传转账凭证</i></div></li><li><div class="btn_wrap">'+
		  			'<a href="javascript:void(0);" class="sub_btn">提交</a></div></li></ul></form></div>';
		
		}else if(flag == "1"){  //查看凭证
			_html = '<div class="upload_succ"><p class="title">查看转账凭证！</p>'+
					'<ul class="succ_list">	<li><p class="g3">转账信息：</p><div><span class="f" style="height:25px;line-height:25px;">汇款人：</span>'+
					'<div class="f" style="width:634px;"><p class="credence_corpname"></p></div></div>'+
					'<p class="" style="clear:left;">汇款金额：<span class="credence_account"></span></p></li>'+
					'<li style="margin-bottom:0;overflow:hidden;"><span class="f g3" style="height:25px;line-height:25px;">备注信息：</span>'+
					'<div class="f" style="width:634px;min-width:100px;"><p class="msg"></p></div></li>'+
					'<li><p>订单号：<span class="g3" id="orderPreferenceId"></span></p></li><li><p class="g3" style="margin-bottom: 3px;">转账凭证：</p>'+
					'<div class="img_div f">'+
					'<div class="iput_wrap" id="imgWrap">'+
		  			'<img src="" class="dn" id="voucherImg" alt="" />'+
		  			'<img src="" class="dn" id="voucherImg1" alt="" onclick="showVoucherPic(\'voucherImg2\')" />'+
		  			'<div class="dn"><img src="" id="voucherImg2" alt="" onload="reSizeImg(this);" /></div>'+
					'</div></li></ul></div>';
			
		}
		
		
		currLayer = layer.open({
		  	type: 1,
		  	title:'',
		  	skin: 'cred_skin_class', 
		  	area: ['785px', hg], //上传凭证height:435px
		  	shade:[0.5],
		  	content:_html,
			success: function(layero, index){
				uploader = createUploader('credFile');
				uploader.init();
			}
		});
		
		//debugger
		if(flag ==  "1" || flag == "-1"){  //查询
			$.aAjax({
				url: ykyUrl.pay + "/v1/payment?ids="+item.orderPay.id,  //"http://192.168.1.101:9053/v1/payment", //ykyUrl.pay
				//url: "http://localhost:9070/v1/payment?ids=809686312444493824",
				type:"GET",
				//data:JSON.stringify(jsonData),
	 	  	    success: function(data){
	 	  	    	
	 	  	    	if($("#idx")[0]){  //重新上传
	 	  	    		$("#corpName").val(data.bankName);
	 	  	    		$("#amount").val(data.amount);
	 	  	    		$("#remarks").val(data.comments);
	 	  	    		var _img = $("#voucherImg1");
	 	  	    		_img[0].src = data.paymentExternalUrl;
	 	  	    		_img.removeClass("dn");
	 	  	    		$("#voucherImg2")[0].src = data.paymentExternalUrl;
	 	  	    		
	 	  	    		var _url = data.paymentExternalUrl;
	 	  	    		if(_url && _url.indexOf("?") != "-1"){
	 	  	    			url0 = _url.split("?")[0];
	 	  	    		}
	 	  	    		url1 = _url;
	 	  	    		url2 = _url;
	 	  	    		id = data.paymentId;
	 	  	    	}else{	 	//查看
	 	  	    		$(".credence_corpname").text(data.bankName);
	 	  	    		var _currency = "USD";
	 	  	    		if(data.currencyUomId == 'CNY'){
	 	  	    			_currency = "RMB";
	 	  	    		}
	 	  	    		$(".credence_account").text(data.amount + _currency);
	 	  	    		//$(".credence_account").text(data.amount);
	 	  	    		$(".msg").text(data.comments);
	 	  	    		$("#orderPreferenceId").text(item.id);
	 	  	    		var _img = $("#voucherImg1");
	 	  	    		_img[0].src = data.paymentExternalUrl;
	 	  	    		_img.removeClass("dn");
	 	  	    		$("#voucherImg2")[0].src = data.paymentExternalUrl;
	 	  	    		
	 	  	    		url0 = data.paymentExternalUrl;
	 	  	    		url1 = data.paymentExternalUrl;
	 	  	    		url2 = data.paymentExternalUrl;
	 	  	    		id = data.paymentId;
	 	  	    	}
	 	  	    	
	 	  	    },
	 	  	    error:function(e){
	 	  	    	layer.closeAll('loading');
	 	  	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	 	  	    }
			});
		}
	});
	
	//提交
	var corpName,
		amount,
		remarksString;
	$(document).on("click",".sub_btn", function(event) {
		
		event.preventDefault();
		
		var flag1 = false,flag2 = false;
		
		if(!credenceInfoVerify().form()){
			flag1 = true;
		}
		var _img = $("#voucherImg1")[0];
		if(_img.className == "dn"){
			$("#credFileContent")[0].style.display = "block";
			flag2 = true;
		}else{
			$("#credFileContent")[0].style.display = "none";
		}
		
		if(flag1 || flag2){
			return false;
		}
		
		corpName= $.trim($("input[name='corpName']").val());
		amount = $("input[name='amount']").val();
		remarksString = $("input[name='remarks']").val();
		
		//通过小标得到参数
		var idx = $("#idx").val();
		var item = showList.list.list[idx];
		
		var amount = parseFloat($("#amount").val()).toFixed(2);  //输入的值
		var a = amoutTotal.toFixed(2);  //订单值
		if(parseFloat(amount) < parseFloat(a) ){
			$("#amountTotal")[0].style.display = "block";
			var amountError = $("em[for='amount']")[0];
			if(amountError){
				amountError.style.display = "none";
			}
			return false;
		}else{
			$("#amountTotal")[0].style.display = "none";
		}
		
		if(currency == "RMB"){
			currency = "CNY";
		}
		
		var jsonData = {
				  "amount": amount,  //总金额
				  "bankName": $.trim($("#corpName").val()),   //汇款人
				  "comments": $("#remarks").val(),    //
				  "currencyUomId": currency,   //币种
				  "partyIdFrom": item.purchaserId,  //买方
				  "partyIdTo": item.vendorId,	  //卖方
				  "paymentExternalUrlThumb": url0,  //缩略图片地址
				  "paymentExternalUrl": url0,  //原图片地址
				  "paymentMethodId": item.orderPay.paymentMethodId,  //支付方式
				  "paymentPreferenceId": item.orderPay.id,      //用来说明这个支付是对应哪个订单的 ,
				  "paymentTypeId": "CUSTOMER_PAYMENT",         //支付类型（客户的付款收入）
		};
		
		var url = "",ajaxType = "";
		if(flag == "-1"){   //重新上传
			url = ykyUrl.pay + "/v1/payment/update";
			jsonData = $.extend({"paymentId":id},jsonData);
			ajaxType = "PUT";
		}else{
			url = ykyUrl.pay + "/v1/payment";
			ajaxType = "POST";
		}

		var activityIdLsit = item.activityIds ? item.activityIds.split(',') : []
		var showUnitAmount = ''

        function getUnitAmount () {
            $.aAjax({
                url: ykyUrl.pay + '/v1/coupons/orderactivity/' + item.id + '?partyId=' + item.partyId,
                data: JSON.stringify(activityIdLsit),
                type: 'POST',
                success: function (resp) {
                    var data = resp.data || []
                    var unitData = data[0] || {}
                    if (unitData.unitAmount) {
                        showUnitAmount = getUnitAmountHtml (unitData.unitAmount, unitData.couponCurrency)
                    }
                },
                complete: function () {
                    setTimeout(showDialogUnit, 10)
                }
            })
        }

        function getUnitAmountHtml (amt, currency) {
            return '<li><div class="unit-main-box">' +
                '<img class="unit-img" src="' + ykyUrl._this + '/images/unit.png" alt="优惠券">' +
                '系统已将活动下单红包（<span style="color: #fe110f;">' +
				amt +
                '</span>' + (currency === 'CNY' ? '元' : '美金') + '优惠券）发放到该账户' +
				'<br/>用户登录账号后，可在“用户中心>资产中心>我的优惠券”中查看。' +
                '</div></li>'
        }

        function showDialogUnit() {
            layer.close(currLayer);
            succLayer = layer.open({
                type: 1,
                title:' ',
                //btn:['修改','关闭'],
                //btn:['关闭'],
                skin: 'cred_skin_class succ_skin_class',
                area: ['785px', '555px'],
                shade:[0.5],
                content:'<div class="upload_succ"><p class="title">查看转账凭证！</p>'+
                '<ul class="succ_list">	<li><p class="g3">转账信息：</p><div><span class="f" style="height:25px;line-height:25px;">汇款人：</span>'+
                '<div class="f" style="width:634px;"><p class="credence_corpname"></p></div></div>'+
                '<p class="" style="clear:left;">汇款金额：<span class="credence_account"></span></p></li>'+
                '<li style="margin-bottom:0;overflow:hidden;"><span class="f g3" style="height:25px;line-height:25px;">备注信息：</span>'+
                '<div class="f" style="width:634px;min-width:100px;"><p class="msg"></p></div></li>'+
                '<li><p>订单号：<span class="g3" id="orderPreferenceId"></span></p></li><li><p class="g3" style="margin-bottom: 3px;">转账凭证：</p>'+
                '<div class="img_div" style="width: 35px; height: 35px;">'+
                '<div class="iput_wrap" id="imgWrap">'+
                '<img src="" class="dn" id="voucherImg" alt="" />'+
                '<img src="" class="dn" id="voucherImg1" alt="" onclick="showVoucherPic(\'voucherImg2\')" />'+
                '<div class="dn"><img src="" id="voucherImg2" alt="" onload="reSizeImg(this);" /></div>'+
                '</div></li>' + showUnitAmount + '</ul></div>',
                success: function(layero, index){
                    $(".credence_corpname").text(corpName);
                    var _currency = "USD";
                    if(currency == 'CNY'){
                        _currency = "RMB";
                    }
                    $(".credence_account").text(amount + _currency);
                    //$(".credence_account").text(amount+$(".input_list .unit").text());
                    $(".msg").text(remarksString);
                    $("#orderPreferenceId").text(item.id);
                    //var _img = $("#voucherImg")[0].src;  //原图路径，
                    var _img1 = $("#voucherImg1");
                    _img1[0].src = url2;
                    _img1.removeClass("dn");
                    $("#voucherImg2")[0].src = url2;

                },
                cancel:function(){
                    layer.close(succLayer);
                    location.reload()
                }
            });
        }
		
		$.aAjax({
			url: url ,//ykyUrl.transaction + "/v1/orders/"+item.id+"/receive",
			type:ajaxType,
			data:JSON.stringify(jsonData),
 	  	    success: function(data){
                getUnitAmount()
 	  	    	//layer.close(l);
 	  	    },
 	  	    error:function(e){
 	  	    	layer.closeAll('loading');
 	  	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
 	  	    }
		});
	});

	var formReset = function(obj){
		obj.find("input").val("").removeClass("error").removeClass("invalid").removeClass('valid');
		obj.find("em").remove();
	}
	var credenceInfoVerify = function(){
		return $(".credence_frm").validate({
			rules: { 
				corpName: {
					required: true,
					remitterCheck: [""]
				},
				amount: {
					required: true
				}/*,
				credFileContent:{
					required: true
				}*/
			},
			messages:{
				corpName: {
					required: "汇款人不能为空",
				},
				amount: {
					required: "汇款金额不能为空",
				}/*,
				credFileContent: {
					required: "请上传转账凭证"
				}*/
			},
			errorElement:"em",
			success:function(label,element){

				var sucessem = $(element).parents("li").find("em.success");
				if(sucessem.length > 0){
					sucessem.remove();
				}
				$(element).removeClass('invalid');  //输入成功则去除非法类
				if($("#amountTotal")[0].style.display == "block"){
					label[0].style.display = "none";
					$(label).remove();
					return false;
				}
				$(label).removeClass("error").addClass("success").addClass("icon-confirm");
				setMsgPos2(element,$(label));
				setTimeout(function(){
					$(label).remove();
				},3000);
			},
			errorPlacement:function(error,element){
				var amountTotal = $("#amountTotal")[0];
				if(amountTotal.style.display == "block"){
					amountTotal.style.display = "none";
				}
				var sucessem = $(element).parents("li").find("em.success");
				if(sucessem.length > 0){
					sucessem.remove();
				}
				error.appendTo($(element).parents("li"));
				var errorObj = $(element).parents("li").find("em.error");
				$(element).addClass('invalid');  //输入成功则去除非法类
				
				setMsgPos2(element,errorObj);
			},
			showErrors:function(errorMap,errorList){
				if(errorList.length){
					var element = $(errorList).get(0).element;
	            	var error = $($(errorList).get(0).element).parents("li").find('em.error');
	            } 
	            this.defaultShowErrors();
		 		if(errorList.length){
		 			setMsgPos2(element,error);
		 		}
			} 
		});
		/* 插件验证end */
	}

	function setMsgPos2(element,errorObj){
		var elementObj = $(element);
		var width = elementObj.innerWidth();
		var height = elementObj.innerHeight();
		var marginR = elementObj.parents(".iput_wrap").siblings('.label').css("margin-right");
		var leftPos = elementObj.parents(".iput_wrap").siblings('.label').width()+width+Number(marginR.replace("px",""))+15;
		if(elementObj.attr("name")=="amount"){
			leftPos+= 65;
		}
		if(elementObj.attr("name")=="credenceUpload"){
			leftPos+= 325;
		}
		var topPos = (height- errorObj.innerHeight())/2;
		errorObj.css('left',leftPos);
		errorObj.css('top',topPos);
	}

	//金额校验
	/* 只能输入最多六位整数和六位笑小数 */
	$(document).on("keydown","input[name='amount']", function(event) {
		var target = event.srcElement;
		if(target==null)target = event.target;
		//控制字符不能输入
		if(!(event.keyCode>=48 && event.keyCode<=57)
				//字母不能输入
				&& !(event.keyCode>=96 && event.keyCode<=117)
				//如果不是浮点数则不能输入.
				&& (!event.keyCode!=190)
				//允许输入数字和,
				&& event.keyCode!=188 && event.keyCode>46){
			/*if(layer)layer.tips('只能最多输入10位整数和2位小数', $(target),{
				tips: [1, '#b1191a'],
				time: 2000
			});*/
		}
	});

	$(document).on("keyup","input[name='amount']", function(event) {
		var $amountInput = $(this);
		//响应鼠标事件，允许左右方向键移动 
		event = window.event || event;
		if (event.keyCode == 37 | event.keyCode == 39) {
		    return;
		}
		//先把非数字的都替换掉，除了数字和. 
		$amountInput.val($amountInput.val().replace(/[^\d.]/g, "").
		//第一位不能为0
		//replace(/^[0]/g,'').
	    //只允许一个小数点              
	    replace(/^\./g, "").replace(/\.{2,}/g, ".").
	    //只能输入小数点后6位
	    replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3').
	    replace(/^(\d\d\d\d\d\d\d\d\d\d)[^.]*$/,'$1'))
	});

	$(document).on("blur","input[name='amount']", function(event) {
		var target = event.srcElement;
		if(target==null)target = event.target;
		var $amountInput = $(this);
		//最后一位是小数点的话，移除
		$amountInput.val(($amountInput.val().replace(/\.$/g, "")));
		if($amountInput.val()!=''&&Number($amountInput.val())==0){
		  	$amountInput.val('');
			if(layer)layer.tips('请输入大于0的数值', $(target),{
				tips: [1, '#b1191a'],
				time: 2000
			});
		}
	});
});
