var vm ='';
$(function(){  
	  var fileTemp = $('#fileTemp').html(),dat={ t : 0};

	  vm = new Vue({
	        el: '#manufacturer-add',
	        data: { 
	        	manufacturerData:{
	        		logo:'' ,
	        		brandName:'',
	        		desc:'',
	        		vendorAlias:[],
	        	},
	  			brandid : getQueryString('id'),
	  			inNum : 0,
	  			len : 0,
	  			keyname: '选择供应商',
	  	        validate:{
	  	            "required": true
	  	        },
	  	        id:'selbox',
	  	        name:'selbox',
	  	        options:[],
	  	        optionName: 'supplierName',
	  	        optionId: 'id',
	  	        selected:[],
	  	        multiple:false,  
	  	        placeholder:'搜索供应商',
	  	        vendorAliasCache: {},
	  	        aliasName: '',
	        },
	        created: function(){
	        	var _this = this;
	        	syncData(ykyUrl.party + "/v1/party/allparty", "PUT", {}, function (data, err) {
	  	          if (data) {
	  	        	  data.forEach(function(e){
//	  	        		if(e.partyGroup.groupName){
							e.supplierName = e.partyGroup.groupName? e.partyGroup.groupName: '';
//						}
	  	        	  })
	  	        	  data.splice(0, 0, {supplierName: '不限', id: ''}); 
	  	        	 _this.options = data;
	  	          } 
	  	      });
	        },
	        watch : {
	        	"manufacturerData.desc" : function(val){
	        		this.inNum = val.length;
	        	},
//	        	"manufacturerData.brandName": function(val){
//	        		var _this = this
//	        		if(_this.manufacturerData.vendorAlias.length === 0){
//	        			_this.manufacturerData.vendorAlias.push({name:'', vendorName: '', vendorId:''});
//	        		}
//	        		_this.manufacturerData.vendorAlias[0].name = val;
//	        		_this.manufacturerData.vendorAlias.forEach(function(item,i){
//        				if(item.name.toLocaleUpperCase() === _this.manufacturerData.vendorAlias[0].name.toLocaleUpperCase() && item.vendorId == _this.manufacturerData.vendorAlias[0].vendorId  && i !== 0){
//        					layer.msg(_this.manufacturerData.vendorAlias[0].name+'已存在，请重新输入制造商别名')
//        				}
//        			})
//	        	},
	        	'aliasName': function(val){
	        		var _this = this
	        		_this.aliasName = val;
	        	}
	        },
	        methods:{
	        	delField: function(i){
	        		this.manufacturerData.vendorAlias.splice(i,1);
	        	},
	        	addField: function(){
	        		var _this = this;
	        		_this.vendorAliasCache = {vendorName: '', vendorId: ''};
	        		_this.aliasName = '';
	        		_this.selected = [''],
	            	layer.open({
		        		type: 1,
		        		title: '添加别名',
		        		area: ['800px', '500px'],
		        		offset: '150px',
		        		move: false,
		        		skin: "s_select_data",
		        		content: $(".alias-layer"),
		        		btn: ['保存', '取消'],
		        		yes: function(index, layero) {
		        			if(!_this.aliasName){
		        				layer.msg('请输入制造商别名');
		        				return false;
		        			}
		        			var flag
		        			_this.vendorAliasCache.name = _this.aliasName;
		        			_this.manufacturerData.vendorAlias.forEach(function(item){
		        				if(item.name.toLocaleUpperCase() === _this.vendorAliasCache.name.toLocaleUpperCase() && item.vendorId === _this.vendorAliasCache.vendorId){
		        					layer.msg(_this.vendorAliasCache.name+'已存在，请重新输入制造商别名')
		        					flag = true;
		        				}
		        			})
		        			if(flag){
		        				return false;
		        			}
		        			syncData(ykyUrl.product + "/v1/products/brands/getByAlias?vendorId="+_this.vendorAliasCache.vendorId+"&alias="+_this.aliasName, "get", null, function (data, err) {
		        		          if (!err) { 
		        		        	  if(data){
		        		        		  if(getQueryString('id') != data.id){
		        		        			  layer.msg(data.brandName+'已使用该别名');
		        		        			  return false;
		        		        		  }
		        		        		  _this.manufacturerData.vendorAlias.push(_this.vendorAliasCache);
		        		        		  layer.close(index);
		        		        	  }else{
		        		        		  _this.manufacturerData.vendorAlias.push(_this.vendorAliasCache);
		        		        		  layer.close(index);
		        		        	  }
		        		          } 
		        		      });
		        			
		        			
		        		},
		        		cancel: function(index, layero) {
		        			
		        		}
		        	})
	        	},
	        	editField: function(i){
	        		var _this = this;
	        		sessionStorage.setItem('alias', JSON.stringify(_this.manufacturerData.vendorAlias[i]));
//	        		var aliasItem = JSON.parse(sessionStorage.getItem('alias'));
	        		
	        		_this.vendorAliasCache.vendorName = JSON.parse(sessionStorage.getItem('alias')).vendorName ? JSON.parse(sessionStorage.getItem('alias')).vendorName: '';
	        		_this.vendorAliasCache.vendorId = JSON.parse(sessionStorage.getItem('alias')).vendorId ? JSON.parse(sessionStorage.getItem('alias')).vendorId: '';
	        		_this.aliasName = JSON.parse(sessionStorage.getItem('alias')).name;
	        		_this.selected = [JSON.parse(sessionStorage.getItem('alias')).vendorId],
	            	layer.open({
		        		type: 1,
		        		title: '编辑别名',
		        		area: ['800px', '500px'],
		        		offset: '150px',
		        		move: false,
		        		skin: "s_select_data",
		        		content: $(".alias-layer"),
		        		btn: ['保存', '取消'],
		        		yes: function(index, layero) {
		        			if(!_this.aliasName){
		        				layer.msg('请输入制造商别名');
		        				return false;
		        			}
		        			var flag
		        			_this.vendorAliasCache.name = _this.aliasName;
		        			_this.manufacturerData.vendorAlias.forEach(function(item, k){
		        				if(item.name.toLocaleUpperCase() === _this.vendorAliasCache.name.toLocaleUpperCase() && item.vendorId === _this.vendorAliasCache.vendorId && k !== i){
		        					layer.msg(_this.vendorAliasCache.name+'已存在，请重新输入制造商别名')
		        					flag = true;
		        				}
		        			})
		        			if(flag){
		        				return false;
		        			}
		        			syncData(ykyUrl.product + "/v1/products/brands/getByAlias?vendorId="+_this.vendorAliasCache.vendorId+"&alias="+_this.aliasName, "get", null, function (data, err) {
		        		          if (!err) { 
		        		        	  if(data){
		        		        		  if(getQueryString('id') != data.id){
		        		        			  layer.msg(data.brandName+'已使用该别名');
		        		        			  return false;
		        		        		  }
		        		        		  _this.manufacturerData.vendorAlias[i].vendorName=_this.vendorAliasCache.vendorName;
		        		        		  _this.manufacturerData.vendorAlias[i].name=_this.vendorAliasCache.name;
		        		        		  _this.manufacturerData.vendorAlias[i].vendorId=_this.vendorAliasCache.vendorId;
		        		        		  _this.len++
		        		        		  layer.close(index);
		        		        	  }else{
		        		        		  _this.manufacturerData.vendorAlias[i].vendorName=_this.vendorAliasCache.vendorName;
		        		        		  _this.manufacturerData.vendorAlias[i].name=_this.vendorAliasCache.name;
		        		        		  _this.manufacturerData.vendorAlias[i].vendorId=_this.vendorAliasCache.vendorId;
		        		        		  _this.len++
		        		        		  layer.close(index);
		        		        	  }
		        		          } 
		        		      });
		        			
		        			
		        		},
		        		cancel: function(index, layero) {
		        			_this.vendorAliasCache.vendorName = JSON.parse(sessionStorage.getItem('alias')).vendorName ? JSON.parse(sessionStorage.getItem('alias')).vendorName: '';
			        		_this.vendorAliasCache.vendorId = JSON.parse(sessionStorage.getItem('alias')).vendorId ? JSON.parse(sessionStorage.getItem('alias')).vendorId: '';
			        		_this.aliasName = JSON.parse(sessionStorage.getItem('alias')).name;
			        		sessionStorage.removeItem('alias');
		        		}
		        	})
	        	},
	        	
	        	//选择品牌点击事件
	    		getBrandSelected: function(e){
	    			var _this = this;
	    			_this.vendorAliasCache.vendorName = e.displayName;
	    			_this.vendorAliasCache.vendorId = e.id;
	    		},
	        }
	   });
	  var brandid = getQueryString('id');
	  if(brandid){
		  syncData(ykyUrl.product + "/v1/products/brands/" + brandid , "get", null, function (data, err) {
	          if (data) { 
//	        	  vm.manufacturerData = $.extend({},  vm.manufacturerData, data);
	        	  vm.manufacturerData.brandName = data.brandName;
	        	  vm.manufacturerData.vendorAlias = data.vendorAlias;
	        	  vm.manufacturerData.logo = data.logo ? data.logo : '';
	        	  vm.manufacturerData.desc = data.desc ? data.desc : '';
	        	  
//	        	  vm.manufacturerData.vendorAlias.splice(0, 0, {name: data.brandName, brandName: '', vendorId: ''}); 
	              $('.c-btn').removeAttr('disabled');
	              dat.t= data.vendorAlias.length;
	          } 
	      });
	  }
	  
	//checkAbledButton();
	//验证
    formValidate('#manu-from', {}, function () {
        //序列化form表单数据
        var formArry = $('#manu-from').serializeObject();
        var flag;
        for(var i = 0; i < vm.manufacturerData.vendorAlias.length; i++){
        	for(var j = i+1; j < vm.manufacturerData.vendorAlias.length; j++){
        		if(vm.manufacturerData.vendorAlias[i].name.toLocaleUpperCase() == vm.manufacturerData.vendorAlias[j].name.toLocaleUpperCase() && vm.manufacturerData.vendorAlias[i].brandId == vm.manufacturerData.vendorAlias[j].brandId){
        			layer.msg(vm.manufacturerData.vendorAlias[i].name+'已存在，请重新输入制造商别名');
    				flag = true;
        		}
        	}
        }
        
        if(flag){
        	return false;
        }
        
        formArry.vendorAlias = vm.manufacturerData.vendorAlias;
        if(brandid){
            syncData(ykyUrl.product + "/v1/products/manufacturers/" + brandid, "put", formArry, function (data, err) {
                if (data) {
                    layer.msg('更新成功！');  
                    window.location.href=ykyUrl._this + "/manufacturer.htm";
                }
            });
        }else{
        	 syncData(ykyUrl.product + "/v1/products/manufacturers", "post", formArry, function (data, err) {
                 if (data) {
                     layer.msg('创建成功！');
                     window.location.href=ykyUrl._this + "/manufacturer.htm";
                 }
             });
        }
    });

    $('.del_logo').on('click', function(){
    	vm.manufacturerData.logo ='';
    }); 
  /*  $('.logo-box').mouseover(function(){
    	vm.isShow = false;
    }); 
    $('.logo-box').mouseout(function(){
    	vm.isShow = true;
    }); */ 
    
    //添加控件
//    $('#addControl').click(function(){ 
//    	var $control=$('.act-control-box .form-group');
//    	if($control.length < 2000){
//	        $('.act-control-box').append(applyTpl(fileTemp, dat));
//	        dat.t++; 
//        } 
//    	vm.len = $('.act-control-box .form-group').length;
//    	
//    });   
    var uploader = createUploader({
			buttonId: "uploadBtn", 
			uploadType: "ent.logo", 
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
						vm.manufacturerData.logo = _yetAnotherFileUrl;
						//vm.isShow = true; 
					} else {
						layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
					}
					up.files=[];
				}
			}
	    }); 
    uploader.init(); 
    
   $('.btn-concle').on('click', function(){
	  history.go(-1);
   });
});

//删除控件
//function delField(obj){
//	if($('.act-control-box .form-group').length ==1){
//		$(obj).parents('.form-group').find('.form-control').val('');
//	}else{ 
//	    $(obj).parents('.form-group').remove();
//	}
//}; 