var vm;
var addData;
var recommendationId = getQueryString("recommendationId");
var validate= false;
vm = new Vue({
    el: '#advertise_edit',
    data: {
    	initData: {
    		category: [{categoryId: '6001', categoryName: '图片广告'}],
    		page: [{pageId: '7001', pageName: '首页'}, {pageId: '7002', pageName: '登录页'},{pageId: '7003', pageName: '商品详情页'}],	//广告页面
    		position: [{positionId: '8001', positionName: '顶部'},{positionId: '8002', positionName: '侧边栏'}]
    	},
    	pageTitle: '新增',
    	title: '',	//广告标题
    	categoryId: '6001', //广告类型
    	pageId: '7001',	//广告页面
    	positionId: '8001', //广告位置
    	startDate: '',  //开始时间
    	expiryDate: '', //结束时间
		proportion: '', //曝光占比
		image: '',	//广告效果图
		background: '',	//背景色
		linkUrl: '',	//广告链接
		categoryName: '图片广告',	//广告类型Value
		pageName: '首页',	//广告页面Value
		positionName: '顶部', //广告位置Value
		distributorDatakeyname: '选择供应商', //供应商组件
		distributorDatavalidate:{
            "required": true
        },
        distributorDataid:'selbox',
        distributorDataname:'selbox',
        distributorDataoptions:[

        ],
        distributorDataoptionId: 'id',
        distributorDataoptionName: 'name',
        distributorDataselected:[],
        distributorDatamultiple:true,   //复选
        distributorDataplaceholder:'搜索供应商',
        distributorDatasels:[],
        distributorDatasels1:[],
        manufacturerDatakeyname: '选择制造商',
        manufacturerDatavalidate:{
            "required": true
        },
        manufacturerDataid:'selbox',
        manufacturerDataname:'selbox',
        manufacturerDataoptions:[
                 
        ],
        manufacturerDataoptionId: 'id',
        manufacturerDataoptionName: 'brandName',
        manufacturerDataselected:[],
        manufacturerDatamultiple:true,   //复选
        manufacturerDataplaceholder:'搜索制造商',
        manufacturerDatasels:[],
        manufacturerDatasels1:[],
        manufacturerIsFuzzySearch:true, //制造商模糊搜索
        manufacturerReloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}', //模糊搜索api
        showManufacturerSelect:false
	},
	mounted: function() {
		datetimeInit();
		
		this.startDate = moment().format('YYYY-MM-DD H:00:00');
		this.expiryDate = moment().format('YYYY-MM-DD H:00:00');
		
		if(recommendationId) {
			this.pageTitle = '编辑';
			queryInfo();
			$('#pageId').attr('disabled', true);
			$('.breadcrumb .active').text('广告位编辑');
			$('title').text('广告位-编辑');
		}
	},
	created:function(){
		getSupplier();
		getManufacturer();
	},
	watch: {
		pageId: function(val, oldVal) {
			var $this = this;
			$.each(this.initData.page, function(i, item) {
				if(item.pageId == val) {
					
					if(item.pageId == '7002') {
						$this.positionName = '';
					}
					
					$this.pageName = item.pageName;
				}
			}) 
			//登录页的广告位置,详情页的顶部只有顶部
			if(val=='7002'||val=='7003'){
				$this.positionId = '8001';
			}
		},
		positionId:function(val,oldVal){
			var $this = this;
			$.each(this.initData.position, function(i, item) {
				if(item.positionId == val) {
					$this.positionName = item.positionName;
				}
			})
			if($this.pageId == '7002') {  //登录页的positionName为空
				$this.positionName = '';
			}
		}
	},
	methods : {
		//广告链接格式化
		linkUrlFormat: function() {
			if(!this.linkUrl) return;
			
			if(this.linkUrl.indexOf('http://') == 0 || this.linkUrl.indexOf('https://') == 0) {
				this.linkUrl = this.linkUrl.replace(/^(http:)|(https:)/, '');
			}else if(this.linkUrl.indexOf('//') != 0) {
				this.linkUrl = '//' + this.linkUrl;
			}
		},
		//取消
		cancel: function() {
			history.go(-1);
		},
		//保存
		save: function() {
			save();
		},
	  distributorDatagetSelected: function(obj){  //获取选中值
		   this.distributorDatasels=obj;
	  },
	  distributorDatadel: function (id) {
	      vm.$refs.distributorDataletter.del(id);
	  },
	  distributorDatadel1:function(id){
		  var that = this;
		  var temp = [];
		  $.each(that.distributorDatasels1,function(i,item){
			  if(id!=item.id){
				  temp.push(item);
			  }
		  })
		  that.distributorDatasels1 = temp;
		  /*that.distributorDatasels = temp;*/
		  that.distributorDatadel(id);
	  },
	  choosedistributor:function(){
		  var that = this;
		  layer.open({
				type: 1,
				title: '请选择供应商',
		area: ['1000px','600px'],
		offset: '100px',
		move: false,
		skin: "s_select_data",
		content: $("#distributorApp"),
		btn: ['确认', '取消'],
				yes:function(){
					layer.closeAll();
					that.changedistributor();
				},
				cancel:function(){
					that.distributorDatasels = JSON.parse(JSON.stringify(that.distributorDatasels1));
					var arr = [];
					$.each(that.distributorDatasels,function(i,e){
						if(e.id){
							arr.push(e.id);
						}
					})
					that.distributorDataselected = arr;
				}
			})
	  },
	  changedistributor:function(){
		  var that = this;
		  that.distributorDatasels1 = that.distributorDatasels; 
	  },
	  manufacturerDatagetSelected: function(obj){  //获取选中值
         this.manufacturerDatasels=obj;
	  },
	  manufacturerDatadel: function (id) {
          vm.$refs.manufacturerDataletter.del(id);
      },
      manufacturerDatadel1:function(index){
		  this.manufacturerDatasels1.splice(index,1);
		  this.manufacturerDatasels.splice(index,1);
		  this.manufacturerDataselected.splice(index,1);
	  },
      chooseManufacturer:function(){
    	  var that = this;
		  that.showManufacturerSelect = true;
    	  layer.open({
				type: 1,
				title: '请选择制造商',
				area: ['1000px','500px'],
				offset: '100px',
				move: false,
				//skin: "s_select_data",
				content: $("#manufacturerApp"),
				btn: ['确认', '取消'],
				yes:function(){
					layer.closeAll();
					that.changeManufacturer();
					that.showManufacturerSelect = false;
				},
				cancel:function(){
					that.showManufacturerSelect = false;
					that.manufacturerDatasels = JSON.parse(JSON.stringify(that.manufacturerDatasels1));
					var arr = [];
					$.each(that.manufacturerDatasels,function(i,e){
						if(e.id){
							arr.push(e.id);
						}
					})
					that.manufacturerDataselected = arr;
				}
			})
      },
      changeManufacturer:function(){
    	  var that = this;
		  that.manufacturerDatasels1 = that.manufacturerDatasels;
		  var ids = [];
          $.each(this.manufacturerDatasels,function(i,e){
        	  if(e.id){
        		  ids.push(e.id);
        	  }
          })
          this.manufacturerDataselected = ids;
      },
	}
});

//时间控件初始化
function datetimeInit() {
	$('#startDate').datetimepicker({
	    language:  config.language, 
	    minView: 'day',   //设置只显示到小时
	    format: "yyyy-mm-dd hh:00:00",
	    autoclose: true
	}).on('change',function(ev){
	    var startDate = $('#startDate').val();
	    if(new Date(startDate)>new Date(vm.expiryDate)){
	    	vm.expiryDate = startDate;
	    	$("#expiryDate").val(startDate);
	    }
	    vm.startDate = startDate;
	});
	
	$('#expiryDate').datetimepicker({
		 language:  config.language, 
		 minView: 'day',   //设置只显示到小时
	     format: "yyyy-mm-dd hh:00:00",
	     autoclose: true
	}).on('change',function(ev){
	    var expiryDate = $('#expiryDate').val();
	    vm.expiryDate = expiryDate;
	});
}
//请求获取初始数据 ，赋值
function queryInfo() {
	$.aAjax({
		url:ykyUrl.info + '/v1/recommendations/' + recommendationId,
		type:'GET',
		success:function(data){	
			vm.startDate = data.startDate || moment().format('YYYY-MM-DD H:00:00');
			vm.expiryDate = data.expiryDate || moment().format('YYYY-MM-DD H:00:00');
			vm.linkUrl = data.contentMap.url || '';
			vm.title = data.contentMap.title || '';
			vm.categoryId = data.categoryId || '',
			vm.pageId = data.extend1 || '',
			vm.positionId = data.extend2 || '8001',
			vm.proportion = data.contentMap.proportion || '',
			vm.image = data.contentMap.image || '',
			vm.background = data.contentMap.background || '';
			if(data.distributor&&data.distributor.length){
				vm.distributorDatasels = JSON.parse(data.distributor);
				vm.distributorDatasels1 = JSON.parse(data.distributor);
			}
			if(data.manufacturer&&data.manufacturer.length){
				var list = JSON.parse(data.manufacturer);
				var arr = []
				$.each(list,function(i,e){
					e.brandName = e.name;
					if(e.id){
						arr.push(Number(e.id));
					}
				})
				vm.manufacturerDataselected = arr;
				vm.manufacturerDatasels = JSON.parse(JSON.stringify(list));
				vm.manufacturerDatasels1 = JSON.parse(JSON.stringify(list));
			}
		},
		error:function(){
			console.log("error")
		}
	})
}

//获取分销商distributor
function getSupplier(){
	var supplierUrl =ykyUrl.party +"/v1/party/allparty";
	var params = {
		   "roleType": "SUPPLIER",
		    "status": "PARTY_ENABLED"
		}
	$.aAjax({
		url: supplierUrl,
		type:"PUT",
		data:JSON.stringify(params),
		contentType:'application/json',
		dataType:'json',
	    success: function(data) {
	    	var tempdistributorList = [];
	    	$.each(data,function(i,item){
	    		tempdistributorList.push({
	    			'id':item.id,
	    			'name':item.partyGroup.groupName
	    		})
	    	})
	    	vm.distributorDataoptions= tempdistributorList;
	    },
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText)
	    }
	});
}

//获取制造商
function getManufacturer(){
	var manufacturerUrl =ykyUrl.product +"/v1/products/brands";
	$.aAjax({
		url: manufacturerUrl,
		type:"GET",
	    success: function(data) {
	    	vm.manufacturerList = data;
	    	var tempmanufacturerList = [];
	    	$.each(data,function(i,item){
	    		tempmanufacturerList.push({
	    			'id':item.id,
	    			'brandName':item.brandName
	    		})
	    	})
	    	vm.manufacturerDataoptions = tempmanufacturerList;
	    },
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText)
	    }
	});
}
//保存数据
function save() {
	//广告标题校验 
	if(!vm.title){
		layer.msg("请输入广告标题！",{
			skin:"c_tip"
		});
		$('#title').focus();
		return;
	}
	
	//上传图片
	if(vm.image==""){
		layer.msg("请上传图片！",{
			skin:"c_tip"
		});
	   return;
    }
	
	if(vm.pageId != '7002'&&vm.pageId != '7003') {  //首页需要投放时间(包括头部和侧边栏两个位置)

		//投放时间校验
		var startDate= $("#startDate").val();
		var expiryDate= $("#expiryDate").val();
	    var currentTime = new Date();
	    
		if(!startDate){
			layer.msg("请输入开始时间！",{
				skin:"c_tip"
			});
			$('#startDate').focus();
			return;
		}
		
		if(!expiryDate){
			layer.msg("请输入结束时间！",{
				skin:"c_tip"
			});
			$('#expiryDate').focus();
			return;
		}
		
	   var expiryTimer = Date.parse(expiryDate);
	   var startTimer = Date.parse( startDate );
	   
	   if( currentTime  >=  startTimer ){
		   layer.msg("活动时间必须大于当前时间！",{
				skin:"c_tip"
			});
		   $('#startDate').focus();
		   return;
	   }
	   if( expiryTimer  <=  startTimer ){
		   layer.msg("结束时间必须大于开始时间！",{
				skin:"c_tip"
			});
		   $('#expiryDate').focus();
		   return;
	   }
	}
	
	if(vm.pageId=='7001'&&vm.positionId == '8001'){  //登录页头部修改曝光率必填
		//曝光占比校验
		if(vm.proportion==""){
			layer.msg("曝光占比不能为空！",{
				skin:"c_tip"
			});
			$('#proportion').focus();
		   return;
	    }
		
		if(!/^[123456789]$/.test(vm.proportion)) {
			layer.msg("曝光占比位1-9的数字！",{
				skin:"c_tip"
			});
			$('#proportion').focus();
		   return;
		}
	}
	if(!((vm.positionId==8002&&vm.pageId==7001)||vm.pageId==7003)){  //侧边栏,详情页不需要背景色
		//广告背景色
		if(!vm.background){
			layer.msg("请输入背景色！",{
				skin:"c_tip"
			});
			$("#background").focus();
			return;
		}
	}
	
	if(!(vm.pageId=='7002'||(vm.pageId==7001&&vm.positionId==8002))) {  //登录页广告链接为非必须
		if(!vm.linkUrl) {
			//活动链接
			layer.msg("请输入广告链接！",{
				skin:"c_tip"
			});
			$("#url").focus();
			return;
		}
	}
	
   

   var listData;
   var contentData
   if('7002'==vm.pageId&&'8001'==vm.positionId){  //首页广告
	   contentData = {
			    title: vm.title,
				proportion: '',  //曝光占比
				image: vm.image,     		//图片路径
				url: vm.linkUrl,      		//链接地址
				background: vm.background,   //背景色
				pageName: vm.pageName,   //广告页面
				positionName: vm.positionName	
		   }
	   listData = {
			    "categoryTypeId": "advertisement",    //固定要传类型为广告
				categoryId: vm.categoryId,  //广告类型
				"content": JSON.stringify(contentData),
				extend1: vm.pageId,    			//广告页面
		   }
   }else if('7001'==vm.pageId&&'8001'==vm.positionId){  //顶部广告
	   contentData = {
			    title: vm.title,
				proportion: vm.proportion,  //曝光占比
				image: vm.image,     		//图片路径
				url: vm.linkUrl,      		//链接地址
				background: vm.background,   //背景色
				pageName: vm.pageName,   //广告页面
				positionName: vm.positionName	
		   }
	   
	   var brands = []; //manufacturer
		var vendors = [];  //分销商
		$.each(vm.manufacturerDatasels1,function(i,item){
			var temp ={
				id: item.id,
				name:item.brandName
			}
			brands.push(temp);
		})
		$.each(vm.distributorDatasels1,function(i,item){
			var tempVendor ={
				id: item.id,
				name:item.name
			}
			vendors.push(tempVendor);
		})
		
	   listData = {
			    "categoryTypeId": "advertisement",    //固定要传类型为广告
				categoryId: vm.categoryId,  //广告类型
				"content": JSON.stringify(contentData),
				"expiryDate": vm.expiryDate,    	 //结束时间
				"startDate": vm.startDate,   		//开始时间
				extend1: vm.pageId,    			//广告页面
				extend2: vm.positionId,			//广告位置
				distributor:JSON.stringify(vendors), //所属分销商
				manufacturer:JSON.stringify(brands)  //所属制造商
		   }
	   
   }else if('7001'==vm.pageId&&'8002'==vm.positionId){ //侧边栏
	   
	   contentData = {
			    title: vm.title,
				proportion: '',  //曝光占比
				image: vm.image,     		//图片路径
				url: vm.linkUrl,      		//链接地址
				background: '',   //背景色
				pageName: vm.pageName,   //广告页面
				positionName: vm.positionName	
		   }
	   listData = {
			    "categoryTypeId": "advertisement",    //固定要传类型为广告
				categoryId: vm.categoryId,  //广告类型
				"content": JSON.stringify(contentData),
				"expiryDate": vm.expiryDate,    	 //结束时间
				"startDate": vm.startDate,   		//开始时间
				extend1: vm.pageId,    			//广告页面
				extend2: vm.positionId,			//广告位置
		   }
   }else if('7003'==vm.pageId){  //商品详情页广告
	   contentData = {
			    title: vm.title,
				proportion: '',  //曝光占比
				image: vm.image,     		//图片路径
				url: vm.linkUrl,      		//链接地址
				background: '',   //背景色
				pageName: vm.pageName,   //广告页面
				positionName: ''	
		   }
	   listData = {
			    "categoryTypeId": "advertisement",    //固定要传类型为广告
				categoryId: vm.categoryId,  //广告类型
				"content": JSON.stringify(contentData),
				extend1: vm.pageId,    			//广告页面
		   }
   } 
	
	$.aAjax({
		url: ykyUrl.info + '/v1/recommendations/' + (recommendationId ? recommendationId : ''),
		type:recommendationId ? 'PUT':'POST',		
		data:JSON.stringify(listData),
		success:function(data){
			layer.msg("保存成功！",{
				skin:"c_tip"
			});
			setTimeout(function(){
			   location.href = ykyUrl._this + "/advertisement.htm";  //保存成功跳转列表页
				
			},2000)				
		},
		error:function(data){
			if(data.responseJSON.errCode == "ADVERTISEMENTTITLE_EXIST"){
				layer.msg("广告名称已存在！",{
					skin:"c_tip"
				});
			}
		}
	})
}


var uploader1 = createUploader({
	buttonId: "uploadback", 
	uploadType: "notice.publicRead",   //文件夹地址要后台给
	url:  ykyUrl.webres,
	types: "jpg,png,jpeg,gif",
	fileSize: "5mb",
	isImage: true, 
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				console.log(_yetAnotherFileUrl);
				console.log(file); 
				vm.image = _yetAnotherFileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploader1.init();

