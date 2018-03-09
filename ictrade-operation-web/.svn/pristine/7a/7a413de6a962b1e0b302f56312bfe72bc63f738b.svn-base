/**
 * Created by yansha on 2017/8/17.
 */
/**
 * 文件上传组件
 */
Vue.component('multi-file', {
    template: `<div class="form-group"> 
        <label :for="fileId" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div class="col-sm-7 col-md-8 col-lg-9 checkVal"  :title="keyname" >  
                <div class="items-box" v-for="(index, idx) in nums">   
                    <div class="file-input"> <input class="form-control" type="text" v-model="attachUrls[idx]" :id="fileId+ index" :name="fileName" /> </div>
                    <div class="manu_logo" >  
                        <input class="file-btn " :class="{ 'pos-abs': isImage=='true'}" type="button" value="上传文件" :id="buttonId+ index" >   
                        <span v-if="isImage=='true'"> 
                            <img alt="" :src="attachUrls[idx] ? attachUrls[idx] : '/dist/img/defaultFace.jpg'" /> 
                            <a @click="del" href="javascript:void(0);">删除</a> 
                        </span>
                    </div>
                    <div class="manu_text" > <span v-show="false">{{n}}</span>
                        <!--建议上传120*44像素，-->支持{{types}}，文件大小不超过{{fileSize}}
                    </div>
                    <div class="remove-box" v-if="index!=0"><a href="javascript:void(0);" @click="handleRemove(index)"> 删除</a></div>
                </div>
                <div v-if="nums.length < maxLength" class="add-box"> <a href="javascript:void(0);" @click="addItem"><i class="fa fa-plus"></i>增加</a></div>
            </div>
        </div>`,
    props:{
        url: String,
        buttonId:String,
        fileId:String,
        fileName:String,
        uploadType:String,
        types:String,
        fileSize:String,
        isImage:String,
        validate:Object,
        class:String,
        keyname: String,
        attachFiles:{
            type:Array,
        	default(){
            	return []
            }
        },
        maxLength:Number
    },
    data(){
        return{
            isEdit:true,
            uploader:this.buttonId + 'Upload' ,
            attachUrls:[],
            nums:[0],
            size:1,
            n:0,
            isLoad:false,
            oldNums:[0]
        }
    },
    watch:{
        uploaders: function (val, old) { 
            var olds=this.oldNums,nums=this.nums;
            var arr=this.arrMinus(nums, olds);
            this.$nextTick(function () {
                this.oldNums = this.arrContact(this.oldNums, arr);
                for(var i=0; i<arr.length; i++) {
                    for(var j=0; j<nums.length; j++){
                        if(arr[i]===nums[j]){
                            this.uploadInit(j, arr[i]);
                        }
                    }
                }
            }); 
        },
        attachFiles: function (val) {
            this.attachUrls =val;
            if(this.attachFiles.length>0&&!this.isLoad){  
                this.isLoad=true;
                 if(this.attachFiles.length>this.size){ 
	                 var arr=[];
		        	 this.size=this.attachFiles.length;
		             for(var i=0; i<this.size; i++) {
		                 arr.push(i);
		             }
		             this.nums=arr;
                 }
            } 
        }
    },
    computed: {
        getNums () { 
            return this.nums;
        },
        uploaders(){
            var val=this.getNums;
            var uploader = this.uploader;
            var ids=[];
            for(var i=0; i<val.length; i++) {
                ids.push(uploader+val[i])
            }
            return ids;
        }
    },
    mounted(){
        var nums=this.getNums;
        for(var i=0; i<nums.length; i++) {
            this.uploadInit(i, nums[i]);
        }
    },
    methods:{
        arrContact: function(array1, array2){
            var result=array1;
            for(var i = 0; i < array2.length; i++){
                var isExist = false;
                for(var j = 0; j < array1.length; j++){
                    if(array2[i] === array1[j]){
                        isExist = true;
                        break;
                    }
                };
                if(!isExist){
                    result.push(array2[i]);
                }
            };
            return result;
        },
        arrMinus: function (arr1, arr2) {
            var arr=arr1.concat([]);
            for(var i=arr1.length; i>=0; i--){
                for(var j=0; j<arr2.length; j++){
                    if(arr1[i]==arr2[j]){
                        arr.splice(i,1);
                    }
                }
            }
            return arr;
        },
        uploadInit:function (i, idx) {
            var that=this;
            if(this.uploaders[i]){
            	//that.attachUrls[i]='';
                this.uploaders[i] = createUploader({
                    buttonId: this.buttonId+idx,
                    uploadType: this.uploadType,
                    url:  this.url,
                    types: this.types,
                    fileSize: this.fileSize,
                    isImage: this.isImage,
                    init:{
                        FileUploaded : function(up, file, info) {
                            layer.close(index);
                            if (info.status == 200 || info.status == 203) {
                                that.attachUrls[i] = _fileUrl;
                                that.n++;
                            } else {
                                layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120});
                            }
                            up.files=[];
                        }
                    }
                });
                this.uploaders[i].init();
            }
        },
        handleRemove: function (item) {
            var index=this.nums.indexOf(item);
            this.nums.splice(index, 1);
            this.attachUrls.splice(index, 1);
        },
        addItem: function () {
            //var num=this.getNums;
            this.size=this.size+1;
            if(this.nums.length<this.maxLength){
                this.nums.push(this.size-1);
            }
        }
    }
});
