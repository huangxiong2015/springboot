/**
 * Created by yansha on 2017/9/30.
 */ 
Vue.component('description-set', {
    template: `<div class="">
    <form :name="formData.id" :id="formData.id" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
    <div class="row">
      <section class="col-lg-12">
          <div class="box box-danger "> 
               <div class="box-body ">  
	               <div class="form-group ">
		               <div class="col-md-12"> 
		                   <label for="desc" class="col-sm-2 control-label">模块标题：</label>
		                    <div class="col-sm-5" >
		                       <input type="text" class="form-control" name="showSet[title]" id="title" required="required" v-model="initData.title" />
		                    </div>
		               </div>
		           </div>
                    <div class="form-group ">
                        <div class="col-md-12"> 
                        	<label for="desc" class="col-sm-2 control-label">模块内容：</label>
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
                    <button type="button" @click="requireSaveData" class="btn btn-danger c-btn">保存</button>  
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
                    id:'descForm',
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
                		contentSet: {},
                		showSet:{}
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
                newsContent:''
            },
            ctlSelected: 1,
            datas: {},
            ueditor: ykyUrl.ictrade_ueditor,
            step:0,
            descJson:this.descData
        }
    },
    watch:{
    	'descData.promotionContent.contentSet.content':function(val){
    		this.descJson=this.descData;
    		this.setVal();
    	}
    },
    mounted(){
        if (window.addEventListener) {
            window.addEventListener('message', this.saveNews, false);
        } else if (window.attachEvent) {
            window.attachEvent('message', this.saveNews, false);
        }
        if (this.descJson.promotionContent && this.descJson.promotionContent.contentSet&&this.descJson.promotionContent.contentSet.content){
            this.init();
        }
        this.saveData();
    },
    methods: {
        init : function() {
            var $this=this;
            this.initData.title = this.descJson.promotionContent.showSet.title;
            setTimeout(function() {
                if ($this.descJson.promotionContent.contentSet.content) {
                    var content = 'loadContent' + $this.descJson.promotionContent.contentSet.content;
                    window.frames[0].postMessage(content, $this.ueditor);
                }
            }, 500);
        },
        setVal: function(){ 
        	var content ='loadContent';
        	if(this.descJson.promotionContent && this.descJson.promotionContent.contentSet && this.descJson.promotionContent.contentSet.content){ 
       		 	content = 'loadContent' + this.descJson.promotionContent.contentSet.content;	 
        	} 
    		window.frames[0].postMessage(content, this.ueditor);
        },
        saveNews : function(event) {
        	
            var formid = this.formData.id;
            this.initData.newsContent = event.data;
            $('#'+ this.formData.id).submit();
          
        },
        saveData: function() {
            var that = this;
            var formid = this.formData.id;
            var api = this.api;//this.formData.url;
            var message = this.formData.message;
            formValidate('#'+formid, null, function() {
                //序列化form表单数据
                var formArry = $('#'+formid).serializeObject();
                formArry.contentSet={
                		content:  that.initData.newsContent
                }; 
                formArry.showSet={
                		title:  that.initData.title,
                		showTitle:  true
                } //=that.descJson.promotionContent.showSet; 
                var data=that.descJson;  
        		data.promotionContent=formArry;
        		data.promoModuleType= "CUSTOM" ;
        		console.log('提交'); 
                if (that.step>=0) {
                    syncData(api, "PUT",  data, function(res, err) {
                    	 if (res!=null) {
                            console.log('请求成功!');
                            that.$emit('form-submit', data, that.step);
                        }
                    }); 
                }else{
                	setTimeout(function() {
      	                that.$emit('form-submit', data);
      	                //that.$emit('step-change', that.step);
      	                that.step=0;
      	            }, 600);
                }
            }, message);
        }, 
       preClick:function(n){ 
       		this.step=n;
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