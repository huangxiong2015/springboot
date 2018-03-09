	//收货地址
	//var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	//var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	//var isEmail = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	//var isChn = /^[a-zA-Z\u4E00-\u9FA5]{1,20}$/;
	/*收货地址校验*/
	jQuery.validator.addMethod("addrNameCheck", function(value, element, param) {
		
		var addrNameReg= /^[a-zA-Z\u4E00-\u9FA5]{1,20}$/; //
		var par = false;
		    if($.trim(value)!= ''){
				if(!AddrName.test(value)){
		 			param[0] = "请填写正确的手机号码";
		 		}else{
		 			par = true;
		 		}
		    }
		    return par;
		},  "{0}");
	
	jQuery.validator.addMethod("accountCheck", function(value, element, param) {
		var par = false;
			$.ajax({
				url: ykyUrl.party + '/v1/account/mail?account=' + value,
				type: 'GET',
				async:false,
				success: function(res){
					if(res){
						param[0] = "账户名称已存在";
					}else{
						par = true;
					}
				}
			});
			return par;
		},  "{0}");
	jQuery.validator.addMethod("addrMobileCheck", function(value, element, param) {
		
		var phoneReg= /^((\+?86)|(\(\+86\)))?(13[0-9][0-9]{8}|15[0-9][0-9]{8}|18[0-9][0-9]{8}|147[0-9]{8}|17[0-9]{9}|1349[0-9]{7})$/;
		var par = false;
		    if($.trim(value)!= ''){
				if(!phoneReg.test(value)){
		 			param[0] = "请填写正确的手机号码";
		 		}else{
		 			par = true;
		 		}
		    }
		    return par;
		},  "{0}");
	jQuery.validator.addMethod("addrPhoneCheck", function(value, element, param) {
		
		var phoneReg= /^([0-9]{3,4})?[-]?[0-9]{7,8}$/; //
		var par = false;
		    if($.trim(value)!= ''){
				if(!phoneReg.test(value)){
		 			param[0] = "请填写正确的固定号码";
		 		}else{
		 			par = true;
		 		}
		    }
		    return par;
		},  "{0}");
	jQuery.validator.addMethod("addrMailCheck", function(value, element, param) {
		
		var mailReg= /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i; //长度限制：1-50位，字符类型：中文、大/小写英文、符号、数字及其组合
		var par = false;
		    if($.trim(value)!= ''){
				if(!mailReg.test(value)){
		 			param[0] = "请填写正确的电子邮箱";
		 		}else{
		 			par = true;
		 		}
		    }
		    return par;
	},  "{0}");
	
	//汇款人
	jQuery.validator.addMethod("remitterCheck", function(value, element, param) {
		
		var corpNameReg= /^[a-zA-Z0-9\u4e00-\u9fa5\s`\-\=~\!@#$%\^&\*\(\)_\+\[\]\{\}\\\|;':",\.\/<>\?。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥×＠＃％＆＋｛｝｜／｀１２３４５６７８９０－＝’·]*$/; //长度限制：1-50位，字符类型：中文、大/小写英文、符号、数字及其组合
		var par = false;
		    if($.trim(value)!= ''){
				if(!corpNameReg.test(value)){
		 			param[0] = "汇款账号格式输入有误，请重新输入";
		 		}else{
		 			par = true;
		 		}
		    }
		 return par;
	},  "{0}");
	
	//公司官网
	jQuery.validator.addMethod("validWebSite", function(value, element, param) {
		var par = false;
		var regWebSite = /^((https|http|ftp|rtsp|mms):\/\/)?(([0-9a-z_\!~\*'\(\)\.&\=\+$%\-]+:)?[0-9a-z_\!~\*'\(\)\.&\=\+$%\-]+@)?(([0-9]{1,3}\.){3}[0-9]{1,3}|([0-9a-z_\!~\*'\(\)\-]+\.)*([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\.[a-z]{2,3})(:[0-9]{1,4})?((\/?)|(\/[0-9a-z_\!~\*'\(\)\.;\?:@&\=\+$,%#\-]+)+\/?)$/gi;
		if(value!=''&&!regWebSite.test(value)){
			param[0] = "请输入正确的网址，如http://www.yikuyi.com！";
		}else{
			param[0] = "";
			par = true;
		}

		return par;
	},  "{0}");
	
	//联系人
	jQuery.validator.addMethod("username", function(value, element, param) {

		var nameReg= new RegExp(/^[\u4E00-\u9FA5a-zA-Z_\s]+$/);
		var length = value.length;
		var par = false;
		if(value != ''){
			if(length > 20){
				param[0] = "联系人不能大于20个字符！";
				par = false;
			}else if(!nameReg.test(value)){
				param[0] = "联系人不可包含除汉字、英文和_外的字符！";
			}else{
				par = true;
			}
		}
		return par;
	},  "{0}");
	
	//电子传真
	jQuery.validator.addMethod("isFax", function(value, element, param) {
		var length = value.length;
		var mobile = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
		var par = false;
		if(value != ''){
			if(length < 7){
				param[0] = "电子传真不能少于7个字符";
			}else {
				if(!mobile.test(value)){
					param[0] = "格式错误，请输入正确格式的传真，如：400-930-0083";
				}else{
					par = true;
				}
			}
		}else{
			par = true;
		}
		return par;
	},  "{0}");

	//联系QQ
	jQuery.validator.addMethod("isqq", function(value, element, param) {
		var mobile = /^[1-9][0-9]{4,19}$/;
		var par = false;
		if(value != ''){
			if(!mobile.test(value)){
				param[0] = "由5至20位数字组成，不能以0开头！";
			}else{
				par = true;
			}
		}else{
			par = true;
		}
		return par;
	}, "{0}");
	//邮箱是否存在校验
	jQuery.validator.addMethod("existAcount2", function(value, element, param) {
	    var mobile = /^[1][34578][0-9]{9}$/;
	    var mail = /^[A-Za-z0-9d]+([-_.]+[A-Za-z0-9d]+)*@([A-Za-z0-9d]+[-.])+[A-Za-zd]{1,5}$/;
	    var returnValue = false;
	    if (mobile.test(value)) {
	        param[0] = param[0]?param[0]:"手机号码已存在";
	    } else {
	        if (mail.test(value)) {
	            param[0] = param[0]?param[0]:"邮箱地址已存在";
	        }
	    }
	    $.ajax({
	        url: ykyUrl.party + "/v1/account/" + $.base64.encode(value.trim()),
	        type: "GET",
	        async: false,
	        success: function(data) {
	            returnValue = !data;
	        },
	        error: function() {
	            console.error("判断用户名是否存在接口错误");
	        }
	    });
	    return this.optional(element) || returnValue;
	}, "{0}");
	

	//职位-其他
	jQuery.validator.addMethod("corCategoryOther", function(value, element,param) {
		var exp = /^[a-zA-Z\u4e00-\u9fa5\s`\-\=~\!@#$%\^&\*\(\)_\+\[\]\{\}\\\|;':",\.\/<>\?。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥×＠＃％＆＋｛｝｜／｀１２３４５６７８９０－＝’·]{1,30}$/;
		
		if(!exp.test(value)) {
			param[0] = '1-30位中文、英文、符号及组合';
		}
		return this.optional(element) || exp.test(value);
	},  "{0}");
	
	
	jQuery.validator.addMethod("addmaxitermlength", function( value, element, param ) {
		var length = $.isArray( value ) ? value.length : this.getLength($.trim(value), element);
		return this.optional(element) || length <= param;
	}, "最多可以选择{0}项");
	
	
	//两位小数
	jQuery.validator.addMethod("floatTwo", function(value, element,param) {
		var exp = /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/;
		
		if(!exp.test(value)) {
			param[0] = '输入只能含有两位小数';
		}
		return this.optional(element) || exp.test(value);
	},  "{0}");
	
	//不能输入中文
	jQuery.validator.addMethod("noCn", function(value, element,param) {
		var exp = /^[^\u4e00-\u9fa5]{0,}$/;
		
		if(!exp.test(value)) {
			param[0] = '只能是英文、符号、数字及其组合';
		}
		return this.optional(element) || exp.test(value);
	},  "{0}");
	
	
	//大于0的正整数
	jQuery.validator.addMethod("noZero", function(value, element,param) {
		var exp = /^\+?[1-9]\d*$/;
		
		if(!exp.test(value)) {
			param[0] = '只能输入大于0的正整数';
		}
		return this.optional(element) || exp.test(value);
	},  "{0}");
	
	//大于0的浮点数
	jQuery.validator.addMethod("floatZero", function(value, element,param) {
		var exp = /[1-9]\d*(\.\d*[1-9]{1,2})?/;
		
		if(!exp.test(value)) {
			param[0] = '只能输入大于0的数字';
		}
		return this.optional(element) || exp.test(value);
	},  "{0}");
	
	
	
	//不能输入数字
	jQuery.validator.addMethod("noNumber", function(value, element,param) {
		var exp = /^[0-9]*$/;
		var flag = false;
		if($.trim(value).length > 0){
			var valArr = value.split('');
			for(var i = 0; i < valArr.length; i++){
				if(exp.test(valArr[i])) {
					param[0] = '不能含有输入数字';
					flag = false;
					break;
				}else{
					flag = true;
				}
			}
		}
		return this.optional(element) || flag;
	},  "{0}");
	//供应商简称唯一校验
	jQuery.validator.addMethod("vendorShotNameCheck", function(value, element, param) {
		var par = false;
		var url;
			if(getQueryString('partyId')){
				url = ykyUrl.party + '/v1/vendorManage/findGroupName?groupName=' + value +'&partyId='+ getQueryString('partyId')
			}else{
				url = ykyUrl.party + '/v1/vendorManage/findGroupName?groupName=' + value
			}
			$.aAjax({
				url: url,
				type: 'GET',
				async:false,
				success: function(res){
					if(res){
						param[0] = "供应商简称已存在";
					}else{
						par = true;
					}
				}
			});
			return par;
		},  "{0}");