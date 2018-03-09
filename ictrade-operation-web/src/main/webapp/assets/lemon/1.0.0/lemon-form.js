/**
 * Created by licaibo on 2017/6/13.
 */

/**
 * field
 * @desc 单文本或多文本
 */
Vue.component('field', {
    template : `  <div class="form-group">  
    <label :for="keyId" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
    <div
        class="col-sm-7 col-md-8 col-lg-9 checkVal" 
        :title="keyname" 
        :class="[{
          'is-textarea': type === 'textarea',
          'is-nolabel': !keyname
        }]">  
            <textarea
                @change="$emit('change', currentValue)"
                ref="textarea"
                :id="keyId"
                :name="name"
                class="form-control"
                :class="inputClass" 
                :placeholder="rePlaceholder ? rePlaceholder : keyname"   
                v-if="type === 'textarea'"
                :rows="rows"
                :disabled="disabled"
                :readonly="readonly"
                v-model.trim="currentValue" 
                v-bind="validate"
              >
            </textarea>
             <input   
                 v-else-if="type==='email'"
                @change="$emit('change', currentValue)"
                ref="input"
                type="email" 
                :id="keyId"
                :name="name"
                :placeholder="rePlaceholder ? rePlaceholder : keyname"   
                :disabled="disabled"
                :readonly="readonly"
                class="form-control" 
                :class="inputClass" 
                v-model.trim="currentValue" 
                @input="handleInput"
                v-bind="validate" 
             /> 
             <input   
                v-else-if="type==='number'"
                @change="$emit('change', currentValue)"
                ref="input"
                type="number" 
                :id="keyId"
                :name="name"
                :placeholder="rePlaceholder ? rePlaceholder : keyname"  
                :number="type === 'number'"
                :disabled="disabled"
                :readonly="readonly"
                class="form-control" 
                :class="inputClass" 
                v-model.trim="currentValue" 
                @input="handleInput"
                v-bind="validate" 
             />   
            <input    
                v-else-if="type==='tel'"
                @change="$emit('change', currentValue)"
                ref="input"
                type="tel" 
                :id="keyId"
                :name="name"
                :placeholder="rePlaceholder ? rePlaceholder : keyname"   
                :disabled="disabled"
                :readonly="readonly"
                class="form-control" 
                :class="inputClass" 
                v-model.trim="currentValue" 
                @input="handleInput"
                v-bind="validate" 
             />  
            <input    
                v-else-if="type==='password'"
                @change="$emit('change', currentValue)"
                ref="input"
                type="password" 
                :id="keyId"
                :name="name"
                :placeholder="rePlaceholder ? rePlaceholder : keyname"   
                :disabled="disabled"
                :readonly="readonly"
                class="form-control" 
                :class="inputClass" 
                v-model.trim="currentValue" 
                @input="handleInput"
                v-bind="validate" 
             />  
            <input    
                v-else-if="type==='url'"
                @change="$emit('change', currentValue)"
                ref="input"
                type="url" 
                :id="keyId"
                :name="name"
                :placeholder="rePlaceholder ? rePlaceholder : keyname"   
                :disabled="disabled"
                :readonly="readonly"
                class="form-control" 
                :class="inputClass" 
                v-model.trim="currentValue" 
                @input="handleInput"
                v-bind="validate" 
             />  
              
            <input    
                v-else-if="type==='color'"
                @change="$emit('change', currentValue)"
                ref="input"
                type="color" 
                :id="keyId"
                :name="name"
                :placeholder="rePlaceholder ? rePlaceholder : keyname"   
                :disabled="disabled"
                :readonly="readonly"
                class="form-control" 
                :class="inputClass" 
                v-model.trim="currentValue" 
                @input="handleInput"
                v-bind="validate" 
             />   
            <input    
                v-else
                @change="$emit('change', currentValue)"
                ref="input"
                type="text" 
                :id="keyId"
                :name="name"
                :placeholder="rePlaceholder ? rePlaceholder : keyname"   
                :disabled="disabled"
                :readonly="readonly"
                class="form-control" 
                :class="inputClass" 
                v-model.trim="currentValue" 
                @input="handleInput"
                v-bind="validate" 
             />  
        <div class="mint-field-other">
          <slot></slot>
        </div>
      </div>
    </div>`,
    props: {
        type: {
            type: String,
            default(){
                return 'text';
            }
        },
        rows: String,
        keyname: String,
        placeholder: String,
        readonly: Boolean,
        disabled: Boolean,
        value: {
            type: String,
            default(){
                return ''
            }
        },
        attr: Object,
        keyId:String,
        name:String,
        validate: Object,
        inputClass: String
    } ,
    data() {
        return {
            active: false,
            currentValue: this.value,
            rePlaceholder: this.attr&&this.attr.placeholder?this.attr.placeholder : this.placeholder,
        };
    },
    methods: {
        doCloseActive() {
            this.active = false;
        },
        handleInput(evt) {
            this.currentValue = evt.target.value;
        },
        handleClear() {
            if (this.disabled || this.readonly) return;
            this.currentValue = '';
        }
    },
    watch: {
        value(val) {
            this.currentValue = val;
        },
        currentValue(val) {
            this.$emit('input', val);
        },
        attr: {
            immediate: true,
            handler(attrs) {
                this.$nextTick(() => {
                    const target = [this.$refs.input, this.$refs.textarea];
                    target.forEach(el => {
                        if (!el || !attrs) return;
                        Object.keys(attrs).map(name => el.setAttribute(name, attrs[name]));
                    });
                });
            }
        }
    }
})
/**
 * date-range-mounth
 * @desc 时间区间
 */
Vue.component('date-range-mounth', {
    template: ` <div class="form-group"> 
        <label :for="startkey" class="col-sm-2 col-md-2 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div class="col-sm-8 col-md-5 col-lg-5 checkVal"  :title="keyname" > 
            <div class="input-daterange input-group"> 
                     <input type="text" 
                         :id="startkey" 
                         :name="startname" 
                         v-bind="validate" 
                         class="form-control" 
                         :class="inputClass" 
                         :value="startDate" 
                         :placeholder="startPlaceholder"
                         :readonly="readonly"
                         :disabled="disabled"
                       />
                     <span class="input-group-addon">至</span> 
                     <input type="text" 
                         :id="endkey" 
                         :name="endname" 
                         v-bind="validate" 
                         class="form-control" 
                         :class="inputClass" 
                         :value="endDate"  
                         :placeholder="endPlaceholder"
                         :readonly="readonly"
                         :disabled="disabled"
                     /> 
                </div>   
        </div> 
        <div class="col-sm-4 col-md-5 col-lg-4 input-span-box" v-if="showDateRange"><span class="input-span"  v-for="(item, index) in dateRange" @click="changeDate(index)">{{item}}</span></div>
    </div>`,
    props:{
        keyname: String,
        startkey: String,
        startname: String,
        endkey: String,
        endname: String,
        inputClass:String,
        startPlaceholder: String,
        endPlaceholder: String,
        validate:Object,
        startValue:String,
        endValue:String,
        readonly:[String, Boolean],
        disabled:[String, Boolean],
        dateRange:{
            type:Object,
            default(){
                return {};
            }
        },
        showDateRange: {
            type: Boolean,
            default: false
        }
    },
    data(){
        return {
            startDate:'',
            endDate:''
        }
    },
    watch:{
        startValue:function (val) {
            this.startDate=val;
        },
        endValue:function (val) {
            this.endDate=val;
        }
    },
    methods:{
        changeDate:function (val) {
            var data = new Date();
            this.endDate = data.Format('yyyy-MM-dd');
            data=data.setMonth(data.getMonth()-val, data.getDate());
            this.startDate = new Date(data).Format('yyyy-MM-dd');
        },
        handleClear: function () {
            var data = new Date();
            this.startDate = this.endDate = data.Format('yyyy-MM-dd');
            this.startDate='';
            this.endDate='';
        }
    }
})

/**
 * daterange
 * @desc 时间区间
 */
Vue.component('daterange', {
    template: ` <div class="form-group"> 
        <label :for="startkey" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div
            class="col-sm-7 col-md-8 col-lg-9 checkVal" 
            :title="keyname" > 
             <div  class="input-daterange input-group"> 
                 <input type="text" 
                     :id="startkey" 
                     :name="startname" 
                     v-bind="validate" 
                     class="form-control" 
                     :class="inputClass" 
                     :value="startDate" 
                     :placeholder="startPlaceholder"
                   />
                 <span class="input-group-addon">至</span> 
                 <input type="text" 
                     :id="endkey" 
                     :name="endname" 
                     v-bind="validate" 
                     class="form-control" 
                     :class="inputClass" 
                     :value="endDate"  
                     :placeholder="endPlaceholder"
                 /> 
            </div>
        </div>
    </div>`,
    props:{
        startkey: String,
        startname: String,
        endkey: String,
        endname: String,
        inputClass:String,
        startPlaceholder: String,
        endPlaceholder: String,
        readonly: Boolean,
        disabled: Boolean,
        validate:Object,
        startValue:String,
        endValue:String,
        keyname: String
    },
    data(){
        return {
            startDate:'',
            endDate:''
        }
    },
    watch:{
        startValue:function (val) {
            this.startDate=val;
        },
        endValue:function (val) {
            this.endDate=val;
        }
    },
    methods:{
        handleClear: function () {
            var data = new Date();
            this.startDate = this.endDate = data.Format('yyyy-MM-dd');
            this.startDate='';
            this.endDate='';
        }
    }


})
/**
 * range
 * @desc 数字区间
 */
Vue.component('range', {
    template: `<div class="form-group"> 
        <label :for="startkey" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div
            class="col-sm-7 col-md-8 col-lg-9 checkVal" 
            :title="keyname" > 
             <div  class="input-group">
                <input type="text" 
                    :id="startkey" 
                    :name="startname" 
                    v-bind="validate" 
                    class="form-control" 
                    :class="inputClass" 
                    v-model="startData"  
                    @blur="startBlur" 
                    :placeholder="startPlaceholder" 
                    :readonly="readonly"
                    :disabled="disabled"
                /> 
                <span class="input-group-addon">至</span> 
                <input type="text" 
                    :id="endkey" 
                    :name="endname"
                    v-bind="validate" 
                    class="form-control" 
                    :class="inputClass" 
                    v-model="endData" 
                    @blur="endBlur" 
                    :placeholder="endPlaceholder"
                    :readonly="readonly"
                    :disabled="disabled"
                 />
            </div>
        </div>
    </div>`,
    props:{
        nativeType:String,
        startkey: String,
        startname: String,
        endkey: String,
        endname: String,
        inputClass:String,
        startPlaceholder: String,
        endPlaceholder: String,
        readonly: Boolean,
        disabled: Boolean,
        validate:Object,
        startValue:String,
        endValue:String,
        keyname: String
    },
    data(){
        return {
            startData:'',
            endData:''
        }
    },
    watch:{
        startValue:function (val) {
            this.startData=val;
        },
        endValue:function (val) {
            this.endData=val;
        }
    },
    methods: {
        startBlur () {
            var end=this.endData,val=this.startData;
            this.endData=(val!=='' && val > end)? val: end;
        },
        endBlur () {
            var start=this.startData,val=this.endData;
            this.startData= (val==='' || val > start)? start: val;
        },
        handleClear: function () {
            this.startData='';
            this.endData='';
        }
    }

})

/**
 * checkbox-control
 * @desc 多选按钮
 */
Vue.component('checkbox-control', {
    template : `<div class="form-group">
        <label :for="id" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div class="col-sm-7 col-md-8 col-lg-9 checkVal" 
                :title="keyname" > 
                <div :style="styleObject" :class="'checkbox inline ' + inputClass"> 
                    <span class="check-span" v-for="(item, index) in checks">
                        <input type="checkbox" :name="name" :id="id +'_'+ index" :value="item[0]" class="input_check" v-model="ctlSelected" v-bind="validate"> 
                        <label :for="id +'_'+ index"></label> {{item[1]}}
                     </span> 
                </div>
            </div>
        </div>`,
    props : {
        id :  String,
        name: String,
        optionId : String,
        optionName: String,
        inputClass : String,
        styleObject: String ,
        api :  String,
        selOptions: Array,
        validate: Object,
        keyname: String,
        options: Array
    },
    data(){
        return {
            current: {
                option: '',
            },
            region : this.options ? this.options:[],
            ctlSelected : this.selOptions ? this.selOptions:[]
        }
    },
    watch : {
        options:function (val, oldVal) {
            this.region = val;
        },
        option (){
            this.current.option = '';
        },
        selOptions: function (val, oldVal) {
            this.ctlSelected=val;
        }
    },
    mounted() {
        var $this = this;
        if($this.api){
            httpGet($this, $this.api, {} , function (res , err) {//页面加载前调用方法
                if(res){
                    $this.region = res;
                }
            });
        }
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
        },
        handleClear: function () {
            this.ctlSelected=[];
        }
    },
    computed : {
        checks (){
            return this._filter(this.region);
        }
    }
});

/**
 * radio-control
 * @desc 单选按钮
 */
Vue.component('radio-control', {
    template : `<div class="form-group"> 
        <label :for="id" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div
            class="col-sm-7 col-md-8 col-lg-9 checkVal" 
            :title="keyname" > 
            <div :style="styleObject" :class="'radio inline '+ inputClass" >
                <span class="radio-span" v-for="(item, index) in radios">
                    <input type="radio" :name="name" :id="id +'_'+ index" :value="item[0]" class="chk_ input_radio" v-model="ctlSelected"> 
                    <label :for="id +'_'+ index"></label> {{item[1]}}
                 </span> 
            </div> 
        </div> 
    </div>`,
    props : {
        keyname: String,
        id : String,
        name:String,
        optionId : String,
        optionName:String,
        inputClass : String,
        styleObject: String ,
        api :  String,
        selectedOption:String,
        validate:Object,
        options:Array
    },
    data(){
        return {
            current: {
                option: '',
            },
            region : this.options ? this.options :'',
            ctlSelected : this.selectedOption ? this.selectedOption :''
        }
    },
    watch : {
        options:function (val) {
            this.region = val;
        },
        option (){
            this.current.option = '';
        },
        selectedOption: function (val, oldVal) {
            this.ctlSelected=val;
        }
    },
    created : function () {
        var $this = this;
        if($this.api){
            httpGet($this, $this.api, {} , function (res , err) {//页面加载前调用方法
                if(res){
                    $this.region = res;
                }
            });
        }
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
        },
        handleClear: function () {
            this.ctlSelected='';
        }
    },
    computed : {
        radios (){
            return this._filter(this.region);
        }
    }
});

/**
 * radio-control
 * @desc 下拉选
 */
/**
 * Created by yikuyi on 2017/1/17.
 */

Vue.component('select-control', {
    template: `
        <div class="form-group"> 
                <label :for="id" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
                <div
                    class="col-sm-7 col-md-8 col-lg-9 checkVal" 
                    :title="keyname" > 
                    <template v-if="attr.multiple || multiple">
                        <select :style="styleObject"  v-bind="attr" :class="'form-control ' + inputClass" v-model="ctlSelecteds" :name="name" :id="id">  
                            <option v-if="attr.placeholder" value="" v-text="rePlaceholder"></option>
                            <option v-for="item in selects" :value="item[0]" v-text="item[1]"></option>
                        </select>
                    </template>
                    <template v-else> 
                       <select :style="styleObject"  v-bind="attr" :class="'form-control ' + inputClass" v-model="ctlSelected"  :name="name" :id="id" >  
                            <option  v-if="attr.placeholder" value="" v-text="rePlaceholder"></option>
                            <option v-for="item in selects" :value="item[0]" v-text="item[1]"></option>
                        </select>
                    </template> 
        </div> 
    </div>`,
    props: {
        keyname:String,
        id: String,
        name: String,
        optionId: String,
        optionName: String,
        inputClass: String,
        styleObject: String,
        multiple: [String, Boolean],
        placeholder: {
            type: Object,
            default () {
                return {
                    option: '请选择'
                }
            }
        },
        validate: Object,
        api: String,
        options: Array,
        selectedOption: [String, Array],
        selectedOptions:Array,
        attr:{
            type:[Object, Array],
            default(){
                return {}
            }
        }
    },
    data() {
        return {
            current: {
                option: '',
            },
            region: this.options ? this.options: [],
            ctlSelected:'',
            ctlSelecteds:[],
            rePlaceholder: this.attr?this.attr.placeholder:this.placeholder.option
        }
    },
    watch: {
        options:function (val) {
            this.region = val;
        },
        option() {
            this.current.option = '';
        },
        selectedOption: function(val) {
            this.ctlSelected = val;
        },
        selectedOptions:function(val) {
            this.ctlSelecteds = val;
        }
    },
    created: function() {
        var $this = this;
        if($this.api){
            httpGet($this, $this.api, {}, function(res, err) { //页面加载前调用方法
                if (res) {
                    $this.region = res;
                }
            });
        }
    },
    methods: {
        /*change: function(value) {
         if(this.multiple){
         var pos=this.ctlSelecteds.indexOf(value);
         if(pos > -1){
         this.ctlSelecteds.splice(pos,1);
         }else{
         this.ctlSelecteds.push(value);
         }
         }else{
         this.ctlSelected = value;
         }
         },*/
        _filter(items) {
            const result = []
            var id = this.optionId ? this.optionId : 'value';
            var name = this.optionName ? this.optionName : 'text';
            for (var i = 0; i < items.length; i++) {
                result.push([items[i][id], items[i][name]])
            }
            return result
        },
        handleClear: function () {
            this.ctlSelected='';
            this.ctlSelecteds=[];
        }
    },
    computed: {
        selects() {
            return this._filter(this.region);
        }
    }
});


/**
 * select-province
 * @desc 三级联动
 */
Vue.component('select-province', {
    template : `<div class="form-group">
        <label :for="id" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.citySel" class="text-red">*</span></label>  
        <div class="col-sm-7 col-md-8 col-lg-9 checkVal" 
                :title="keyname" > 
            <div class="city-picker row">
                <div class="pull-left col-sm-4"  > 
                    <select class="form-control" :class="inputClass" :style="styleObject" v-bind="validate" v-model="provinceSelected" @change="change"   :name="items.province.name" :id="items.province.id">
                        <option value="" v-text="placeholder.province"></option>
                        <option v-for="item in provinces" :value="item[0]" v-text="item[1]"></option>
                    </select>
                </div>
                <div class="pull-left col-sm-4" >
                    <select class="form-control"  :class="inputClass"  :style="styleObject"  v-bind="validate" v-model="citySelected" @change="change"  :name="items.city.name" :id="items.city.id"">
                        <option value="" v-text="placeholder.city"></option>
                        <option v-for="item in cities" :value="item[0]" v-text="item[1]"></option>
                    </select>
                </div>
                <div class="pull-left col-sm-4" >
                    <select class="form-control" :class="inputClass"  :style="styleObject"  v-bind="validate" v-model="districtSelected"  @change="change":name="items.district.name" :id="items.district.id""> 
                        <option value="" v-text="placeholder.district"></option>
                        <option v-for="item in districts" :value="item[0]" v-text="item[1]"></option>
                    </select>
                </div>
            </div>
        </div>
    </div> `,
    props : {
        keyname: String,
        inputClass : '',
        styleObject: '' ,
        parentId : '',
        items:{
            type:Object,
            default(){
                return {
                    province :{
                        id:'province',
                        name:'province'
                    },
                    city : {
                        id:'city',
                        name:'city'
                    },
                    district : {
                        id:'district',
                        name:'district'
                    }
                }
            }
        },
        placeholder : {
            type : Object,
            default(){
                return {
                    province : '请选择',
                    city : '请选择',
                    district : '请选择'
                }
            }
        },
        optionId:'',
        optionName:'',
        api :  String,
        setProvince: '',
        setCity:'',
        setDistrict:'',
        validate: Object
    },
    data(){
        return {
            current: {
                province : this.setProvince ? this.setProvince: '',
                city : this.setCity ? this.setCity: '',
                district : this.setDistrict ? this.setDistrict: '',
            },
            sign: 0 ,
            region : [],
            citydata : [],
            districtdata : [],
            id: this.optionId ? this.optionId : 'id',
            name: this.optionName ? this.optionName : 'name',
            provinceSelected:'',
            citySelected:'',
            districtSelected: '',
        }
    },
    created : function () {
        var $this = this;
        httpGet($this, $this.api, {}, function (res , err) {//页面加载前调用方法
            if(res){
                $this.region = res;
                $this.provinceSelected = $this.current.province;
                $this.current.province='';
            }
        });
    },
    watch : {
        setProvince:function (val) {
            this.current.province=val;
            this.provinceSelected =val;
        },
        setCity:function (val) {
            this.current.city=val;
            this.citySelected =val;
        },
        setDistrict:function (val) {
            this.current.district=val;
            this.districtSelected =val;
        },
        provinceSelected : function (val) {
            this.citySelected = ''
            this.districtSelected = ''
            this.ajax('citydata', val);
        },
        citySelected: function (val) {
            this.districtSelected = ''
            this.ajax('districtdata', val);
        }
    },
    methods : {
        change:function(){
            this.$emit('onchange',  {
                province: this.provinceSelected,
                city: this.citySelected,
                district: this.districtSelected
            });
        },
        ajax: function (obj, val) {
            var $this = this;
            console.log(obj);
            var param={};
            if(!val || val.length==0){
                if(obj == 'citydata'){
                    this.citydata = [];
                    this.districtdata = [];
                }else{
                    this.districtdata = [];
                }
            }else{
                var paramid = this.parentId ? this.parentId : 'pid';
                param[paramid]=val;
                httpGet($this, $this.api, param , function (res , err) {//页面加载前调用方法
                    if(res){
                        $this[obj] = res;
                        if(obj=='citydata'){
                            $this.citySelected = $this.current.city;
                            $this.current.city='';
                        }else{
                            $this.districtSelected =$this.current.district;
                            $this.current.district='';
                        }
                    }
                });
            }
        },
        _filter (items) {
            const result = []
            for (let i=0; i < items.length; i++) {
                result.push([items[i][this.id], items[i][this.name]])
            }
            return result
        },
        handleClear: function () {
            this.provinceSelected='';
            this.citySelected='';
            this.districtSelected='';
        }
    },
    computed : {
        provinces (){
            return this._filter(this.region);
        },
        cities (){
            return  this._filter(this.citydata);
        },
        districts (){
            return  this._filter(this.districtdata);
        }
    }
});

/**
 * search-select
 * @desc 可搜索选择
 */
Vue.component('sel-option', {
    template: `<option :value="setOptions[optionId]" :selected="setOptions.selected">{{setOptions[optionName]}}</option>  `,
    props:{
        option:Object,
        selectedOptions: Array,
        optionId:'',
        optionName:''
    },
    computed:{
        setOptions : function(){
            var data = this.option;
            var datas = this.selectedOptions;
            var $this=this;
            delete data.selected;
            for( var i in datas){
                if($this.option[$this.optionId] === datas[i] ){
                    data.selected=true;
                }
            }
            return data;
        }
    }

})
Vue.component('search-select', {
    template: `<div class="form-group"> 
        <label :for="id" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div
            class="col-sm-7 col-md-8 col-lg-9 checkVal" 
            :title="keyname" >  
           <select v-if="selectType=='multi'" class="form-control select2-hidden" v-bind="validate" multiple="" tabindex="-1" aria-hidden="true" :id="id" :name="name" > 
                <option value="" v-text="inputText"></option>
                 <template v-for="(option, idx) in retOptions">
                    <sel-option :option="option" :selected-options="selOptions" :option-id="optionId" :option-name="optionName"></sel-option>
                </template> 
           </select> 
           <select  v-else class="form-control select2-hidden" tabindex="-1" aria-hidden="true" :id="id" :name="name"  v-bind="validate" v-model="selOption" >
                <option value="" v-text="inputText"></option> 
                <option v-for="(option, idx) in retOptions" :value="option[optionId]" >{{option[optionName]}}</option>
           </select>    
           <div  class="ui fluid  search selection dropdown"
               :class="{ 'active visible':showMenu ,'multiple': selectType=='multi'}"
               @click="openOptions">
                <i class="dropdown icon"></i>
                <template  v-if="selectType=='multi'" v-for="(sel, idx) in selOptions"> 
                     <template v-for="(option, idx) in retOptions" v-if="option[optionId] === sel">
                          <a class="ui-label transition visible" >
                              {{option[optionName]}}<i class="fa fa-close" @click="deleteItem(option)"></i>
                          </a>
                     </template>  
                </template> 
                <template v-for="(option, idx) in retOptions"  v-if="!selectType && option[optionId] === selOption"> 
                    <span class="ui-span">{{option[optionName]}}</span>
                </template> 
                <input class="search" autocomplete="off" tabindex="0"
                       v-model="searchText"
                       ref="input"
                       :style="inputWidth"
                       @blur="blurInput"
                       @keydown.up="prevItem"
                       @keydown.down="nextItem"
                       @keyup.enter="enterItem"
                       @keydown.delete="deleteTextOrLastItem"
                />
                <div  class="text" :class="textClass">
                    {{inputText}}  
                </div>
                <div class="menu" :class="menuClass" :style="menuStyle" tabindex="-1"> 
                     <template  v-if="selectType && selectType=='multi'" v-for="(option, idx) in filteredOptions">
                        <div class="item" :class="{ 'selected': option.selected }"
                             @click="selectItems(option)"
                             @mousedown="mousedownItem">
                          {{option[optionName]}}
                        </div>
                     </template>
                     <template  v-else v-for="(option, idx) in filteredOption">
                        <div class="item" :class="{ 'selected': option.selected }"
                             @click.stop="selectedItem(option)"
                             @mousedown="mousedownItem">
                            {{option[optionName]}}
                        </div>
                     </template>
                </div>
           </div> 
           <slot></slot>
           </div>
        </div>`,
    props: {
        'options': {
            type: Array,
            default: () => { return [] }
        },
        'api': {
            type: String,
            default: () => { return '' }
        },
        'selectedOptions': {
            type: Array,
            default: () => { return [] }
        },
        'selectedOption': {
            type: String,
            default: () => { return ''}
        },
        'isError': {
            type: Boolean,
            default: false
        },
        placeholder: {
            type: String,
            default: ''
        },
        selectType: String,
        id : String,
        name :  String,
        validate: String,
        class: String,
        optionId: String,
        optionName: String,
        keyname:String
    },
    data () {
        return {
            showMenu: false,
            searchText: '',
            mousedownState: false, // mousedown on option menu
            retOptions : this.options,
            selOptions:  this.selectedOptions,
            selOption:  this.selectedOption
        }
    },
    created : function () {
        var $this = this;
        if (this.api){
            httpGet($this, $this.api, {}, function (res, err) {//页面加载前调用方法
                if (res) {
                    $this.retOptions = res;
                }
            });
        }
    },
    watch:{
        selectedOption : function (val) {
            var that=this;
            var blu = function () {
                $("#" + that.id).blur();
            }
            setTimeout(blu, 500);
        }
    },
    computed: {
        inputText () {
            let text='';
            if (!this.searchText) {
                text = this.placeholder
                //return text
            }
            return text
        },
        textClass () {
            if (this.placeholder) {
                return 'default'
            } else {
                return ''
            }
        },
        inputWidth () {
            return {
                width: ((this.searchText.length + 1) * 8) + 20 + 'px'
            }
        },
        menuClass () {
            return {
                visible: this.showMenu,
                hidden: !this.showMenu
            }
        },
        menuStyle () {
            $("#" + this.id).blur();
            return {
                display: this.showMenu ? 'block' : 'none'
            }
        },
        nonSelectOptions () {
            var rets=[],$this=this;
            if(this.selOption){
                var data={};
                data[$this.optionId]=this.selOption;
                rets[0]=data;
            }else if(this.selOptions){
                _.forEach(this.selOptions, function(value) {
                    var data={};
                    data[$this.optionId]=value;
                    rets.push(data);
                });
            }
            return _.differenceBy(this.retOptions, rets, this.optionId)
        },
        filteredOption () {
            if (this.searchText) {
                return this.retOptions.filter(option => {
                    return option[this.optionName].match(new RegExp(this.searchText, 'i'))
                })
            } else {
                return this.retOptions
            }
        },
        filteredOptions () {
            if (this.searchText) {
                return this.nonSelectOptions.filter(option => {
                    return option[this.optionName].match(new RegExp(this.searchText, 'i'))
                })
            } else {
                return this.nonSelectOptions
            }
        }
    },
    methods: {
        deleteTextOrLastItem () {
            if (!this.searchText && this.selOptions.length > 0) {
                this.deleteItem(_.last(this.selOptions))
            }
        },
        // cursor on input
        openOptions () {
            this.showMenu = true
            this.mousedownState = false
            this.$refs.input.focus()
        },
        blurInput () {
            common.blurInput(this)
        },
        closeOptions () {
            common.closeOptions(this)
        },
        prevItem () {
            common.prevItem(this)
        },
        nextItem () {
            common.nextItem(this)
        },
        enterItem () {
            common.enterItem(this)
        },
        mousedownItem () {
            common.mousedownItem(this)
        },
        selectItems (option) {
            this.searchText = '';
            var opt=option[this.optionId];
            const selectedOptions = _.unionWith(this.selOptions, [opt], _.isEqual);
            this.closeOptions();
            this.selOptions= selectedOptions;
            //this.$emit('select', selectedOptions, option)
        },
        deleteItem (option) {
            var opt=option[this.optionId];
            var arr=this.selOptions;
            arr.splice($.inArray(opt,arr),1);
            //const selectedOptions = this.selOptions.remove(opt);
            this.selOptions= arr;
            //this.$emit('select', selectedOptions, option)
        },
        selectedItem (option) {
            this.searchText = ''; // reset text when select item
            this.closeOptions();
            this.selOption=option[this.optionId];
            // this.$emit('select', option);
        },
        handleClear: function () {
            this.selOptions=[];
        }
    }
});

/**
 * 文件上传组件
 */
Vue.component('file-upload', {
    template: `<div class="form-group"> 
        <label :for="fileId" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div
            class="col-sm-7 col-md-8 col-lg-9 checkVal" 
            :title="keyname" > 
                <div class="manu_logo" >  
                    <input class="file-btn " :class="{ 'pos-abs': isImage=='true'}" type="button" value="上传文件" :id="buttonId" >   
                    <span v-if="isImage=='true'"> 
                        <img alt="" :src="attachUrl ? attachUrl : '/dist/img/defaultFace.jpg'" /> 
                        <a @click="del" href="javascript:void(0);">删除</a> 
                    </span>
                </div>
                <div class="manu_text" > 
                    建议上传120*44像素，支持jpg、jpeg、png、gif，文件大小不超过5MB
                </div> 
                <input type="text" v-model="attachUrl" :id="fileId" :name="fileName"  style="visibility: hidden;height:0;" v-bind="validate"> 
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
        attachFile:String,
        keyname: String
    },
    data(){
        return{
            attachUrl: this.attachFile,
            isEdit:true,
            uploader: this.buttonId + 'Upload'
        }
    },
    watch:{
        attachFile : function(val) {
            this.attachUrl=val;
        }
    },
    mounted(){
        this.uploadInit();
    },
    methods:{
        del:function () {

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
                init:{
                    FileUploaded : function(up, file, info) {
                        layer.close(index);
                        if (info.status == 200 || info.status == 203) {
                            that.attachUrl = _fileUrl;
                        } else {
                            layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120});
                        }
                        up.files=[];
                    }
                }
            });
            this.uploader.init();
        },
        handleClear: function () {
            this.attachUrl='';
        }
    }
});

/**
 * lemon-button
 * @desc 按钮
 */
Vue.component('lemon-button',{
    template : ` <button
    :type="datas.nativeType ? datas.nativeType : 'submit'"
    class="btn ml10 mr10"
    :class="[datas.inputClass, 'btn-' + datas.type, 'input-' + datas.size, {
      'is-disabled': datas.disabled,
      'is-plain': datas.plain
    }]"
    @click="handleClick"
    :disabled="datas.disabled"> 
      <slot name="icon" v-if="datas.icon || $slots.icon">
        <i v-if="datas.icon" class="fa" :class="'fa-' + datas.icon"></i>
      </slot> 
     <slot></slot> 
  </button>`,
    methods:{
        handleClick(evt) {
            this.$emit('click', evt);
        }
    },
    props: {
        datas:Object
    }
});

/**
 * lemon-form
 * @desc form表单
 * @module components/form/lemon-form
 */
Vue.component('lemon-form', {
    template:`  <form class="form-horizontal" :name="formData.name" :id="formData.id" onsubmit="return false;">
        <div id="main" >
            <div class="col-sm-11"  v-for="(item, index) in fields" v-bind:class="item.rowCss ? item.rowCss : formData.columnCount ? {'col-md-6 col-lg-6': formData.columnCount===2,'col-sm-6 col-md-6 col-lg-4': formData.columnCount===3,'col-sm-6 col-md-4 col-md-3': formData.columnCount===4} : ''" > 
                 <template  v-if="item.type==='file'"> 
                    <file-upload  
                        :keyname="item.keyname"
                        :file-id= "item.key"
                        :file-name= "item.name"
                        :button-id= "item.config.buttonId"
                        :upload-type= "item.config.uploadType"
                        :url= "item.config.url"
                        :types= "item.config.types"
                        :file-size= "item.config.fileSize"
                        :is-image= "'false'" 
                        :validate="item.validate"  
                        :class="item.inputClass"
                        :attach-file="datas[item.key]"
                        v-bind="item.attr"
                        ref="fileUploadRef"
                    ></file-upload>
                </template> 
                 <template  v-else-if="item.type==='image'"> 
                    <file-upload
                        :keyname="item.keyname"
                        :file-id= "item.key"
                        :file-name= "item.name"
                        :button-id= "item.config.buttonId"
                        :upload-type= "item.config.uploadType"
                        :url= "item.config.url"
                        :types= "item.config.types"
                        :file-size= "item.config.fileSize"
                        :is-image= "'true'" 
                        :validate="item.validate"  
                        :class="item.inputClass"
                        :attach-file="datas[item.key]" 
                        v-bind="item.attr"
                        ref="imgUploadRef"
                    ></file-upload> 
                </template>   
                 <template  v-else-if="item.type==='range'"> 
                    <range 
                        :keyname="item.keyname"
                        :startkey="item.startkey"
                        :startname="item.startname"
                        :endkey="item.endkey"
                        :endname="item.endname"
                        :input-class="item.inputClass"
                        :start-placeholder="item.startPlaceholder"
                        :end-placeholder="item.endPlaceholder"
                        :readonly="item.readonly"
                        :disabled="item.disabled"
                        :validate="item.validate"
                        :start-value="datas[item.startkey]" 
                        :end-value="datas[item.endkey]" 
                        ref="rangeRef"
                    ></range> 
                </template>   
                <template  v-else-if="item.type==='daterange'">  
                    <daterange 
                        :keyname="item.keyname"
                        :startkey="item.startkey"
                        :startname="item.startname"
                        :endkey="item.endkey"
                        :endname="item.endname"
                        :input-class="item.inputClass"
                        :start-placeholder="item.startPlaceholder"
                        :end-placeholder="item.endPlaceholder"
                        :readonly="item.readonly"
                        :disabled="item.disabled"
                        :validate="item.validate"
                        :start-value="datas[item.startkey]" 
                        :end-value="datas[item.endkey]"  
                        ref="dateRangeRef"
                    ></daterange> 
                </template> 
                <template  v-else-if="item.type==='date-range-mounth'">  
                    <date-range-mounth 
                        :keyname="item.keyname"
                        :startkey="item.startkey"
                        :startname="item.startname"
                        :endkey="item.endkey"
                        :endname="item.endname"
                        :input-class="item.inputClass"
                        :start-placeholder="item.startPlaceholder"
                        :end-placeholder="item.endPlaceholder"
                        :readonly="item.readonly"
                        :disabled="item.disabled"
                        :validate="item.validate"
                        :start-value="datas[item.startkey]" 
                        :end-value="datas[item.endkey]" 
                        :date-range="item.dateRange"
                        :show-date-range="item.showDateRange" 
                        ref="dateRangeMounthRef"
                    ></date-range-mounth> 
                </template>  
                 <template  v-else-if="item.type==='multi-select'">
                    <search-select 
                        :keyname="item.keyname"
                        :options="item.options"
                        :selected-options="item.items"
                        :placeholder="item.placeholder? item.placeholder : '请选择'" 
                        :id="item.key"
                        :name="item.name" 
                        selectType="multi"
                        :option-id="item.optionId"
                        :option-name="item.optionName"
                        v-bind="item.validate" 
                        ref="multiSelectRef"
                    ></search-select>
                </template>
                <template  v-else-if="item.type==='basic-select'"> 
                     <search-select
                        :keyname="item.keyname"
                        :options="item.options"
                        :selected-option="item.item"
                        :placeholder="item.placeholder? item.placeholder : '请选择'"
                        :id="item.key"
                        :name="item.name"
                        :option-id="item.optionId"
                        :option-name="item.optionName"
                        v-bind="item.validate"  
                        ref="basicSelectRef"
                     ></search-select>
                </template> 
                <template v-else-if="item.type==='select'">   
                      <select-control
                        :keyname="item.keyname" 
                        :id="item.key"
                        :name="item.name"
                        :option-id="item.optionId"
                        :option-name="item.optionName"
                        :options="item.options"
                        :api="item.url"
                        :multiple="item.multiple"
                        :selected-option="datas[item.key]" 
                        :validate="item.validate"  
                        :attr="item.attr"
                        ref="selectRef"
                    ></select-control>
                </template>
                <template  v-else-if="item.type==='radio'">  
                    <radio-control
                        :keyname="item.keyname" 
                        :id="item.key"
                        :name="item.name"
                        :option-id="item.optionId"
                        :option-name="item.optionName"
                        :options="item.options"
                        :api="item.url"
                        :selected-option="datas[item.key]"
                        :validate="item.validate" 
                        v-bind="item.attr"
                         ref="radioRef"
                    ></radio-control>
                </template> 
                <template  v-else-if="item.type==='checkbox'"> 
                    <checkbox-control
                        :keyname="item.keyname" 
                        :id="item.key"
                        :name="item.name"
                        :option-id="item.optionId"
                        :option-name="item.optionName"
                        :options="item.options"
                        :api="item.url"
                        :sel-options="datas[item.key]"
                        :validate="item.validate"  
                         ref="checkboxRef"
                    ></checkbox-control>
                </template>
                <template  v-else-if="item.type==='city'">  
                     <select-province   
                        :keyname="item.keyname" 
                        :input-class="item.inputClass"  
                        :style-object="item.styleObject" 
                        :items="item.items"
                        :option-id="item.optionId"
                        :option-name="item.optionName"
                        :api="item.url"   
                        :parent-id="item.parentId"
                        :validate="item.validate" 
                        :set-province="datas[item.items.province.value]"
                        :set-city="datas[item.items.city.value]"
                        :set-district="datas[item.items.district.value]"  
                        ref="selectProvinceRef"
                    ></select-province>
                </template>
                <template  v-else-if="item.type==='other'">
                    <component :is="item.componentName" :data="item" :values="datas[item.key]"></component>
                </template> 
                <template  v-else>
                    <field 
                        :keyname="item.keyname" 
                        :type="item.type"
                        :placeholder="item.placeholder" 
                        :key-id="item.key" 
                        :name="item.name" 
                        :class="item.inputClass" 
                        :validate="item.validate"  
                        :value="datas[item.key]"
                        v-bind="item.attr"
                        :rows="item.rows" 
                        ref="fieldRef"
                    ></field> 
                </template>
            </div>
            <div class="col-sm-11"><slot name="desc"></slot></div>
            <div class="form-group">
                <div class="col-sm-12" style="text-align:center" :style="[formData.buttonStyle?formData.buttonStyle:'']"> 
                    <lemon-button v-for="btn in formData.buttons" :datas="btn" @click="callfunc(btn.callback)">{{btn.text}}</lemon-button>
                </div>
            </div> 
            <slot></slot>
        </div>
        </form>`,
    props: {
        formData: Object
    },
    data(){
        return {
            datas:{},
            fields:[]
        }
    },
    created(){
        this.fields= _.sortBy(this.formData.fields,  'sort');
    },
    watch:{
        'formData.data':function (val) {
            this.datas=val;
        }
    },
    mounted(){
        var fields=this.fields;
        var that=this;
        this.loadForm();
        for(var i in fields){
            //日期设置
            if(fields[i].type==='date'){
                $('#'+fields[i].key).datepicker({
                    format:  config.dateFormat,
                    autoclose: true,
                    todayHighlight: true
                });
            }
            if(fields[i].type==='daterange'){
                $('.input-daterange').datepicker({
                    format:  config.dateFormat,
                    autoclose: true,
                    todayHighlight: true
                });
            }
            if(fields[i].type==='date-range-mounth'){
                $('.input-daterange').datepicker({
                    format:  config.dateFormat,
                    autoclose: true,
                    todayHighlight: true
                });
            }
        }
        if(that.formData.dataurl){
            httpGet(that, that.formData.dataurl, {} , function (res , err) {//页面加载前调用方法
                if(res){
                    that.datas = $.extend({}, res, that.formData.data) ;
                }
            });
        }else{
            that.datas = that.formData.data?that.formData.data:{};
        }
    },

    methods:{
        callfunc : function(func){
            var that=this;
            var formid=this.formData.id;
            var formArry = $('#'+formid).serializeObject();
            if(func){
                that.$emit(func.action, formArry);
            }
        },
        loadForm: function () {
            var that=this;
            var formid=this.formData.id;
            var api=this.formData.url;
            var message= this.formData.message;
            formValidate('#'+formid, null, function () {
                //序列化form表单数据
                var formArry = $('#'+formid).serializeObject();
                if(api){
                    syncData(api, "post", formArry, function (data, err) {
                        if (data) {
                            console.log('请求成功!');
                        }
                    });
                }
                that.$emit('form-submit', formArry);
            },message);
        },
        clearForm: function () {
            var arr=this.$refs.fieldRef,
                arr1=this.$refs.selectProvinceRef,
                arr2=this.$refs.checkboxRef,
                arr3=this.$refs.radioRef,
                arr4=this.$refs.selectRef,
                arr5=this.$refs.basicSelectRef,
                arr6=this.$refs.multiSelectRef,
                arr7=this.$refs.dateRangeMounthRef,
                arr8=this.$refs.dateRangeRef,
                arr9=this.$refs.rangeRef,
                arr10=this.$refs.imgUploadRef,
                arr11=this.$refs.fileUploadRef;
            arr=arr.concat(arr1).concat(arr2)
                .concat(arr3).concat(arr4)
                .concat(arr5).concat(arr6)
                .concat(arr7).concat(arr8)
                .concat(arr9).concat(arr10).concat(arr11);
            if(arr.length>0){
                for(var i=0; i<arr.length; i++){
                    if(arr[i]){
                        arr[i].handleClear();
                    }
                }
            }
        }
    }
});