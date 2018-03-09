/**
 * Created by zhoulixia on 2017/9/30.
 */

Vue.component('product-edit',{
	template:`
			<div class="product-edit-style">
				<div class="box-header with-border">
					<a class="btn btn-sm btn-danger pull-right add-product" style="width:inherit;" @click="showModal = true">
						<i class="fa fa-plus"></i> 新增商品
					</a>
				</div>
				<div class="chart box-body" style="position: relative;">
				   <rowspan-table
				          :columns="gridColumns"
				          :pageflag="pageflag"
				          :query-params="queryParams"
				          :checkflag = "checkflag"
				          :api="url"
				          :refresh="refresh"
				          :show-pagesize-change="showPagesizeChange"  
			        	  ref = "productList"
				  >
				  </rowspan-table>
				  <div class="col-sm-12">
				  	<button id="multipleDelete" class="btn btn-success sendData margin10" @click="multipleDelete('')"> 批量删除</button>
				  	<button id="export" class="btn btn-success sendData margin10" @click="derivedProduct(1)"><i class="fa fa-download mr5"></i> 导出</button>
				  	<button id="export" class="btn btn-success sendData margin10" @click="derivedProduct(2)"><i class="fa fa-download mr5"></i> 导出异常商品</button>
			  		</div>
				</div>
				<div class="box-footer text-center activity-info">
					<button type="button" class="btn btn-default" @click="stepChange(-1)">上一步</button>
					<button type="button" class="btn btn-danger" @click="saveData">保  存</button> 
				</div>
				<modal v-show="showModal"
					   @close="showModal = !showModal"
					   :show-foot = "showFoot"	           
			           :modal-style="modalStyle">
					<h3 slot="header">添加商品</h3>
					<section id="file-upload">
						<form>
							<div class="form-group">
								<div class="form-control">
									<a class="btn btn-primary" id="uploadBtn">上传文件</a>
									<span v-show="fileName !== ''">{{fileName}} <i class="label-success">上传成功</i></span>
									 <a :href="fileTemplateUrl">Excel模板下载</a>	
									<div class="tip">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现上传商品</div>
								</div>
							</div>
							<div class="text-center">
								<button type="button" class="btn btn-primary" @click="confirmUpload">保  存</button>
								<button type="button" class="btn btn-default" @click="cancelUpload">取消</button>
							</div>														
						</form>
					</section>
				</modal>
				<form id="exportForm" action="" method="GET" target="_blank" style="display: none;">
					<input type="hidden" name="Authorization">
					<input name="status" type="hidden"/>
					<input name="promoModuleProductIds" type="hidden"/>
					<input name="promoModuleId" type="hidden"/>
					<input name="promotionId" type="hidden"/>
				</form>
		</div>`,
	props:{
		productEditData:{
			type:Object,
			default(){
				return {}
			}
		},
		api:{
			type:String,
			default(){
				return '' 
			}
		},
		productAnalysisApi:{
			type:String,
			default(){
				return ''
			}
		},
		delProductApi:{
			type:String,
			default(){
				return ''
			}
		},
	    derivedProductApi:{
	    	type:String,
	    	default(){
	    		return ''
	    	}
	    },
	    productDataApi:{
	    	type:String,
	    	default(){
	    		return ''
	    	}
	    }
	},
	data:function(){
		return{
			fileName: '', //上传文件的名称
			fileUrl: '',  //上传文件的url
			fileTemplateUrl:ykyUrl._this + '/resources/download/活动装修模板.xlsx',   //excel文件模板
			showModal:false,
			showFoot:false,
			modalStyle:{
				width:700,
				maxHeight:300,
				overflowY:'auto'
			},
			queryParams: { //请求接口参数
                    defaultStatus: true, //监测参数变化标识                   
                    pageSize: 10,
                    page: 1,
                    draft:'Y'
                },
            checkflag : true,
        	pageflag : true,
        	refresh : true,
        	showPagesizeChange : true,
			url: this.productDataApi ? this.retBool(this.productDataApi,this.productEditData):'' ,
			gridColumns:[
			        	    {key :'selected', name: '',width: '50px'},
			        	    {key: 'index', name: '序号',width: '50px'},    
			        	    {key: 'manufacturer', name: '制造商', cutstring: true,textColor:{
			        	    	condition: "'{status}'=='UNABLE'",
			        	    	color: 'red'
			        	    }},
			        	    {key: 'manufacturerPartNumber', name: '型号', width: '180px', cutstring: true, textColor:{
			        	    	condition: "'{status}'=='UNABLE'",
			        	    	color: 'red'
			        	    }},
			        	    {key: 'vendorName', name: '分销商', cutstring: true,textColor:{
			        	    	condition: "'{status}'=='UNABLE'",
			        	    	color: 'red'
			        	    }},
			        	    {key: 'totalQty', name: '库存', width:'90px', cutstring: true},
			        	    {key: 'sourceName', name: '仓库', cutstring: true,textColor:{
			        	    	condition: "'{status}'=='UNABLE'",
			        	    	color: 'red'
			        	    }},
			        	    {key: 'currencyUomId', name: '币种', width: '50px', callback:{
			        			action : 'changeCurrency',
			        			params : ['{currencyUomId}']
			        		}},
			        	    {key: 'qtyBreak', name: '阶梯', endString: '+', type: 'array',width:'100px',textColor:{
			        	    	condition: "'{status}'=='UNABLE'",
			        	    	color: 'red'
			        	    }},
			        	    {key: 'priceBreak', name: '限时价', type:'array',textColor:{
			        	    	condition: "'{status}'=='UNABLE'",
			        	    	color: 'red'
			        	    }},
			        	    {key: 'errorDescription', name: '备注', cutstring: true, textColor:{
			        	    	condition: "'{status}'=='UNABLE'",
			        	    	color: 'red'
			        	    }},
			        	    {
			        			key : 'operate',
			        			name : '操作',
			        			align : 'center',
			        			width: '60px',
			        			items : [ {
			        				className : 'btn-delete',
			        				text : '删除',
			        				show : true,
			        				callback : {
			        					action : 'deleteItem',
			        					params : [ '{promoModuleProductId}']
			        				}
			        			} ]
			        		}
			        	    
			        	    ]
		}
	},
	mounted(){
		var $this = this;
		var uploader = createUploader({  //商品文件上传初始化
			buttonId: "uploadBtn", 
			uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
			url:  ykyUrl.webres,
			types: "xls,xlsx,csv",//可允许上传文件的类型
			fileSize: "5mb", //最多允许上传文件的大小
			isImage: false, //是否是图片
			init:{
				FileUploaded : function(up, file, info) { 
		            layer.close(index);
					if (info.status == 200 || info.status == 203) {
						$this.fileName = file.name;
						$this.fileUrl = _fileUrl;
					} else {
						layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
					}
					up.files=[];
				}
			}
		}); 
		uploader.init();
	},
	methods:{
        retBool: function(show, data) {
        	if (show) {
                if (typeof(show) == 'boolean') {
                    return show;
                }
                var retShow = show.replace(/{([^}]+)}/g, function($0, $1) {
                    var val = '';
                    val = data[$1];
                    return val == null ? '' : val;
                });
                return retShow;
            }
        },
		confirmUpload:function(){ //上传商品保存事件
			var $this = this;
			var productEditData = this.productEditData;
			if(this.fileName === ''){
				layer.msg('请选择文件！')
				return false;
			}
			var index = layer.load(1, {
				  shade: [0.1,'#fff'] //0.1透明度的白色背景
				});
			var url = $this.productAnalysisApi + '?promotionId=' + productEditData.promotionId + '&promoModuleId=' + productEditData.promoModuleId + '&fileUrl=' + $this.fileUrl + '&oriFileName=' + $this.fileName;
			syncData(url, 'POST', null, function(res,err){
				layer.close(index);
				if(err == null){
					$this.fileName='';
					$this.fileUrl='';
					$this.queryParams.defaultStatus = !$this.queryParams.defaultStatus;
					$this.showModal = false;
					
					$this.$nextTick(function(){
						vm.$children[0].ickeck = [];
						vm.$children[0].isAll = false;
						$('#checkedIds').val('');
						$('.chk_al1').prop('checked', false)
					});
				}
			})
		},
		cancelUpload:function(){  //上传商品取消事件
			this.showModal = false;
			this.fileName='';
			this.fileUrl='';
		},
		derivedProduct:function(type){  //商品导出事件
			var conTr = $(".table-container tbody tr");
			var conTd = $(".table-container tbody tr td"); 
			if(conTr.length == 1 && conTd.length == 1) {
				layer.msg('没有数据可以下载')
				return false;
			}
			if(type == 1){
				var ids = '', checkedItem;
				checkedItem = eval('('+$("#checkedIds").val()+')');
				if(checkedItem.length > 0){
					for(var i =0; i<checkedItem.length; i++){
						if(ids !== ''){
							ids += ','+checkedItem[i].promoModuleProductId
						}else{
							ids = checkedItem[i].promoModuleProductId;
						}
					}
				}else{
					layer.msg('请选择需要下载的数据')
					return false;
				}
				$("input[name=promoModuleProductIds]").val(ids);
				$("input[name=status]").val('');
			}else{
				$("input[name=promoModuleProductIds]").val('');
				$("input[name=status]").val('UNABLE');
			}
			$("input[name=Authorization]").val($('#pageToken').val());
			$("input[name=promoModuleId]").val(this.productEditData.promoModuleId);
			$("input[name=promotionId]").val(this.productEditData.promotionId);
			var form=$("#exportForm");//定义一个form表单
		    form.attr("action",this.derivedProductApi);
		    form.submit();//表单提交
		},		
		saveData: function(){
			var $this = this,data = this.productEditData;
			var conTr = $(".table-container tbody tr");
			var conTd = $(".table-container tbody tr td"); 
			if(conTr.length == 1 && conTd.length == 1) {
				layer.msg('请上传商品！')
				return false;
			}
			var idx=layer.load(1, {
				  shade: [0.1,'#fff'] 
				});
			syncData($this.api,'PUT',data,function(res,err){ 
				if(err == null){
					$this.$emit('cancle');
				}
				layer.close(idx);
			})		
		},
		multipleDelete:function(prdId){  //商品删除事件
			var that = this;
			var ids = [], checkedItem;
			if(prdId){
				ids.push(prdId);
			}else{
				checkedItem = eval('('+$("#checkedIds").val()+')');
				if(checkedItem.length > 0){
					for(var i =0; i<checkedItem.length; i++){
						ids.push(checkedItem[i].promoModuleProductId)
					}
				}else{
					layer.msg('请选择需要删除的数据')
					return false;
				}
			}
			var index = layer.confirm('确认删除？',{
				 btn: ['确认','取消'] //按钮
			},function(){
				var url = that.retBool(that.delProductApi,that.productEditData);
				multiDelete(url, 'DELETE', ids,
						function(res, err) {//页面加载前调用方法
							that.queryParams.defaultStatus = !that.queryParams.defaultStatus;
						});
				layer.close(index);
			})
		},
        stepChange:function(n){
			this.$emit('step-change', n);
		},
	}
})
var deleteItem = function(index, params){
	vm.$refs['productListSet'].$refs['productEdit'].multipleDelete(params[0]);
};
function changeCurrency(index,e){
	if(e[0]){
		var upperStr = e[0].toUpperCase();
		
		if(upperStr === 'CNY'){
			upperStr = 'RMB';
		}
		return upperStr;
	}	
}