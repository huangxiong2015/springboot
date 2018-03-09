/**
 * Created by licaibo on 2017/4/12.
 */
Vue.component('sel-option', {
    template: `<option :value="setOptions[optionId]" :selected="setOptions.selected">{{setOptions[optionName]}}</option>  `,
    props:{
        option:Object,
        selectedOptions: Array,
        optionId: String,
        optionName: String
    },
    computed:{
        setOptions : function(){
            var data = this.option;
            var datas = this.selectedOptions;
            var $this=this;
            delete data.selected;
            for( var i in datas){
                if(data[$this.optionId] === datas[i][$this.optionId] || data[$this.optionId] === datas[i] ){
                    data.selected=true;
                }
            }
            return data;
        }
    }

})

Vue.component('multi-select', {
    template: `<div>  
       <select class="form-control select2-hidden" v-bind="validate" multiple="multiple" tabindex="-1" aria-hidden="true" :id="id" :name="name" > 
            <option value="" v-text="inputText"></option>
            <template v-for="(option, idx) in retOptions">
                <sel-option :option="option" :selected-options="selOptions" :option-id="optionId" :option-name="optionName"></sel-option>
            </template>
          </select>
            
     <div class="ui fluid multiple search selection dropdown"
           :class="{ 'active visible':showMenu }"
           @click="openOptions">
        <i class="dropdown icon"></i>
        <template v-for="(sel, idx) in selOptions">
             <template v-for="(option, idx) in retOptions" v-if="option[optionId] === sel">
                  <a class="ui-label transition visible" style="display: inline-block !important;">
                      {{option[optionName]}}<i class="fa fa-close" @click="deleteItem(option)"></i>
                  </a>
             </template> 
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
              {{option[optionName]}}
            </div>
          </template>
        </div>
      </div>
    </div>`,
    props: {
        'options': {
            type: Array
        },
        'api': {
            type: String
        },
        'selectedOptions': {
            type: Array,
            default: () => { return [] }
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
        optionId: String,
        optionName: String,
        validate:Object 
    },
    data () {
        return {
            showMenu: false,
            searchText: '',
            mousedownState: false, // mousedown on option menu
            retOptions : this.options,
            selOptions:  this.selectedOptions
        }
    },
    created : function () {
        var $this = this;
        httpGet($this, $this.api, {} , function (res , err) {//页面加载前调用方法
            if(res){
                $this.retOptions = res;
            }
        });
    },
    watch:{
        selectedOption : function (val) {
            var that=this;
            var blu = function () {
                $("#" + that.id).blur();
            }
            setTimeout(blu, 500);
        },
        selectedOptions: function (val) {
            var that=this;
            that.selOptions=val;
            var blu = function () {
                $("#" + that.id).blur();
            }
            setTimeout(blu, 500);
        }
    },
    computed: {
        inputText () {
            if (this.searchText) {
                return ''
            } else {
                return this.placeholder
            }
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
            _.forEach(this.selOptions, function(value, index) {
                var data={};
                data[$this.optionId]=value;
                rets.push(data);
            });
            return _.differenceBy(this.retOptions, rets, this.optionId)
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
        selectItem (option) {
            var opt=option[this.optionId];
            const selectedOptions = _.unionWith(this.selOptions, [opt], _.isEqual);
            this.closeOptions();
            this.selOptions= selectedOptions;
            //this.$emit('select', selectedOptions, option)
        },
        deleteItem (option) {
            var opt=option[this.optionId];
            var arr=this.selOptions;
            arr.splice($.inArray(opt,arr),1)
            //const selectedOptions = this.selOptions.remove(opt);
            this.selOptions= arr;
            //this.$emit('select', selectedOptions, option)
        }
    }
});