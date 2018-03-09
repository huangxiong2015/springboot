/**
 * Created by yikuyi on 2017/1/17.
 */
Vue.component('select-province', {
    template : `<div class="form-group">
        <label :for="id" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{keyname}} <span v-if="validate&&validate.citySel" class="text-red">*</span></label>  
        <div class="col-sm-7 col-md-8 col-lg-9 checkVal" 
                :title="keyname" > 
            <div class="city-picker row">
                <div class="pull-left col-sm-4"  > 
                    <select class="form-control" :class="inputClass" :style="styleObject" v-bind="validate"  v-model="provinceSelected"   :name="nameProvince" :id="idProvince">
                        <option value="" v-text="placeholder.province"></option>
                        <option v-for="item in provinces" :value="item[optionId]" :data-item="item" v-text="item[optionName]"></option>
                    </select>
                </div>
                <div class="pull-left col-sm-4" >
                    <select class="form-control"  :class="inputClass"  :style="styleObject"  v-bind="validate" v-model="citySelected"   :name="nameCity" :id="idCity">
                        <option value="" v-text="placeholder.city"></option>
                        <option v-for="item in cities" :value="item[optionId]" :data-item="item" v-text="item[optionName]"></option>
                    </select>
                </div>
                <div class="pull-left col-sm-4" >
                    <select class="form-control" :class="inputClass"  :style="styleObject"  v-bind="validate" v-model="districtSelected"  :name="nameDistrict" :id="idDistrict">
                        <option value="" v-text="placeholder.district"></option>
                        <option v-for="item in districts" :value="item[optionId]" :data-item="item" v-text="item[optionName]"></option>
                    </select>
                </div>
            </div>
        </div>
    </div> `,
    props : {
        keyname: String,
        inputClass : '',
        styleObject: '' ,
        queryParam:{},
        nameProvince:{
            type:String,
            default:'province'
        },
        nameCity:{
            type:String,
            default:'city'
        },
        nameDistrict:{
            type:String,
            default:'district'
        },
        idProvince:{
            type:String,
            default:'province'
        },
        idCity:{
            type:String,
            default:'city'
        },
        idDistrict:{
            type:String,
            default:'district'
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
        optionId:{
            type:String,
            default:'id'
        },
        optionName:{
            type:String,
            default:'name'
        },
        api :  String,
        setProvince: {
            type:String,
            default(){
                return ''
            }
        },
        setCity: {
            type:String,
            default(){
                return ''
            }
        },
        setDistrict: {
            type:String,
            default(){
                return ''
            }
        },
        parentId:'',
        validate: Object
    },
    data(){
        return {
            current: {
                province : this.setProvince ,
                city : this.setCity,
                district : this.setDistrict,
            },
            sign: 0 ,
            region : [],
            citydata : [],
            districtdata : [],
            id: this.optionId ,
            name: this.optionName ,
            provinceSelected: '' ,
            citySelected: '',
            districtSelected: '',
            param:this.queryParam?this.queryParam:{},
            isLoad:true
        }
    },
    mounted : function () {
        var $this = this,param=this.param;
        var api=$this.getUrl($this.api, param);
        httpGet($this, api, null, function (res , err) {//页面加载前调用方法
            if(res){
                $this.region = res;
                $this.provinceSelected = $this.current.province;
            }
        });
    },
    watch : {
        setProvince:function (val) {
            this.current.province=val;
            this.provinceSelected=val;
        },
        setCity:function (val) {
            this.current.city=val;
            this.citySelected=val;
        },
        setDistrict:function (val) {
            this.current.district=val;
            this.districtSelected=val;
        },
        provinceSelected : function (newVal,oldVal) {
            if(newVal=== undefined){
            	this.provinceSelected = oldVal
            }else{
            	this.citySelected = '';
                this.districtSelected = '';
                this.ajax('citydata', newVal);
                this.changeSelected();
            }
        },
        citySelected: function (val) {
            this.districtSelected = ''
            this.ajax('districtdata', val);
            this.changeSelected();
        },
        districtSelected:function (val) {
            this.changeSelected();
        }
    },
    methods : {
        changeSelected:function(){
            this.$emit('onchange',  {
                province: this.provinceSelected,
                city: this.citySelected,
                district: this.districtSelected
            });
        },
        ajax: function (obj, val) {
            var $this = this,param=this.param;
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
                var api=$this.getUrl($this.api, param);
                httpGet($this, api, null , function (res , err) {//页面加载前调用方法
                    if(res){
                        $this[obj] = res;
                        if(obj=='citydata'){
                            $this.current.province='';
                            $this.citySelected =$this.current.city;
                        }else{
                            $this.current.city='';
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
                var obj={};
                obj[this.optionId]=items[i][this.optionId];
                obj[this.optionName]=items[i][this.optionName];
                result.push(obj);
            }
            return result
        },
        getUrl: function(url, data) {
            var $this = this;
            var retUrl = url.replace(/{([^}]+)}/g,
                function($0, $1) {
                    var val = '';
                    if ($1.indexOf('.') > -1) {
                        val = $this.retVal($1, data);
                    } else {
                        val = data[$1];
                    }
                    return val == null ? '' : val;
                });
            return retUrl;
        },
        _filters: function (id, parms) {
            var val,that=this;
            this[parms].forEach(function (value) {
                if(value[that.optionId]==id){
                    val=value;
                }
            })
            return val;
        },
        getSelected: function () {
            var obj={};
            if(this.provinceSelected){
                obj[this.nameProvince]= this._filters(this.provinceSelected, 'provinces');
            }
            if(this.citySelected) {
                obj[this.nameCity] = this._filters(this.citySelected, 'cities');
            }
            if(this.districtSelected) {
                obj[this.nameDistrict] = this._filters(this.districtSelected, 'districts');
            }
            return obj;
        },
        resetSelected:function () {
            this.provinceSelected='';
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