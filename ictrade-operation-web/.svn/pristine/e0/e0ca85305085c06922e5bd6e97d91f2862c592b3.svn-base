/**
 * Created by yikuyi on 2017/1/17.
 */
Vue.component('radio-control', {
    template : `
            <div :style="styleObject" :class="'checkVal '+ inputClass" >
                <span class="radio-span" v-for="(item, index) in options">
                    <input type="radio" :name="name" :id="id +'_'+ index" :value="item[0]" class="chk_ input_radio" v-model="ctlSelected" v-bind="validate"> 
                    <label :for="id +'_'+ index"></label> {{item[1]}}
                 </span> 
            </div>
        `,
    props : {
        id : String,
        name:String,
        optionId : String,
        optionName:String,
        inputClass : String,
        styleObject: String ,
        api :  String,
        selOption: String,
        validate: Object
    },
    data(){
        return {
            current: {
                option: '',
            },
            region : [],
            ctlSelected : this.selOption ? this.selOption :''
        }
    },
    watch : {
        option (){
            this.current.option = '';
        },
        ctlSelected: function (val, oldVal) {
            console.log(val);
        }
    },
    created : function () {
        var $this = this;
        httpGet($this, $this.api, {} , function (res , err) {//页面加载前调用方法
            if(res){
                $this.region = res;
            }
        });
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