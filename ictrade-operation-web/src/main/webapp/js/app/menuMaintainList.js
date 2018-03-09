var vm = new Vue({
	el: '#contArea',
	data: {
	  dataList:[],
	  menuData:{
		"menuId": "",        
        "menuName": "",
        "orderSeq": "",
        "menuUrl": "",
        "menuStatus": "ACTIVE",
        "menuTypeId": "",
        "styleTypeId": "SLIDER",
        "parentMenuId": "",
        "menuLogoUrl": ""
	  },
	},
	methods: {
		subMenu:function(e){
			var typeId = $(e.target).data('id');
			if(typeId=='HELP_CENTER'){
				window.location.href = thisPrefix+'/helpMaintain.htm';
			}else{
				window.location.href = thisPrefix+'/menuMaintainList.htm?menuTypeId='+typeId;
			}
		},
		editMenu:function(event){//新增、编辑菜单信息
			 vm.menuData.orderSeq = vm.dataList.length ? vm.dataList.length + 1 : 1;
			 vm.menuData.menuStatus = 'ACTIVE';
			layer.open({
				type: 1,
				title:'新增菜单',
			    area: '700px',
			    offset: '150px',
			    move:false,
			    skin:"s_select_data",
			    content:$(".edit_cont")
			    ,btn: ['保存', '取消']
			    ,yes: function(index, layero){
			    	vm.menuData.menuName = $.trim(vm.menuData.menuName);
			    	vm.menuData.menuId = $.trim(vm.menuData.menuId);
					 if(vm.menuData.menuName&&vm.menuData.menuId){
						 $.aAjax({
							 url: ykyUrl.database+ '/v1/menus',
							 type: 'POST',
							 data: JSON.stringify(vm.menuData),
							 success:function(data){
								layer.close(index);
								if(sessionStorage.getItem("operationMunus")){
									 sessionStorage.removeItem('operationMunus');
									 $.AdminLTE.layout.fix();
								 }
								location.href = thisPrefix+'/menuMaintain.htm';				 
							 },
							 error:function(e){
								 layer.close(index);
								 layer.msg(JSON.parse(e.responseText).errMsg, {icon: 2});
								 console.log("error");
							 }
						 })
					 }	
			   }
			   ,cancel: function(index, layero){
				   
			  }
			})
			 $("#menuName").focus();			 
		 }
	}
})

vm.$watch('menuData.menuId', function (newVal, oldVal) {
	  // 做点什么
		if(newVal!==oldVal){
			vm.menuData.menuId = newVal.replace(/[^\d]/g,'');
		}
	})
var  initData = function(){
	$.aAjax({
		 url:ykyUrl.database +'/v1/menus/menutypeList',
		 type:'GET',
		 success:function(data){
			 if(data.length>0){
				 vm.dataList = data;
			 }
		 },
		 error:function(data){
			 console.log('error');
		 }
	})
}
initData();