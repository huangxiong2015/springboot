/**
 * Created by zhoulixia on 2017/9/30.
 */

Vue.component('product-set',{
	template:`
			<form class="form-horizontal product-set-style" id="productSetForm">				
				<div class="form-group">
				<div class="col-md-12">
					<label class="col-sm-2 control-label">显示字段设置：</label> 
					<div class="col-sm-10">
					<div class="checkbox inline">
						<table class="set-fields">
							<tr>
								<td>
									<div>字段显示</div>
								</td>
								<td v-for="ele in fieldsList">								
										<div class="check-span">
											<input type="checkbox" :id="ele.value" class="input_check" :value="ele.value" v-model="fieldsId"/> 
											<label :for="ele.value"></label>{{ele.textName}}
							            </div>																            
								</td>
							</tr>
							<tr>
								<td>
									<div>自定义字段名</div>
								</td>
								<td v-for="ele in fieldsList">
									<div class="">
										<input type="text" class="set-input form-control" maxlength="5" v-model.trim="ele.alias">
									</div>
								</td>
							<tr>
						</table>
					</div>						
			            
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<label class="col-sm-2 control-label">商品折扣设置：</label> 
					<div class="col-sm-10">
						<input type="text" name="discount[value]" class="set-input form-control" id="discount" v-model.trim="discount" :disabled="isDiscount == true ? false : true">
						<input type="hidden" name="discount[isOpen]" v-model="isDiscount">
						<span class="control-label">请输入0~1之间数字，支持两位小数，例：0.9代表9折</span>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<label class="col-sm-2 control-label">折扣限时生效：</label> 
					<div class="col-sm-2">
						<select class="input-sm form-control" :disabled="isDiscount == true ? false : true" name="effectSet[isLimit]" v-model="isLimitEffective">
							<option value=true>是</option>
							<option value=false>否</option>
						</select>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<label class="col-sm-2 control-label">生效日期：</label> 
					<div class="col-sm-5">
		                <div id="createDataRange" class="input-daterange input-group">
		                	<div class="input-append date startDates form_datetime">
								<input id="startTime" type="text" name="effectSet[startTime]" class="form-control" readonly="readonly" :disabled="isDiscount == true && isLimitEffective == 'true' ? false : true" v-model="effectiveStartTime"> 					                	
		                	</div>
			                <span class="input-group-addon">至</span> 
			                <div class="input-append date endDates form_datetime">
			                	<input id="endTime" type="text" name="effectSet[endTime]" class="form-control" readonly="readonly" :disabled="isDiscount == true && isLimitEffective == 'true' ? false : true" v-model="effectiveEndTime">
		                	</div>
		                </div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<label class="col-sm-2 control-label">模块非生效期：</label> 
					<div class="col-sm-10">
						<div class="radio inline">
							<span class="radio-span">
								<input id="ineffectiveShow" type="radio" class="input_radio" name="effectSet[ineffectiveIsShow]" value="true" :disabled="isDiscount == true && isLimitEffective == 'true' ? false : true" v-model="ineffectiveIsShow"/> 
								<label for="ineffectiveShow"></label>显示
				            </span>
			            </div>
			            <div class="radio inline">
							<span class="radio-span">
								<input id="ineffectiveNotShow" type="radio" class="input_radio" name="effectSet[ineffectiveIsShow]" value="false" :disabled="isDiscount == true && isLimitEffective == 'true' ? false : true" v-model="ineffectiveIsShow"/> 
								<label for="ineffectiveNotShow"></label>不显示
				            </span>
			            </div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<label class="col-sm-2 control-label">推广词：</label> 
					<div class="col-sm-4">
						<input type="text" class="form-control" name="effectSet[extensionWords]" :disabled="isDiscount == true && isLimitEffective == 'true' ? false : true" v-model="extensionWords">
					</div>
					<span class="control-label inline">开始/结束还有00:00:00</span>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<label class="col-sm-2 control-label">显示页数设置：</label> 
					<div class="col-sm-10">
						<div>显示页数<input type="input" required id="pageNum" name="page[pageNum]" max="20" min="1" class="set-input form-control digits" v-model.trim="pageNum"/>页<span class="control-label" style="margin-left:10px;">最多显示20页</span></div>
						<div>每页显示<input type="input" required id="perPage" name="page[perPage]" max="50" min="1"  class="set-input form-control digits" v-model.trim="perPage"/>条<span class="control-label" style="margin-left:10px;">每页最多显示50条</span></div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">				
					<label class="col-sm-2 control-label">添加商品：</label>
					<div class="col-sm-2" v-for="(v,k) in productSetType">
						<div class="radio inline">
							<span class="radio-span">
								<input type="radio" :id="k" name="dataSource" class="input_radio" :value="k" v-model="dataSource" :disabled="isSetProduct?true:false"/> 
								<label :for="k"></label>{{v}}
				            </span>
			            </div>
					</div>				
				</div>
			</div>
			<div class="form-group" v-show="dataSource == 'GET_BY_UPLOAD'">
				<div class="col-md-12">					
					<div class="col-sm-8 col-md-offset-2">
						<div class="set-wrap clear-float">
							<div>
								<span class="radio inline col-sm-3">库存读取方式</span>
								<div class="radio inline" v-for="ele in qtyType">
									<span class="radio-span">
										<input type="radio" :id="ele.id" name="uploadData[useStockQty]" class="input_radio" :value="ele.value" v-model="useStockQty"/> 
										<label :for="ele.id"></label>{{ele.name}}
						            </span>
					            </div>
							</div>
							<div class="clear-float" style="line-height:35px;margin:10px 0px;">
								<div class="col-sm-3"><a class="file-btn" id="uploadBtn">上传文件</a></div>
								<div class="col-sm-3 file-name" v-if="fileName" v-text="fileName" :title="fileName"></div>	
								<div class="col-sm-2" v-if="fileName"><i class="label-success">上传成功</i></div>
								<div class="col-sm-4"><a :href="fileTemplateUrl">Excel模板下载</a></div>
							</div>
							<div class="col-sm-12"style="color:#fe5c1f">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现上传商品</div>
							<input type="hidden" name="uploadData[fileUrl]" v-model="fileUrl"/>
							<input type="hidden" name="uploadData[fileName]" v-model="fileName"/>
						</div>
					</div>				
				</div>
			</div>
			<div class="form-group" v-show="dataSource == 'GET_BY_CONDITION'">
				<div class="col-md-12">					
					<div class="col-sm-10 col-md-offset-2">
						<div class="set-wrap clear-float">
							<div>
								<label class="col-sm-2 checkbox">活动分销商：</label>
								<div class="col-sm-10">
									<a class="checkbox inline" @click="selectVendor(activeVendorList)">选择</a>
									<div class="lemon-area">
										<div class="lemon-tag" v-for="(ele,index) in activeVendorList">
											<span class="lemon-tag-text" v-text="ele.supplierName"></span> 
											<i class="fa fa-close" @click="deleteSystemSet(index,'activeVendorList')"></i>
										</div>
									</div>
								</div>
							</div>
							<div>
								<label class="col-sm-2 checkbox">活动制造商：</label>
								<div class="col-sm-10">
									<a class="checkbox inline" @click="selectBrand(activeBrandList)">选择</a>
									<div class="lemon-area">
										<div class="lemon-tag" v-for="(ele,index) in activeBrandList">
											<span class="lemon-tag-text" v-text="ele.brandName"></span> 
											<i class="fa fa-close" @click="deleteSystemSet(index,'activeBrandList')"></i>
										</div>
									</div>								
								</div>
							</div>
							<div>
								<label class="col-sm-2 checkbox">活动商品分类：</label>
								<div class="col-sm-10">
									<a class="checkbox inline" @click="selectCategory(activeCategoryList)">选择</a>
									<div class="lemon-area">
										<div class="lemon-tag" v-for="(ele,index) in activeCategoryList">
											<span class="lemon-tag-text" v-text="ele.name"></span> 
											<i class="fa fa-close" @click="deleteSystemSet(index,'activeCategoryList')"></i>
										</div>
									</div>
								</div>							
							</div>	
						</div>
					</div>				
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">				
					<label class="col-sm-2 control-label">显示活动图标：</label>
					<div class="col-sm-1">
						<div class="radio inline">
							<span class="radio-span">
								<input type="radio" id="isShowFlag" name="promotionFlag[show]" class="input_radio" value=true v-model="showFlag"/> 
								<label for="isShowFlag"></label>是
				            </span>
			            </div>
					</div>
					<div class="col-sm-1">
						<div class="radio inline">
							<span class="radio-span">
								<input type="radio" id="notShowFlag" name="promotionFlag[show]" class="input_radio" value=false v-model="showFlag"/> 
								<label for="notShowFlag"></label>否
				            </span>
			            </div>
					</div>
				</div>
			</div>
			<div class="form-group" v-if="showFlag == 'true'">
				<div class="col-md-12">				
					<label class="col-sm-2 control-label">图标内容：</label>
					<div class="col-sm-2">
						<select class="input-sm form-control" name="promotionFlag[type]" v-model="flagType">
							<option value="1">促销</option>
							<option value="2">满减</option>
							<option value="0">自定义</option>
						</select>
					</div>					
				</div>
			</div>
			<div class="form-group"  v-show="showFlag == 'true' && flagType == 0">
	            <div class="col-md-12">
	                <label class="col-sm-2"></label>			                
	                <div class="col-sm-5 upload-icon-wrap">
	                	<div class="line" v-for="(value,key,index) in flagUrl">
		                	<div class="check-span">
								<input type="checkbox" :id="key" class="input_check" :value="key" v-bind="{checked:value.img?'checked':''}" @click="resetFlag($event,key)" /> 
								<label :for="key"></label>{{flagName[key]}}
				            </div>
							<div class="icon-wrap">				                		
		                		<img width=100 height=100 :src="value.img" v-if="value.img">
		                		<span class="icon-plus add-icon" v-if="!value.img"></span>
		                		<span :id="key+'Upload'" class="upload-btn show-btn">上传图片</span>		                		
		                	</div>
		                	<a class="delete-icon" v-if="value.img" @click="value.img = ''" >删除图片</a>
	                	</div>
	                </div>
	             </div>
	          </div>
            <div class="modal-footer">
            	<button type="button" class="btn btn-default" @click="stepChange(-1)">上一步</button> 
            	<button type="button" class="btn btn-danger" @click="nextStep('next')"  v-if="dataSource == 'GET_BY_UPLOAD'">下一步</button>
    			<button type="button" class="btn btn-danger" @click="nextStep('save')" v-if="dataSource == 'GET_BY_CONDITION'">保存</button>
            </div>
			<modal v-if="showModal"
				   @close="toggleModal"
		           @click-ok="modalOk"
		           @click-cancel="toggleModal"
		           :modal-style="modalStyle">
						<h3 slot="header" v-text="modalTitle">请选择分销商</h3>
						<div v-if="showVendor">							
							
							<letter-select							
							     :keyname="vendorSelect.keyname"
							     :validate="vendorSelect.validate"
							     :options="vendorSelect.options"
							     :id="vendorSelect.id"
							     :option-id="vendorSelect.optionId"
							     :option-name="vendorSelect.optionName"		 
							     @get-selecteds="getVendorSelected"
							     :placeholder="vendorSelect.placeholder"
							     :selected="vendorSelect.selected"
							     :searchText="vendorSelect.searchText"
							     :char="vendorSelect.char"
						    	 :multiple="vendorSelect.multiple"					    		 
							     ref="vendorLetter"
						 	></letter-select>
						 	<div class="lemon-area" v-if="tempActiveVendorList.length">
								<p>当前已选中：</p>
								<div class="s-l-area">
									<div class="lemon-tag" v-for="(ele,index) in tempActiveVendorList">
										<span class="lemon-tag-text" v-text="ele.displayName"></span>
										<i class="fa fa-close" @click="deleteTempActive(ele.id,'vendorLetter')"></i>
									</div>
								</div>
							</div>
					 	</div>
					 	<div v-if="showBrand">
						 	<letter-select
							     :keyname="brandSelect.keyname"
							     :validate="brandSelect.validate"
						    	 :options="brandSelect.options"
							     :id="brandSelect.id"
							     :name="brandSelect.name"
							     :option-id="brandSelect.optionId"
							     :option-name="brandSelect.optionName"		 
							     @get-selecteds="getBrandSelected"
							     :placeholder="brandSelect.placeholder"
							     :selected="brandSelect.selected"
							     :searchText="brandSelect.searchText"
							     :char="brandSelect.char"
						    	 :multiple="brandSelect.multiple"
								 :is-fuzzy-search="brandSelect.isFuzzySearch"
								 :reload-api="brandSelect.reloadApi"
							     ref="brandLetter"
						 	></letter-select>
						 	<div class="lemon-area" v-if="tempActiveBrandList.length">
						 		<p>当前已选中：</p>
						 		<div class="s-l-area">
								<div class="lemon-tag" v-for="(ele,index) in tempActiveBrandList">
									<span class="lemon-tag-text" v-text="ele.brandName"></span>
									<i class="fa fa-close" @click="deleteTempActive(ele,'brandLetter')"></i>
								</div>
								</div>
							</div>
					 	</div>
					 	<div v-if="showCategory">
						 	<lemmon-tab
						 		:api="categorySelect.api"
					 			:init-data="categorySelect.initData"
				 				:id="categorySelect.id"
			 					:name="categorySelect.name"
	 							:children="categorySelect.children"
	 							@get-data="getCategorySelected"
		 						ref="categoryTab"
						 	></lemmon-tab>
					 	</div>
		 	</modal>		 	
			</form>`,
	props:{
		productSetData:{
			type:Object,
			default(){
				return {
					"promotionContent":{
	            		 "contentSet":{},
	            		 "showSet":{
		                    id:'1',
		                    url:'',
		                    message:{}
	            		 }
					}
				}
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
	    }
	},
	data:function(){
		return{
			pageNum:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.page && this.productSetData.promotionContent.contentSet.page.pageNum ? this.productSetData.promotionContent.contentSet.page.pageNum : 10,
			perPage:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.page && this.productSetData.promotionContent.contentSet.page.perPage ? this.productSetData.promotionContent.contentSet.page.perPage : 10,
			dataSource:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.dataSource ? this.productSetData.promotionContent.contentSet.dataSource:'GET_BY_UPLOAD',
			useStockQty:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.uploadData && this.productSetData.promotionContent.contentSet.uploadData.useStockQty ?　this.productSetData.promotionContent.contentSet.uploadData.useStockQty　: false,
			fileUrl:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.uploadData && this.productSetData.promotionContent.contentSet.uploadData.fileUrl ?　this.productSetData.promotionContent.contentSet.uploadData.fileUrl　: '',
			fileName:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.uploadData && this.productSetData.promotionContent.contentSet.uploadData.fileName ?　this.productSetData.promotionContent.contentSet.uploadData.fileName　: '',
			discount:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.discount && this.productSetData.promotionContent.contentSet.discount.isOpen ? this.productSetData.promotionContent.contentSet.discount.value : '',
			isDiscount:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.discount && this.productSetData.promotionContent.contentSet.discount.isOpen ? this.productSetData.promotionContent.contentSet.discount.isOpen : false,
			showFlag:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.promotionFlag && this.productSetData.promotionContent.contentSet.promotionFlag.show ? String(this.productSetData.promotionContent.contentSet.promotionFlag.show) : 'true',
			flagType:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.promotionFlag && (this.productSetData.promotionContent.contentSet.promotionFlag.type || this.productSetData.promotionContent.contentSet.promotionFlag.type == 0) ? this.productSetData.promotionContent.contentSet.promotionFlag.type : 1,
			flagUrl:{
				list:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.promotionFlag && this.productSetData.promotionContent.contentSet.promotionFlag.list ? this.productSetData.promotionContent.contentSet.promotionFlag.list : {img:''},
				detail:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.promotionFlag && this.productSetData.promotionContent.contentSet.promotionFlag.detail ? this.productSetData.promotionContent.contentSet.promotionFlag.detail : {img:''},
				popwindow:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.promotionFlag && this.productSetData.promotionContent.contentSet.promotionFlag.popwindow ? this.productSetData.promotionContent.contentSet.promotionFlag.popwindow : {img:''},
			},
			isLimitEffective:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.effectSet && this.productSetData.promotionContent.contentSet.effectSet.isLimit ?　this.productSetData.promotionContent.contentSet.effectSet.isLimit　: false,
			effectiveStartTime:'',
			effectiveEndTime:'',
			ineffectiveIsShow:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.effectSet && this.productSetData.promotionContent.contentSet.effectSet.ineffectiveIsShow ?　this.productSetData.promotionContent.contentSet.effectSet.ineffectiveIsShow　: true,
			extensionWords:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.effectSet && this.productSetData.promotionContent.contentSet.effectSet.endTime ?　this.productSetData.promotionContent.contentSet.effectSet.extensionWords　: '',					
			fieldsId:[],
			fieldsList:[],
			vendor:[],
			brand:[],
			showModal:false,
			showVendor:false,
			showBrand:false,
			showCategory:false, 
			isUploadFile:false,
			isSetProduct:false,
			activeVendorList:[],   //选中的分销商
			activeBrandList:[],    //选中的制造商
			activeCategoryList:this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.condition && this.productSetData.promotionContent.contentSet.condition.category ? this.productSetData.promotionContent.contentSet.condition.category : [], //选中的分类
			tempActiveVendorList:[],   //临时选中的分销商
			tempActiveBrandList:[],    //临时选中的制造商
			tempActiveCategoryList:[], //临时选中的分类
			modalTitle:'',
			modalStyle:{
				width:1000,
				maxHeight:600,
				overflowY:'auto'
			},
			productSetType:{  //商品添加类型
				'GET_BY_UPLOAD':'手工上传商品',
				'GET_BY_CONDITION':'系统选择商品'
			},
			qtyType:[{   //库存读取方式类型
			        	 id:'uploadQty',
			        	 name:'上传库存',
			        	 value:false
			         },
			         {
			        	 id:'systemQty',
			        	 name:'系统库存',
			        	 value:true
			         }],
			vendorSelect:{  //分销商弹窗组件数据
				keyname:'选择分销商',
				validate:{
                    "required": true
                },
                id:'selbox',
                name:'selbox',
                options:[],
                optionId: 'id',
                optionName: 'supplierName',
                selected:[],
                placeholder:'搜索分销商',
            	multiple:true
			},
			brandSelect:{   //制造商弹窗组件数据
				keyname:'选择制造商',
				validate:{
                    "required": true
                },
                id:'selbox',
                name:'selbox',
                options:[],
                optionId: 'id',
                optionName: 'brandName',
                selected:[],
                placeholder:'搜索制造商',
                multiple:true,
                isFuzzySearch:true,
            	reloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}'
			},
			categorySelect:{   //分类弹窗组件数据
				api:ykyUrl.product + "/v1/products/categories/list",
				initData:[],
				id:'_id',
				name:'cateName',
				children: 'children'
			},
			fieldsInit:[{   //显示字段初始设置
				value:'id',
				textName:'型号',
				alias:'',
				seq:0
			},{
				value:'vendorName',
				textName:'分销商',
				alias:'',
				seq:1
			},{
				value:'manufacturer',
				textName:'制造商',
				alias:'',
				seq:2
			},{
				value:'qty',
				textName:'库存',
				alias:'',
				seq:3
			},{
				value:'originalPrice',
				textName:'原价',
				alias:'',
				seq:4
			},{
				value:'newPrice',
				textName:'促销价',
				alias:'',
				seq:5
			},{
				value:'operator',
				textName:'操作',
				alias:'',
				seq:6
			},],
			flagName:{  //  自定义图标
				list:'列表页',
				detail:'详情页',
				popwindow:'列表弹窗'
			},
			fieldsIdInit:['id','vendorName','manufacturer','qty','originalPrice','newPrice','operator'],
			fileTemplateUrl:ykyUrl._this + '/resources/download/活动装修模板.xlsx'
		}
	},
	created:function(){
		var $this = this;
		this.effectiveStartTime = this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.effectSet && this.productSetData.promotionContent.contentSet.effectSet.startTime ?　this.timeStampChange(this.productSetData.promotionContent.contentSet.effectSet.startTime)　: '',
		this.effectiveEndTime = this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.effectSet && this.productSetData.promotionContent.contentSet.effectSet.endTime ?　this.timeStampChange(this.productSetData.promotionContent.contentSet.effectSet.endTime)　: '',				
		this.fieldsId = this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet && this.productSetData.promotionContent.contentSet.fields ? [] : this.fieldsIdInit
		this.fieldsList = this.productSetData && this.productSetData.promotionContent && this.productSetData.promotionContent.contentSet &&this.productSetData.promotionContent.contentSet.fields ? this.getCustomField() : this.fieldsInit;
		if((this.dataSource == 'GET_BY_UPLOAD' && this.fileUrl) || (this.dataSource == 'GET_BY_CONDITION' && (this.vendor.length||this.brand.length||this.activeCategoryList.length))){
			this.isSetProduct = true;
		}
		if(this.dataSource == 'GET_BY_CONDITION' || !this.isSetProduct){
			var index = layer.load(1, {
				  shade: [0.1,'#fff'] //0.1透明度的白色背景
				});
			syncData(ykyUrl.party + "/v1/party/allparty", 'PUT', {roleType:'SUPPLIER',status:'PARTY_ENABLED'}, function(res,err) {  //供应商数据请求
				if (res) {
					$.each(res,function(i,e){
						if(e.partyGroup.groupName){
							e.supplierName = e.partyGroup.groupName;
						}
					})
					$this.vendorSelect.options = res;
					$this.vendor = $this.productSetData && $this.productSetData.promotionContent && $this.productSetData.promotionContent.contentSet && $this.productSetData.promotionContent.contentSet.condition?$this.productSetData.promotionContent.contentSet.condition.vendor:[];
				}
			});	
			syncData(ykyUrl.product + "/v1/products/brands", 'GET', null, function(res,err) {   //制造商数据请求
				layer.close(index);
				if (res) {
					$this.brandSelect.options = res;
					$this.brand = $this.productSetData && $this.productSetData.promotionContent && $this.productSetData.promotionContent.contentSet && $this.productSetData.promotionContent.contentSet.condition?$this.productSetData.promotionContent.contentSet.condition.brand:[];
				}
			});
		}
	},
	mounted:function(){
		var $this = this;
		var nums=this.flagUrl;
        for(var k in nums) {
            this.uploadInit(k,nums[k]);
        }
        var uploader = createUploader({   //商品文件上传初始化
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
        				$this.isUploadFile = true;
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
        
        $('#startTime').datetimepicker({
		    language:  config.language, 
		    format: 'yyyy-mm-dd hh:ii:00',
		    minuteStep: 1,
		    autoclose: true,
		    todayHighlight: true,
		    pickerPosition: "bottom-left"
		}).on('change',function(ev){
		    var startDate = $('#startTime').val();
		    $("#endTime").datetimepicker('setStartDate',startDate);
		    $this.effectiveStartTime = startDate;
		});
		
		$('#endTime').datetimepicker({
			 language:  config.language, 
		     format: 'yyyy-mm-dd hh:ii:00',
		     minuteStep: 1,
		     autoclose: true,
		     todayHighlight: true
		}).on('change',function(ev){
		    var endTime = $('#endTime').val();
		    $("#startTime").datetimepicker('setEndDate',endTime);
		    $this.effectiveEndTime = endTime;
		});
		if($this.isLimitEffective == 'true' && $this.effectiveStartTime && $this.effectiveEndTime){
			 $('#startTime').datetimepicker('setEndDate',$this.effectiveEndTime);
			 $('#endTime').datetimepicker('setStartDate',$this.effectiveStartTime);
		}
	},
	watch:{
		vendor:function(val,oldVal){ //分销商监听获取选中的分销商数据
			var $this = this;
			var arr = [];
			$.each(val,function(i,e){
				var obj = _.find($this.vendorSelect.options, function(chr) {
					  return chr.id == e;
					})
				arr.push(obj);
			})
			$this.activeVendorList = arr;
		},
		brand:function(val,oldVal){ //制造商监听获取选中的制造商数据
			var $this = this;
			var arr = [];
			$.each(val,function(i,e){
				var obj = _.find($this.brandSelect.options, function(chr) {
					  return chr.id == e;
					})
				arr.push(obj);
			})
			$this.activeBrandList = arr;
		},
		fieldsId:function(val,oldVal){
			var $this = this,value;
			value = _.find(val, function(chr) {
				 if(chr == 'newPrice'){
					 return chr;
				 }
			})
			if(value){
				$this.isDiscount = true;				
			}else{
				$this.isDiscount = false;
				$this.discount = '';
				$("#discount").parent().removeClass('has-error');
				$this.isLimitEffective = 'false';
			}
			if(oldVal.length && oldVal.length > val.length){
				var changeVal = _.difference(val,oldVal);
				var diffVal;
				for(var i=0; i < oldVal.length; i++){   
			        var flag = true;   
			        for(var j=0; j < val.length; j++){   
			            if(oldVal[i] == val[j])   
			            flag = false;   
			        }   
			        if(flag)   
			        	diffVal = oldVal[i];   
			    }
				$.each(this.fieldsList,function(i,e){
					if(e.value == diffVal){
						e.alias = '';
						return false;
					}
				})
			}
		},
		discount:function(val,oldVal){
			var reg = /^0\.(0[1-9]|[1-9]{1,2})$/;
			if((val && reg.test (Number(val))) || !val){
				$("#discount").parent().removeClass('has-error');
			}else{
				$("#discount").parent().addClass('has-error');
			}
		},
		dataSource:function(val){  //商品添加类型监听
			if(val == 'GET_BY_UPLOAD'){
				this.activeVendorList = [];
				this.activeBrandList = [];
				this.activeCategoryList = [];
			}else{
				this.fileName = '';
				this.fileUrl = '';
			}
		},
		isLimitEffective:function(val){
			if(val == 'false'){
				this.effectiveStartTime = '';
				this.effectiveEndTime = '';
				this.ineffectiveIsShow = 'true';
				this.extensionWords = ''
				$('#startTime').datetimepicker('setEndDate', '9999-01-01');
				$('#endTime').datetimepicker('setStartDate', '1900-01-01');
			}
		}
	},
	methods:{
		timeStampChange:function(timeStr){
			return new Date(timeStr).Format("yyyy-MM-dd hh:mm:ss")
		},
		getCustomField:function(){  //获取自定义字段名
			var $this = this;
			var arr = [];
			$.each($this.productSetData.promotionContent.contentSet.fields,function(i,e){
				if(e.alias){
					$.each($this.fieldsInit,function(idx,ele){
						if(e.value == ele.value){
							ele.alias = e.alias;
							return false;
						}
					})
				}
				arr.push(e.value);
			})
			this.fieldsId = arr;
			return this.fieldsInit;
		},
		selectVendor:function(list){//打开分销商弹窗
			var $this = this;
			$this.showModal = true;
			$this.vendorSelect.selected = [];
			$this.modalTitle = '请选择分销商';
			$.each(list,function(i,e){
				$this.vendorSelect.selected.push(e.id);
			})
			this.showVendor = true;			
		},
		selectBrand:function(list){ //打开制造商弹窗
			var $this = this;
			$this.showModal = true;
			$this.brandSelect.selected = [];
			$this.modalTitle = '请选择制造商';
			$.each(list,function(i,e){
				$this.brandSelect.selected.push(e.id);
			})
			this.showBrand = true;			
		},
		selectCategory:function(list){  //打开分类弹窗
			var $this = this;
			$this.categorySelect.initData = [];
			$this.modalTitle = '请选择商品分类';
			$.each(list,function(i,e){
				$this.categorySelect.initData.push(e);
			})
			$this.showModal = true;
			setTimeout(function(){
				$this.showCategory = true
			},600)
			
		},
		getVendorSelected:function(obj){  //分销商选中事件
			this.tempActiveVendorList = obj;
		},		
		getBrandSelected:function(obj){  //制造商选中事件
			this.tempActiveBrandList = obj
		},
		getCategorySelected:function(obj){  //分类选中事件
			this.tempActiveCategoryList = obj
		},
		deleteSystemSet:function(index,list){ //删除选中的分销商、制造商、分类事件
			var $this = this;
			$this[list].splice(index,1);
		},
		deleteTempActive:function(obj,ref){ //删除临时选中的分销商、制造商事件
			this.$refs[ref].del(obj);
		},
		toggleModal:function(){  //分销商、制造商、分类弹窗close事件
			var $this = this;
			if($this.showVendor){//分销商弹窗取消事件
				$this.closeModalEvent(1,$this.activeVendorList,'tempActiveVendorList')
			}else if($this.showBrand){//制造商弹窗取消事件
				$this.closeModalEvent(2,$this.activeBrandList,'tempActiveBrandList')
			}else if($this.showCategory){//分类弹窗取消事件
				$this.closeModalEvent(3,$this.activeCategoryList,'tempActiveCategoryList')
			}
		},
		modalOk:function(){  //分销商、制造商、分类弹窗ok事件
			var $this = this;
			if($this.showVendor){  //分销商弹窗取消事件
				$this.closeModalEvent(1,$this.tempActiveVendorList,'activeVendorList')
			}else if($this.showBrand){  //制造商弹窗取消事件
				$this.closeModalEvent(2,$this.tempActiveBrandList,'activeBrandList')
			}else if($this.showCategory){  //分类弹窗取消事件
				$this.closeModalEvent(3,$this.tempActiveCategoryList,'activeCategoryList')					
			}
		},
		closeModalEvent:function(type,list,dataList){  //分销商、制造商、分类弹窗关闭数据存储
			var $this = this;									
			$this.showModal = false;
			if(type == 1){
				$this[dataList] = list;
				$this.showVendor = false;
			}else if(type == 2){
				$this[dataList] = list;
				$this.showBrand = false;
			}else if(type == 3){
				$this[dataList] = list;
				$this.showCategory = false;
			}
		},
		uploadInit:function(key,idx){  //活动图标上传初始化
			var $this = this;
			createUploader({
				buttonId: key+"Upload", 
				uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
				url:  ykyUrl.webres,
				types: "jpg,png,jpeg,gif",//可允许上传文件的类型
				fileSize: "5mb", //最多允许上传文件的大小
				isImage: true, //是否是图片
				init:{
					FileUploaded : function(up, file, info) { 
			            layer.close(index);
						if (info.status == 200 || info.status == 203) {
							$this.flagUrl[key].img = signatureData.image.replace(/http\:|https\:/,"");
						} else {
							layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
						}
						up.files=[];
					}
				}
			}).init();			
		},
		resetFlag:function(e,k){  //活动图标点击事件
			if(!$(e.currentTarget).is(':checked')){
				this.flagUrl[k].img = '';
			}
		},
		getIdArray:function(arr,key){ //获取分销商、制造商id数组方法
			var array = [];
			$.each(arr,function(i,e){
				if(e[key]){
					array.push(e[key]);
				}
			})
			return array;
		},
		dataValidate:function(){  
			var isNP = _.find(this.fieldsId, function(chr) {
				  return chr == 'newPrice' ? true : false;
				})
			if (isNP && !this.discount){
				$("#discount").parent().addClass('has-error');
				layer.msg('请填写商品折扣设置！');
				return;
			}
			if(this.isLimitEffective == 'true'){
				if(!this.effectiveStartTime || !this.effectiveEndTime){
					layer.msg('请选择生效日期！');
					return;
				}
				var startDate = this.productSetData.activityStartDate,
					endDate = this.productSetData.activityEndDate;
				//活动结束时间返回的数据为当天0点，实际应该是当天23:59:59点，所以再原来的时间上加1天
				if(new Date(startDate) > new Date(this.effectiveStartTime) || new Date(endDate).getTime() + 86400000 < new Date(this.effectiveEndTime)){
					layer.msg('当前活动时间为'+(new Date(startDate)).Format("yyyy-MM-dd")+'~'+(new Date(endDate)).Format("yyyy-MM-dd")+'，折扣限时生效时间需在此期间内！');
					return;
				}
			}
			var pnVail = $("#pageNum").valid(),
				ppVail = $("#perPage").valid();
			if(!pnVail || !ppVail){
				return;
			}
			if(this.dataSource == 'GET_BY_UPLOAD' && !this.fileUrl){
				layer.msg('请添加活动商品！');
				return;
			}
			if(this.dataSource == 'GET_BY_CONDITION' && !this.activeBrandList.length &&  !this.activeVendorList.length && !this.activeCategoryList.length){
				layer.msg('请添加活动商品！');
				return;
			}
			if(this.showFlag == 'true' && this.flagType == 0 && !$('.upload-icon-wrap input[type="checkbox"]:checked').length){
				layer.msg('请上传活动图标！');
				return;
			}
			return true;
		},
		getFields:function(){ //获取显示字段数组
			var idList = this.fieldsId,fieldList = this.fieldsList,arr = [];
			for(var i=0;i<idList.length;i++){
				for(var j=0;j<fieldList.length;j++){
					if(idList[i] == fieldList[j].value){
						arr.push(fieldList[j])
					}
				}
			}
			arr = _.sortBy(arr, function(item) {
					  return item.seq;
				});
			return arr;
		},
		nextStep:function(type){ //下一步/保存
			var $this = this;
			var falg = this.dataValidate();
			if(!falg){
				return;
			}
			var data = this.getModuleData();
			console.log(data);
			if(type == 'next'){
				var url = $this.productAnalysisApi + '?promotionId=' + data.promotionId + '&promoModuleId=' + data.promoModuleId + '&fileUrl=' + $this.fileUrl + '&oriFileName=' + $this.fileName;				
				if($this.isUploadFile && $this.fileName){
					var index = layer.load(1, {
						  shade: [0.1,'#fff'] //0.1透明度的白色背景
						});
					syncData(url, 'POST', null,function(res,err){
						layer.close(index);
						if(err == null){
							console.log('文件解析成功');
							setTimeout(function() {
								$this.$emit('form-submit',data,1);
								$this.$emit('step-change', 1); 
				            }, 600);
						}
					})
				}else{
					$this.$emit('form-submit',data,1);
					$this.$emit('step-change', 1);
				}
			}else{
				syncData($this.api,'PUT',data,function(res,err){ 
						if(err == null){
							$this.$emit('cancle');
						}
					})
				}			
		},
		getModuleData:function(){ //获取模块数据
			var $this = this;
			var obj = $("#productSetForm").serializeObject();
			obj.condition = {};
			obj.search = {};
			obj.fields = this.getFields();			
			obj.condition['vendor'] = this.getIdArray(this.activeVendorList, 'id');
			obj.condition['brand'] = this.getIdArray(this.activeBrandList, 'id');
			obj.condition['category'] = this.activeCategoryList;
			obj.dataSource = $('input[name="dataSource"]:checked').val();
			if(obj.promotionFlag.show == 'true' && obj.promotionFlag.type == 0){
				obj.promotionFlag = $.extend({},obj.promotionFlag,$this.flagUrl);
			}
			obj.search = this.getSearchText(obj);
			//将折扣生效时间改为时间戳
			if(obj.effectSet && obj.effectSet.startTime && obj.effectSet.endTime){
				obj.effectSet.startTime = Date.parse(new Date(obj.effectSet.startTime));
				obj.effectSet.endTime = Date.parse(new Date(obj.effectSet.endTime));
			}
			var data = $this.productSetData;
			data.promotionContent.contentSet = obj;
			return data;
		},
		getSearchText:function(data){ //获取前台需要的搜索框placeholder
			var obj = {};
			if(data.dataSource == 'GET_BY_UPLOAD'){
				obj.text = '搜索更多此类产品';
			}else{
				var startStr = '搜索更多',endStr = '产品';
				var condition = data.condition,
					vendorLen = condition.vendor.length,
					brandLen = condition.brand.length,
					cateLen = condition.category.length;
					isInitUrl = true;
				var strObj = {
						vendorStr:'',
						brandStr:'',
						cateStr:''
						}
				if(cateLen == 1){
					if((!vendorLen && !brandLen) || (vendorLen == 1 && brandLen == 1) || (vendorLen == 1 && !brandLen) || (!vendorLen && brandLen == 1)){
						isInitUrl = false;
						strObj.vendorStr = vendorLen ? 'vendorId=' + this.activeVendorList[0].id : '';
						strObj.brandStr = brandLen ? 'manufacturer=' + this.activeBrandList[0].id : '';
						strObj.cateStr = cateLen ? 'cat=' + condition.category[0].id.split('/')[1] : '';
						obj.text = startStr + condition.category[0].name.split('/')[1] + endStr;
					}else{
						obj.text = '搜索更多此类商品';
					}
				}else if((brandLen == 1 && vendorLen == 1 && !cateLen) || (brandLen == 1 && !vendorLen && !cateLen)){
					isInitUrl = false;
					strObj.vendorStr = vendorLen ? 'vendorId=' + this.activeVendorList[0].id : '';
					strObj.brandStr = brandLen ? 'manufacturer=' + this.activeBrandList[0].id : '';
					obj.text = startStr + this.activeBrandList[0].brandName + endStr;
				}else if(vendorLen == 1 && !brandLen && !cateLen){
					isInitUrl = false;
					strObj.vendorStr = vendorLen ? 'vendorId=' + this.activeVendorList[0].id : '';
					obj.text = startStr + this.activeVendorList[0].supplierName + endStr;
				}else{
					obj.text = '搜索更多此类产品';
				}
				if(!isInitUrl){
					var arr = [];
					for(var k in strObj){
						if(strObj[k]){
							arr.push(strObj[k])
						}
					}
					obj.url = arr.join('&');
				}
			}
			//console.log(obj);
			return obj;
		},
		stepChange:function(n){
			this.$emit('step-change', n);
		},
		
	}
})