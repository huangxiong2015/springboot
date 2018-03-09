var contactUserName,//申请人名称
	contactUserMail//申请人邮箱
function reImg(i) {
	var picType = '';
	reSizeImg($('#showImg' + i).get(0));
	reSizeImg($('#c' + i).get(0));

	//判断上传文件是否是PDF
	picType = $('#uploadImg' + i).data('type');
	detailList.picsList[picType] = $('#uploadImg' + i).attr('data-urlPreview');
	if ($('#uploadImg' + i).attr('data-url').indexOf('.pdf') != -1) {
		detailList.picsList[picType + 'Pdf_NAME'] = $('#uploadImg' + i).attr(
			'data-pdfName');
		detailList.picsList[picType + 'Pdf_NAME'] = $('#uploadImg' + i).attr(
			'data-pdfName');
		detailList.picsList[picType + 'Pdf'] = true;
	} else {
		detailList.picsList[picType + 'Pdf'] = false;
	}

	$('#' + picType + 'Save').val($('#uploadImg' + i).attr('data-url'));
}
//资质审核组件
zzRenzheng.init('#qual', {
	type: 0,
	busilisType:  '3-TO-1' ,
	imgSrc:{
		BUSI_LIC_PIC: '', //营业执照影印件(大陆)  /  注册证书(香港)
		TAX_REG_PIC: '', //税务登记证影印件(大陆) / 商业登记证(香港)
		OCC_PIC: '',
	}
});
//联系人地址省市县联动
selectArea.init("#ld");
$("#corCategory").selectmenu({
		width: 250,
		change: function() {
			detailList.list.map.CORPORATION_CATEGORY_ID = this.value;
			
			//公司类型-其他
			if(this.value == 2006) {
				$('#corCategory').parents('.row').next().show();
			}else {
				$('#corCategory').parents('.row').next().hide();
				$('#corCategoryOther').valid();
			}
			$('#corCategoryOther').val('');
		},
		close: function() {
			$(this).trigger("blur");
		}
	}).selectmenu("menuWidget")
	.addClass("overflow");

$("#Entprovince").selectmenu({
		width: 150,
		change: function() {
			//事件
			detailList.list.city = '';
			detailList.list.country = '';
			cityList.list=[];
			countryList.list=[];		
			cityList.activeCity='';
			countryList.activeCountry='';
			cityListData(this.value);
			$("#Entprovince").selectmenu("refresh");
		},
		close: function() {
			$(this).trigger("blur");
		}
	}).selectmenu("menuWidget")
	.addClass("overflow");

$("#Entcity").selectmenu({
		width: 150,
		change: function() {
			//事件
			countryListData(this.value);
			$("#Entcity").selectmenu("refresh");
		},
		close: function() {
			$(this).trigger("blur");
		}
	}).selectmenu("menuWidget")
	.addClass("overflow");

$("#Entcountry").selectmenu({
	width: 150,
	change: function() {
		//事件
		//$("#country").selectmenu("refresh");
	},
	close: function() {
		$(this).trigger("blur");
	}
}).selectmenu("menuWidget").addClass("overflow");

$("#personalTitle").selectmenu({
	width: 150,
	change: function() {
		//事件
		//$("#country").selectmenu("refresh");
	},
	close: function() {
		$(this).trigger("blur");
	}
}).selectmenu("menuWidget").addClass("overflow");

$(function() {		
	var validate = false;
	$.aAjax({
		url: ykyUrl.party + '/v1/customers/'+ $('#applyUserId').val() +'/username',
		type: 'GET',
		async: false,
		success: function(data) {
			if(data) {
				$("#contactUserName").val(data.lastNameLocal);
				$("#contactUserMail").val(data.mail);				
			}
		}
	})
	//获取公司类型
	$.aAjax({
		url: ykyUrl.database + '/v1/category/companytype',
		type: 'GET',
		async: false,
		success: function(data) {
			detailList.corCategoryList = data;
		}
	});
	/*获取所属行业*/
	$.aAjax({
		url: ykyUrl.database + '/v1/category/industry',
		type: 'GET',
		async: false,
		success: function(data) {
			detailList.industryList = data;
		}
	});
	/*所属行业*/
	var selected = $('#industryCategory').val().length?$('#industryCategory').val().split(','):[]; //選中的選項
	$('#company_genre .company_type').each(function() { //行业
		var $this = $(this);

		$.each(selected, function(index, item) {
			if ($this.find('input[type=checkbox]').val() == item) {
				$this.find('.check-box-white').addClass('check-box-red');
			}
		});

		$(this).find('.check-box-white').on('click', function() {
			var isSelected = $(this).hasClass('check-box-red');
			var value = $(this).find('input[type=checkbox]').val();
			if (isSelected) {
				for (var i = 0; i < selected.length; i++) {
					if (selected[i] == value) {
						selected.splice(i, 1);
						detailList.selected.remove(value);
						break;
					}
				}
			} else {
				selected.push(value);
				detailList.selected.ensure(value);
			}
			$(this).toggleClass("check-box-red");
			$('#industryCategory').val(selected.join(','));

			if (selected.join(',').indexOf("1008") !== -1) {
				if ($('.company_genre').find('div:last').find("input[type='text']").length ===
					0) {
					var othervalue = getOthersValue(detailList.list.otherAttrs,
						'INDUSTRY_CATEGORY_ID_OTHER');
					$('.company_genre').find('div:last').append(
						'<div class="rel"><input class="getother" type="text" id="otherAttr" name="otherAttr" maxlength="10" value="' +
						othervalue + '"/></div>');
				}
			} else {
				if ($('.company_genre').find('div:last').find("input[type='text']").length >
					0) {
					otherAttr = $("#otherAttr").val();
					$('.company_genre').find('div:last').remove();
					//$(".error").hide();
					$('.company_genre').find('div:last span').removeClass("mt8");
				}
			}
		});
	});
});

//获取路径中的参数值
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r !== null) return unescape(r[2]);
	return null;
}
function getOthersValue(otherValue, key) {
	var other = "";
	if (otherValue !== "" && typeof(otherValue) != "undefined") {
		for (var i = 0; i < otherValue.length; i++) {
			other = otherValue[i][key];
			if (other) {
				break;
			}
		}
	}
	if (typeof(other) == "undefined") other = "";
	return other;
}
//省
var provinceList = avalon.define({
	$id: "provinceList",
	list: [],
	activeProvince: '',
	id: getQueryString("id"),
	changeProvince: function() {
		var id = $("#Entprovince").val();
		cityListData(id);
	}
});
$.aAjax({
	url: ykyUrl.database + '/v1/geos/100000/BELONG_TO',
	data: provinceList.id,
	//去掉数据模型中的所有函数
	success: function(data) {
		provinceList.list = data;
		if (detailList.list.province) {
			provinceList.activeProvince = detailList.list.province;			
		} else {
			provinceList.activeProvince = '';
		}
		cityListData(provinceList.activeProvince);
		$("#Entprovince").selectmenu("refresh");
	}
});
//市
var cityList = avalon.define({
	$id: "cityList",
	list: [],
	activeCity: '',
	id: getQueryString("id"),
	changeCity: function() {
		var id = $("#Entcity").val();
		countryListData(id);
	}
});

function cityListData(province) {
	if(province==''){		
		return;
	}
	$.aAjax({
		url: ykyUrl.database + '/v1/geos/' + province + '/BELONG_TO',
		data: cityList.id,
		//去掉数据模型中的所有函数
		success: function(data) {
			cityList.list = data;
			if (cityList.list) {
				if (detailList.list.city) {
					cityList.activeCity = detailList.list.city;
				} else {
					cityList.activeCity = "";
				}
			}
			countryListData(cityList.activeCity);
			$("#Entcity").selectmenu("refresh");
		}
	});
}
//县
var countryList = avalon.define({
	$id: "countryList",
	list: [],
	activeCountry: '',
	id: getQueryString("id")
});

function countryListData(city) {
	if(city==""){
		$("#Entcountry").selectmenu("refresh");
		return;
	}
	$.aAjax({
		url: ykyUrl.database + '/v1/geos/' + city + '/BELONG_TO',
		data: countryList.id,
		//去掉数据模型中的所有函数
		success: function(data) {
			countryList.list = data;
			if (countryList.list) {
				if (detailList.list.city) {
					countryList.activeCountry = detailList.list.country;
				} else {
					countryList.activeCountry = '';
				}
			}
			$("#Entcountry").selectmenu("refresh");
		}
	});
}

var initBackData = [],
	validate;

var detailList = avalon.define({
	$id: "list",
	list: [],
	userList: [],//企业账号及子账号
	operationList: [],//操作记录
	corCategoryList: [],//公司类型
	corCategoryOther: '', //公司类型-其他
	industryList: [],//所属行业
	contactInfo:[],//联系人信息
	initData:{},
	isAccountManagement:false,//判断子账号是否为可编辑
	//id: getQueryString("id"),
	//corporationId: getQueryString("corporationId"),
	positionList:[{"id":"buyer","name":"采购员"},{"id":"purchasingManager","name":"采购经理"},{"id":"engineer","name":"工程师"},{"id":"other","name":"其他"}],//职业列表
	selected: [],
	status: '',
	showVoucherPic: function(id) { //查看图片
		showVoucherPic(id);
		reSizeImg($('#' + id).get(0));
	},
	toHtml: function(data) {

		if (data == 'undefined' || !data) {
			return "";
		} else {
			return data;
		}
	},
});

detailList.$watch('zztype', function(newVal) {
	$('.mainland_addr span').removeClass('selected');
	if (newVal == '1') {
		$('.mainland_addr').find('[data-val=1]').addClass('selected');
	} else {
		$('.mainland_addr').find('[data-val=0]').addClass('selected');
	}
});

detailList.$watch('list', function() {
	setTimeout(function() {
		$('#corCategory').selectmenu('refresh');
	});
});

//公司注册地select
$('#selectSite').selectmenu({
	width: 250
});
//获取路径中的参数值
function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

function save() {
	//企业信息
	var entInformation = {
			"companyName": $("#Entname").val(),//公司名称
			"corCategory":$("#corCategory").val(),//公司类型
			"corCategoryOther": detailList.corCategoryOther,//公司类型-其他
			"industryCategory":detailList.selected.join(','),//所属行业
			"otherAttr":$("#otherAttr").val(),//所属行业其他
			"enterpriseVo":{
				"province": $("#Entprovince").val(),//省id
				"provinceName": $("#Entprovince").val()?$('#Entprovince option:selected').text():'',//省name
				"city": $("#Entcity").val(),//市id
				"cityName": $("#Entcity").val()?$('#Entcity option:selected').text():'',//市name
				"country": $("#Entcountry").val(),//县id
				"countryName": $("#Entcountry").val()?$('#Entcountry option:selected').text():'',//县name
				"address": $("#Entaddress").val(),//公司地址
			},						
			"dCode":$("#dCode").val(),//邓氏编码
			"webSite":$("#webSite").val(),//公司官网
			"reason":$("#reason").val(),//修改说明
			"comments": $("#comments").val(),//客服备注
			"name":$("#name").val(),//联系人姓名
			"mobile":$("#mobile").val(),//联系人手机号
			"mail":$("#mail").val(),//联系人邮箱
			"qq":$("#qq").val(),//联系人qq
			"personalTitle":$("#personalTitle").val(),//职位
			"address":$("#address").val(),//联系人地址
			"province": $("#ld .province span").attr("data-id"),//省id
			"provinceName": $("#ld .province span").attr("data-id")?$("#ld .province span").text():'',//省name
			"city": $("#ld .city  span").attr("data-id"),//市id
			"cityName": $("#ld .city  span").attr("data-id")?$("#ld .city  span").text():'',//市name
			"country": $("#ld .county span").attr("data-id"),//县id
			"countryName": $("#ld .county span").attr("data-id")?$("#ld .county span").text():'',//县name
		}	
	//企业资质
	var entQual = {
		"REG_ADDR":$(".qual-type span").attr("data-id"),//注册地址
		"BUSI_LIS_TYPE":$(".dalu_type .radio-item .radio-selected").attr("data-name"),//营业执照类型
		"BUSI_LIC_PIC":$(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-type")=='img'?$(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-src"):$(".qual-list .qual-item .BUSI_LIC_PIC").data("url"),//营业执照
		"TAX_REG_PIC":$(".qual-list .qual-item .TAX_REG_PIC").attr("data-type")=='img'?$(".qual-list .qual-item .TAX_REG_PIC").attr("data-src"):$(".qual-list .qual-item .TAX_REG_PIC").data("url"),//税务登记
		"OCC_PIC":$(".qual-list .qual-item .OCC_PIC").attr("data-type")=='img'?$(".qual-list .qual-item .OCC_PIC").attr("data-src"):$(".qual-list .qual-item .OCC_PIC").data("url"),//组织机构
		"BUSI_PDF_NAME":$(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-type")=='pdf'?$(".qual-list .qual-item .BUSI_LIC_PIC").data("pdfname"):'',
		"TAX_PDF_NAME":$(".qual-list .qual-item .TAX_REG_PIC").attr("data-type")=='pdf'?$(".qual-list .qual-item .TAX_REG_PIC").data("pdfname"):'',
		"OCC_PDF_NAME":$(".qual-list .qual-item .OCC_PIC").attr("data-type")=='pdf'?$(".qual-list .qual-item .OCC_PIC").data("pdfname"):'',
	}
	var entQualMap={};
	//必填校验
	$("#infoForm").submit();
	if (!validate) {
		return;
	}
	if(entInformation.entInformation==''){
		$('#corCategory').valid()
		return;
	}
	if (entInformation.industryCategory.length < 1) {
		layer.msg("请选择所属行业！");
		return;
	}else{
		if(!entInformation.otherAttr){
			delete entInformation.otherAttr
		}
	}
	/*if(!entInformation.province || !entInformation.city || !entInformation.country){
		layer.msg("请完善联系地址");
		return;
	}*/
	if(entQual.REG_ADDR == '0' && entQual.BUSI_LIS_TYPE == '3-TO-1'){//大陆三证合一
//		if(!entQual.BUSI_LIC_PIC || entQual.BUSI_LIC_PIC==''){
//			layer.msg("请上传营业执照影印件!");
//		}
		entQualMap.REG_ADDR = entQual.REG_ADDR;
		entQualMap.BUSI_LIS_TYPE = entQual.BUSI_LIS_TYPE;
		entQualMap.BUSI_LIC_PIC = entQual.BUSI_LIC_PIC;
		if($(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-type")=="pdf"){
			entQualMap.BUSI_PDF_NAME = entQual.BUSI_PDF_NAME;
		}
	}else if(entQual.REG_ADDR == '0' && entQual.BUSI_LIS_TYPE == 'COMMON'){//大陆普通营业执照
//		if(!entQual.BUSI_LIC_PIC || entQual.BUSI_LIC_PIC==''){
//			layer.msg("请上传营业执照影印件!");
//			return;
//		}
//		if(!entQual.TAX_REG_PIC || entQual.TAX_REG_PIC==''){
//			layer.msg("请上传税务登记证影印件!");
//			return;
//		}
//		if(!entQual.OCC_PIC || entQual.OCC_PIC==''){
//			layer.msg("请上传组织机构代码证影印件!");
//			return;
//		}
		entQualMap.REG_ADDR = entQual.REG_ADDR;
		entQualMap.BUSI_LIS_TYPE = entQual.BUSI_LIS_TYPE;
		entQualMap.BUSI_LIC_PIC = entQual.BUSI_LIC_PIC;
		entQualMap.TAX_REG_PIC = entQual.TAX_REG_PIC;
		entQualMap.OCC_PIC = entQual.OCC_PIC;
		if($(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-type")=="pdf"){
			entQualMap.BUSI_PDF_NAME = entQual.BUSI_PDF_NAME;
		}
		if($(".qual-list .qual-item .TAX_REG_PIC").attr("data-type")=="pdf"){
			entQualMap.TAX_PDF_NAME = entQual.TAX_PDF_NAME;
		}
		if($(".qual-list .qual-item .OCC_PIC").attr("data-type")=="pdf"){
			entQualMap.OCC_PDF_NAME = entQual.OCC_PDF_NAME;
		}
	}else if(entQual.REG_ADDR == '1'){//香港
//		if(!entQual.BUSI_LIC_PIC || entQual.BUSI_LIC_PIC==''){
//			layer.msg("请上传注册证书(CR)!");
//			return;
//		}
//		if(!entQual.TAX_REG_PIC || entQual.TAX_REG_PIC==''){
//			layer.msg("请上传商业登记证(BR)!");
//			return;
//		}
		entQualMap.REG_ADDR = entQual.REG_ADDR;
		entQualMap.BUSI_LIS_TYPE = 'HK-CODE';
		entQualMap.BUSI_LIC_PIC = entQual.BUSI_LIC_PIC;
		entQualMap.TAX_REG_PIC = entQual.TAX_REG_PIC;
		if($(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-type")=="pdf"){
			entQualMap.BUSI_PDF_NAME = entQual.BUSI_PDF_NAME;
		}
		if($(".qual-list .qual-item .TAX_REG_PIC").attr("data-type")=="pdf"){
			entQualMap.TAX_PDF_NAME = entQual.TAX_PDF_NAME;
		}
	}else if(entQual.REG_ADDR == '2'){//台湾
//		if(!entQual.BUSI_LIC_PIC || entQual.BUSI_LIC_PIC==''){
//			layer.msg("请上传盈利事業登記證!");
//			return;
//		}
//		if(!entQual.TAX_REG_PIC || entQual.TAX_REG_PIC==''){
//			layer.msg("请上传稅籍登記證!");
//			return;
//		}
		entQualMap.REG_ADDR = entQual.REG_ADDR;
		entQualMap.BUSI_LIS_TYPE = 'TW-CODE';
		entQualMap.BUSI_LIC_PIC = entQual.BUSI_LIC_PIC;
		entQualMap.TAX_REG_PIC = entQual.TAX_REG_PIC;
		if($(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-type")=="pdf"){
			entQualMap.BUSI_PDF_NAME = entQual.BUSI_PDF_NAME;
		}
		if($(".qual-list .qual-item .TAX_REG_PIC").attr("data-type")=="pdf"){
			entQualMap.TAX_PDF_NAME = entQual.TAX_PDF_NAME;
		}
	}else if(entQual.REG_ADDR == '3'){//境外
//		if(!entQual.BUSI_LIC_PIC || entQual.BUSI_LIC_PIC==''){
//			layer.msg("请上传CERTIFICATE OF INCORPORATION!");
//			return;
//		}
		entQualMap.REG_ADDR = entQual.REG_ADDR;
		entQualMap.BUSI_LIS_TYPE = 'ABROAD-CODE';
		entQualMap.BUSI_LIC_PIC = entQual.BUSI_LIC_PIC;
		if($(".qual-list .qual-item .BUSI_LIC_PIC").attr("data-type")=="pdf"){
			entQualMap.BUSI_PDF_NAME = entQual.BUSI_PDF_NAME;
		}
	}
	$.aAjax({//保存企业信息
		url:ykyUrl.party + "/v1/customers/account",
		type: "POST",
		async: false,
		data:JSON.stringify(entInformation),
		success:function(data){
			if(data.entId && data.userId){
				var content = {
						"id" : data.entId,
						"map": entQualMap,
						"name": $("#Entname").val(),//公司名称
						"contactUserName":$("#contactUserName").val(),//联系人信息
						"mail":$("#contactUserMail").val(),//邮箱
						"applyUser" : $("#name").val(),
		        		"applyMail" : $("#mail").val(),
		        		"applyInformation" : $("#mobile").val(),
				};
				var jsonVal = {
						"applyContent":JSON.stringify(content),
						"applyUserId":$('#applyUserId').val(),
						"processId":"ORG_DATA_REVIEW",//资质审核流程
						"applyOrgId": data.entId,
						"reason":"",
						"applyPageUrl":"",
						"callBackUrl":"",
					};
				//content.id = data.entId;
				//jsonVal.applyOrgId = data.entId
				$.aAjax({//保存企业资质
					url:ykyUrl.party + "/v1/enterprises/editEntApply",
					type: "POST",
					async: false,
					data:JSON.stringify(jsonVal),
					success:function(res){
						layer.msg("保存成功！")
						setTimeout(function(){
							location.href = location.pathname +'?action=detail&id=' + data.userId +'&corporationId=' + data.entId;
						},2000)						
					},
					error:function(){
						location.href = location.pathname +'?action=entEdit&id=' + data.userId +'&corporationId=' + data.entId;
					}
				})
			}			
		},
		error:function(){
			layer.msg("网络异常，保存失败！")
		}
	})
}
//验证
$("#infoForm").validate({
	rules: {
		Entname: {//公司名称
			required: true,
			rangelength: [4, 50]
		},
		/*Entaddress: {//公司地址
			required: true,			
		},*/
		name: {//联系人姓名
			required: true,
			username: [""]
		},
		mobile:{//联系人手机号
			required: true,
			addrMobileCheck:['']
		},
		mail:{//联系人邮箱
			required: true,
			addrMailCheck:[''],
			existAcount2:['']
		},
		/*address: {//联系人地址
			required: true
		},*/
		webSite: {//公司官网
			validWebSite: [""]
		},
		fax: {//传真
			isFax: [""]
		},
		qq: {
			isqq: [""]
		},
		otherAttr: {
			required: true
		},
		reason: {
			required: true
		},		
		corCategory:{
			required: true
		},
		corCategoryOther:{ //公司类型-其他
			required: function() {
				return $('#corCategory').val() == 2006;
			},
			corCategoryOther: [""]
		} 
	},
	messages: {
		Entname: {//公司名称
			required: '公司名称不能为空',
			rangelength:'公司名称长度只能在4-50位字符之间'
		},
		/*Entaddress: {//公司地址
			required: '公司地址不能为空',			
		},*/
		name: {//联系人姓名
			required: '联系人姓名不能为空',
		},
		mobile:{//联系人手机号
			required: '联系人手机号码不能为空',			
		},
		mail:{//联系人邮箱
			required: '联系人邮箱不能为空',
		},
		/*address: {//联系人地址
			required: '联系人地址不能为空'
		},*/				
		otherAttr: {
			required: "不能为空"
		},
		reason: {
			required: "修改原因不能为空"
		},
		corCategory:{
			required: "请选择公司类型"
		},
		corCategoryOther: {
			required: '公司类型不能为空'
		}
	},
	errorElement: "em",
	success: function(label) {
		$(label).removeClass("error icon-triangle-leftmin"); // speech-bubble speech-bubble-left

		$(label).addClass("icon-confirm");
		if ($(label).parent().find('input').val() === '') {
			$(label).removeClass("icon-confirm");
		}
	},
	errorPlacement: function(error, element) {
		var f = error[0].getAttribute("for");
		var ele = $("em[for=" + f + "]");
		if (ele.length > 0) {
			ele[0].remove();
		}
		error.appendTo(element.parent());
		$(error).addClass("error icon-triangle-leftmin");
	},
	submitHandler: function() {
		validate = true;
	}
});

//联系人去掉收尾空格
$('#contactUserName').on('blur', function() {
	var name = $.trim($(this).val());
	$(this).val(name);
});
