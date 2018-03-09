var vm
$(function(){ 

	  vm = new Vue({
	        el: '#distributor-detail',
	        data: { 
	        	vendorId:getQueryString('id'),
	        	initData:{
		    		abstracts:"", //分销商简介
				    attachUrl:"", //分销商图片url 编辑时存在newsAttachment.attachUrl
				    categoryTypeId:"VENDOR", //固定
				    content:"", //代理线、类别、热销型号、最新型号、活动图标、自定义图标内容、图标链接 json字符串
				    newsId:"", //分销商id
				    orderSeq:"", //排序位置
				    title:"" //分销商名称
		    	},
		    	content:{
		    		agents:[], //代理线
		    		cate:[], //类别
		    		hotSaleModel:[], //热销型号
		    		latestModel:[], //最新信号
			    	labelContent:'nothing', //活动图标
			    	customContent:'', //自定义图标内容
			        activityUrl:'' //图标标签链接
		    	},
		    	labelType:{
		    		nothing:'无',
		    		promotion:'促销',
		    		fullCut:'满减',
		    		fullDelivery:'满送',
		    		seckill:'秒杀',
		    		flashPurchase:'闪购',
		    		custom:'自定义',
		    	},
		    	hotList:{
		    		refresh:false,
		    		datas:[],
		    		defaultTip:'暂无数据',
		    		columns:[
	    		         	{key :'index', name: '序号', align:'center', width: '50px'},
	    		         	{key :'model', name: '型号', align:'center',cutstring: true,},
	    		         	{key :'manufacturer', name: '原厂', align:'center',cutstring: true,}
		    		         ]
		    		
		    	},
		    	newestList:{
		    		refresh:false,
		    		datas:[],
		    		defaultTip:'暂无数据',
		    		columns:[
	    		         	{key :'index', name: '序号', align:'center', width: '50px'},
	    		         	{key :'model', name: '型号', align:'center',cutstring: true,},
	    		         	{key :'manufacturer', name: '原厂', align:'center',cutstring: true,}
		    		         ]
		    	},
		    	//agentName:''
	        },
	        computed: {
	        	agentName:function(){
	        		var arr = [];
	        		if(this.content && this.content.agentsData){
	        			$.each(this.content.agentsData,function(i,e){
	        				if(e.brandName){
	        					arr.push(e.brandName);
	        				}
	        			})
	        		}
	        		return arr.join('、');
	        	},
	        	cateName:function(){
	        		var arr = [];
	        		if(this.content && this.content.cateData){
	        			$.each(this.content.cateData,function(i,e){
	        				if(e.name){
		        				var cateArr = e['name'].split('/');
		        				arr.push(cateArr[cateArr.length-1]);
	        				}
	        			})
	        		}
	        		return arr.join('、');
	        	},
	        	activeIcon:function(){
	        		if(this.content){
	        			if(this.content.labelContent == 'custom'){
		        			return this.content.customContent + '(自定义)';
		        		}else{
		        			var key = this.content.labelContent;
		        			return this.labelType[key];
		        		}
	        		}
	        		
	        	}
	        },
			created(){
				var that = this;
					syncData(ykyUrl.info + '/v1/news/' + this.vendorId, 'GET', null, function(res,err){
						if(err == null){
							that.initData = res ? $.extend({},this.initData,res) : {};
							that.content = res.content ? $.extend({},this.content,JSON.parse(res.content)) : {};
							that.initData.attachUrl = res.newsAttachment && res.newsAttachment.attachUrl ? res.newsAttachment.attachUrl : '';
							that.hotList.datas = that.content.hotModel ? that.content.hotModel : [];
							that.newestList.datas = that.content.newestModel ? that.content.newestModel : [];
							that.$nextTick(function(){
								that.hotList.refresh = !this.hotList.refresh;
								that.newestList.refresh = !this.newestList.refresh;
							})
						}
					})
			}
	   });
});