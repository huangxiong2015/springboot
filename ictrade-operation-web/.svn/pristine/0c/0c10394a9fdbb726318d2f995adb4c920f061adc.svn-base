var app = new Vue({
	el: '#supplierDetail',
	data: {
		initData:{
			extend1:'',
			createdDate:''
		},
		content:{
			contactUserName:'',
			email:'',
			imageUrl:'',
			phone:'',
			tel:'',
			receiptFinance:'',
			orderFinance:'',
			bankFinance:''
		},
		server:'',
		serverList:['receiptFinance','orderFinance','bankFinance']
	},
	created:function(){
		var that = this;
		syncData(ykyUrl.info + '/v1/recommendations/' + $('#detailId').val(),'GET',null,function(res,err){
			if(err == null){
				that.initData = $.extend({}, that.initData, res);
				that.content = res.contentMap ? $.extend({}, that.content, res.contentMap) : that.content;
				var serverArr = [];
				$.each(that.serverList, function(index,ele){
					if(that.content[ele] != ''){
						serverArr.push(that.content[ele]);
					}
				})
				that.server = serverArr.join(',');
			}
		})
	},
	methods:{
		showBigImage:function(){
			layer.open({
				  type: 1,
				  title: false,
				  closeBtn: 0,
				  area: 'auto',
				  maxWidth: $(window).width() * 0.9,
				  shadeClose: true,
				  content: $('#bigImageWrap'),
				  success: function(layero){
					  var maxWidth = $(window).width() * 0.9;
			  			var maxHeight = $(window).height() * 0.9;
			  			var src = layero.find('img').attr('src');
			  			
			  			var imgEle = new Image();
			  			imgEle.src = src;
			  			
			  			if(imgEle.width > imgEle.height) {
			  				layero.find('img').css('maxWidth', maxWidth);
			  			}else {
			  				layero.find('img').css('maxHeight', maxHeight);
			  			}
			  			var topPx = ($(window).height() - layero.find('img').height()) / 2;
			  		  	var leftPx = ($(window).width() - layero.find('img').width()) / 2;
			  	  		$('.layui-layer').css('top', topPx);
			  	  		$('.layui-layer').css('left', leftPx);
			  	  		$('.layui-layer-content').height('auto');
				  }
				});
		}
	}
});