/**
 * Created by yikuyi on 2017/5/8.
 */

/**
 * 文件上传组件
 */
Vue.component('file-upload', {
    template: `<div>
            <input class="file-btn " :class="{ 'pos-abs': isImage=='true' , isImage}" type="button" value="上传文件" :id="buttonId" > 
            <div class="manu_logo" > 
               <span v-if="isImage=='true'"> 
                    <img alt="" :src="attachUrl ? attachUrl : '/dist/img/defaultFace.jpg'" /> 
                    <a @click="del" href="javascript:void(0);">删除</a> 
               </span> 建议上传120*44像素，支持jpg、jpeg、png、gif，文件大小不超过5MB
             </div> 
            <input type="text" v-model="attachUrl" :id="fileUrl" :name="fileUrl"  style="visibility: hidden;height:0;" :required="required"> 
        </div>`,
    props:{
        url: String,
        buttonId:String,
        fileUrl:String,
        uploadType:String,
        types:String,
        fileSize:String,
        isImage:String,
        required:String,
        class:String
    },
    data(){
        return{
            attachUrl: "",
            isEdit:true,
            uploader: this.buttonId + 'Upload'
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
        }
    }
});
/**
 * 可搜索选择
 */
Vue.component('serch-select', {
    template: `<div> 
       <select  v-if="selectType=='multi'" class="form-control select2-hidden" tabindex="-1" aria-hidden="true" :id="id" :name="name"  :required="required" v-model="selectedOption.value" >
            <option value="" v-text="inputText"></option>
            <option v-for="(option, idx) in options" :value="option.value">{{option.text}}</option>  
       </select>
       <select v-else class="form-control select2-hidden" :class="{'required':required}" multiple="" tabindex="-1" aria-hidden="true" :id="id" :name="name" > 
            <option value="" v-text="inputText"></option>
            <template v-for="(option, idx) in options">
                <option v-if="selectedOptions.length>0" v-for="(selected, idx) in selectedOptions" :value="option.value" :selected="selected.value == option.value">{{option.text}}</option> 
                <option v-if="selectedOptions.length <= 0" :value="option.value" >{{option.text}}</option>  
            </template>
       </select> 
       <div  v-if="selectType=='multi'" class="ui fluid multiple search selection dropdown"
           :class="{ 'active visible':showMenu }"
           @click="openOptions">
            <i class="dropdown icon"></i>
            <template v-for="(option, idx) in selectedOptions">
              <a class="ui label transition visible" style="display: inline-block !important;">
                {{option.text}}<i class="fa fa-close" @click="deleteItem(option)"></i>
              </a>
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
            <div class="text" :class="textClass">{{inputText}}</div>
            <div class="menu" :class="menuClass" :style="menuStyle" tabindex="-1">
                 <template v-for="(option, idx) in filteredOptions">
                    <div class="item" :class="{ 'selected': option.selected }"
                         @click="selectItem(option)"
                         @mousedown="mousedownItem">
                      {{option.text}}
                    </div>
                 </template>
            </div>
       </div>
        <div v-else class="ui fluid search selection dropdown"
           :class="{ 'active visible':showMenu }" @click="openOptions">
            <i class="dropdown icon"></i> 
            <input class="search" autocomplete="off" tabindex="0" 
                    v-model="searchText" 
                    ref="searchInput"
                   @blur="blurInput" 
                   @keydown.up="prevItem"
                   @keydown.down="nextItem"
                   @keyup.enter="enterItem"
                   @keydown.delete="deleteTextOrItem"
            />
            <div class="text" :class="textClass">{{inputText}}</div>
            <div class="menu" :class="menuClass" :style="menuStyle" tabindex="-1">
              <template v-for="(option, idx) in filteredOption">
                <div class="item" :class="{ 'selected': option.selected }"
                     @click.stop="selectedItem(option)"
                     @mousedown="mousedownItem">
                  {{option.text}}
                </div>
              </template>
            </div>
       </div>
    </div>`,
    props: {
        'options': {
            type: Array
        },
        'selectedOptions': {
            type: Array,
            default: () => { return [] }
        },
        'selectedOption': {
            type: Object,
            default: () => { return { value: '', text: '' } }
        },
        'isError': {
            type: Boolean,
            default: false
        },
        placeholder: {
            type: String,
            default: ''
        },
        id : '',
        name : '',
        required:'',
        class:'',
        selectType:''
    },
    data () {
        return {
            showMenu: false,
            searchText: '',
            mousedownState: false // mousedown on option menu
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
                if (this.selectedOption.text) {
                    text = this.selectedOption.text
                }
                //return text
            }

            return text
            /*    if (this.searchText) {
             return ''
             } else {
             return this.placeholder
             }*/
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
            return _.differenceBy(this.options, this.selectedOptions, 'value')
        },
        filteredOptions () {
            if (this.searchText) {
                return this.nonSelectOptions.filter(option => {
                    return option.text.match(new RegExp(this.searchText, 'i'))
                })
            } else {
                return this.nonSelectOptions
            }
        },
        filteredOption () {
            if (this.searchText) {
                return this.options.filter(option => {
                    return option.text.match(new RegExp(this.searchText, 'i'))
                })
            } else {
                return this.options
            }
        }
    },
    methods: {
        deleteTextOrItem () {
            if (!this.searchText && this.selectedOption) {
                this.selectItem({})
                this.openOptions()
            }
        },
        deleteTextOrLastItem () {
            if (!this.searchText && this.selectedOptions.length > 0) {
                this.deleteItem(_.last(this.selectedOptions))
            }
        },
        // cursor on input
        openOptions () {
            if(this.type == 'multi'){
                this.$refs.input.focus()
            }else{
                this.$refs.searchInput.focus()
            }
            this.showMenu = true
            this.mousedownState = false

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
        selectedItem (option) {
            this.searchText = ''; // reset text when select item
            this.closeOptions();
            this.$emit('select', option);
        },
        selectItem (option) {
            const selectedOptions = _.unionWith(this.selectedOptions, [option], _.isEqual)
            this.closeOptions()
            this.$emit('select', selectedOptions, option)
        },
        deleteItem (option) {
            const selectedOptions = _.reject(this.selectedOptions, option)
            this.$emit('select', selectedOptions, option)
        }
    }
});
/**
 * 选择
 */
Vue.component('select-control', {
    template : `
        <select  :style="styleObject" :class="'form-control ' + inputClass" v-model="ctlSelected" @change="change($event.target.value)"  :name="name" :id="id" :required="required">
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
        placeholder : {
            type : Object,
            default(){
                return {
                    option : '请选择'
                }
            }
        },
        api :  String,
        selectedValue:'',
        required:''
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
                $this.ctlSelected = $this.selectedValue ? $this.selectedValue:'';
            }
        });
    },
    methods : {
        change : function (value) {
            this.ctlSelected= value;
        },
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
/**
 * 城市组件
 */
Vue.component('select-province', {
    template : `
        <div class="city-picker row">
            <div class="pull-left col-sm-4"  >
                <select :class="inputClass" :style="styleObject" v-model="provinceSelected" @change="change"  :name="nameProvince ? nameProvince : 'province'" :id="idProvince ? idProvince : 'province'">
                    <option value="" v-text="placeholder.province"></option>
                    <option v-for="item in provinces" :value="item[0]" v-text="item[1]"></option>
                </select>
            </div>
            <div class="pull-left col-sm-4" >
                <select :class="inputClass"  :style="styleObject" v-model="citySelected" @change="change"  :name="nameCity ? nameCity : 'city'" :id="idCity ? idCity : 'city'">
                    <option value="" v-text="placeholder.city"></option>
                    <option v-for="item in cities" :value="item[0]" v-text="item[1]"></option>
                </select>
            </div>
            <div class="pull-left col-sm-4" >
                <select :class="inputClass"  :style="styleObject" v-model="districtSelected"  @change="change" :name="nameDistrict ? nameDistrict :'district'" :id="idDistrict ? idDistrict : 'district'">
                    <option value="" v-text="placeholder.district"></option>
                    <option v-for="item in districts" :value="item[0]" v-text="item[1]"></option>
                </select>
            </div>
        </div>
        `,
    props : {
        inputClass : '',
        styleObject: '' ,
        parentId : '',
        nameProvince:'',
        nameCity:'',
        nameDistrict:'',
        idProvince:'',
        idCity:'',
        idDistrict:'',
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
            districtSelected:'',
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
        setProvince:function () {
            this.current.province=this.setProvince;
        },
        setCity:function () {
            this.current.city=this.setCity;
        },
        setDistrict:function () {
            this.current.district=this.setDistrict;
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
 *
 */
Vue.component('input-control', {
    template: `<div class="col-sm-11"  v-bind:class="data.rowCss ? data.rowCss : classObject ? {'col-md-6 col-lg-6': classObject.isTwo,'col-sm-6 col-md-6 col-lg-4': classObject.isThree,'col-sm-6 col-md-4 col-md-3': classObject.isFour} : ''" > 
         <div v-if="data.type=='text'" class="form-group"> 
            <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>   
             <div class="col-sm-7 col-md-8 col-lg-9"> 
             <input 
                 :type="data.type" 
                 :id="data.key" 
                 :name="data.name" 
                 :placeholder="data.keyname" 
                 :required="data.required" 
                 class="form-control" 
                 :class="data.inputClass" 
                 v-bind="data.validateStr"
                 />
             </div>
        </div> 
        <div v-else-if="data.type=='textarea'" class="form-group"> 
            <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>  
            <div class="col-sm-7 col-md-8 col-lg-9"><textarea rows="5" :placeholder="data.keyname" :id="data.key" :name="data.name" :required="data.required" :class="'form-control ' + data.inputClass"> </textarea> </div>
        </div>
        <div v-else-if="data.type== 'radio'" class="form-group"> 
            <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>
            <div class="col-sm-7 col-md-8 col-lg-9" :id="index" >
                <div class="radio inline" v-for="(item, i)  in data.options" >
                  <span v-if="i==0" class="radio-span"><input type="radio" class="input_radio" :name="data.name"  :id="item.value + '_' + i"  :value="item.value"  :required="data.required "  v-bind="data.validateStr"  checked /> <label :for="item.value + '_' + i"></label> {{item.text}}</span>
                  <span  v-else class="radio-span"><input type="radio" class="input_radio"  :name="data.name"  :id="item.value + '_' + i"  :value="item.value"  /> <label :for="item.value + '_' + i"></label> {{item.text}}</span>
                </div> 
            </div>
        </div>
         <div v-else-if="data.type== 'checkbox'" class="form-group"> 
              <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>
              <div class="col-sm-7 col-md-8 col-lg-9 checkVal" >  
                  <div class="checkbox inline" v-for="(item, i) in data.options" >
                      <span  v-if="i==0" class="check-span">
                          <input type="checkbox" :name="data.name" :value="item.value" :id="data.key + '_' + i"  class="input_check" :required="data.required" v-bind="data.validateStr" /> <label :for="data.key + '_' + i"></label> {{item.text}}
                      </span>
                       <span   v-else class="check-span">
                          <input type="checkbox" :name="data.name"  :value="item.value"  :id="data.key + '_' + i" class="input_check"/> <label :for="data.key + '_' + i"></label> {{item.text}}
                      </span>
                  </div> 
              </div>
         </div>
         <div v-else-if="data.type== 'date'" class="form-group"> 
          <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>
              <div class="col-sm-7 col-md-8 col-lg-9" >  
                  <div class="input-group date" v-if="data.inputClass=='pull-right'">
                        <div class="input-group-addon">
                          <i class="fa fa-calendar"></i>
                        </div>
                        <input type="text"  class="form-control" :class="data.inputClass" :id="data.key" :name="data.name"  :required="data.required" />
                  </div>
                  <div class="input-group date" v-else> 
                      <input type="text"  class="form-control" :class="data.inputClass" :id="data.key" :name="data.name"  :required="data.required" />
                      <div class="input-group-addon">
                        <i class="fa fa-calendar"></i>
                      </div>
                  </div>  
              </div>
         </div> 
         <div v-else-if="data.type== 'range'" class="form-group"> 
              <label :for="data.createStart" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>
              <div class="col-sm-7 col-md-8 col-lg-9" >  
                    <div  class="input-group">
                        <input type="text" :id="data.startkey" :name="data.startname" :required="data.required" class="form-control" :class="data.inputClass" />
                         <span class="input-group-addon">至</span> 
                         <input type="text" :id="data.endkey" :name="data.endname" :required="data.required" class="form-control" :class="data.inputClass" />
                    </div>
              </div>
         </div>
         <div v-else-if="data.type== 'daterange'" class="form-group"> 
              <label :for="data.createStart" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>
              <div class="col-sm-7 col-md-8 col-lg-9" >  
                    <div  class="input-daterange input-group">
                        <input type="text" :id="data.startkey" :name="data.startname" :required="data.required" class="form-control" :class="data.inputClass" />
                         <span class="input-group-addon">至</span> 
                         <input type="text" :id="data.endkey" :name="data.endname" :required="data.required" class="form-control" :class="data.inputClass" />
                    </div>
              </div>
         </div>
         <div v-else-if="data.type== 'multi-select'"  class="form-group"> 
            <serch-select 
                :options="data.options"
                :selected-options="data.items"
                placeholder="请选择"
                @select="onSelect"
                :id="data.key"
                :name="data.name"
                :required="data.required"
                :select-type="multi"
                v-bind="data.validateStr" 
            ></serch-select>
         </div>
         <div v-else-if="data.type== 'basic-select'"  class="form-group"> 
            <serch-select
                :options="data.options"
                :selected-options="data.items"
                placeholder="请选择" 
                :id="data.key"
                :name="data.name"
                :required="data.required" 
                v-bind="data.validateStr" 
            ></serch-select>
         </div>
         <div v-else-if="data.type== 'city'" class="form-group"> 
              <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>
              <div class="col-sm-7 col-md-8 col-lg-9" :id="index">   
                    <select-province   
                        :input-class="data.inputClass"  
                        :style-object="data.styleObject" 
                        :name-province="data.nameProvince" 
                        :name-city="data.nameCity"
                        :name-district="data.nameDistrict"
                        :id-province="data.idProvince" 
                        :id-city="data.idCity"
                        :id-district="data.idDistrict"
                        :api="data.url"
                        :set-province="data.setProvince"
                        :set-city="data.setCity"
                        :set-district="data.setDistrict"
                        :required="data.required"
                        v-bind="data.validateStr" 
                    ></select-province>
              </div>
         </div>
         <div v-else-if="data.type== 'select'" class="form-group"> 
              <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label">{{data.keyname}}<span v-if="data.required" class="text-red">*</span></label> 
              <div v-if="data.url" class="col-sm-7 col-md-8 col-lg-9"> 
                   <select-control
                      :input-class="data.inputClass"
                      :id="data.key"
                      :name="data.name"
                      :option-id="data.optionId"
                      :option-name="data.optionName"
                      :api="data.url"
                      :required="data.required"
                      v-bind="data.validateStr" 
                  ></select-control>
                
              </div>
              <div v-else  class="col-sm-7 col-md-8 col-lg-9"> 
                  <select  
                        :id="data.key"
                        :name="data.name"
                        :required="data.required" 
                        :class="'form-control ' + data.inputClass"
                        :multiple="data.isMultiple=='true'"
                        v-bind="data.validateStr" >
                        <option v-for="item in data.options" :value="item.value" >{{item.text}} </option>
                  </select>  
              </div>
         </div>  
          <div v-else-if="data.type== 'file'" class="form-group"> 
              <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label">{{data.keyname}}<span v-if="data.required" class="text-red">*</span></label>
              <div class="col-sm-7 col-md-8 col-lg-9"> 
                    <file-upload  
                        :file-url= "data.key"
                        :button-id= "data.config.buttonId"
                        :upload-type= "data.config.uploadType"
                        :url= "data.config.url"
                        :types= "data.config.types"
                        :file-size= "data.config.fileSize"
                        :is-image= "'false'"
                        :required="data.required"
                        :class="data.inputClass"
                    ></file-upload>
              </div>
         </div> 
          <div v-else-if="data.type== 'image'" class="form-group"> 
              <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label">{{data.keyname}}<span v-if="data.required" class="text-red">*</span></label>
              <div class="col-sm-7 col-md-8 col-lg-9"> 
                <file-upload
                    :file-url= "data.key"
                    :button-id= "data.config.buttonId"
                    :upload-type= "data.config.uploadType"
                    :url= "data.config.url"
                    :types= "data.config.types"
                    :file-size= "data.config.fileSize"
                    :is-image= "'true'" 
                    :required="data.required"
                    :class="data.inputClass"
                ></file-upload> 
              </div>
         </div> 
          <div v-else-if="data.type== 'button' || data.type== 'submit'"> 
               <div :style="'text-align:'+ data.align" > 
                <input :type="data.type" 
                       :id="data.key"
                       :name="data.name"  
                       :value="data.keyname"
                       :class="data.inputClass"/> 
                </div>
         </div>
         <div v-else class="form-group"> 
               <label :for="data.key" class="col-sm-4 col-md-4 col-lg-2 control-label"> {{data.keyname}} <span v-if="data.required" class="text-red">*</span></label>   
               <div class="col-sm-7 col-md-8 col-lg-9">  
                   <input  
                       :type="data.type" 
                       :id="data.key"
                       :name="data.name"
                       :placeholder="data.keyname" 
                       :required="data.required" 
                       v-bind="data.validateStr" 
                       class="form-control" 
                       :class="data.inputClass"  
                   /> 
               </div>
         </div>
    </div>`,
    props: {
        data: Object,
        index: Number,
        classObject: Object
    },
    computed: {
        // a computed getter
        isError () {
            return this.items.length === 0
        }
    },
    methods: {
        onSelect (items, lastSelectItem) {
            this.items = items
            this.lastSelectItem = lastSelectItem
        }
    }
});

Vue.component('control', {
    template: `<div id="main" >
        <div  class="form-group ">
            <input-control v-for="(item, index) in jsonData" :data="item" :index="index" :class-object="classObject"></input-control>
        </div>
    </div>`,
    props: {
        jsonData : Array,
        classObject: Object
    }
});