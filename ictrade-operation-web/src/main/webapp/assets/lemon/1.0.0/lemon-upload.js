/**
 * Created by yansha on 2017/6/14.
 */
/**
 * 文件上传组件
 */
Vue.component('file-upload', {
    template: `<div class="form-group"> 
        <label :for="fileId" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div
            class="col-sm-7 col-md-8 col-lg-9 checkVal" 
            :title="keyname" > 
                <div class="manu-logo" >  
                    <input  v-if="attachUrls.length < maxLength" class="file-btn " :class="{ 'pos-abs': isImage=='true'}" type="button" :value="buttonText" :id="buttonId" >   
                </div>
                <div class="manu-file-list" v-if="isImage=='true'"> 
                    <span v-if="!multi">
                        <img  class="manu-mini" alt="" @click="showIndex=1" :src="attachUrl ? attachUrl : defaultPic" /> 
                        <a @click="drop" v-show="attachUrl" href="javascript:void(0);">删除</a> 
                       <span class="manu-large" v-show="showIndex==1">
                            <a href="javascript:void(0);" @click="showIndex=-1">隐藏</a><a :href="attachUrl ? attachUrl : defaultPic" target="_blank"> 原图</a>
                            <img alt="" :src="attachUrl ? attachUrl : defaultPic"  /> 
                        </span>
                    </span>
                    <ul >
                        <li  v-if="multi" v-for="(url, index) in attachUrls">
                            <img class="manu-mini" @click="showIndex=index" alt="" :src="url ? url : defaultPic"  /> 
                            <a @click="del(index)" href="javascript:void(0);">删除</a> 
                            <span class="manu-large" v-show="showIndex==index">
                                <a href="javascript:void(0);" @click="showIndex=-1">隐藏</a><a :href="url ? url : defaultPic" target="_blank"> 原图</a>
                                <img alt="" :src="url ? url : defaultPic"  /> 
                            </span>
                        </li>
                    </ul>
                </div>
                <div class="manu-text" > 
                    {{manuText}}
                </div> 
                <input type="text" v-if="!multi" v-model="attachUrl" :id="fileId" :name="fileName"  style="visibility: hidden;height:0;" v-bind="validate"> 
                <input type="text" v-if="multi" v-model="attachUrls" :id="fileId" :name="fileName"  style="visibility: hidden;height:0;" v-bind="validate"> 
            </div>
        </div>`,
    props:{
        url: String,
        buttonText:{
            type:String,
            default(){
                return '上传文件'
            }
        },
        manuText: {
        	type: String,
        	default(){
        		return '建议上传120*44像素，支持jpg、jpeg、png、gif，文件大小不超过5MB'
        	}
        },
        buttonId:String,
        fileId:String,
        fileName:String,
        uploadType:String,
        types:String,
        fileSize:String,
        isImage:String,
        validate:Object,
        class:String,
        attachFile:String,
        multiSelection:Boolean,
        defaultPic:{
            type:String,
            default(){
                return '/dist/img/defaultFace.jpg'
            }
        },
        attachFiles:{
            type:Array,
            default(){
                return []
            }
        },
        keyname: String,
        multi:{
            type: Boolean,
            default(){
                return false
            }
        },
        maxLength:{
            type: Number,
            default(){
                return 1
            }
        }
    },
    data(){
        return{
            attachUrl: this.attachFile,
            isEdit:true,
            uploader: this.buttonId + 'Upload',
            attachUrls:this.attachFiles,
            showIndex:-1
        }
    },
    watch:{
        attachFile : function(val) {
            this.attachUrl=val;
        },
        attachFiles: function (val) {
            this.attachUrls=val;
        }
    },
    mounted(){
        this.uploadInit();
    },
    methods:{
        drop: function () {
            this.attachUrl='';
        },
        del:function (index) {
            this.attachUrls.splice(index, 1);
        },
        uploadInit:function () {
            var that=this;
            this.uploader = createUploader({
                buttonId: this.buttonId,
                uploadType: this.uploadType,
                url:  this.url,
                types: this.types,
                fileSize: this.fileSize,
                isImage: this.isImage,
                multiSelection:this.multiSelection,
                init:{
                    FileUploaded : function(up, file, info) {
                        layer.close(index);
                        if (info.status == 200 || info.status == 203) {
                            if(that.multi){
                                if(that.attachUrls.length<that.maxLength){
                                    that.attachUrls.push(_fileUrl);
                                }
                            }else{
                                that.attachUrl = _fileUrl;
                            }
                            $('#'+that.fileId).blur();
                        } else {
                            layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120});
                        }
                        up.files=[];
                    }
                }
            });
            this.uploader.init();
        }
    }
});