/**
 * Created by yansha on 2017/9/26.
 */
/**
 * 文件上传组件
 */
Vue.component('file-upload', {
    template: ` <div class="checkVal">
        <div class="manu_logo" >  
            <input class="file-btn " :class="{ 'pos-abs': isImage=='true'}" type="button" value="上传文件" :id="buttonId" >   
            <span v-if="isImage=='true'"> 
                <img alt="" :src="attachUrl ? attachUrl : defaultImg" /> 
                <a  v-show="attachUrl" @click="del" href="javascript:void(0);">删除</a> 
            </span>
        </div>
        <div class="manu_text" > 
        	支持{{types}}，文件大小不超过{{fileSize}}。
        </div> 
        <input type="text" v-model="attachUrl" :id="fileId" :name="fileName"  style="visibility: hidden;height:0;" v-bind="validate">
    </div>`,
    props: {
        url: String,
        buttonId: String,
        fileId: String,
        fileName: String,
        uploadType: String,
        types: String,
        fileSize: String,
        isImage: String,
        validate: Object,
        class: String,
        attachFile: String
    },
    data() {
        return {
            attachUrl: this.attachFile,
            isEdit: true,
            uploader: this.buttonId + 'Upload',
            defaultImg: ykyUrl._this + '/images/defaultImg01.jpg'
        }
    },
    watch: {
        attachFile: function(val) {
            this.attachUrl = val;
        }
    },
    mounted() {
        this.uploadInit();
    },
    methods: {
        del: function() {
        	this.attachUrl='';
        },
        uploadInit: function() {
            var that = this;
            this.uploader = createUploader({
                buttonId: this.buttonId,
                uploadType: this.uploadType,
                url: this.url,
                types: this.types,
                fileSize: this.fileSize,
                isImage: this.isImage,
                init: {
                    FileUploaded: function(up, file, info) {
                        layer.close(index);
                        if (info.status == 200 || info.status == 203) {
                            that.attachUrl = signatureData.image.replace(/http\:|https\:/,"");
                        } else {
                            layer.msg("上传文件失败,请重新上传！", { icon: 2, offset: 120 });
                        }
                        up.files = [];
                    }
                }
            });
            this.uploader.init();
        },
        handleClear: function() {
            this.attachUrl = '';
        }
    }
});

Vue.component('banner-set', {
    template: `<div class="">
    <form :name="formData.id" :id="formData.id" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
    <div class="row">
      <section class="col-lg-12">
          <div class="box box-danger "> 
               <div class="box-body ">
                    <div class="form-group">
                        <div class="col-md-12">
                            <label for="specurl" class="col-sm-2 control-label">banner图片：<span  class="text-red">*</span></label>
                            <div class="col-sm-9" > 
                                <file-upload  
                                    :file-id= "imgField.key"
                                    :file-name= "imgField.name"
                                    :button-id= "imgField.config.buttonId"
                                    :upload-type= "imgField.config.uploadType"
                                    :url= "imgField.config.url"
                                    :types= "imgField.config.types"
                                    :file-size= "imgField.config.fileSize"
                                    :is-image= "'true'" 
                                    :validate="imgField.validate"  
                                    :class="imgField.inputClass"
                                    :attach-file="initData.bannerImg"
                                    v-bind="imgField.attr" 
                                ></file-upload>
                            </div> 
                        </div>
                    </div>
                    <div class="form-group">
	                    <div class="col-md-12">
	                        <label for="specurl" class="col-sm-2 control-label">banner背景色：<span  class="text-red">*</span></label>
	                        <div class="col-sm-2" > 
	                        	<input type="text"  class="form-control" id="backgroundColor" name="contentSet[backgroundColor]"  v-model="initData.backgroundColor" />
	                        </div> 
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <label for=":" class="col-sm-2 control-label">活动细则：<span class="text-red">*</span></label>
                            <div class="col-sm-10" > 
                                <div class="radio inline" >
                                    <span class="radio-span">
                                        <input type="radio" name="contentSet[showDesc]" id="isNeed" value="true" class="chk_ input_radio" v-model="initData.showDesc"> 
                                        <label for="isNeed"></label> 需要
                                     </span> 
                                </div> 
                                 <div class="radio inline" >
                                    <span class="radio-span">
                                        <input type="radio" name="contentSet[showDesc]" id="isNoNeed" value="false" class="chk_ input_radio" v-model="initData.showDesc"> 
                                        <label for="isNoNeed"></label> 不需要
                                     </span> 
                                </div> 
                                
                            </div> 
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-md-12">
                            <label for="desc" class="col-sm-2 control-label">细则详情：</label>
                             <div class="col-sm-10" >
                                <!-- 使用的时，将这个部分引入到页面中start -->
                                <div id="desc" class="ueditor_wrapper"> 
                                    <iframe style="width:100%;border: none;min-height:550px;" name="ruleFrame" id="ruleFrame" :src="ueditor" scrolling="no" frameborder="0"  allowTransparency="true">
                                    </iframe>
                                </div>
                                <!-- 使用的时，将这个部分引入到页面中end --> 
                            </div>
                        </div>
                    </div>
               </div>
                <input type="hidden" id="iframeUrl_Id" :value="ueditor"> 
                <div class="box-footer text-center">
                    <button type="button" @click="requireSaveData" class="btn btn-danger c-btn">保存</button> 
                    <button type="button" @click="cancle" class="btn btn-cancle c-btn">取消</button>
                </div>
          </div>
      </section>
    </div>
    </form> 
</div>`,
    props: {
        formData:{
            type:Object,
            default(){
                return{
                    id:'createBanner',
                    url:'',
                    message:{}
                }
            }
        },
        api:'',
        bannerData: {
            type: Object,
            default(){
                return {
                	promotionContent: {
                		contentSet: {}
                	} 
                }
            }
        }
    },
    data() {
        return {
            imgField: {
                "key": "bannerImg",
                "name": "contentSet[bannerImg]",
                "validate":{
                    "required": true
                },
                "config": {
                    "buttonId": "uploadBtn",
                    "types": "jpg,png,jpeg,gif",
                    "url": pluginURL,
                    "uploadType": "ent.logo",
                    "fileSize": "1mb"
                }
            },
            initData: {
                bannerImg:'',
                showDesc:true,
                newsContent:''
            },
            ctlSelected: 1,
            datas: {},
            ueditor: ykyUrl.ictrade_ueditor
        }
    },
    mounted(){
        if (window.addEventListener) {
            window.addEventListener('message', this.saveNews, false);
        } else if (window.attachEvent) {
            window.attachEvent('message', this.saveNews, false);
        }
        if (this.bannerData.promotionContent && this.bannerData.promotionContent.contentSet&&this.bannerData.promotionContent.contentSet.bannerImg){
            this.init();
        }
        this.saveData();
    },
    methods: {
        init : function() {
            var $this=this;
            this.initData.title = this.bannerData.promotionContent.showSet.title;
            this.initData.showDesc= this.bannerData.promotionContent.contentSet.showDesc;
            this.initData.backgroundColor= this.bannerData.promotionContent.contentSet.backgroundColor;
            this.initData.bannerImg = this.bannerData.promotionContent.contentSet.bannerImg;
            setTimeout(function() {
                if ($this.bannerData.promotionContent.contentSet.content) {
                    var content = 'loadContent' + $this.bannerData.promotionContent.contentSet.content;
                    window.frames[0].postMessage(content, $this.ueditor);
                }
            }, 500);
        },
        saveNews : function(event) {
            var formid = this.formData.id;
            this.initData.newsContent = event.data;
            $('#'+ this.formData.id).submit();
        },
        saveData: function() {
            var that = this;
            var formid = this.formData.id;
            var api = this.api;
            var message = this.formData.message;
            formValidate('#'+formid, null, function() {
                //序列化form表单数据
                var formArry = $('#'+formid).serializeObject();
//                if(!that.initData.newsContent && that.initData.showDesc === 'true'){
//                	layer.msg('细则详情不能为空');
//                	return false;
//                }
                formArry.contentSet.content= that.initData.newsContent;
                //formArry.type='banner';
                formArry.showSet={ "showTitle":false} 
                var data=that.bannerData;   
        		data.promotionContent=formArry;
        		data.promoModuleType= "BANNER";
                if (api) {
                    syncData(api, "PUT",  data, function(res, err) {
                        if (res!=null) {
                            console.log('请求成功!');
                            that.$emit('form-submit', data);
                        }
                    });
                }
            }, message);
        },
        requireSaveData: function () {
            //发送指令到iframe请求返回数据
            window.frames[0].postMessage('save_btn',this.ueditor);
        },
        cancle: function() {
            this.$emit('cancle');
        }
    }
})