var vm ;
$(function(){
   vm = new Vue({
        el: '#productstandedit',
        data: {
    		keyname: '选择制造商',
            validate:{
                "required": true
            },
            id:'selbox',
            name:'selbox',
            options: [],
            optionName: 'brandName',
            optionId: 'id',
            selected:[],
            multiple:false,   //单选
            placeholder:'搜索制造商',
            showBox:true,
            activeManufacturerId:[],
            manufacturerIsFuzzySearch:true, //制造商模糊搜索
            manufacturerReloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}', //模糊搜索api
            manufacturerPartNumber:'',//型号
            manufacturerId:[],  //保存时制造商id
            showModal: false,
            chosenManufacturer:[],
            showManufacturer:false,
            modalStyle: {
                width: 1100,
                height:500,
				overflowY:'auto',
                border:'#f00 1px solid'
            },
            modalText:{
            	okText:"确定",
                cancelText:"取消",
                title:"选择制造商"
            }
        },  
        created:function(){
        	getManufacturer();
        },
        methods:{
        	  getSelected: function(obj){  //获取选中值
        		  vm.chosenManufacturer = [];
                  vm.chosenManufacturer.push(obj) ; 
                },
                choose:function(){
                	this.showModal = true;
                	this.chosenManufacturer = this.activeManufacturerId;
                	if(this.activeManufacturerId[0]&&this.activeManufacturerId[0].id){
                		var id = this.activeManufacturerId[0].id;
                		this.selected = (typeof id)=='number'?id.toString():id;
                	}
                	this.showManufacturer = true;
                	vm.$forceUpdate();
                },
                modalOk: function(data) { //ok调用方法
                    this.showModal = false;
                    this.showManufacturer = false;
                    this.activeManufacturerId = this.chosenManufacturer;
                },
                toggleModal: function() { //显示隐藏；cancel调用方法
                    console.log('toggleDialog')
                     this.showManufacturer = false;
                    this.showModal = !this.showModal;
                },
                m_save:function(){
            		//调保存接口
                	var  str = vm.manufacturerPartNumber ;
                	if(str == ''){
                		layer.msg('请填写型号', {time: 3000})
                		return
                	}
                	if(vm.activeManufacturerId.length > 0  &&  vm.activeManufacturerId[0].id ){
                		var id = vm.activeManufacturerId[0].id;
                		var name = vm.activeManufacturerId[0].brandName;   //供应商名称
                		var brandShort = vm.activeManufacturerId[0].brandShort ; //供应商简称
                    	if(id == 'undefined' || id =='' ){
                    		layer.msg('请选择制造商', {time: 3000})
                    		return
                    	}
                	}else{
                		layer.msg('请选择制造商', {time: 3000})
                		return
                	}
                	
                	var addData={
                			manufacturerPartNumber:vm.manufacturerPartNumber,
                			manufacturerId:id,
                			manufacturer:name,
                			brandShort:brandShort
                	   }
                	syncData(ykyUrl.product + "/v1/products/stand/saveWhiteList", 'POST', addData, function(res,err) {
            			if (err == null && res == '') {
            				layer.msg('新增成功', {time: 3000});
            				window.location = ykyUrl._this + '/productstand.htm';
            				
            			}
            		},false);
        		},
            	m_sure : function(){
            		vm.manufacturerId = vm.activeManufacturerId ;
        		},
        		m_cancel:function(){
        			window.location = ykyUrl._this + '/productstand.htm';		
        		},
            
        }
    }); 
   
 //获取制造商
   function getManufacturer(){
	    var url = ykyUrl.product +"/v1/products/brands";
	    var arr = [];
	    syncData(url, 'GET', null, function(res,err){
	    	if(err == null){
	    		var list = res;
	    		$.each(list,function(i,item){
	    			arr.push({
	    				id:item.id,
	    				brandName:item.brandName
	    			})
	    		})
	    		vm.options = arr;
	    	}
	    })
   }
    
}); 