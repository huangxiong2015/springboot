 var vm = new Vue({
        el: "#app",
        data: {
            list: [],
            //proInfo:{},
            previewDisplaySidebar:"N", //编辑-侧导航状态
            activityTitle : "",
            promotionStatus:"DISABLE",
            dragging: false,
            count: 1,
            addType: "BANNER", //新增模块缓存
            editType: "BANNER",
            barPosition:0,
            promotionId:"",
            viewUrl:'',					//预览URL 
            showFixed:false,
            showModal: false,
            modalStyle: {
                width: 350,
                height: 180
            },
            title:"",
            settingModal: {
                modalStyle: {
                    'width': 1080,
                    'maxHeight': 500
                },
                title:"",
                showConModal: false,
                showFoot: false
            },
            content: '', 
            defaultData:{
            },
            bannerFormData:{
            	 id:'createBanner',  //formID
                 url:ykyUrl.product +'/v1/promotionModuleDraft'              //设置banner提交地址
            },
            titleFormData:{
                id:'createTitle',  //formID
                url:ykyUrl.product +'/v1/promotionModuleDraft'              //设置title提交地址
            }, 
            descFormData:{ 
                id:'createDesc',  //formID
                url:ykyUrl.product +'/v1/promotionModuleDraft'              //设置desc提交地址
            }, 
            couponApi: '../../data/coupon.json',
            activityApi: "../../data/active.json" ,
            ceatePromotionApi: ykyUrl.product + '/v1/promotionModuleDraft',
            editPromotionApi: ykyUrl.product + '/v1/promotions',
            editProductPromotionApi: ykyUrl.product + '/v1/promoModuleProductDraft',
            promotionApi: ykyUrl.product + '/v1/promotions/{{id}}/', 
            promotionOder: ykyUrl.product + "/v1/promotionModuleDraft/orderSeq", 
            promotionOder: ykyUrl.product + "/v1/promotionModuleDraft/orderSeq",
            productAnalysisApi: ykyUrl.product + '/v1/promoModuleProductDraft/products/parse',
            deleteProductApi: ykyUrl.product + '/v1/promoModuleProductDraft/{promoModuleId}/promotionId/{promotionId}/products/delete',
            derivedProductApi: ykyUrl.product + '/v1/promoModuleProductDraft/products/export',
            productDataApi: ykyUrl.product + '/v1/promotions/{promotionId}/module/{promoModuleId}/product'
        },
        watch:{
            list:function (val) {//监听数组，拖动后修改排序
                function getIdlist(arr){
                	var idlist = [];
                	
                	for(var i =0; i< arr.length;i++){
                		idlist.push(arr[i].promoModuleId)
                	}
                	
                	return idlist;
                }
                
                var idUplist = getIdlist(this.list);
                syncData(this.promotionOder, "POST", idUplist , function(data, err) {
                	//console.log("move")
				 });
            },
            previewDisplaySidebar:function(){//修改侧导航
            	var $this= this;
            	syncData($this.editPromotionApi, "PUT", {previewDisplaySidebar:$this.previewDisplaySidebar,promotionId:$this.promotionId}, function(data, err) {
					
				});
            }
        }, 
        mounted() {
        	this.init();
        },
        methods: {
        	init: function(){
        		 var $this = this,
        		 	cliheight = $(window).height();
        		 this.getProId();//获取URL上活动ID
        		 
        		 //设置弹出框高度
                 if(cliheight>500)
                	 this.settingModal.modalStyle.maxHeight = cliheight;
                 
                 //获取列表数据
                 syncData($this.getPromotionApi("module/draft"), "GET",  null, function(data, err) {
                	 if (data && data.length>0) {
                    	 $this.previewDisplaySidebar = data[0].previewDisplaySidebar ? data[0].previewDisplaySidebar : "N";
                    	 $this.promotionStatus = data[0].promotionStatus;
                    	 $this.activityTitle = data[0].promotionName;
                    	 var list = data[0].list;
                    	 for(var i = 0;i < list.length;i++){
                    		 list[i].activityStartDate = data[0].startDate;
                    		 list[i].activityEndDate = data[0].endDate;
                    	 }
                         $this.list = list;
                     }
 				 });
                 
                 //获取头部导航位置
                 $this.barPosition = $("#app").position().top;
                 window.addEventListener('scroll', this.handleScroll);
        	},
        	getPromotionApi:function(type){//重组URL
        		var link = (this.promotionApi.replace("{{id}}",this.promotionId)) + type
        		return link
        	},
        	getModleTitle:function(el){
        		var type = el.promoModuleType,
        			title = "";
        		if(type=='BANNER'){ 
                    title = 'Banner';
                }else if(type=='COUPON'){ 
                    title = '优惠券';
                }else if(type=='PRODUCT_LIST'){ 
                    title = '商品列表';
                }else { 
                    title = '自定义模块';
                }
        		
        		if(el.promotionContent){
        			if(el.promotionContent.showSet.title)
        				title=el.promotionContent.showSet.title + " - " + title
        		}else{
        			
        		}
        		
        		return title;
        		
        	},
        	handleScroll () {//滚动，判定是否fixed头部导航
        	    this.showFixed = window.scrollY > this.barPosition;
        	},
            getType: function(val) {
                console.log(val);
                this.addType = val;
            },
            add: function() {//新增模块
            	var $this = this;
            	var promot={
            			"isSave": true,
            			"promoModuleType": this.addType,//活动类型
            			"promotionId":this.promotionId//活动ID
            	}
            	
            	syncData($this.ceatePromotionApi, "POST",  promot, function(data, err) {
            		if(data!=null){
            			$this.init();
            			$this.addType = "BANNER";
            		}
				}); 
                //this.list.push(promot); 
            },
            del: function(index, data) {//删除模块
            	//删除模块接口，promoModuleId:模块ID
            	var $this = this;
            	var ly = layer.confirm("确定删除该模块？删除后将不能恢复",
            			 {
            		 		btn:['删除','取消'],
            		 		title:"删除模块",
            		 		yes:function(){
            		 			syncData($this.ceatePromotionApi+ '/' + data.promoModuleId, "DELETE",  null, function(data, err) {
        						   if(data!=null){
        							  layer.msg("删除成功");
        						    }
        						}); 
            		 			$this.list.splice(index, 1);
            		 			layer.close(ly);
            		 		}
            			 })
            },
            edit: function(index, el) {//编辑模块
                this.editType = el.promoModuleType;
                if(el.promoModuleType=='BANNER'){ 
                    this.title = 'Banner';
                }else if(el.promoModuleType=='COUPON'){ 
                    this.title = '优惠券';
                }else if(el.promoModuleType=='PRODUCT_LIST'){ 
                    this.title = '商品列表';
                }else { 
                    this.title = '自定义模块';
                }
                this.defaultData=el;
                console.log(this.editType);
                this.settingModal.showConModal = true;
                //this.list[el.id].isSave = true;
            },
            getSetTip: function(el) {
                var tip = "",
                    span = function(c, n) {
                        return "<span class='badge " + c + "'>" + n + "</span>"
                    }

                tip = isNaN(el.promotionContent) ? span("bg-green", "已配置") : span("bg-red", "未配置");

                return tip
            },
            modalOk: function(data) { //ok调用方法
                this.add(this.addType);
                this.toggleModal();
            },
            toggleModal: function() { //显示隐藏；cancel调用方法
                this.showModal = !this.showModal;
            },
            modalConOk: function(index) { //ok调用方法
                this.add(this.addType);
                this.toggleConModal();
            },
            toggleConModal: function() { //显示隐藏；cancel调用方法
                this.settingModal.showConModal = !this.settingModal.showConModal;
            },
            clickShow: function() { //显示modal
                this.showModal = true;
                this.content = '';
                
            }, 
            formSubmit: function(data) {//提交后
                console.log(data);
                this.init();//this.list=data;//刷新列表
                this.toggleConModal();
            },
            cancle:function(){  
            	/*var data=JSON.parse(sessionStorage.getItem('descJson')); 
            	this.defaultData=data;*/
            	this.init();//刷新列表
                this.showModal = false;
                this.settingModal.showConModal = false;
            },
            publish:function(id){
            	var $this = this;
            	var ly = layer.confirm("确定发布该活动？发布后本次修改即时生效",
           			 {
           		 		btn:['发布','取消'],
           		 		title:"发布活动",
           		 		yes:function(){ 
	           		 		var index = layer.load(1, {
	          				  shade: [0.1,'#fff'] 
	          				});
           		 			syncData($this.getPromotionApi("status")+"?status=ENABLE", "PUT", null, function(data, err) {
           		 				layer.close(index);
           		 				layer.close(ly);
       						  if (err == null) {
       							  layer.msg("发布成功");
       						      }
       						});  
           		 		}
           			 })
            	
            },
            getProId:function(){
            	var linksArr = window.location.pathname.split("/");
            	
            	if(linksArr.length>=2){
            		this.promotionId = linksArr[linksArr.length-2]; 
            		this.viewUrl=ykyUrl.portal+'/promotion/'+this.promotionId+'/preview.htm';
            	}
            	else {
            		layer.msg("活动ID获取失败");
				}
            },
            getPublishTxt:function(){
            	//return this.promotionStatus == "DISABLE" ? "发布" : "重新发布"
                return "发布"
            } 
            
        }
    });