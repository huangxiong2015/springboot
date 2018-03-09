/**
 * Created by yansha on 2017/7/24.
 */
/**
 * selectBox组件  调用： <select-box :options=[{ids:'1001', names:'文本'}] :optionId='ids' :optionName='names'></select-box>
 * @param validate 检验规则，非必填
 * @param api   获取数据接口，与options二选一
 * @param options   静态选项数据data，与api二选一
 * @param optionId  选项值
 * @param optionName 选项文本
 * @param selected 默认选中，非必填
 * @param id 输入框ID，必填
 * @param checkName 多选框名称，必填
 * @param placeholder 占位提示，非必填
 * @param otherChekVal 其他选项值
 * @param otherInputId 其他选项input的ID
 * @param otherInputName 其他选项input的Name
 * @since 2017-07-24
 * @author yansha@yikuyi.com
 */

Vue.component('select-box', {
    template: `<div class="form-group">
        <label :for="id" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>  
        <div class="col-sm-7 col-md-8 col-lg-9 checkVal"  :title="keyname"  > 
            <div class="select-down form-control" @click="showbox=!showbox"> 
                <span :if="selected && selected.length>0">{{rePlaceholder}}</span>
                <span :if="!selected || selected.length==0">{{placeholder}}</span>
                <i class="icon-triangle-downmin"></i>
            </div> 
            <div class="select-area">
                <div v-for="ele in selectArr" class="area-item" @click="del(ele)"> 
                    <span > {{ele[optionName]}}</span> 
                    <i class="icon-close_min close"></i> 
                </div> 
                <input type="text" style="visibility: hidden;height: 0" :value="selects" v-bind="validate" :name="name" :id="id" /> 
            </div>
            <div class="sel-box" v-show="showbox">  
                <div class="checkbox inline"> 
                    <span class="check-span" v-for="el in datas"> 
                        <input type="checkbox" class="chk_ input_check" v-model="selects" :value="el[optionId]" :id="id+ el[optionId]" v-bind="validate" />
                        <label v-bind="{for: id +  el[optionId] }" ></label> {{el[optionName]}}
                        <input v-if="el[optionId]===otherChekVal" v-show="showInput" type="text" class="input-sm" v-model="otherValue" v-bind="otherAttr" /> 
                    </span> 
                </div> 
                <div class="industry_btn mt20 tc" style="text-align: center;"> 
                    <button type="button" class="btn ml10 mr10" @click="checkBtn">确认</button>
                    <button type="button" class="btn ml10 mr10" @click="concleBtn">取消</button> 
                </div> 
            </div> 
            <em class="focustip"></em> 
        </div>
    </div>`,
    props: {
        keyname: '',
        validate: Object,
        options: Array,
        optionId: '',
        optionName: '',
        selected: Array,
        api: '',
        id: '',
        name: '',
        rePlaceholder: '',
        placeholder: '',
        showInput: Boolean,
        otherChekVal: '',
        otherAttr: {
            id: '',
            name: '',
        }
    },
    data(){
        return {
            selecteds: this.selected ? this.selected : [],
            selects: this.selected ? this.selected : [],
            selectArr:[],
            showbox: false,
            otherValue: '',
            datas: this.options ? this.options : []
        }
    },
    mounted(){
        if(this.api){
            this.loadtype();
        }
    },
    watch: {
        selected: function (val) {
            this.selects=val;
            this.selecteds=val;
            this.retArr(val , this.datas);
        },
        selecteds: function (val) {
            this.retArr(val , this.datas);
        },
        selects: function (val) {
            var ckVal=this.otherChekVal;
            if(val.indexOf(ckVal)>-1){
                this.showInput=true;
            }else{
                this.otherValue='';
                this.showInput=false;
            }
        },
        datas: function (val) {
            this.retArr(this.selecteds , val);
        }
    },
    methods: {
        loadtype : function() {
            var that=this;
            httpGet(that, that.api, {} , function (res , err) {//页面加载前调用方法
                if(res){
                    that.datas = res;
                }
            });
        },
        del: function (obj) {
            this.selectArr = _.dropWhile(this.selectArr, obj);
            this.selecteds = _.without(this.selecteds, obj[this.optionId]);
            this.selects = this.selecteds;
        },
        checkBtn: function () {
            var val=this.otherChekVal;
            if(this.selects.indexOf(val)>-1 && this.otherValue===''){
                $('#otherText').css('border-bottom','#b11919 solid 1px');
                return false;
            }
            this.selecteds = this.selects;
            this.showbox = !this.showbox;
        },
        concleBtn: function () {
            this.showbox = !this.showbox;
            var val=this.otherChekVal;
            this.selects = this.selecteds;
        },
        retArr: function (arr, obj) {
            var arrs=[],
                that=this,
                val=this.otherChekVal;
            this.selectArr=[];
            for(var i in arr){
                for(var j in obj){
                    if(arr[i]===obj[j][that.optionId] && arr[i]===val){
                        var data={},text=obj[j][that.optionName];
                        data[that.optionId]=obj[j][that.optionId];
                        data[that.optionName]=text +'（'+that.otherValue+'）';
                        arrs.push(data);
                    }else if(arr[i]===obj[j][that.optionId]){
                        arrs.push(obj[j]);
                    }
                }
            }
            this.selectArr=arrs;
        }
    }
});