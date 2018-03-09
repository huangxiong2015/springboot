/**
 * Created by zengwei on 2017/9/27.
 */
Vue.component('coupon-set', {
    template: `<form name="createBanner" id="couponSetForm" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
                    <div class="row">
                        <section class="col-lg-12">
                            <div class="box box-danger ">
                            	<input type="hidden" name="promoModuleId" :value="modelId">
                            	<input type="hidden"  name="contentSet[coupon]" :value="selectCouponIDList" >
                                <div class="box-body ">
                                    <div class="form-group">
                                        <div class="col-md-12">
                                            <label for="specurl" class="col-sm-2 control-label">选择优惠券：<span  class="text-red">*</span></label>
                                            <div class="col-sm-10">
	                                            <div class=" coupon-par limit">
		            				                <a class="select-coupon" @click="selectCoupon">
		            				                	<span>请选择优惠券</span>
		            				                	<i class="icon-down_arrow1 g9"></i>
		            				                </a>
		            				                <div class="coupon-wrap">
		            				                	<p v-html="couponList.length?'选择优惠券':'暂无优惠券，请先创建'"></p>
		            				                	<div class="coupon-list">
		            				                		<template v-for="(item,index) in couponList">
		            				                			<span>
		            					                			<input type="checkbox" :id="'coupon'+index" :value="item"  v-model="temporaryCouponList">
		            						                		<label class="s-coupon" :for="'coupon'+index">{{item.couponName}}</label>
		            					                		</span>
		            				                		</template>
		            				                	</div>
		            				                	<p>
		            				                		<a v-if="couponList.length" @click="saveCouponList" class="coupon-save">保存</a>
		            				                		<a v-if="couponList.length" @click="cancelCouponList('cancel')" class="coupon-cancel">取消</a>
		            				                		<a v-if="!couponList.length"@click="cancelCouponList('know')" class="coupon-save close-box">知道了</a>
		            				                	</p>
		            				                </div>	
		            			                </div>
                                            </div>
                                        </div>
                                      </div>
                                      <div class="form-group" v-if="selectCouponList.length>0">
                                        <div class="col-md-12" >
	                                        <label for="specurl" class="col-sm-2 control-label"></label>
	                                        <div class="col-sm-8 coupon-par">
		        				                <div class="selected-list">
		        				                	<label class="s-selected" v-for="item in selectCouponList">
		        				                		<span v-html="item.couponName" :id="item.couponId"></span>
		        				                		<i class="icon-close-min delete-btn" @click="deleteCoupon(item)"></i>
		        				                	</label>				                	
		        				                </div>	
		        			                </div>
	                                    </div>
	                                 </div>
	                                 <div class="form-group">
                                        <div class="col-md-12">
                                            <label for="specurl" class="col-sm-2 control-label">优惠券样式：</label>
                                                <div class=" coupontype">
                                                    <input type="radio" value="0" v-model="isCustomer"  name="contentSet[style][isDefault]">默认样式
                                                </div>
                                                <div class="coupontype">
                                                    <input type="radio" value="1" v-model="isCustomer" name="contentSet[style][isDefault]">自定义
                                                </div>
                                        </div>
                                        <div class="col-md-12" v-if="isCustomer == 1">
                                            <label for="specurl" class="col-sm-2 control-label"> </label>
                                            <div class="col-sm-10">
	                                            <div class="col-sm-12 couponCusListWrap">
	                                                <span  v-if="selectCouponList.length<=0">未选择优惠券</span>
	                                                <table class="couponuploadTable">
	                                                	<tbody>
	                                                		<tr v-for="(item,index) in selectCouponList">
	                                                			<td class="uploadTitle"><span class="coupon-s-item ">{{item.couponName}}</span></td>
	                                                			<td><file-upload  
	                                                            	:file-id= "imgField.key + index"
	                                                                :file-name= "imgField.name"
	                                                                :button-id= "imgField.config.buttonId + index"
	                                                                :upload-type= "imgField.config.uploadType"
	                                                                :url= "imgField.config.url"
	                                                                :types= "imgField.config.types"
	                                                                :file-size= "imgField.config.fileSize"
	                                                                :is-image= "'true'" 
	                                                                :validate="imgField.validate"  
	                                                                :class="imgField.inputClass"
	                                                                :attach-file="item.couponImg"
	                                                                v-bind="bindFile" 
	                                                            ></file-upload></td>
	                                                		</tr>
	                                                	</tbody>
	                                                </table>
	                                                
	                                              </div>
                                            </div>
                                        </div>
                                        </div>
                                        <div class="col-md-12">
                                            <label for="specurl" class="col-sm-2 control-label"></label>

                                        </div>
                                    </div>
                                </div>
                                <div class="box-footer text-center">
                                    <button type="button" @click="saveData" class="btn btn-danger c-btn">保存</button>
                                </div>
                            </div>
                        </section>
                    </div>
                </form>`,
    props: {
        // couponData: couponData
    	formData:{
            type:Object,
            default(){
                return{
                	promoModuleId:'',
                    url:'',
                    message:{}
                }
            }
        },
        descData: {
            type: Object,
            default(){
                return {
                	promotionContent: {
                		contentSet: {
                			coupon:[],
                			style:{                 //优惠券样式
                	            isDefault:true,     //是否默认
                	            files:[]             //上传样式，非默认
                	        }
                		},
                		showSet:{showTitle:false }
                	},
            		promoModuleId:"",
                }
            }
        },
        api: String
    },
    data() {
        return {
            modalStyle: {
                width: 350,
                height: 180
            },
            promotionContent:{
        		contentSet: {
        			coupon:[],
        			style:{                 //优惠券样式
        	            isDefault:0,     //是否默认
        	            files:[]             //上传样式，非默认
        	        }
        		},
        		showSet:{showTitle:false }
        	},
        	bindFile:"",
            couponData: this.descData,
            couponList:[],//优惠券列表
            temporaryCouponList:[],//选中的优惠券临时列表
            selectCouponList:[],
            modelId:this.descData.promoModuleId,
            selectCouponIDList:[],
            isCustomer: 0,
            imgField: {
                "key": "attachUrl",
                "name": "contentSet[style][files][]",
                "validate":{
                    "required": true
                },
                "attr":"selectCouponList",
                "config": {
                    "buttonId": "uploadBtn",
                    "types": "jpg,png,jpeg,gif",
                    "url": pluginURL,
                    "uploadType": "productUpload.activityProductsUpload",
                    "fileSize": "5mb"
                }
            },
            putApi:ykyUrl.product + "/v1/promotionModuleDraft",
            datas:{}
        }
    },
    mounted() {
        var $this = this;
        //this.descData.promotionContent={};
        this.getCouponList("");
        if(this.descData.promotionContent)
        	this.promotionContent = this.descData.promotionContent;
        
    },
    watch:{
//    	selectCouponList:function(){
//    		console.log(this.selectCouponList);
//    		
//    	}
    },
    methods: {
        getCouopon: function(id) {
            for (var i = 0; i < this.couponData.length; i++) {
                if (this.couponData[i].id == id)
                    return this.couponData[i];
            }
        },
        getFileList:function(){
        	var arr = [],
        		that = this;
        	$.each(that.selectCouponList,function(index,item){
        		arr.push(item.couponImg);
        	})
        	return arr;
        },
        setFiles:function(index){
        	var f = (typeof this.promotionContent.contentSet.style.files !="undefined" && this.promotionContent.contentSet.style.files[index] !="undefined")  ? this.promotionContent.contentSet.style.files[index]:""
        	return f
        },
        setFiletoArr:function(){
        	var that = this,
        		arr= $("input[name='contentSet[style][files][]']");
        	if(arr.length==0)
        		return
        		
        	$.each(arr,function(index,item){
        		that.selectCouponList[index].couponImg = $(item).val();
        	})
        },
        setFormVal:function(){
        	var that = this;
            //var formid = this.formData.id;
            var api = this.putApi;
        	//formValidate('#couponSetForm', null, function() {
                //序列化form表单数据
                var formArry = $('#couponSetForm').serializeObject();
                that.couponData.promotionContent = $.extend(that.promotionContent,formArry) ;
                that.couponData.promotionContent.contentSet.coupon = that.selectCouponIDList;
                that.couponData.promotionContent.contentSet.style.files = that.getFileList();
                if (api) {
                    syncData(api, "PUT", that.couponData, function(data, err) {
                        if (data!=null) {
                            console.log('请求成功!');
                            that.$emit('form-submit', formArry);
                        }
                    });
                }
                //that.$emit('form-submit', formArry);
            //}, null);
        	
        	
        },
        saveData: function() {
        	var that = this;
        	this.setFiletoArr();//设置上传的图片到对应数组
        	
        	this.validation(this.setFormVal);
        	//this.setFormVal();
        	//$('#couponSetForm').submit();
            
        },
        validation:function(cb){
        	var cList=$("input[name='contentSet[coupon]']"),
        		filesList = $("input[name='contentSet[style][files][]']"),
        		fileStatus = true;
        	
        	if(!cList.val()){
        		layer.msg("选择优惠券");
        		return
        	}
        	
        	$.each(filesList,function(i,e){
				if(!$(e).val())
					fileStatus = false
			})
			
			if(!fileStatus){
        		layer.msg("请上传自定义优惠券图片！");
        		return
        	}
        	
        	cb();
        },
        getCouponList:function(activityId){//获取优惠券列表
			var that = this;
			var data = {activityId:activityId,couponMethodType:"FREE_WGET"};
			syncData(ykyUrl.pay + "/v1/coupons/couponActivity/findByActivityId", 'GET', data, function(res,err) {
				if (!err && res !== '' && !activityId) {//获取所有优惠券列表
					$.each(res,function(i,e){
						var obj = {
								couponId:e.couponId,
								couponName:e.couponName,
								couponImg:""
						}
						that.couponList.push(obj);
					})
				}else{//获取当前活动优惠券列表
					if(res.length){
						$.each(res,function(i,e){//优惠券couponList只有couponId、couponName
							var obj = {
									couponId:e.couponId,
									couponName:e.couponName,
									couponImg:""
							}
							that.selectCouponList.push(obj);
						})
						that.temporaryCouponList = that.selectCouponList;
					}else{
						that.status = 'NOT_VALID';
					}								
				}
				if(that.couponData.promotionContent != null)
					that.setEdit()
			})
		},
		setEdit:function(){
			var that = this,
				selectObj = [],
				editId = that.couponData.promotionContent.contentSet.coupon;
			
			$.each(editId,function(i,e){
				$.each(that.couponList,function(j,c){
					if(e == c.couponId){
						var cop = c;
						c.couponImg = that.couponData.promotionContent.contentSet.style.files[i];
						selectObj.push(c)
					}
						
				})
			})
			
			that.selectCouponList = selectObj;
			that.temporaryCouponList = selectObj;
			that.setSelectCouponIDList(selectObj);
			that.isCustomer=this.couponData.promotionContent.contentSet.style.isDefault;
			//that.couponList
		},
		setSelectCouponIDList:function(arr){
			var idlist = [];
			for (var i = 0; i < arr.length; i++) {
				idlist.push(arr[i].couponId);
            }
			this.selectCouponIDList = idlist;
		},
        selectCoupon:function(){//选择优惠券
			var that = this;
			$('.coupon-par').addClass('show');	
		},
		saveCouponList:function(){//保存优惠券选择
			var that = this;
			that.setFiletoArr();
			that.selectCouponList = that.temporaryCouponList;
			that.setSelectCouponIDList(that.selectCouponList);
			$('.coupon-par').removeClass('show');
		},
		cancelCouponList:function(type){//取消选择优惠券
			var that = this;
			if(type == 'cancel'){
				that.temporaryCouponList = that.selectCouponList;
			}
			$('.coupon-par').removeClass('show');
		},
		cancle: function() {
	        this.$emit('cancle');
	    },
		deleteCoupon:function(item){//删除优惠券
			var that = this;
			var cId = item.couponId;
			that.setFiletoArr();

			$.each(that.selectCouponList,function(i,e){
				if(e.couponId == cId){
					that.selectCouponList.splice(i,1);
					return false;
				}
			})
			
			that.setSelectCouponIDList(that.selectCouponList);
		}
    }
})