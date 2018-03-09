/**
 * Created by yikuyi on 2017/1/17.
 */
Vue.component('select-control', {
    template : `
        <select :style="styleObject" :class="'form-control ' + inputClass" v-model="ctlSelected" @change="change('selected', $event.target.value)"  :name="name" :id="id" :requied="requied">
            <option value="" v-text="placeholder.option"></option>
            <option v-for="item in options" :value="item[0]" v-text="item[1]"></option>
        </select>
        `,
    props : {
        id : '',
        name:'',
        optionId : '',
        optionName:'',
        inputClass : '',
        styleObject: '' ,
        requied : '',
        placeholder : {
            type : Object,
            default(){
                return {
                    option : '请选择'
                }
            }
        },
        api :  String,
       selectedValue:''
    },
    data(){
        return {
            current: {
                option: '',
            },
            region : [],
            ctlSelected : ''
        }
    },
    watch : {
        option (){
            this.current.option = '';
        },
        selectedValue : function (val) {
            this.ctlSelected = this.selectedValue ? this.selectedValue : '';
        }
    },
    created : function () {
        var $this = this;
        httpGet($this, $this.api, {} , function (res , err) {//页面加载前调用方法
            if(res){
                $this.region = res;
                $this.ctlSelected = $this.selectedValue ? $this.selectedValue : '';
            }
        });
    },
    methods : {
        change : function (field, value) {
            this.ctlSelected= value;
        },
        _filter (items) {
            const result = []
            var id = this.optionId ? this.optionId : 'id';
            var name = this.optionName ? this.optionName : 'name';
            for (var i=0; i < items.length; i++) {
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