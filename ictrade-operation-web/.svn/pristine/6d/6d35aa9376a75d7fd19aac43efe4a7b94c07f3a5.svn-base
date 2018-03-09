/**
 * Created by yikuyi on 2017/1/17.
 */
Vue.component('checkbox-control', {
    template : `
            <div :style="styleObject" :class="'checkVal ' + inputClass">
                <span class="check-span" v-for="(item, index) in options">
                    <input type="checkbox" :name="name" :id="id +'_'+ index" :value="item[0]" class="input_check" v-model="ctlSelected" v-bind="validate"> 
                    <label :for="id +'_'+ index"></label> {{item[1]}}
                 </span> 
            </div>
        `,
    props : {
        id :  String,
        name: String,
        optionId : String,
        optionName: String,
        inputClass : String,
        styleObject: String ,
        api :  String,
        selOptions: Array,
        validate: Object
    },
    data(){
        return {
            current: {
                option: '',
            },
            region : [],
            ctlSelected : []
        }
    },
    watch : {
        option (){
            this.current.option = '';
        },
        selOptions: function (val, oldVal) {
            this.ctlSelected=val;
        }
    },
    mounted : function () {
        var $this = this;
        setTimeout(function() {
	        httpGet($this, $this.api, {} , function (res , err) {//页面加载前调用方法
	            if(res){
	                $this.region = res;
	            }
	        });
        }, 600);
    },
    methods : {
        _filter (items) {
            const result = []
            var id = this.optionId ? this.optionId : 'id';
            var name = this.optionName ? this.optionName : 'name';
            for (let i=0; i < items.length; i++) {
                result.push([items[i][id], items[i][name]])
            }
            return result
        }
    },
    computed : {
        options (){
            return this._filter(this.region);
        }
    }
});