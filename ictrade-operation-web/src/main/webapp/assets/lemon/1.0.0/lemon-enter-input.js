/**
 * Created by yansha on 2017/8/11.
 */
Vue.component('enter-input',{
    template:`<div class="form-group">
        <label :for="id" class="col-sm-2 control-label"> {{keyname}}</label>  
        <div class="col-sm-9 checkVal" 
                :title="keyname" > 
                <div class="lemon-area">
                    <div class="lemon-tag" v-for="item in values"><span class="lemon-tag-text">{{item}}</span> <i class="fa fa-close" @click="del(item)" ></i></div> 
                     <input    
                        ref="input"
                        type="text" 
                        :placeholder="placeholder ? placeholder : keyname"    
                        :class="inputClass" 
                        v-model.trim="currentValue"  
                        @keyup.enter="setValue"
                     />   
                </div>   
                <input type="hidden" v-model="values"  :id="id" :name="name"/> 
            </div>
        </div>
</div>`,
    props:{
        keyname:String,
        id:String,
        name:String,
        inputClass:String,
        placeholder:String,
        options:Array
    },
    data(){
        return{
            currentValue:'',
            values: []
        }
    },
    watch:{
        options: function (val) {
        	for(var i = 0; i< val.length; i++){
        		if(val[i] !== ''){
        			this.values.push(val[i]);
        		}
        	}
//            =val;
        }
    },
    methods:{
        setValue: function () {
            if(this.currentValue&&this.values.indexOf(this.currentValue)<0){
                this.values.push(this.currentValue);
                this.currentValue='';
            }
        },
        del:function (item) {
            this.values.splice(this.randomIndex(item), 1);
        },
        randomIndex: function (item) {
            return this.values.indexOf(item);
        }
    }

})