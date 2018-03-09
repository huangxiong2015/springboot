 var showList = avalon.define({
	$id: "list",
	list: [],
	companyList:[],
	industryList:[],
	selIndustry:[],
	activeIndustry:[],
	isLoading: "数据加载中...",
	isfirst: true,
	allchecked: false,
	audited: getQueryString("type"),
	//失效接口
   failureClick:function(id){
		_html = "<div class='y_prompt_content_des' >"+
		 	    		"<p>确定失效认证企业？</p>" +
		 	    		"<p>失效后，该企业所有账号将无法享受企业专享特权。<br/>请填写失效原因：</p>"+
		 	    		"<p><textarea type='text' class='w340' id='description' placeholder='请填写账号失效原因（50字以内）' name='description'  maxlength='100' onkeyup='desLen();' onblur='validateVal()'></textarea></p>"+
		 	    		"<p id='des'>！失效原因不能为空</p></div>";
		var l = layer.confirm(_html, {
				offset: "auto",
				btn: ['确      认', '取      消'], //按钮
				title: '提示',
				area: ['550px', '380px'],
				move: false,
				skin: "up_skin_class",
				yes:function(){
					var result = 1;
					if($("#des")[0]){   //如果存在输入框
						result = 0;
						if($("#description").val() == ""){
							$("#description").addClass('bred');
							return;
						}
					}
					var reason = "1";
	 	  	    	var des = $("#des")[0];
	 	  	    	if(des){   //如果存在输入框
	 	  	    		reason = $("#description").val();
					}
			    	$.aAjax({
						url: ykyUrl.party + '/v1/enterprises/invalidAccount/'+id+"?reason="+reason,
						type: 'PUT',
						async: false,
						success: function(data) {
							layer.close(l);
			 	  	    	location.reload()
						},
						error:function(e){
			 	  	    	layer.closeAll('loading');
			 	  	    	console.log("系统错误，审批失败，请联系系统管理员："+e);
			 	  	    }
			    	});
				}
			})				
	}
 
});
 
var getParameters = function() { 
	var arr = ["partyCode", "name", "corCategory","industry","industryOther","categoryId","province","city","country","activeStatus","accountStatus","applyDateStart", "applyDateEnd",
			"orgLimitStart", "orgLimitEnd"
		],
		par = {}; 
	for (var i = 0; i < arr.length; i++) {
		par[arr[i]] = decodeURI(getQueryString(arr[i]));
	} 
	return par;
};

var search = avalon.define({
	$id: "search",
	parameter: getParameters(),
	goSearch: function() {
		var wrap = $("#searchFrom");
		var inputList = wrap.find("input"),
			selectList = wrap.find("select"),
			parStr = "&",
			parObj = {};
			
		
			

		function toUrl(pArr) {
			for (var k in pArr) {
				parStr += k + "=" + pArr[k] + "&";
			}
		}
		$.each(inputList, function(i, input) {
			var val = input.value;
			if (val !== "") {
				parObj[input.name] = encodeURI($.trim(input.value));
			}
		});
		$.each(selectList, function(i, select) {
			var val = "",
				selectObj = $(select, "option:selected");
			if (selectObj.val()) {
				parObj[select.name] = select.value;
			}
		});
		
		toUrl(parObj);
		parStr = parStr.substring(0, parStr.length - 1);
		
		
		
		if($(".ui-area .province span").attr("data-id") != undefined && $(".ui-area .province span").attr("data-id") != ''){
			parStr += "&province="+ $(".ui-area .province span").attr("data-id"); 
		}
		if($(".ui-area .city span").attr("data-id") != undefined &&  $(".ui-area .city span").attr("data-id") != ''){
			parStr += "&city="+ $(".ui-area .city span").attr("data-id");
		}
		if($(".ui-area .country span").attr("data-id") != undefined && $(".ui-area .country span").attr("data-id") != ''){
			parStr += "&country="+$(".ui-area .country span").attr("data-id"); 
		}

		
		var size=showList.list.pageSize?showList.list.pageSize:'10';
		var str = "&";
		if (parStr !== "") {
			str = "&";
		}
		window.location.href = window.location.pathname + '?action=certificationEnt' + parStr + str +
			"page=1&size=" + size; 

	}
});

function changeSize(count){
	showList.list.pageSize = count;
	showList.list.pageNum = 1;
	search.parameter.pageSize = count;
	search.parameter.page = 1;
	$(".condition_inquery")[0].click();
}
function init(){
	var queryPage = 0,
		querySize = 0;
	if (!showList.list.pageNum) {
		queryPage = 1;
	} else {
		queryPage = showList.list.pageNum;
	}
	if (!showList.list.pageSize) {
		querySize = 10;
	} else {
		querySize = showList.list.pageSize;
	}
	queryPage = getQueryString("page");
	querySize = getQueryString("size");
	listPage = queryPage ? queryPage : 1;
	listSize = querySize ? querySize : 10;
	
	var listUrl = ykyUrl.party + '/v1/enterprises/entCertificationList';
	var tempData = "";
	if (window.location.search.indexOf("?") != -1) {
		tempData = "?" + window.location.search.split("?")[1];
	} else {
		tempData = "?page=" + listPage + "&size=" + listSize;
	}
	
	$.aAjax({
		url: listUrl + tempData, //+window.location.search,
		success: function(data) { 
			if (data.length === 0)
				showList.isLoading = "暂无数据";
			
			for(var i = 0; i < data.list.length; i++){
				if(data.list[i].industryCategory){
					var industryCategoryId = data.list[i].industryCategory.split(",");
					industryCategoryId.sort(function(a,b){return a-b});
					var industryCategoryName = [];
					for(var j = 0; j<industryCategoryId.length; j++){
						for(var k = 0; k < showList.industryList.length; k++){
							if(industryCategoryId[j] === showList.industryList[k].categoryId){
								if(industryCategoryId[j] === '1008'){
									industryCategoryName.push(showList.industryList[k].categoryName+'('+data.list[i].otherAttr+')');
								}else{
									industryCategoryName.push(showList.industryList[k].categoryName);
								}
								
							}
						}
					}
					data.list[i].industryCategoryName = industryCategoryName.join(",");
				}
			}
	
			showList.list = data; 
			showList.list.list=contactArr(data.list, showList.companyList, showList.industryList); 
			showList.isfirst = false;
			setPageOption(data);
			loadPage(window.location.pathname + GetRequestWithout(window.location.search,
			"page"));
		}
	});
}
/*根据id拼接数组*/
function contactArr(arr, arr1, arr2){ 
    var result =[]; 
    $.each(arr, function (i, x)
    {
        result[i] = x; 
        $.each (arr1, function (j, y)
        {
        	if(x.corCategory==y.categoryId){
        		result[i].companyName=y.categoryName;
        	} 
        });
        $.each (arr2, function (j, k)
        {
        	if(x.industryCategory==k.categoryId){
        		result[i].industryName=k.categoryName;
        	}  
        });
    }); 
    return result; 
}

 

/*所屬行業  */
$(".select_industry").on("click",function(){
	if($(".industry_content").hasClass("dn")){
		$(".industry_content").removeClass("dn")
	}else{
		$(".industry_content").addClass("dn")
	}
})
$(".industry_btn .btn_y").on("click",function(){
	var flag = false,industryIds=[]; 
	$.each($(".industry_list .check-box-red"),function(idx,ele){
		var id=$(ele).attr("id");
		if(id=='1008'&&$("#industryOther").val()==''){
			flag = true;
		}else{  
			industryIds.push(id); 
		}
		showList.selIndustry=$.unique(industryIds);
	})
	if(flag){
		$("#industryOther").css("border-color","#b1191a");
		$("#industryOther").siblings("div").css("color","#b1191a");			
		return;
	}
	$("#industryOther").css("border-color","#e4e4ea");
	$("#industryOther").siblings("div").css("color","#999");
	$(".industry_content").addClass("dn");		
	returnActiveIndustry(showList.selIndustry);
	$(".select_tip").text("重新选择所属行业"); 
	
})

$(".industry_btn .btn_c").on("click",function(){
	$("#industryOther").css("border-color","#e4e4ea");
	$("#industryOther").siblings("div").css("color","#999");
	$(".industry_content").addClass("dn");
	returnActiveIndustry(showList.selIndustry);
	renderSelectIndustry(showList.selIndustry);

	var isOther = false;
	$.each($(".industry_list .check-box-red"),function(){
		if($(this).attr("id")=='1008'){
			isOther = true;
		}
	})
	if(!isOther){
		$("#industryOther").hide();
		$("#industryOther").siblings("div").hide();
	} 
})

function returnActiveIndustry(arr){
	var str = '',ids='',industryList=showList.industryList; 
	$.each(arr, function(idx, ele){ 
		if(idx==0){
			ids += ele;
		}else{ 
			ids += ','+ ele;
		} 
		$.each(industryList, function(index, el){	
			if(el.categoryId===ele){ 
				if(ele=='1008'){
					var industryOther;
					
					
						if( getQueryString('industryOther') && getQueryString('industryOther') !== ''){
							if($("#industryOther").val() !== ''){
								industryOther = $("#industryOther").val();
							}else{
								$("#industryOther").show();
								$("#industryOther").siblings("div").hide();
								$("#industryOther").val(getQueryString('industryOther'))
								industryOther = getQueryString('industryOther');
							}
							str += '<div class="area-item" id="'+ele+'">'+el.categoryName+'（'+industryOther+'）<i class="icon-close-min close"></i></div>';
						}else{
							if($("#industryOther").val() !== ''){
								industryOther = $("#industryOther").val();
								str += '<div class="area-item" id="'+ele+'">'+el.categoryName+'（'+industryOther+'）<i class="icon-close-min close"></i></div>';
							}
						}	
				}else{
					str += '<div class="area-item" id="'+ele+'">'+el.categoryName+'<i class="icon-close-min close"></i></div>';
				}
				
/*				
				if($(".ui-area .country span").attr("data-id") != undefined && $(".ui-area .country span").attr("data-id") != ''){
					parStr += "&country="+$(".ui-area .country span").attr("data-id"); 
				}*/
				
				
			}
		});
	})

	$(".select-area").html(str);
	$("#industry").val(ids);
	removeSelectItem() 
}

function renderSelectIndustry(list){
	$(".industry_list .check-box-white").removeClass("check-box-red");
	$.each(list,function(idx,ele){
		$.each($(".industry_list .check-box-white"),function(i,e){
			if(ele == $(e).attr('id')){
				$(e).addClass("check-box-red");
			}
		})
	})
}
function selectIndustry(){
	$(".industry_list .check-box-white").on('click',function(){
		if($(this).hasClass('check-box-red')){
			$(this).removeClass('check-box-red');
			$(this).removeClass('temporary_active');
			if($(this).attr("id")=="1008"){
				$("#industryOther").hide();
				$("#industryOther").siblings("div").hide();
			}
		}else{
			$(this).addClass('check-box-red');
			$(this).addClass('temporary_active');
			if($(this).attr("id")=="1008"){
				$("#industryOther").val("");
				$("#industryOther").show();
				if($(this).hasClass("isfirst")){
					placeholder();
					$(this).removeClass("isfirst");
				}
				$("#industryOther").siblings("div").show();
			}
		}		
	})
}


function removeSelectItem(){
	$('.area-item .close').on("click",function(){
		var _id = $(this).parent().attr("id");
		var activeIndustry=showList.selIndustry;
		if(activeIndustry.length ===0){
			$("#industry").val('');
		}else{
			$.each(activeIndustry, function(idx,ele){
				if(ele == _id){
					if(ele=='1008'){
						$("#industryOther").val("");
						$("#industryOther").hide();						
					}
					activeIndustry.splice(idx, 1);
					renderSelectIndustry(activeIndustry);
					return false;
				}
			})
		}
		
		
		showList.selIndustry=$.unique(activeIndustry); 
		returnActiveIndustry(activeIndustry);
		$(this).parents(".area-item").remove(); 
	})
} 

  /*所属行业*/
  function renderIndustryList(){
  	$.aAjax({
  		url: ykyUrl.database + '/v1/category/industry',
  		type:'GET',
  		success:function(data){
  			showList.industryList=data;
  			var str = '';
		    	$.each(data,function(idx,ele){
		    		if(ele.categoryId=="1008"){
		        		str += '<div class="company_type"><span class="check-box-white isfirst" id="'+ele.categoryId+'"></span><i class="g3">'+ele.categoryName+'</i><input id="industryOther" name="industryOther" class="other_attr v-success dn" maxlength="10" placeholder="请填写所属行业"/></div>'
		    		}else{
		        		str += '<div class="company_type"><span class="check-box-white" id="'+ele.categoryId+'"></span><i class="g3">'+ele.categoryName+'</i></div>'
		    		}
		    	})
		    	$(".industry_list").html(str);
		    	$(".industry_list").mCustomScrollbar();
		    	selectIndustry(); 
		    	
		    	var industryIds=search.parameter.industry.split(',');
				industryIds=$.unique(industryIds);  
				//行业选项初始化
				if(industryIds.length>=1){ 
					showList.selIndustry=industryIds;
					returnActiveIndustry(industryIds);
					renderSelectIndustry(industryIds);
				}
  		}
  	})

  }
	 
/*公司类型*/ 
  function renderCorCategory(){
  	$.aAjax({
  		url: ykyUrl.database + '/v1/category/companytype',
  		type:'GET',
  		success:function(data){ 
  			var str = '<option value="">请选择</option>', 
	  			corCategory=getQueryString('corCategory');
		    	$.each(data,function(idx,ele){
		    		str += '<option value="'+ele.categoryId+'" >'+ele.categoryName+'</option>'		 
		    	})
		    	$("#corCategory").html(str); 
				showList.companyList = data; 
				$("#corCategory").val(corCategory); 
			}
		});

		
  }


$(function(){
		//初始化，加载数据
		init(); 
		renderIndustryList();
		renderCorCategory();
		
		
		//判断审核开始时间和结束时间   申请时间 
  		$("#applyDateStart, .cc1").on("click",function(){
  		    laydate(start);
  		}); 		
  		$("#applyDateEnd, .cc2").on("click",function(){
  		    laydate(end);
  		});

  		
  		 var start = {
  		        elem: '#applyDateStart',
  		        format: 'YYYY-MM-DD',
  		       // min: laydate.now(),    //设定最小日期为当前日期
  		        istime: false,    //是否显示时间
  		        istoday: false,
  		        isclear: false,   //是否显示清空
  		        //选择好日期的回调
  		        choose: function(dates){ 
  		            $(this.elem).trigger("blur");
  		            var startTime = $('#applyDateStart').val();
  		       	    var createdateEnd = $('#applyDateEnd').val();
  		       	    var arr1 = startTime.split('-');
  		       	    var sdate=new Date(arr1[0],parseInt(arr1[1]-1),arr1[2]);
  		       	    var arr2 = createdateEnd.split('-');
  		       	    var edate=new Date(arr2[0],parseInt(arr2[1]-1),arr2[2]);
  		       	    if(sdate > edate){
  		       			layer.tips('开始时间不能大于结束时间', '#applyDateStart',{
  		       				tips: 1,
  		     				time: 1000,
  		       			});
  		       			$('#applyDateStart').val('');
  		       		};
  		        }
  		    };
  		 var end = {
  		      elem: '#applyDateEnd',
  		      format: 'YYYY-MM-DD',
  		      // min: laydate.now(),    //设定最小日期为当前日期
  		      istime: false,    //是否显示时间
  		      istoday: false,
  		      isclear: false,   //是否显示清空
  		      //选择好日期的回调
  		      choose: function(dates){ 
  		          $(this.elem).trigger("blur");
  		          var startTime = $('#applyDateStart').val();
  		     	  var createdateEnd = $('#applyDateEnd').val();
  		     	  var arr1 = startTime.split('-');
  		     	  var sdate=new Date(arr1[0],parseInt(arr1[1]-1),arr1[2]);
  		     	  var arr2 = createdateEnd.split('-');
  		     	  var edate=new Date(arr2[0],parseInt(arr2[1]-1),arr2[2]);
  		     	  if(sdate > edate){
  		     			layer.tips('开始时间不能大于结束时间', '#applyDateEnd',{
  		     				tips: 1,
  		     				time: 1000,
  		     			});
  		     			$('#applyDateEnd').val('');
  		     		};
  		      }
  		 };  		 
  		 //判断开始时间和结束时间
  		 $('#search_all').on('click',function(){
  			var startTime = $('#applyDateStart').val();
  			var createdateEnd = $('#applyDateEnd').val();
  			//日期格式转换
  			var reg = /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/;
  			var arr1 = startTime.split('-');
  			var sdate=new Date(arr1[0],parseInt(arr1[1]-1),arr1[2]);
  			var arr2 = createdateEnd.split('-');
  			var edate=new Date(arr2[0],parseInt(arr2[1]-1),arr2[2]);
  			if(startTime == '' && createdateEnd == ''){
  		
  			}else if(startTime == '' && createdateEnd){
  				 layer.open({
  					    title: '信息',
  				        type: 1,
  				        area: ['260px', '160px'],
  				        shadeClose: true, //点击遮罩关闭
  				        content: '\<\div style="padding-top: 50px;padding-left: 80px;";>请填写开始时间\<\/div>'
  				    });
  				 return false;
  			}else if(startTime && createdateEnd == ''){
  			
  			}else if(!reg.test(startTime) || !reg.test(createdateEnd)){
  				layer.open({
  				    title: '信息',
  			        type: 1,
  			        area: ['260px', '160px'],
  			        shadeClose: true, //点击遮罩关闭
  			        content: '\<\div style="padding-top: 50px;padding-left: 80px;";>时间格式不正确\<\/div>'
  			    });
  			}else{

  			}
  		 });
  		 
		$("#status").selectmenu()
  		.selectmenu( "menuWidget" )
  	    .addClass( "overflow" );
  	});



  	 //判断开始时间和结束时间    审核时间

  	  		$("#orgLimitStart, .cc3").on("click",function(){
  	  		    laydate(start2);
  	  		});  		
  	  		$("#orgLimitEnd, .cc4").on("click",function(){
  	  		    laydate(end2);
  	  		});	
  	  		
  	  		var start2 = {
  	  		        elem: '#orgLimitStart',
  	  		        format: 'YYYY-MM-DD',
  	  		       // min: laydate.now(),    //设定最小日期为当前日期
  	  		        istime: false,    //是否显示时间
  	  		        istoday: false,
  	  		        isclear: false,   //是否显示清空
  	  		        //选择好日期的回调
  	  		        choose: function(dates){ 
  	  		            $(this.elem).trigger("blur");
  	  		            var startTime = $('#orgLimitStart').val();
  	  		       	    var createdateEnd = $('#orgLimitEnd').val();
  	  		       	    var arr1 = startTime.split('-');
  	  		       	    var sdate=new Date(arr1[0],parseInt(arr1[1]-1),arr1[2]);
  	  		       	    var arr2 = createdateEnd.split('-');
  	  		       	    var edate=new Date(arr2[0],parseInt(arr2[1]-1),arr2[2]);
  	  		       	    if(sdate > edate){
  	  		       			layer.tips('开始时间不能大于结束时间', '#orgLimitStart',{
  	  		       				tips: 1,
  	  		     				time: 1000,
  	  		       			});
  	  		       			$('#orgLimitStart').val('');
  	  		       		};
  	  		        }
  	  		    };

  	  		 
  	  		 var end2 = {
  	  		      elem: '#orgLimitEnd',
  	  		      format: 'YYYY-MM-DD',
  	  		      // min: laydate.now(),    //设定最小日期为当前日期
  	  		      istime: false,    //是否显示时间
  	  		      istoday: false,
  	  		      isclear: false,   //是否显示清空
  	  		      //选择好日期的回调
  	  		      choose: function(dates){ 
  	  		          $(this.elem).trigger("blur");
  	  		          var startTime = $('#orgLimitStart').val();
  	  		     	  var createdateEnd = $('#orgLimitEnd').val();
  	  		     	  var arr1 = startTime.split('-');
  	  		     	  var sdate=new Date(arr1[0],parseInt(arr1[1]-1),arr1[2]);
  	  		     	  var arr2 = createdateEnd.split('-');
  	  		     	  var edate=new Date(arr2[0],parseInt(arr2[1]-1),arr2[2]);
  	  		     	  if(sdate > edate){
  	  		     			layer.tips('开始时间不能大于结束时间', '#orgLimitStart',{
  	  		     				tips: 1,
  	  		     				time: 1000,
  	  		     			});
  	  		     			$('#orgLimitEnd').val('');
  	  		     		};
  	  		      }
  	  		 };
  	  		 
  	  		 //判断开始时间和结束时间
  	  		 $('#search_all').on('click',function(){
  	  			var startTime = $('#orgLimitStart').val();
  	  			var createdateEnd = $('#orgLimitEnd').val();
  	  			//日期格式转换
  	  			var reg = /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/;
  	  			var arr1 = startTime.split('-');
  	  			var sdate=new Date(arr1[0],parseInt(arr1[1]-1),arr1[2]);
  	  			var arr2 = createdateEnd.split('-');
  	  			var edate=new Date(arr2[0],parseInt(arr2[1]-1),arr2[2]);
  	  			if(startTime == '' && createdateEnd == ''){
  	  		
  	  			}else if(startTime == '' && createdateEnd){
  	  				 layer.open({
  	  					    title: '信息',
  	  				        type: 1,
  	  				        area: ['260px', '160px'],
  	  				        shadeClose: true, //点击遮罩关闭
  	  				        content: '\<\div style="padding-top: 50px;padding-left: 80px;";>请填写开始时间\<\/div>'
  	  				    });
  	  				 return false;
  	  			}else if(startTime && createdateEnd == ''){
  	  			
  	  			}else if(!reg.test(startTime) || !reg.test(createdateEnd)){
  	  				layer.open({
  	  				    title: '信息',
  	  			        type: 1,
  	  			        area: ['260px', '160px'],
  	  			        shadeClose: true, //点击遮罩关闭
  	  			        content: '\<\div style="padding-top: 50px;padding-left: 80px;";>时间格式不正确\<\/div>'
  	  			    });
  	  			}else{

  	  			}
  	  		 });
  	  		 
  //placeholder的IE兼容性问题 
var placeholder = function(){ 

	$('input[placeholder],textarea[placeholder]').each(function(){
      //取得提示文字    
      var that = $(this),
      text = that.attr('placeholder');    
      that.attr("placeholder","");
      //移动
      var div = $("<div/>");
      if(that.css("float")){
    	  div.css("float",that.css("float"))
      }
      if(that.css("display")){
    	 // div.css("display",that.css("display"))
    	  div.css("display","inline-block")
      }
      var cssWidth = 0;
      if(that.css("width")){
    	  cssWidth = parseInt(that.css("width").substring(0,that.css("width").indexOf("px")));
      }
      var cssPLeft = 0;
      if(that.css("padding-left")){
    	  cssPLeft = parseInt(that.css("padding-left").substring(0,that.css("padding-left").indexOf("px")));
      }
      var cssPRight = 0;
      if(that.css("padding-right")){
    	  cssPRight = parseInt(that.css("padding-right").substring(0,that.css("padding-right").indexOf("px")));
      }
      var cssBLeft = 0;
      if(that.css("border-left-width")){
    	  cssBLeft = parseInt(that.css("border-left-width").substring(0,that.css("border-left-width").indexOf("px")));
      }
      var cssBRight = 0;
      if(that.css("border-right-width")){
    	  cssBRight = parseInt(that.css("border-right-width").substring(0,that.css("border-right-width").indexOf("px")));
      }
	  div.css("width",cssWidth+cssPLeft+cssPRight+cssBLeft+cssBRight+"px");
	  that.css("width",cssWidth+"px");
      if(that.css("margin-left")){
    	  div.css("margin-left",that.css("margin-left"));
    	  that.css("margin-left","0px");
      }
      if(that.css("margin-top")){
    	  div.css("margin-top",that.css("margin-top"));
    	  that.css("margin-top","0px");
      }
      if(that.css("margin-bottom")){
    	  div.css("margin-bottom",that.css("margin-bottom"));
    	  that.css("margin-bottom","0px");
      }
      that.before(div);
      that.appendTo(div);
      //创建
      var inputHolder = $("<div/>");
      inputHolder.text(text);
      that.before(inputHolder);
      //设定css
      inputHolder.css("font-size",that.css("font-size"));
      
      if(this.type == "textarea"){
    	  inputHolder.css("line-height","30px");
      }else{
    	  inputHolder.css("line-height",that.outerHeight()+"px");
      }
      inputHolder.css("padding-left","10px");
      inputHolder.css("vertical-align","middle");
      inputHolder.css("position","absolute");
      inputHolder.css("color","#999");
      if(that.val()!=""){
    	  inputHolder.hide();
      }
      //点击获取焦点
      inputHolder.click(function(){
    	  inputHolder.hide();
    	  that.focus();
      });
      that.bind("focus",function(){
    	  inputHolder.hide();
      })
      that.bind("blur",function(){
    	  if(that.val()==""){
	    	  inputHolder.show();
    	  }else{
    		  inputHolder.hide();
    	  }
      });
    });   
}  	  		 


