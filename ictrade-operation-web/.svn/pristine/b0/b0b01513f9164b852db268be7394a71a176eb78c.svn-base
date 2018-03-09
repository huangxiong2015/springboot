/**
 * Created by yansha on 2017/9/30.
 */

Vue.component('title-set', {
    template: `<div class="">
    <form :name="formData.id" :id="formData.id" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
    <div class="row">
      <section class="col-lg-12">
          <div class="box box-danger "> 
               <div class="box-body ">  
                    <div class="form-group ">
                        <div class="col-md-12"> 
                            <label for="desc" class="col-sm-2 control-label">导航名称：</label>
                             <div class="col-sm-5" >
                                <input type="text" class="form-control" name="showSet[title]" id="title" required="required" v-model="initData.title" />
                             </div>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-md-12"> 
                            <label for="desc" class="col-sm-2 control-label">模块标题：</label>
                             <div class="col-sm-10" >
                                <!-- 使用的时，将这个部分引入到页面中start -->
                                <div id="desc" class="ueditor_wrapper" style="margin-left:-8px">  
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
                    <button type="button" @click="requireSaveData" class="btn btn-danger c-btn">下一步</button>  
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
                    id:'createTitle',
                    url:'',
                    message:{}
                }
            }
        },
        titleData: {
            type: Object,
            default(){
                return {
                	promotionContent:{
                		showSet:{
                			desc:'',
                			title:'',
                			showTitle:true
                		}
                	}
                }
            }
        },
        api:''
    },
    data() {
        return {
            initData: {
                title:'',
                showTitle:true,
                desc:''
            },
            ctlSelected: 1,
            datas: {},
            ueditor: ykyUrl.ictrade_ueditor,
            submitJson:{},
            step:1,
            titleJson:this.titleData
        }
    },
    watch:{
    	'titleData.promotionContent':function(val){
    		this.titleJson.promotionContent=val; 
    		this.setVal();
    	},
    	'titleData.promotionContent.showSet.desc':function(val){
    		this.titleJson=this.titleData; 
    		this.setVal();
    	},
	    'titleData.promotionContent.showSet.title':function(val){
	    	this.titleJson=this.titleData; 
    		this.setVal();
	    } 
    },
    mounted(){
        if (window.addEventListener) {
            window.addEventListener('message', this.saveTitle, false);
        } else if (window.attachEvent) {
            window.attachEvent('message', this.saveTitle, false);
        }
        if (this.titleJson.promotionContent && this.titleJson.promotionContent.showSet){
            this.init();
        }
        this.saveData();
    },
    methods: {
        init : function() { 
        	var $this=this;
            var title= this.titleJson.promotionContent.showSet.title,desc=this.titleJson.promotionContent.showSet.desc; 
            this.initData.title = title?title:''; 
            var time = 500;
            setTimeout(function() {
                if (desc) {
                	console.log(desc + '+' + time);
                    var content = 'loadContent' + desc;
                    window.frames[0].postMessage(content, $this.ueditor);
                }
            }, time);
        },
        setVal: function(){ 
        	var content ='loadContent';
        	if(this.titleJson.promotionContent && this.titleJson.promotionContent.showSet && this.titleJson.promotionContent.showSet.desc){ 
       		 	content = 'loadContent' + this.titleJson.promotionContent.showSet.desc;	
                this.initData.title= this.titleJson.promotionContent.showSet.title;
        	}else{
        		this.initData.title='';
        	}
    		window.frames[0].postMessage(content, this.ueditor);
        },
        saveTitle : function(event) {
            var formid = this.formData.id;
            this.initData.desc = event.data;
            $('#'+ this.formData.id).submit();
        },
        saveData: function() {
            var that = this;
            var formid = this.formData.id;
            var api = this.api;
            var message = this.formData.message;
            
            formValidate('#'+formid, null, function() {
            	if(!that.initData.desc){
            		return;
            	}
                //序列化form表单数据
                var formArry = $('#'+formid).serializeObject();
                formArry.showSet.showTitle=true;
                formArry.showSet.desc = that.initData.desc;  
                var data=that.titleJson; 
                if(data.promotionContent){
                	data.promotionContent.showSet=formArry.showSet
                }else{ 
                    data.promotionContent={
                    		showSet:formArry.showSet
                    	};  
                }
                setTimeout(function() {
	                that.$emit('form-submit', data, that.step);
	                that.$emit('step-change', that.step); 
	            }, 500);
                
            }, message);
        },
        getData:function(){
        	 this.requireSaveData(); 
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