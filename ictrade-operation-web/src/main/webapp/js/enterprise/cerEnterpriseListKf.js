var vm = '';
$(function() {
	 
	
	vm = new Vue(
			{
				el : '#cerEnterprise-list',
				data : {
					url : ykyUrl.party + "/v1/enterprises/entCertificationList?action=certificationEnt", //访问数据接口 
					queryParams : { //请求接口参数 
						size : 10, //分页参数
						page : 1, //当前页
						partyCode : '', //yky编码
						name : '', //公司名称
						corCategory : '', //公司类型
						industry : '', //行业
						industryOther: '', //行业-其他
						verifyStatus : '', //认证状态
						accountStatus : '', //开通权限-子账号
						accountPeriodStatus : '', //开通权限-账期
						province: '', //省
						city: '',	//市
						county: '', //区
						defaultStatus : true, //监测参数变化标识
					},
					initData: {
						selectedIndustry: [],	//选中的行业
						tempSelectIndustry: [],	//行业选择浮层选中临时存储选中的行业
						tempCancelSelectIndustry: [],	//行业选择浮层选中临时存储选中的行业
						otherAttr: '',
						isShowIndustryOther: false,
						companytypeList: {},	//公司类型列表
						industryList: {},	//行业列表
						activeStatusList: [{
								code: 'PARTY_VERIFIED',
								value: '通过'
							},
							{
								code: 'INVALID',
								value: '失效'
							}]
					},
					gridColumns : [ //表格列
					{
						key : 'partyCode',
						align : 'center',
						name : 'YKY客户编码',
						default : '-',
						cutstring: true
					}, 
					{
						key : 'name',
						align : 'center',
						name : '公司名称',
						default : '-',
						cutstring: true
					}, 
					{
						key : 'corCategory',
						align : 'center',
						cutstring: true,
						default : '-',
						name : '公司类型',
						callback: {
							action: 'getCompanytype',
							params: ['{corCategory}']
						}
					},
					{
						key : 'industryCategory',
						align : 'center',
						cutstring: true,
						default : '-',
						name : '行业',
						callback: {
							action: 'getIndustry',
							params: ['{industryCategory}', '{otherAttr}']
						}
					},
					{
						key : 'address',
						align : 'center',
						cutstring: true,
						default : '-',
						name : '区域'
					},
					{
						key : 'activeStatus',
						align : 'center',
						cutstring: true,
						name : '认证状态',
						text : {
							PARTY_NOT_VERIFIED : {
								'value': '未申请'
							},
							WAIT_APPROVE : {
								'value': '待审核'
							},
							WAIT_APPROVE : {
								'REJECTED': '不通过'
							},
							PARTY_VERIFIED : {
								'value': '通过'
							},
							INVALID : {
								'value': '失效'
							}
						}	
					},  
					{
						key : 'accountStatus',
						align : 'center',
						cutstring: true,
						name : '开通权限',
						callback: {
							action: 'limits',
							params: ['{accountStatus}', '{accountPeriodStatus}']
						}
					}, 
					{
						key : 'orgLimit',
						align : 'center',
						cutstring: true,
						name : '营业期限'
					},
					{
						key : 'creditComments',
						align : 'center',
						cutstring: true,
						name : '信用备注',
						default : '-'
					},
					{
						key : 'operate',
						align : 'center',
						name : '操作',
						items: [ {
							className : 'btn-detail',
							text : '详情',
							target: '_blank',
							show : true,
							href: ykyUrl._this + '/certificationEntKf.htm?action=detail&id={id}',
                            target: '_blank'
						}, {
							className : 'btn-edit',
							show : true,
							text : '编辑',
							href: ykyUrl._this + '/certificationEntKf.htm?action=edit&id={id}',
                            target: '_blank'
						}]
					}],
					pageflag : true, //是否显示分页
					showTotal: true, //显示列表总数
					refresh : false,
					selectBox:{
						 //组件使用参数
			            api: ykyUrl.database + '/v1/geos/{parentGeoId}/BELONG_TO',//设置请求城市数据接口URL
			            inputClass : 'l-input', //设置控件class
			            nameProvince : 'company[province]',
			            nameCity : 'company[city]',
			            nameDistrict : 'company[district]',
			            setProvince:'',
			            setCity:'',
			            setDistrict:'',
			            parentId: 'parentGeoId',
			            queryParam:{    //参数
			            	parentGeoId:'100000'
			            }
					}
				//重载 

				},
				created: function() {
					var _this = this;
					var categoryUrl = ykyUrl.database + '/v1/category/companytype';
					var industryUrl = ykyUrl.database + '/v1/category/industry';
					syncData(categoryUrl, 'get', {}, function(data) {
						if(data) {
							_this.initData.companytypeList = data;
						}
					});
					syncData(industryUrl, 'get', {}, function(data) {
						if(data) {
							_this.initData.industryList = data;
						}
					});
					
					//省市区
					var defaultData={
							provinceId: 0,
							cityId: 0,
							countryId:0,
							provinceText:'',
							cityText: '',
							countyText:'',
					} 
					selectArea.init('#ld',defaultData);	
				},
				filters: {
					findIndustryName: function(value) {
						var industryList = vm.initData.industryList;
						var returnValue = '';
						
						$.each(industryList, function(i, item) {
							if(item.categoryId == value) {
								returnValue = item.categoryName;
								
								if(value == '1008') {
									returnValue += '('+ vm.initData.otherAttr +')';
								}
								
								return false;
							}
						})
						
						return returnValue;
					}
				},
				watch: {
					'initData.selectedIndustry': function() {
						if(this.queryParams.industry.indexOf('1008')) {
							this.queryParams.industryOther = this.initData.otherAttr;
						}
						this.queryParams.industry = this.initData.selectedIndustry.join(',');
					}
				},
				methods : {
					//搜索方法
					onSearch : function() {

						this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
						
						/*var that = this;
						var qModel = $('#seachForm').serializeObject();
						this.queryParams.size = 10;
						this.queryParams.page = 1;
						for ( var i in qModel) {
							that.queryParams[i] = qModel[i];
						}
						LS.set("queryParams",JSON.stringify(that.queryParams));*/
					},
					//导出
					exportExcels: function() {
						var date_url = ykyUrl.party + '/v1/enterprises/certified/excel?corCategory='+ this.queryParams.industry +'&province=' + this.queryParams.province;
						$("input[name=Authorization]").val($('#pageToken').val());
					    var form=$("#exportForm");//定义一个form表单
					    form.attr("action",date_url);
					    form.submit();//表单提交
					},
					change: function (data) {//data：返回选中的结果；该方法在组件中有调用 ，必须
		                this.queryParams.province = this.province = data.province;//将结果赋值到对象province
		                this.queryParams.city = this.city = data.city;
		                this.queryParams.county = this.district = data.district;
		            },
		            //显示行业选择浮层
		            toggleIndustry: function() {
		            	var _this = this;
		            	
		            	$('.industry_content').show();
		            	
		            	$('.company_type span').removeClass('check-box-red');
		            	$('.company_type span').each(function() {
		            		var id = $(this).attr('data-id');
		            		var selectedIndustry = _this.initData.selectedIndustry;
		            		if(selectedIndustry.indexOf(id) != -1) {
		            			$(this).addClass('check-box-red');
		            		}
		            		
		            	})
		            	
		            },
		            //确认行业选择
		            confirmIndustry: function() {
		            	var _this = this;
		            	
		            	if(this.initData.otherAttr == '' && $('#industryOther').is(':visible')) {
		            		$('#industryOther').addClass('hong');
		            		return;
		            	}
		            	
		            	$.each(this.initData.tempSelectIndustry, function(i, item) {
		            		if(_this.initData.selectedIndustry.indexOf(item) == -1) {
		            			_this.initData.selectedIndustry.push(item);
		            		}
		            	})
		            	
		            	$.each(this.initData.tempCancelSelectIndustry, function(i, item) {
		            		var index = _this.initData.selectedIndustry.indexOf(item);
		            		if(index != -1) {
		            			_this.initData.selectedIndustry.splice(index, 1);
		            		}
		            	})
		            	
		            	_this.initData.selectedIndustry.sort();
		            	
		            	if(_this.initData.selectedIndustry.indexOf('1008') == -1) {
		            		_this.initData.otherAttr = '';
		            	}
		            	
		            	this.initData.tempSelectIndustry = [];
		            	this.initData.tempCancelSelectIndustry = [];
		            	$('.industry_content').hide();
		            },
		            //取消行业选择
		            cancelIndustry: function() {
		            	$('.industry_content').hide();
		            	this.initData.tempSelectIndustry = [];
		            	this.initData.tempCancelSelectIndustry = [];
		            	this.initData.isShowIndustryOther = this.initData.selectedIndustry.indexOf('1008') != -1;
		            },
		            //选择行业
		            selectIndustryItem: function(event) {
		            	var target = $(event.target).closest('.company_type').find('span');
		            	var tempSelectIndustry = this.initData.tempSelectIndustry;
		            	var tempCancelSelectIndustry = this.initData.tempCancelSelectIndustry;
		            	var id = target.attr('data-id');
		            	target.toggleClass('check-box-red');
		            	
		            	if(id == '1008') {
		            		this.initData.isShowIndustryOther = !this.initData.isShowIndustryOther;
		            	}
		            	
		            	if(target.hasClass('check-box-red')) {
		            		this.initData.tempSelectIndustry.push(id);
		            		this.initData.tempSelectIndustry.sort();
		            		
		            		this.initData.tempCancelSelectIndustry.splice(tempCancelSelectIndustry.indexOf(id), 1);
		            	}else {
		            		
		            		this.initData.tempCancelSelectIndustry.push(id);
		            		this.initData.tempCancelSelectIndustry.sort();
		            		
		            		this.initData.tempSelectIndustry.splice(tempSelectIndustry.indexOf(id), 1);
		            		
		            		if(id == '1008') {
		            			$('#industryOther').removeClass('hong');
			            		this.initData.otherAttr = '';
		            		}
		            	}
		            },
		            //移除选中
		            removeSelect: function(event) {
		            	var id = $(event.target).parent('.area-item').attr('data-id');
		            	var index = this.initData.selectedIndustry.indexOf('id');
		            	this.initData.selectedIndustry.splice(index, 1);

		            	if(id == 1008) {
		            		this.initData.isShowIndustryOther = false;
		            		this.initData.otherAttr = '';
		            	}
		            	
		            },
		            //开通权限
		            selectAuto: function(event) {
		            	var value = event.target.value;
		            	
		            	this.queryParams.accountStatus = '';
	            		this.queryParams.accountPeriodStatus = '';
            		
            			if(value == 'ACCOUNT_VERIFIED') {
            				this.queryParams.accountStatus = 'ACCOUNT_VERIFIED';
            			}else if(value == 'PERIOD_VERIFIED') {
            				this.queryParams.accountPeriodStatus = 'PERIOD_VERIFIED';
            			}
		            	
		            }
				}
			});
});

//公司类型
function getCompanytype(index, params) {
	var companyType = '';
	
	if(!params[0]) return '-';
	
	$.each(vm.initData.companytypeList, function(i, item) {
		if(item.categoryId == params[0]) {
			companyType = item.categoryName;
			return false;
		}
	})
	return companyType;
}

//行业
function getIndustry(index, params) {
	var industryList = [];
	
	if(!params[0]) return '-';
	
	$.each(params[0].split(','), function(i, item) {
		var name = industryIdToName(item, params[1]);
		industryList.push(name);
	})
	
	return industryList.join(',');
}

//根据行业ID拿到行业的名称
function industryIdToName(id, otherAttr) {
	var industryName = '';
	
	$.each(vm.initData.industryList, function(i, item) {
		if(item.categoryId == id) {
			industryName = item.categoryName;
			if(id == '1008' && otherAttr) {
				industryName += '('+ otherAttr +')';
			}
			return false;
		}
	})
	return industryName;
}

//开通权限
function limits(index, params) {
	
	var limit = [];
	var accountStatus = params[0];
	var accountPeriodStatus = params[1];
	
	if(accountStatus && accountStatus == 'ACCOUNT_VERIFIED'){
		limit.push('子账号');
	}
	if(accountPeriodStatus && accountPeriodStatus == 'PERIOD_VERIFIED'){
		limit.push('账期');
	}
	return limit.join(', ') || '-';
}

//操作-跳转审核页面
function toExamine(index, params) {
	var id = params[0].entUserId;
	var entId = params[1];
	var type = params[2];
	var applyId = params[3];
	
	var newHref = ykyUrl._this + '/verify.htm?action=examine&id='+ id +'&entId='+ entId + '&type=' + type + '&applyId=' + applyId;
	window.open(newHref);
}

//操作-跳转详情页面
function toDetail(index, params) {
	var id = params[0].entUserId;
	var entId = params[1];
	var type = params[2];
	var applyId = params[3];
	
	var newHref = ykyUrl._this + '/verify.htm?action=detail&id='+ id +'&entId=' + entId + '&type=' + type + '&applyId=' + applyId;
	window.open(newHref);
}
