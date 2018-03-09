/**
 * Created by zhoulixia on 2017/9/30.
 */

Vue.component('product-list-set',{
	template:`
		<div class="product-module-style">	
			<div class="desc-steps">
				<steps :current="contShow" :border-color="'red'">
				    <step title="模块标题">1</step>
				    <step title="商品设置">2</step>
				    <step title="设置完成">2</step>
				</steps>
			</div>
			<div v-if="contShow==0">
				<title-set :api="api" :title-data="produchListJson" @step-change="stepChange" @cancle="cancle" @form-submit="titleFormSubmit" ></title-set>   
			</div>
			<div v-if="contShow==1">
				<product-set :api="api"  :product-analysis-api="productAnalysisApi" @step-change="stepChange" @cancle="cancle" ref="productSet" :product-set-data="produchListJson" @form-submit="productSetSubmit"></product-set>   
			</div>
			<div v-if="contShow==2">
				<product-edit :api="api" :product-analysis-api="productAnalysisApi" :del-product-api="delProductApi" :derived-product-api="derivedProductApi" :product-edit-data="produchListJson" :product-data-api="productDataApi" @step-change="stepChange" @cancle="cancle" ref="productEdit"></product-edit>   
			</div>
		</div>`,
	props:{
        productListData: {
            type: Object,
            default(){
            	return{
            		"promotionContent":{
	            		 "contentSet":{
	            			 
	            		 },
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
			contShow:0, 
			produchListJson:this.productListData
		}
	},
	created:function(){
		
		
	},
	mounted:function(){
		
	},
	watch:{
		
	},
	methods:{
		titleFormSubmit:function (formData, n) { 
	    	this.produchListJson=formData;  
	    	if(n!=-1){ 
	            this.$emit('form-submit', formData);
	    	}
	    },
	    productSetSubmit:function(formData, n){
	    	this.produchListJson=formData;  
	    	if(n!=-1){ 
	            this.$emit('form-submit', formData);
	    	}
	    },
	    productEditSubmit:function(formDara, n){	    	  
	    	this.contShow=this.contShow+n;        	
        	this.produchListJson=formData;
	    },
		stepChange:function(n){
			this.contShow=this.contShow+n
		},
		cancle: function(){
            this.$emit('cancle');
        }
	}
})