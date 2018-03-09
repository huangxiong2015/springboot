/**
 * Created by licaibo on 2017/4/12.
 */
Vue.component('basic-select', {
    template: ` <div>
         <select class="form-control select2-hidden" tabindex="-1" aria-hidden="true" :id="id" :name="name"  :required="required" >
                <option value="" v-text="inputText"></option>
                <option v-for="(option, idx) in options" :value="option.value" :selected="selOption.value==option.value">{{option.text}}</option>  
         </select>
         <div class="ui fluid search selection dropdown"
               :class="{ 'active visible':showMenu }" @click="openOptions">
            <i class="dropdown icon"></i> 
            <input class="search" autocomplete="off" tabindex="0" v-model="searchText" ref="searchInput"
                   @blur="blurInput" 
                   @keydown.up="prevItem"
                   @keydown.down="nextItem"
                   @keyup.enter="enterItem"
                   @keydown.delete="deleteTextOrItem"
            />
            <div class="text" :class="textClass">{{inputText}}</div>
            <div class="menu" :class="menuClass" :style="menuStyle" tabindex="-1">
              <template v-for="(option, idx) in filteredOptions">
                <div class="item" :class="{ 'selected': option.selected }"
                     @click.stop="selectItem(option)"
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
        required:''
    },
    data () {
        return {
            showMenu: false,
            searchText: '',
            mousedownState: false, // mousedown on option menu
            selOption: this.selectedOption
        }
    },
    watch:{
        selOption : function (val) {
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
                if (this.selOption.text) {
                    text = this.selOption.text
                }
                //return text
            }
            return text
        },
        textClass () {
            if (!this.selOption.text && this.placeholder) {
                return 'default'
            } else {
                return ''
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
        filteredOptions () {
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
            if (!this.searchText && this.selOption) {
                this.selectItem({})
                this.openOptions()
            }
        },
        // cursor on input
        openOptions () {
            this.$refs.searchInput.focus()
            this.showMenu = true
            this.mousedownState = false
        },
        blurInput () {
            common.blurInput(this)
        },
        closeOptions () {
            common.closeOptions(this);
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
            this.searchText = ''; // reset text when select item
            this.closeOptions();
            this.selOption=option;
           // this.$emit('select', option);
        }
    }
})
