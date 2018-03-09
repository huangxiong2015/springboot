/**
 * Created by yikuyi on 2017/1/17.
 * 模态窗组件
 */
Vue.component('modal', {
    template: `<transition name="modal">
        <div class="modal-mask">
            <div class="modal-wrapper">
                <div class="modal-container"  :style="containerStyle">
                    <div class="modal-header">
                        <button type="button" class="close" v-on:click="$emit('close')"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title"><slot name="header">{{modalText.title}}</slot></h4>
                    </div>
                    <div class="modal-body" :style="bodyStyle">
                        <slot>Body Slot</slot>
                    </div>
                    <div class="modal-footer" v-if="showFoot">
                        <button type="button" class="btn btn-primary" v-on:click="clickOk" v-text="modalText.okText"></button>
                        <button type="button" class="btn btn-default" v-on:click="clickCancel" v-text="modalText.cancelText"></button>
                    </div>
                </div>
            </div>
        </div>
    </transition>`,
    props: {
        action:  String,
        modalStyle : {
            type:Object,
            default(){
                return{
                    width:300,
                    height:100
                }
            }
        },
        modalText:{
        	type:Object,
	        default(){
	            return{
	                okText:"Ok",
	                cancelText:"Close",
	                title:"Header Slot"
	            }
	        }
        },
        showFoot:{
            type:Boolean,
            default(){
                return true
            }
        }
    },
    methods: {
        clickOk: function() {
            this.$emit('click-ok', this.action);
        },
        clickCancel: function() {
            this.$emit('click-cancel');
        },
        setStyle:function(offset){
        	var style = {};
        	
        	if(typeof this.modalStyle.maxHeight != "undefined"){
        		style.maxHeight = (this.modalStyle.maxHeight+offset.y)+'px';
        	}else{
        		style.height = (this.modalStyle.height+offset.y)+'px';
        	}
        	
        	if(typeof this.modalStyle.maxWidth != "undefined"){
        		style.maxWidth = (this.modalStyle.maxWidth+offset.x)+'px';
        	}else{
        		style.width = (this.modalStyle.width+offset.x)+'px';
        	}
        	
        	return style
        }
    },
    computed:{
        containerStyle: function () {
            return this.setStyle({x:0,y:0})
        },
        bodyStyle: function(){
        	return this.setStyle({x:-60,y:-80})
        }
    }
})
