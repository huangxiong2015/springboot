/**
 * Created by zhoulixia on 2017/10/24.
 */

Vue.component('distr-manuf-select',{
	template:`
			<div class="distr-manuf-select">
				<div class="letter-select">
					<p v-text="letterTitle" style="padding: 10px 10px 0;"></p>
					<div class="col-md-12">
						<letter-select
							 v-if="letterSelect.options.length"
						     :options="letterSelect.options"
						     :id="letterSelect.id"
					    	 :name="letterSelect.name"
						     :option-id="letterSelect.optionId"
						     :option-name="letterSelect.optionName"		 
						     @get-selecteds="getSelected"
						     :selected="letterSelect.selected"
						     :searchText="letterSelect.searchText"
						     :char="letterSelect.char"
					    	 :multiple="letterSelect.multiple"
				    		 :show-search="letterSelect.showSearch"
							 :show-letter="letterSelect.showLetter"
							 :is-fuzzy-search="letterSelect.isFuzzySearch"
							 :reload-api="letterSelect.reloadApi"
							 :reload-req-data="letterSelect.reloadReqData"
				    		 ref="letterSelect"
				    	  ></letter-select>
				    	  <div v-if="!letterSelect.options.length" v-text="defaultTip" style="text-align: center;padding: 15px;"></div>   
					</div>
				</div>
				<div class="">
					<div class="col-md-12" style="border-top: solid 1px #d2d6de;padding-top: 10px;">
					    <p><span style="color:#ffa500;margin-right:10px;">当前已选中{{selectItem.length}}项</span>(最多选择{{maxNum}}项)</p>
					    <div class="select-area" v-if="selectItem.length">
					    	<div class="area-item selected-item" v-for="(ele,idx) in selectItem">
					    		<span v-text="ele[itemName]" :title="ele[itemName]" :name="idx"></span>
					    		<i class="fa fa-close" @click="deleteItem(ele[itemId])"></i>
					    	</div>
					    </div>
					    <div v-if="!selectItem.length" style="text-align:center;padding:10px 10px 20px;">暂无选中数据</div>
					</div>
				</div>
			</div>
			`,
	props:{
		maxNum:{
			type:Number,
			default(){
				return 1
			}
		},
		letterTitle:{
			type:String,
			default(){
				return '请选择'
			}
		},
		defaultTip:{
			type:String,
			default(){
				return '请选择'
			}
		},
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
	                selected:[],
	                placeholder:'搜索',
	            	multiple:true, //单选
	            	isFuzzySearch:false,
	            	reloadApi:'',
	            	reloadReqData:null
				}
			}
		},
		selectItem:{
			type:Array,
			default(){
				return []
			}
		}
	},
	data:function(){
		return {
			itemName:this.letterSelect && this.letterSelect.optionName ? this.letterSelect.optionName :'name',
			itemId:this.letterSelect && this.letterSelect.optionId ? this.letterSelect.optionId :'id'
		}
	},
	methods:{
		getSelected:function(data){
			this.$emit('get-selected', data);
		},
		deleteItem:function(id){
			this.$refs.letterSelect.del(id);
		}
	}
})

Vue.component('product-associate',{
	template:`
			<div class="product-associate">
				<div class="form-group" v-if="isEdit || (!isEdit && isAdd)">
					<div class="col-md-12">
						<label class="col-sm-3 control-label">型号链接<span class="text-red">*</span>：</label>
						<div class="col-sm-8">
							<input type="text" class="set-input form-control" v-model="prdUrl" @change="getPrdData" :readOnly="isEdit?true:false">							
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<label class="col-sm-3 control-label">型号：</label>
						<div class="col-sm-8">
							<p class="s-text" v-text="model?model:'--'"></p>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-12">
						<label class="col-sm-3 control-label">原厂：</label>
						<div class="col-sm-8">
						<p class="s-text" v-text="manufacturer?manufacturer:'--'"></p>
						</div>
					</div>
				</div>
				<div class="form-group" v-if="showImage">
					<div class="col-md-12">
						<label class="col-sm-3 control-label">图片：</label>
						<div class="col-sm-8 manu_logo" style="height:100px;">
							<div class="img-box" style="height:100px;">
								<img width=150 height=150 :src="imageUrl ? imageUrl : defaultImg">
								<input v-if="(!imageUrl && isEdit) || !isReadOnly" type="button" value="上传文件" id="prdImgUpload" class="file-btn"  style="top: inherit;">
							</div>
							<a v-if="(!imageUrl && isEdit) || !isReadOnly" @click="imageUrl = ''" style="margin-top:80px;cursor: pointer;">删除</a>
							<div v-if="!isReadOnly" style="margin-top:30px;"><span v-text="imageRecText?imageRecText:'请上传XXX*XXX尺寸图片，支持jpg、jpeg、png、gif格式大小不超过2M'"></span></div>
						</div>
					</div>
				</div>
				<div class="box-footer text-center">
	               	<button type="button" class="btn btn-danger c-btn" @click="getProductInfo" :class="id == ''?'disabled':''">确认</button> 
	               	<button type="button" class="btn btn-cancle c-btn" @click="cancel">取消</button>
	           	</div>
			</div>
			`,
	props:{
		api:{
			type:String,
			default(){
				return ''
			}
		},
		requestType:{
			type:String,
			default(){
				return 'GET'
			}
		},
		productData:{
			type:Object,
			default(){
				return {
					id:'',
					prdUrl:'', //型号链接
					model:'', //型号
					manufacturer:'',  //原厂
					manufacturerId:'', //原厂id
					imageUrl:'', // 图片地址
					sourceName:'', //仓库名称
					sourceId:'', //仓库id
					vendorId:'', //分销商id
					vendorName:'' //分销商名称
				}
			}
		},
		isEdit:{
			type:Boolean,
			default(){
				return false
			}
		},
		isReadOnly:{
			type:Boolean,
			default(){
				return false
			}
		},
		imageRecText:{
			type:String,
			default(){
				return '请上传XXX*XXX尺寸图片，支持jpg、jpeg、png、gif格式大小不超过2M'
			}
		},
		isAdd:{
			type:Boolean,
			default(){
				return false
			}
		},
		prdType:{
			type:String,
			default(){
				return ''
			}
		},
		showImage:{
			type:Boolean,
			default(){
				return true
			}
		}
	},
	data:function(){
		return {
			id:this.productData && this.productData.id ? this.productData.id : '',
			prdUrl:this.productData && this.productData.prdUrl ? this.productData.prdUrl : '',
			model:this.productData && this.productData.model ? this.productData.model : '',
			manufacturer:this.productData && this.productData.manufacturer ? this.productData.manufacturer : '',
			manufacturerId:this.productData && this.productData.manufacturerId ? this.productData.manufacturerId : '',
			sourceName:this.productData && this.productData.sourceName ? this.productData.sourceName : '',
			sourceId:this.productData && this.productData.sourceId ? this.productData.sourceId : '',
			vendorId:this.productData && this.productData.vendorId ? this.productData.vendorId : '',
			vendorName:this.productData && this.productData.vendorName ? this.productData.vendorName : '',
			imageUrl:this.productData && this.productData.imageUrl ? this.productData.imageUrl : '',
			defaultImg:ykyUrl._this + '/images/defaultImg01.jpg'
		}
	},
	mounted:function(){
		var $this = this;
		this.$nextTick(function(){
			var uploader = createUploader({
				buttonId: "prdImgUpload", 
				uploadType: "notice.publicRead", 
				url:  ykyUrl.webres,
				types: "jpg,png,jpeg,gif",
				fileSize: "2mb",
				isImage: true, 
				init:{
					FileUploaded : function(up, file, info) { 
			            layer.close(index);
						if (info.status == 200 || info.status == 203) {
							$this.imageUrl = _yetAnotherFileUrl;
						} else {
							layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
						}
						up.files=[];
					}
				}
		    }); 
			uploader.init();
		})
	},
	methods:{
		getPrdData:function(){
			var $this = this,
				id = [this.getProductId(this.prdUrl,'id')];
			var index = layer.load(1);
			syncData(this.api,this.requestType,id,function(res,err){
				layer.close(index);
				if(err == null){
					if(res.length){
						var data = res[0];
						$this.id = data.id;
						$this.model = data.spu && data.spu.manufacturerPartNumber ? data.spu.manufacturerPartNumber : '';
						$this.manufacturer = data.spu && data.spu.manufacturer ? data.spu.manufacturer : '';
						$this.manufacturerId = data.spu && data.spu.manufacturerId ? data.spu.manufacturerId : '';
						$this.imageUrl = data.spu && data.spu.images && data.spu.images.length ? $this.getImageUrl(data.spu.images) : '';
						$this.sourceName = data.sourceName ? data.sourceName : '';
						$this.sourceId = data.sourceId ? data.sourceId : '';
						$this.vendorId = data.vendorId ? data.vendorId : '';
						$this.vendorName = data.vendorName ? data.vendorName : '';
					}else{
						layer.msg('未关联到商品相关信息!')
						$this.id = '';
						$this.model = '';
						$this.manufacturer = '';
						$this.manufacturerId = '';
						$this.imageUrl = '';
						$this.sourceName = '';
						$this.sourceId = '';
						$this.vendorId = '';
						$this.vendorName = '';
					}
				}
			} )
		},
		getProductId:function(url,name) {//获取商品id
			if(!url){
				return;
			}
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
			var c = url.split('?')[1],r;
			if(c){
				r=c.match(reg);
			}
			if (r != null){
				return decodeURI(r[2]);
			}else{
				var str1 = url.split('product/')[1];
				var str2 = str1?str1.split('.htm')[0]:'';
				if(str2){
					return str2;
				}else{
					return "";
				}			
			}
		},
		getImageUrl:function(data){
			var obj = {};
			$.each(data,function(i,e){
				obj[e.type] = e.url;
			})
			if(obj.large){
				return obj.large
			}else if(obj.stand){
				return obj.stand
			}else{
				return obj.thumbnail
			}
		},
		getProductInfo:function(){
			if(this.id == ''){
				return;
			}
			var data = {
					id:this.id,
					prdUrl:this.prdUrl,
					model:this.model,
					manufacturer:this.manufacturer,
					manufacturerId:this.manufacturerId,
					imageUrl:this.imageUrl,
					sourceName:this.sourceName,
					sourceId:this.sourceId,
					vendorId:this.vendorId,
					vendorName:this.vendorName,
					manualAdd:true
			}
			this.$emit('get-product-info',data);
		},
		cancel:function(){
			this.$emit('cancel')
		}
	}
})
Vue.component('agent-select',{
	template:`
			<div class="agent-select">
				<letter-select
				:keyname="agentSelect.keyname"
				:validate="agentSelect.validate"
				:options="agentSelect.options"
				:id="agentSelect.id"
				:name="agentSelect.name"
				:option-id="agentSelect.optionId"
				:option-name="agentSelect.optionName"		 
				@get-selecteds="getAgentSelected"
				:placeholder="agentSelect.placeholder"
				:selected="selected"
				:searchText="agentSelect.searchText"
				:char="agentSelect.char"
				:multiple="agentSelect.multiple"
			    :is-fuzzy-search="agentSelect.isFuzzySearch"
				:reload-api="agentSelect.reloadApi"
				:reload-req-data="agentSelect.reloadReqData"
				ref="agentSelect"
				></letter-select>
				<div class="lemon-area" v-if="tempActiveAgent.length">
					<p>当前已选中：</p>
					<div class="s-l-area">
						<div class="lemon-tag" v-for="(ele,index) in tempActiveAgent">
							<span class="lemon-tag-text" v-text="ele[agentSelect.optionName]"></span>
							<i class="fa fa-close" @click="deleteTempActive(ele[[agentSelect.optionId]])"></i>
						</div>
					</div>
				</div>
				<div class="box-footer text-center">
	               	<button type="button" class="btn btn-danger c-btn" @click="getActiveAgent">确认</button> 
	               	<button type="button" class="btn btn-cancle c-btn" @click="cancel">取消</button>
	           	</div>
			</div>
			`,
	props:{
		agentSelect:{
			type:Object,
			default(){
				return {
					keyname:'选择',
					validate:{
	                    "required": true
	                },
	                id:'selbox',
	                name:'selbox',
	                options:[],
	                optionId: 'id',
	                optionName: 'name',
	                selected:[],
	                placeholder:'搜索',
	            	multiple:true,
	            	isFuzzySearch:false,
	            	reloadApi:'',
	            	reloadReqData:null
				}
			}
		},
		maxLen:{
			type:Number,
			default(){
				return 0
			}
		}
	},
	data:function(){
		return {
			tempActiveAgent:this.agentSelect && this.agentSelect.selected && this.agentSelect.selected.length ? this.findById(this.agentSelect.selected) : '',
			selected:this.agentSelect && this.agentSelect.selected ? this.agentSelect.selected : ''
		}
	},
	methods:{
		findById: function(arr) {
            var selected = [],
                that = this;
            if (this.agentSelect.multiple === true) {
                for (var i in arr) {
                    var obj = _.filter(this.agentSelect.options, function(o) {
                        return o[that.agentSelect.optionId] == arr[i]
                    });
                    selected = _.unionWith(selected, obj, _.isEqual);
                }
            } else {
                var obj = _.filter(this.agentSelect.options, function(o) {
                    return o[that.agentSelect.optionId] == arr
                });
                if (obj.length > 0) {
                    selected = obj[0];
                }
            }
            return selected;
        },
		getAgentSelected:function(obj){
			var that = this;
			if(this.agentSelect.multiple && obj.length > this.maxLen && this.maxLen){
				layer.msg('最多可选' + this.maxLen + '项');
				var arr = [];
				$.each(this.tempActiveAgent,function(i,e){
					if(e[that.agentSelect.optionId]){
						arr.push(e[that.agentSelect.optionId]);
					}
				})
				this.selected = arr;
				return
			}
			this.tempActiveAgent = obj;
		},
		getActiveAgent:function(){
			var that = this;
			this.$emit('get-selecteds',{agentObj:this.tempActiveAgent,agentId:that.$refs.agentSelect.sel})
		},
		deleteTempActive:function(id){
			this.$refs['agentSelect'].del(id)
		},
		cancel:function(){
			this.$emit('cancel');
		}
	}
})