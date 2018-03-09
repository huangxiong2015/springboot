Vue.component('delivery-accuracy-edit',{
	template:`
		<modal
			:modal-style=modalStyle
			@close="toggleModal"
            @click-ok="modalOk"
            @click-cancel="toggleModal"
		>
			<h3 slot="header" v-text="headerTitle"></h3>
			<form class="delivery-accuracy-edit" id="create">
				<div class="form-group">
					<div class="col-md-12" v-if="isAdd">
						<label class="col-sm-2 control-label">请选择分销商：<span class="text-red">*</span></label>
						<div class="col-sm-9" style="min-height:180px;">
							<letter-select
								v-if="letterSelect.options.length"
							     :options="letterSelect.options"
							     :id="letterSelect.id"
							     :option-id="letterSelect.optionId"
							     :option-name="letterSelect.optionName"		 
							     @get-selecteds="getSelected"
							     :selected="letterSelect.selected"
							     :searchText="letterSelect.searchText"
							     :char="letterSelect.char"
						    	 :multiple="letterSelect.multiple"
					    		 :show-search="letterSelect.showSearch"
					    		 ref="letterSelect"
				    	  ></letter-select>
				    	  <input type="hidden" name="selectVendor" v-model="selectVendorId" required>
						</div>
					</div>
				</div>
				<div class="form-group" v-if="!isAdd">
					<div class="col-md-12">
						<label class="col-sm-2 control-label">分销商：</label>
						<div class="col-sm-9" style="padding-top:7px;">
							<span v-text="selectVendorName"></span>							
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<label class="col-sm-2 control-label">交期准确率：<span class="text-red">*</span></label>
						<div class="col-sm-2" style="position: relative;">
							<input type="text" class="set-input form-control" name="ltAcc" v-model.trim="ltAcc" required>
							<span style="position:absolute;top:25%;right:0;">%</span>							
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<label class="col-sm-2 control-label">备注：<span class="text-red">*</span></label>
						<div class="col-sm-9">
							<input type="text" class="set-input form-control" name="comments" v-model.trim="comments" required maxlength=20>							
						</div>
					</div>
				</div>
			</form>
		</modal>
		`,
	props:{
		letterSelect:{
			type:Object,
			default(){
				return {
					showSearch:true,
		    		showLetter:true,
					keyname:'选择',
					validate:{
	                    "required": true
	                },
	                id:'selbox',
	                name:'selbox',
	                options:[],
	                optionId: 'id',
	                optionName: 'name',
	                selected:'',
	                placeholder:'搜索',
	                showSearch:true,
	            	multiple:true //单选
				}
			}
		},
		leadtimeData:{
			type:Object,
			default(){
				return {
					id:'',
					name:'',
					leadTimeAcc:'',
					comments:''
				}
			}
		},
		isAdd:{
			type:Boolean,
			default(){
				return true
			}
		}
	},
	data(){
		return{
			modalStyle:{
				 width: 950
			},
			headerTitle:'添加分销商交期准确率',
			selectVendorId: this.leadtimeData && this.leadtimeData.id ? this.leadtimeData.id : '',
			selectVendorName: this.leadtimeData && this.leadtimeData.name ? this.leadtimeData.name : '',
			ltAcc:this.leadtimeData && this.leadtimeData.leadTimeAcc ? this.leadtimeData.leadTimeAcc : '',
			comments:this.leadtimeData && this.leadtimeData.comments ? this.leadtimeData.comments : ''
		}
	},
	watch:{
		ltAcc:function(val){
			//var reg = /^\d(\.\d)?$|^[1-9]\d(\.\d)?$/;
			var reg = /^(100|[1-9]?\d(\.\d)?)$/;
			if(!reg.test(val)){
				$("#create input[name='ltAcc']").addClass("err-bor");
			}else{
				$("#create input[name='ltAcc']").removeClass("err-bor");
			}
		},
		comments:function(val){
			if(val == ''){
				$("#create input[name='comments']").addClass("err-bor");
			}else{
				$("#create input[name='comments']").removeClass("err-bor");
			}
		}
	},
	methods:{
		getSelected:function(obj){
			var ls = this.letterSelect;
			this.selectVendorId = obj[ls.optionId];
			this.selectVendorName = obj[ls.optionName];
			this.$emit('get-selects',obj);
		},
    	toggleModal:function(){
    		this.$emit('click-close');
    	},
    	modalOk:function(){
    		var that = this;
    		var reg = /^(100|[1-9]?\d(\.\d)?)$/;
    		if(this.selectVendorId == '' || this.ltAcc == '' || this.comments == '' || !reg.test(this.ltAcc)){
    			if(this.selectVendorId == ''){
        			layer.msg('请选择分销商！');
        		}
        		if(this.ltAcc == ''){
        			$("#create input[name='ltAcc']").addClass("err-bor");
        		}
        		if(this.comments == ''){
        			$("#create input[name='comments']").addClass("err-bor");
        		}
    			return;
    		}
    		if(this.ltAcc < 90){
    			var index = layer.confirm('请确认交期准确率是否小余90%？', {
    				closeBtn: 0,
    				btn: ['是','否'] //按钮
    				}, function(){
    		    		that.saveData(index);
    				}, function(){
    				  that.ltAcc = '';
    				});
    		}else{
    			that.saveData();
    		}
    	},
    	saveData:function(index){
    		var data = {
    				id:this.selectVendorId ? this.selectVendorId : '',
    				vendorName:this.selectVendorName ? this.selectVendorName : '',
    				leadtimeAccuracyrate:this.ltAcc ? this.ltAcc : '',
    				comments:this.comments ? this.comments : ''
    				
    		}
    		this.$emit('click-ok',data);
    		layer.close(index);
    	}
	}
})


var vm ;
$(function(){
	
      vm = new Vue({
        el: '#manufacturer-list',
        data: {
            url : ykyUrl.party + "/v1/vendors/supplier/list", // 访问数据接口
            queryParams : {           // 请求接口参数
            	pageSize: 10,        // 分页参数
                page:1,              // 当前页
                defaultStatus:true  // 监测参数变化标识
            },
            title:'',
            gridColumns: [          // 表格列
                {key: 'vendorName', name: '分销商',  align:'center', cutstring: true, default : '-'},
                {key: 'leadtimeAccuracyrate', name: '交期准确率',align : 'center',
                	render:function(h,params){
                		var str = params.row && params.row.leadtimeAccuracyrate ? params.row.leadtimeAccuracyrate + '%' : '-';
                		return h('span',{
                			'class':'cutstring'
                		},str);
                	}
                },
                {key: 'comments', name: '备注',align : 'center',cutstring: true, default : '-'},
                {key : 'operate',name : '操作',align : 'center',width:'150px',items : [  
                   {
						className : 'btn-edit',
						show : true,
						text : '修改',
						callback : {
							action : 'editData',
							params : [ '{id}', '{vendorName}', '{leadtimeAccuracyrate}', '{comments}' ]
						}
					}, {
						className : 'btn-delete',
						show : "'{leadtimeAccuracyrate}' != '' || '{comments}' != ''",
						//show:true,
						text : '删除',
						callback : {
							confirm : {
								title : '删除',
								content : '确认删除？'
							},
							action : 'changeData',
							params : [ '{id}']
						}
					} 
					]
				}
            ], 
            pageflag : true,    // 是否显示分页
            refresh: false,     // 重载
            showModal:false,
            letterSelect:{
            	keyname:'选择分销商',
				validate:{
                    "required": true
                },
                id:'selbox',
                name:'selbox',
                options:[],
                optionId: 'id',
                optionName: 'vendorName',
                selected:'',
                placeholder:'搜索分销商',
                showSearch:false,
            	multiple:false //单选
	    	},
	    	ldData:{},
	    	isAdd:true
	    	
        },
        methods:{
    		getVendorList:function(){
    			var that = this;
            	syncData(that.url + '?size=10000','GET',null,function(res,err){
            		if(err == null){
    					that.letterSelect.options = res.list;
    				}
            	})
    		},
        	getSelects:function(ele){
        		this.letterSelect.selected = ele.id;
        	},
        	addData:function(){
        		this.getVendorList();
        		this.isAdd = true;
        		this.letterSelect.selected = '';
        		this.ldData = {};
        		this.showModal = true;
        	},
        	toggleModal:function(){
        		this.showModal = false;
        	},
        	modalOk:function(obj){
        		this.showModal = false;
        		var data = [],arr = ['id','vendorName','leadtimeAccuracyrate','comments'];
        		for(var i in arr){
        			if(obj[arr[i]]){
        				data.push(obj[arr[i]])
        			}
        		}
        		changeData('',data);
        	}
        }
    }); 
      
}); 

function editData(index,params){
	vm.ldData = {
		id:params[0],
		name:params[1],
		leadTimeAcc:params[2],
		comments:params[3]
	}
	vm.isAdd = false;
	vm.showModal = true;	
}
function changeData(index, params){
	var data = {
			partyId:params[0] ? params[0] : '',
			leadtimeAccuracyrate:params[2] ? params[2] : '',
			comments:params[3] ? params[3] : ''
	}
	var index = layer.load(1, {
		  shade: [0.1,'#fff'] //0.1透明度的白色背景
		});
	syncData(ykyUrl.party + "/v1/vendors/updateLeadTime/", 'PUT',data, function(res, err) {// 页面加载前调用方法
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;// 重载
				}.bind(vm), 400);
				layer.close(index);
			});
}
