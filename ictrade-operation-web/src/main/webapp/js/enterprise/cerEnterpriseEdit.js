$("#corCategory").selectmenu({
		width: 250,
		change: function() {
			detailList.list.map.CORPORATION_CATEGORY_ID = this.value;
			
			//公司类型-其他
			if(this.value == 2006) {
				$('#corCategory').parents('.row').next().show();
			}else {
				$('#corCategory').parents('.row').next().hide();
			}
			$('#corCategoryOther').val('');
			$('#corCategoryOther').valid();
		},
		close: function() {
			$(this).trigger("blur");
		}
	}).selectmenu("menuWidget").attr('readonly', 'readonly')
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
			$("#Entprovince").selectmenu();
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

/*$("#personalTitle").selectmenu({
	width: 150,
	change: function() {
		//事件
		//$("#country").selectmenu("refresh");
	},
	close: function() {
		$(this).trigger("blur");
	}
}).selectmenu("menuWidget").addClass("overflow");
*/
$(function() {		
	var validate = false;
	//查询企业账号及子账号
	$.aAjax({
		url: ykyUrl.party + '/v1/enterprises/' + detailList.id + '/accounts',
		async: false,
		type: 'GET',
		//去掉数据模型中的所有函数
		success: function(data) {
			detailList.userList = data;
		}
	});
	/*//操作日志
	var listAction = [];
	listAction.push("Enterprise Modify");
	var listUrl = ykyUrl.party + "/v1/audit?actions=" + listAction +
		"&actionId=" + GetQueryString("id");
	$.aAjax({
		url: listUrl, // +window.location.search,
		type: 'GET',
		async: false,
		success: function(data) {
			if (data.length === 0)
				showList.isLoading = "暂无数据";
			detailList.operationlist = data.list;
		}
	});*/

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
});

function selectIndustryCategory(){
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
					var othervalue = getOthersValue(detailList.list.otherAttrs,'INDUSTRY_CATEGORY_ID_OTHER')?getOthersValue(detailList.list.otherAttrs,'INDUSTRY_CATEGORY_ID_OTHER'):'';
					$('.company_genre').find('div:last').append(
						'<div><input class="getother" type="text" id="otherAttr" name="otherAttr" maxlength="10" value="' +
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
}
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
	/*data: provinceList.id,*/
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
		/*data: cityList.id,*/
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
		/*data: countryList.id,*/
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
	//operationList: [],//操作记录
	corCategoryList: [],//公司类型
	corCategoryOther: '', //公司类型-其他
	industryList: [],//所属行业
	//contactInfo:[],//联系人信息
	initData:{},
	isAccountManagement:false,//判断子账号是否为可编辑
	id: getQueryString("id"),
	corporationId: getQueryString("corporationId"),
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
/*拉取数据*/
if(GetQueryString("id")){
$.aAjax({
	url: ykyUrl.party + '/v1/enterprises/entDetail/1/' +  GetQueryString("id") + '/VIP_CORPORATION',
	type: 'GET',
	/*async:false, */
	//去掉数据模型中的所有函数
	success: function(data) {
		
		  //如果存在yky编码就把保存按钮隐藏
		if(data.partyCode){
			$('#isPartyCode').show();
			$('#partyCode').hide();
			$(".saveCompany").hide();
			$('#partyCodeNew').val(data.partyCode);
			
		}else{
			$('#partyCode').show();
			$('#isPartyCode').hide();
			$(".saveCompany").show();
			$('#partyCodeNew').val($("#isPartyCode").val());
		}
		$("#Entname").val(data.name?data.name:'');
		$("#Entaddress").val(data.address?data.address:'');
		$("#comments").val(data.comments?data.comments:'');
		$("#dCode").val(data.map.D_CODE?data.map.D_CODE:'');
		$("#webSite").val(data.map.WEBSITE_URL?data.map.WEBSITE_URL:'');
		$('#industryCategory').val(data.map.INDUSTRY_CATEGORY_ID?data.map.INDUSTRY_CATEGORY_ID.split(','):'');
		data.corCategory = data.corCategory?data.corCategory:'';
		detailList.list = data;
		
		initBackData = data;
		if(data.map.REG_ADDR !="0"){
			$("#business").hide();
		}
		//选中所属行业
		var industryCategory = data.map.INDUSTRY_CATEGORY_ID?data.map.INDUSTRY_CATEGORY_ID.split(','):[];
		if(industryCategory){
			$.each(industryCategory,function(index,ele){
				$('.check-box-white').each(function() {
					var val = $(this).find('input[type=checkbox]').val();
					if (val == ele) {
						$(this).addClass('check-box-red');
					}	
					if (ele === '1008') {
						$(this).trigger('click');
						$(this).trigger('click');
					}
					if(val ==ele && val =='1008'){
						$("#otherAttr").val(data.map.INDUSTRY_CATEGORY_ID_OTHER)
						$('.company_genre').find('div:last').append(
						'<div><input class="getother" type="text" id="otherAttr" name="otherAttr" maxlength="10" value="' +
						(data.map.INDUSTRY_CATEGORY_ID_OTHER || '') + '"/></div>')
					}
				});
			})
		}
		selectIndustryCategory();
		//获取省份
		provinceList.activeProvince = data.province;
		$('#Entprovince').selectmenu('refresh');
		cityListData(data.province);

		//获取市区
		cityList.activeCity = data.city;
		$('#Entcity').selectmenu('refresh');
		countryListData(data.city);

		//获取县城
		countryList.activeCountry = data.country;
		$('#Entcountry').selectmenu('refresh');
		
		if(data.map.CORPORATION_CATEGORY_ID && data.map.CORPORATION_CATEGORY_ID === '2006') {
			corCategoryOther = data.map.CORPORATION_CATEGORY_ID_OTHER || '';
			$('#corCategoryOther').parents('.row').show();
			$('#corCategoryOther').val(corCategoryOther);
		}
		
      /*

		
		
		$.aAjax({
			url:ykyUrl.party + '/v1/customers/' + detailList.id,
			//url:'http://192.168.1.118:27082/v1/customers/861518187475238912',
			type:'GET',
			success:function(res){
				$("#name").val(res.name);
				$("#mobile").val(res.telNumber);
				$("#mail").val(res.mail);
				$("#qq").val(res.qq);
				$("#address").val(res.address);
				if(!res.personalTitle){
					res.personalTitle = '';
				}
				detailList.contactInfo = res;
				$('#personalTitle').selectmenu('refresh');
				//联系人信息省市县三级联动
				if(res.province || res.city || res.country){					
					selectArea.init("#ld",{
						provinceId: res.province?res.province:'', 
						cityId: res.city?res.city:'', 
						countyId: res.country?res.country:''
					});
				}else{
					selectArea.init("#ld");
				}
				//判断子账号管理是否可编辑
				if(detailList.list.partyCode && res.personType == "MAIN"){
					initAll();
					detailList.isAccountManagement = true;
				}else if(detailList.list.partyCode && res.personType == "COMMON"){
					detailList.isAccountManagement = true;
					$.aAjax({
						url: ykyUrl.party + 'v1/enterprises/entCertificationList?partyCode=' + detailList.partyCode,
						type:'GET',
						success:function(res){
							if(res.list.accountStatus == 'SON'){
								initAll();
							}						
						}
					})
				}
			},
			error:function(){
				console.log('error');
			}
		})*/
	},
	error:function(){
		layer.msg("网络异常，请刷新重试！")
	}
});
}
//点击保存资质
function  saveAll(){
	var partyCode= $('#partyCodeNew').val();
	var entInformation = {
			"id":detailList.id,//id
			"name": $("#Entname").val(),//公司名称
			"corCategory":$("#corCategory").val(),//公司类型
			"industryCategory":$("#industryCategory").val(),//所属行业
			"otherAttrs": [ 
			                {"INDUSTRY_CATEGORY_ID_OTHER":$("#otherAttr").val()?$("#otherAttr").val():''},
							{"CORPORATION_CATEGORY_ID_OTHER": $('#corCategoryOther').val()}
		                ], //所属行业其他
			"province": $("#Entprovince").val(),//省id
			"provinceName": $('#Entprovince option:selected').text(),//省name
			"city": $("#Entcity").val(),//市id
			"cityName": $('#Entcity option:selected').text(),//市name
			"country": $("#Entcountry").val(),//县id
			"countryName": $('#Entcountry option:selected').text(),//县name
			"address": $("#Entaddress").val(),//公司地址
			"dCode":$("#dCode").val(),//邓氏编码
			"webSite":$("#webSite").val(),//公司官网
			"reason":$("#reason").val(),//修改说明
			"comments": $("#comments").val(),//客服备注
			"partyCode":partyCode,
			 map: { //三证信息
				SOCIAL_CODE: showData.info.socialCode,
				ENT_NAME: showData.info.entName,
				LOCATION: showData.info.location,
				BUSI_RANGE: showData.info.busiRange,
				FAX_CODE: showData.info.faxCode,
				FAX_NAME: showData.info.faxName,
				ORG_CODE: showData.info.orgCode,
				ORG_LOCATION: showData.info.orgLocation,
				ORG_NAME: showData.info.orgName,
				ORG_CDATE: showData.info.orgCdate,
				ORG_LIMIT: showData.info.orgLimit,
				HK_ENT_NAME: showData.info.hkEntName,  //香港-公司名称
				HK_SIGN_CDATE: showData.info.hkSignCdate, //香港-签发日期
				HK_BUSI_NAME: showData.info.hkBusiName, //香港-业务所用名称
				HK_ADDR: showData.info.hkAddr, //香港-地址
				HK_EFFECTIVE_DATE: showData.info.hkEffectiveDate, //生效日期
				TW_ENT_NAME: showData.info.twEntName, //台湾-公司名称
				TW_SIGN_CDATE: showData.info.twSignCdate, //台湾-签发日期
				TW_FAX_ENT_NAME: showData.info.twFaxEntName, //台湾-税籍登记证-公司名称
				TW_FAX_SIGN_CDATE: showData.info.twFaxSignCdate, //台湾-税籍登记证-签发日期
				TW_FAX_CODE: showData.info.twFaxCode, //台湾-税务编码
				ABROAD_ENT_NAME: showData.info.abroadEntName, //ABROAD_ENT_NAME
				ABROAD_ENT_NUM: showData.info.abroadEntNum, //ABROAD_ENT_NUM
				REG_ADDR:showData.zzInfo.regAddr,//公司注册地
    			BUSI_LIS_TYPE:showData.zzInfo.busiLisType,//注册类型
    			BUSI_LIC_PIC:showData.zzInfo.busiLicPic,//注册类型、
    			OCC_PIC:showData.zzInfo.oocPic,//注册类型
    			TAX_REG_PIC:showData.zzInfo.taxPic,//注册类型
    			BUSI_PDF_NAME:showData.zzInfo.busiPdfName,//注册类型
    			OCC_PDF_NAME:showData.zzInfo.oocPdfName,//注册类型
    			TAX_PDF_NAME:showData.zzInfo.taxPdfName,//注册类型
    			LOA:showData.zzInfo.loaPic,//注册类型
    			LOA_PDF_NAME:showData.zzInfo.loaPdfName//注册类型

			}
		}
	
	$("#infoAllForm").submit();
	
	
	
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
	$.aAjax({//保存企业信息
		url:ykyUrl.party + "/v1/enterprises/updateQualifications",
		type: "POST",
		async: false,
		data:JSON.stringify(entInformation),
		success:function(data){
			console.log("企业信息保存成功");
			location.reload()
		},
		error:function(){
			console.log("error");
		}
	})

	
}
function  refresh(){
	location.reload()
	
}
//点击yiky编码保存
function save() {
	var partyVO = {
			partyId:detailList.id,
			codePrefix:$("#partyCode").val()
	}
	var partyCode;
	//生成yky编码
	$.aAjax({//保存企业信息
		url:ykyUrl.party + "/v1/partycode",
		type: "POST",
		async: false,
		data:JSON.stringify(partyVO),
		success:function(data){
			 partyCode= data;
			 location.reload()
		},
		error:function(){
			console.log("error");
		}
	})
	//企业信息
	var entInformation = {
			"id":detailList.id,//id
			"name": $("#Entname").val(),//公司名称
			"corCategory":$("#corCategory").val(),//公司类型
			"corCategoryOther": $('#corCategoryOther').val(), //公司类型-其他
			"industryCategory":$("#industryCategory").val(),//所属行业
			"otherAttrs": [ {"INDUSTRY_CATEGORY_ID_OTHER":$("#otherAttr").val()?$("#otherAttr").val():''} ],
			"province": $("#Entprovince").val(),//省id
			"provinceName": $('#Entprovince option:selected').text(),//省name
			"city": $("#Entcity").val(),//市id
			"cityName": $('#Entcity option:selected').text(),//市name
			"country": $("#Entcountry").val(),//县id
			"countryName": $('#Entcountry option:selected').text(),//县name
			"address": $("#Entaddress").val(),//公司地址
			"dCode":$("#dCode").val(),//邓氏编码
			"webSite":$("#webSite").val(),//公司官网
			"partyCode":partyCode
		}
	
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
				
	$.aAjax({//保存企业信息
		url:ykyUrl.party + "/v1/enterprises/editCompany",
		type: "POST",
		async: false,
		data:JSON.stringify(entInformation),
		success:function(data){
			console.log("企业信息保存成功")
			location.reload()
		},
		error:function(){
			console.log("error");
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
		Entaddress: {//公司地址
			required: true,			
		},
		webSite: {//公司官网
			validWebSite: [""]
		},
		fax: {//传真
			isFax: [""]
		},
		otherAttr: {
			required: true
		},
		partyCode:{
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
		Entaddress: {//公司地址
			required: '请完善公司地址',			
		},
		otherAttr: {
			required: "不能为空",
		},
		partyCode: {
			required: "请填写YKY客户编码前三位，点击保存将生成YKY客户编码，生成编码后将无法修改",
		},
		corCategory:{
			required: "请选择公司类型",
		},
		corCategoryOther: {
			required: "公司类型不能为空"
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



//#infoAllForm验证
$("#infoAllForm").validate({
	rules: {
		Entname: {//公司名称
			required: true,
			rangelength: [4, 50]
		},
		Entaddress: {//公司地址
			required: true,			
		},
		webSite: {//公司官网
			validWebSite: [""]
		},
		fax: {//传真
			isFax: [""]
		},
		otherAttr: {
			required: true
		},
		reason: {
			required: true
		},
		partyCode:{
			required: true
		},
		corCategory:{
			required: true
		},
		creditCode:{
			required: true
		},
		organizationCode:{
			required: true
		},
/*		businessTerm:{
			required: true
		}*/
	},
	messages: {
		Entname: {//公司名称
			required: '公司名称不能为空',
			rangelength:'公司名称长度只能在4-50位字符之间'
		},
		Entaddress: {//公司地址
			required: '公司地址不能为空',			
		},
		otherAttr: {
			required: "不能为空",
		},
		reason: {
			required: "修改原因不能为空",
		},
		partyCode: {
			required: "请填写YKY客户编码前三位，点击保存将生成YKY客户编码，生成编码后将无法修改",
		},
		corCategory:{
			required: "请选择公司类型",
		},
		creditCode:{
			required: "统一社会信用代码不能为空",
		},
		organizationCode:{
			required: "组织机构代码不能为空 ",
		},//营业期限
/*		businessTerm:{
			required: "营业期限不能为空",
		}*/
		
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

