var	 vm ;
var  activityId = getQueryString("activityId");    //活动id
$(function(){  
	
vm = new Vue({
        el: '#activity-detail',
        data: {
            datas:{},
            child:[],
            cur: 1,
            curPage: 1,
            pageNum: 10,
            num: 10,   
            rangeNum:1,
            initData:[],
            isShow:false,
            index:0,
            prelist:[],
            arr:['一','二','三','四','五','六','七','八','九','十'],
            couponList:[],
            
        },
        beforeCreate:function(){
        	var that = this;
        	//查询活动详情
        	syncData(
        			ykyUrl.product + "/v1/activities/"+activityId+"/standard",    //查询活动列表信息   zhang
        			//"../js/app/activityData2.json",
					"GET",
					null,
					function(res, err) {
        			    if(res){
					    	 vm.datas = res;
					    	 vm.child = res.periodsList;    //时段的集合 
					    	 }
					  	standard();   //查询商品信息 ，拿到所有时段商品信息 
					  	that.getCouponData();
					  	
					});
    	 
			
            
        },
        methods: {
         //点击展开，收起状态变化
        	 clickRow: function(event,index) {
        		 console.log(event.target);
        		 if(event.target == $('.closeData')[index]){
        			 vm.index= -1;    //点收起，就是一个都不展开
        		 }else{
        			 vm.index= index;
        		 }
        	
           },
           getCouponData:function(){
        	   var that = this;
        	   var data = {activityId:activityId};
        	   syncData(ykyUrl.pay + "/v1/coupons/couponActivity/findByActivityId", 'GET', data, function(res,err) {
					if(res.length){
						that.couponList = res;
					}
				})
           }
        },
        computed: {
           
            showLast: function() {
                return this.cur != this.all;
            }
        },
     
    }); 
		var standard=function(){
			//查询商品信息
			syncData(
				ykyUrl.product + "/v1/activities/"+activityId+"/products/standard?page=1",   //查询活动商品信息     li
				//"../js/app/activityData.json",
				"GET",
				null,
				function(data, err) { 
				if(data){
					  vm.initData = data.list;    //所有商品信息的集合放到initData里面去
					  retData(vm.child,vm.initData);
				} 
		   });
		} 
		
		var retData=function(child,initData){
			  //把商品信息区分时段   child时段集合   initData  数据集合
				 $.each(child,function(idx,ele){
						  var obj={};
						  obj['periodsId']= ele.periodsId;
						  obj['startTime'] = ele.startTime;
						  obj['endTime'] = ele.endTime;
						  obj['list']= setPeriods(ele.periodsId,initData); 
						  if(obj.list.length){
							  vm.prelist.push(obj);          //prelist是全局的数组，存组装好的数据集合
						  }						 
					  
					
				});  
				
		} 
		
		var setPeriods=function(item,initData){
			var list = [];
			 $.each(initData,function(idx,ele){
				  if( item == ele.periodsId){
					 //ele是数组里的对象
					  list.push(ele);
				  }
			});
			return list;
		}
		
		
});


