var vm
$(function() {
	var valid = false;
	 vm = new Vue(
			{
				el : "#activityInformation",
				data : {
					initData : {
						'activityId':'',
						'name':'',
				        'type':'10001',
				        'startDates':'',
				        'endDates':'',
				        'periodsList':[{"startTime": "","endTime": ""}]
					},
					datas:{},
					activityId : $("#activityId").val(),
					from : getQueryString('from'),
					newsHome: operationWebUrl + "/activity.htm",
					num:['一','二','三','四','五','六','七','八','九','十'],
					today:'',
					nowTime:''
				},
				methods : {
					init : function(res) {
						var that = this;
						that.getDate();
						that.datas = res;
						that.initData.activityId = res.activityId;
						that.initData.name = res.name;
						that.initData.type = res.type;
						that.initData.startDates = res.startDates;
						that.initData.endDates = res.endDates;
						if(that.activityId && (!that.from || that.from=='maintain')){
							that.initData.periodsList = [];
							$.each(res.periodsList,function(index,ele){
								if(ele.status == 'ENABLE'){
									that.initData.periodsList.push(ele);
								}
							})
						}else{
							that.initData.periodsList = $.extend(that.initData.periodsList,res.periodsList);
						}
						that.$nextTick(function(){
							setDate();
							$(".startDates.form_datetime").datetimepicker('setEndDate',res.endDates);
							$(".endDates.form_datetime").datetimepicker('setStartDate',res.startDates);
							$.each($(".time-range input"),function(){
								$(this).val(that.today + ' ' + $(this).val());
							})
							this.setTime();
							$.each($(".time-range input"),function(){
								var time = $(this).val().slice(11,$(this).val().length)
								$(this).val(time);
							})
					   });
					},
					saveData : function(event) {
						var that = this;
						var flag = that.timeValidate();
						if(!flag){
							return;
						}
						var url;
						if(that.activityId){
							url = ykyUrl.product + "/v1/activities/"+vm.activityId+"/draft";							
						}else{
							url = ykyUrl.product + "/v1/activities/draft";
						}
						if(that.from){
							that.initData.from = that.from;
						}
						syncData(
								url,
								"POST",
								that.initData,
								function(data, err) {
									if(err==null){
										if((vm.activityId && !vm.from) || (vm.activityId && vm.from=='maintain')){
											location.href = ykyUrl._this + '/activity/maintain/' + data.activityId + '.htm?from=information';
										}else{
											location.href = ykyUrl._this + '/activity/upload/' + data.activityId + '.htm';
										}
										
									}else if(err=='活动名称有重叠'){
										$("#name").addClass("error_bor");
									}
								});
					},
					deleteTime : function(idx){//删除时间段
						var that = this;
						var index = layer.confirm('确认删除？',{
							 btn: ['确认','取消'] //按钮
						},function(){
							that.initData.periodsList.splice(idx,1);
							layer.close(index);
						})
					},
					addTime : function(){//新增时间段						
						var that = this;
						var obj;
						if(that.activityId && that.from=='upload'){
							obj = {startTime:'',endTime:'',activityId:that.activityId};
						}else{
							obj = {startTime:'',endTime:''};
						}
						that.initData.periodsList.push(obj);
						that.$nextTick(function(){
						   that.setTime();
					   });
					},
					getDate : function(){
						var that = this;
						var now = new Date, 
							year = now.getFullYear(),
							month = (now.getMonth()+1)<10 ? '0' + (now.getMonth()+1):(now.getMonth()+1),
							date = now.getDate()<10 ? '0' + now.getDate():now.getDate(),
							hour = now.getHours()<10 ? '0' + now.getHours():now.getHours(),
							minute = now.getMinutes()<10 ? '0' + now.getMinutes():now.getMinutes();
							that.today = year + '-' + month + '-' + date;
			        		that.nowTime = hour + ':' + minute;
					},
					setTime:function(){//时间插件初始化
						var that = this;						
						$(".startTime.form_datetime").unbind(".plugin");//方法移除，防止多次进入change事件
						$(".endTime.form_datetime").unbind(".plugin");
						$('.startTime.form_datetime').datetimepicker({
							language:  config.language, 
							startView: 1,
						    minView: 'hour',   //设置只显示到小时
					        format: 'hh:ii',
					        minuteStep: 30,
					        autoclose: true
					    }).on('change.plugin',function(ev){
					    	that.startTimeChange(this);					        
				        });
						 //	时间控件绑定选择面板弹出 
						$('.endTime.form_datetime').datetimepicker({
							language: config.language,				            
				            startView: 1,
				            minView: 'hour',
				            format: 'hh:ii',
				            minuteStep: 30,
				            autoclose: true,
					    }).on('change.plugin',function(ev){
					        that.endTimeChange(this);
					    });
					},
					startTimeChange : function(dom){//时间段开始change事件
						var that = this;
						var dom = $(dom).find('input');
						var errorTips = '<p class="error"><span class="error_tips">*活动时段冲突</span></p>';
						that.getDate();
				        var obj = {
				        		startTime:$(dom).val(),
				        		endTime:$(dom).parents('.input-daterange').find('input.endTime').val(),
				        		preStartTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.startTime').val():undefined,
				        		preEndTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.endTime').val():undefined,
				        		endDates:that.initData.endDates,				        							        
				        }
				        var flag = that.timeComparison(dom,obj);
				        if(!flag){
				        	return;
				        }
				        if(obj.endDates == that.today && obj.startTime<that.nowTime){
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].startTime = '';
				        	layer.tips('开始时间需大于当前时间', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }
				        if(obj.endTime && obj.startTime>=obj.endTime){//开始时间需小于结束时间
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].startTime = ''
				        	layer.tips('开始时间需小于结束时间', $(dom),{
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }
				        if(obj.preEndTime && obj.startTime<obj.preEndTime){//开始时间需大于上时段的结束时间
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].startTime = ''
				        	if(!$(dom).parents('.input-daterange').find('.error').length){
				        		$(dom).parents('.input-daterange').append(errorTips);
				        	}
				        	return;
				        }else{
				        	$(dom).parents('.input-daterange').find('.error').remove();
				        	$(dom).parents('tr').prev('tr').find('.error').remove();
				        }
				        that.initData.periodsList[$(dom).data('index')].startTime = obj.startTime;
				        if(obj.startTime){
				        	$(dom).removeClass("error_bor")
				        }
					},
					endTimeChange : function(dom){//时间段结束change事件
						var that = this;
						var dom = $(dom).find('input');
						var errorTips = '<p class="error"><span class="error_tips">*活动时段冲突</span></p>';
						that.getDate();
				        var obj = {
				        		endTime:$(dom).val(),
				        		startTime:$(dom).parents('.input-daterange').find('input.startTime').val(),
				        		preStartTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.startTime').val():undefined,
				        		preEndTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.endTime').val():undefined,
				        		nextStartDate:$(dom).parents('tr').next('tr').length?$(dom).parents('tr').next('tr').find('input.startTime').val():undefined,
				        		endDates:that.initData.endDates,
				        }
				        var flag = that.timeComparison(dom,obj);
				        if(!flag){
				        	return;
				        }
				        if(obj.endDates == that.today && obj.endTime<that.nowTime){
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].endTime = ''
				        	layer.tips('结束时间需大于当前时间', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }					        
				        if(obj.startTime && obj.startTime>=obj.endTime){//结束时间需大于开始时间
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].endTime = ''
				        	layer.tips('结束时间需大于开始时间', $(dom),{
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }
				        if(obj.preEndTime && obj.endTime<=obj.preEndTime){//结束时间需大于上时段的结束时间
				        	that.initData.periodsList[$(dom).data('index')].endTime = obj.endTime;
				        	if(!$(dom).parents('.input-daterange').find('.error').length){
				        		$(dom).parents('.input-daterange').append(errorTips);
				        	}			        		
			        		return;
				        }
				        if(obj.nextStartDate && obj.endTime>obj.nextStartDate){//结束时间需小于下时段的开始时间
				        	that.initData.periodsList[Number($(dom).data('index'))+1].startTime = ''
				        	if(!$(dom).parents('tr').next('tr').find('.error').length){
				        		$(dom).parents('tr').next('tr').find('.input-daterange').append(errorTips);
				        	}else{
				        		$(dom).parents('tr').next('tr').find('.error_tips').text("*活动时段冲突");
				        	}
				        }
				        that.initData.periodsList[$(dom).data('index')].endTime = obj.endTime;
				        $(dom).siblings('.error').remove();
				        if(obj.endTime){
				        	$(dom).removeClass("error_bor")
				        }
					},
					timeComparison : function(dom,obj){//时间段比较
						var that = this;
						var type = $(dom).data('type')
						if(!obj.endDates){
							$(dom).val('');
							that.initData.periodsList[$(dom).data('index')][type] = ''
				        	layer.tips('请选择活动时间', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return false
				        }
				        if(obj.preStartTime == '' || obj.preEndTime == ''){//上一个时段不能为空
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')][type] = ''
				        	layer.tips('请完善上一个时间段', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return false
				        }
				        return true;
					},
					timeValidate : function(){
						var that = this;
						var flag = true;
						var input = $('.box-body input')
						$.each(input,function(index,ele){
							if($(this).val()==''){
								$(this).addClass('error_bor');
								if($(this).attr('id')=='name'){
									$('.ipt-vail').removeClass('dn');
								}
								flag = false;
							}
						})
						if(!flag){
							return false;
						}
						that.getDate();
						var errorTips = '<p class="error over_tips"><span class="error_tips">*当前时间已过期，请重新输入有效时间</span></p>';
						if(that.initData.endDates==that.today){
							$.each(that.initData.periodsList,function(index,ele){
								if(ele.endTime<that.nowTime || ele.startTime<that.nowTime){
									$("#endTime" + index).parents('.input-daterange').append(errorTips);
									flag = false;
								}
							})
						}
						if(!flag){
							return false;
						}else{
							return true;
						}
					}
				},
				
			});
	 $("input").on("change",function(){
		 if($(this).val !== ''){
			 $(this).removeClass("error_bor");
		 }
	 })
	 $("#name").on("focus",function(){
		 if($(this).val()=='' && $(".ipt-vail").hasClass("dn")){			
			 $(".ipt-tips").removeClass("dn");
		 }
	 })
	 $("#name").on("blur",function(){
		 $(".ipt-tips").addClass("dn");
		 if($(this).val()==''){			 
			 $(".ipt-vail").removeClass("dn");
			 $(this).addClass("error_bor")
		 }else{
			 vm.initData.name = $.trim($(this).val()); 
			 $(".ipt-vail").addClass("dn");
			 $(this).removeClass("error_bor")
		 }
	 })
	 function setDate(){
		 $('.startDates.form_datetime').datetimepicker({
				language: config.language,
				format: 'yyyy-mm-dd',
				minView: 'month',
		        startView: 2,
		        autoclose: true,
		        startDate:new Date,
		    }).on('change',function(ev){
		    	var startDate = $('#startDates').val();
			    $(".endDates.form_datetime").datetimepicker('setStartDate',startDate);
		        vm.initData.startDates = startDate;
		        });
			 //	时间控件绑定选择面板弹出 
			$('.endDates.form_datetime').datetimepicker({
				language: config.language,
				format: 'yyyy-mm-dd',
				minView: 'month',
		        startView: 2,
		        autoclose: true,
		        startDate:new Date,
		    }).on('change',function(ev){
		    	vm.getDate();
		    	var endDate = $('#endDates').val();
		        $(".startDates.form_datetime").datetimepicker('setEndDate',endDate);
		        vm.initData.endDates = endDate;
		        if(endDate>vm.today){
		        	$("p.over_tips").remove();
		        }
		    });
	 }	
	if(!vm.activityId && !vm.from){
		vm.setTime();
		setDate();
	}
	if (vm.activityId && vm.from) {//获取草稿活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });		
	}else if (vm.activityId) {//获取正式活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/standard', 'GET', null, function(res, msg) {vm.init(res); });
	}
	$('.btn-concle').on('click', function(){		
		window.location.href = operationWebUrl + "/activity.htm"; 					
		});
});
