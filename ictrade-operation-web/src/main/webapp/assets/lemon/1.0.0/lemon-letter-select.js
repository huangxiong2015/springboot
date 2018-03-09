/**
 * Created by yansha on 2017/7/26.
 */
/**
 * letter-select组件  调用： <letter-select
 :keyname="keyname"
 :validate="validate"
 :options="options"
 :id="id"
 :name="name"
 :option-id="optionId"
 :option-name="optionName"
 :multiple="multiple"
 :options="options"
 :show-search="showSearch"
 :show-letter="showLetter"
 :is-fuzzy-search="isFuzzySearch"
 :reload-api="reloadApi"
 :reload-req-data="reloadReqData"
 @get-selecteds="getLetterSelected"
 ></letter-select>
 * @param keyname 字段名
 * @param validate 检验规则，非必填
 * @param api   获取数据接口，与options二选一
 * @param options   静态选项数据data，与api二选一
 * @param optionId  选项值
 * @param optionName 选项文本
 * @param selected   [] 默认选中，非必填
 * @param id 输入框ID，必填
 * @param name 输入框name，如选择模糊搜索时必填，需与模糊搜索参数名保持一致
 * @param placeholder 占位提示，非必填
 * @param showSearch  显示搜索框
 * @param showLetter 显示字母
 * @param isFuzzySearch 是否支持模糊搜索
 * @param reloadApi 模糊搜索接口地址，如isFuzzySearch为true，该项必填 如 http://192.168.1.110:27083/v1/products/brands/alias?keyword={keyword},{}内的参数需与输入框的name保持一致 
 * @param reloadType 模糊搜索请求方式，非必填，默认GET
 * @param reloadReqData 模糊搜索请求数据，非必填
 * @method get-selecteds 方法，返回选中值
 * @method multiple   是否复选
 * @since 2017-07-26
 * @author yansha@yikuyi.com
 */

Vue.component('letter-select', {
    template: `<div class="form-group">         
        <div class="checkVal"  :title="keyname"  >      	
            <div class="pinyin-box">
            	<div class="pinyin-h">
	            	<label :for="id" class="control-label"> {{keyname}} <span v-if="validate&&validate.required" class="text-red">*</span></label>
	            	<span class="search-box" v-if="showSearch"><input type="text" v-model="searchText" :placeholder="placeholder" /></span>
	                <input type="hidden" :id="id" :name="name" v-model="selecteds"  />
            	</div>
                <div class="pinyin-t" v-if="showLetter">
                    <span><a href="javascript:void(0)" v-on:mouseover="selects()" :class="{'hover':returnHover('')}">全部</a></span>
                    <span v-for="item in letterList">
                        <a href="javascript:void(0)" v-on:mouseover="returnObject(item.name)" :class="{'hover':returnHover(item.name)}" v-if="item.hasList">{{item.name === 'other'?'其他':item.name}}</a>
                        <a href="javascript:void(0)" class="disabled" v-if="!item.hasList">{{item.name === 'other'?'其他':item.name}}</a>
                    </span>
                </div>
                <div class="pinyin-c">
                    <div class="con"> 
                        <span v-if="filteredOption && filteredOption.length > 0 && isCheckedall"><a href="javascript:void(0)" @click="letterSelectedAll(filteredOption)"> 不限</a> </span> 
                        <span v-for="el in filteredOption" v-if="filteredOption && filteredOption.length"><a href="javascript:void(0)" @click="getLetterSelected(el[optionId])" :class="{'active':returnActive(el[optionId])}" v-text="returnName(optionName,el)"></a> </span>
                        <div v-if="filteredOption && !filteredOption.length && !isLoading" style="text-align: center;line-height: 100px;">暂无数据</div>
                        <div v-if="isLoading" style="text-align:center;margin-top:30px;"><img :src="loadImgUrl"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>`,
    props: {
        keyname: '',
        validate: Object,
        api: String,
        options: {
            type: Array,
            default () {
                return []
            }
        },
        showLetter:{
            type: Boolean,
            default () {
                return true
            }
        },
        showSearch:{
            type: Boolean,
            default () {
                return true
            }
        },
        id: '',
        name: '',
        optionId: '',
        optionName: '',
        selected: [String, Array],
        multiple: {
            type: Boolean,
            default: false
        },
        placeholder: {
            type: String,
            default: '搜索'
        },
        isCheckedall: {
            type: Boolean,
            default: false
        },
        requestType: {
        	type: String,
        	default: 'GET'
        },
        requestData: {
        	type: Object,
        	default: null
        },
        reloadApi: {
        	type: String,
        	default :''
        },
        reloadType: {
        	type: String,
    		default: 'GET'
        	
        },
        reloadReqData: {
        	type: Object,
    		default: null
        },
        isFuzzySearch: {
        	type: Boolean,
        	default: false
        }
    },
    data() {
        return {
            letterList: [{
                name: 'A',
                hasList: false,
            }, {
                name: 'B',
                hasList: false,
            }, {
                name: 'C',
                hasList: false,
            }, {
                name: 'D',
                hasList: false,
            }, {
                name: 'E',
                hasList: false,
            }, {
                name: 'F',
                hasList: false,
            }, {
                name: 'G',
                hasList: false,
            }, {
                name: 'H',
                hasList: false,
            }, {
                name: 'I',
                hasList: false,
            }, {
                name: 'J',
                hasList: false,
            }, {
                name: 'K',
                hasList: false,
            }, {
                name: 'L',
                hasList: false,
            }, {
                name: 'M',
                hasList: false,
            }, {
                name: 'N',
                hasList: false,
            }, {
                name: 'O',
                hasList: false,
            }, {
                name: 'P',
                hasList: false,
            }, {
                name: 'Q',
                hasList: false,
            }, {
                name: 'R',
                hasList: false,
            }, {
                name: 'S',
                hasList: false,
            }, {
                name: 'T',
                hasList: false,
            }, {
                name: 'U',
                hasList: false,
            }, {
                name: 'V',
                hasList: false,
            }, {
                name: 'W',
                hasList: false,
            }, {
                name: 'X',
                hasList: false,
            }, {
                name: 'Y',
                hasList: false,
            }, {
                name: 'Z',
                hasList: false,
            }, {
                name: 'other',
                hasList: false,
            }],
            style: 'STYLE_FIRST_LETTER',
            retData: {},
            retobj: [],
            sel: this.selected,
            selecteds: [],
            active: false,
            char: '',
            dts: this.options ? this.options : [],
            searchText: '',
            initOption: this.options ? JSON.parse(JSON.stringify(this.options)) : [],
    		loadImgUrl:ykyUrl._this + '/images/loading.gif',
            reloadEnd:true,
            isLoading:false
        }
    },
    watch: {
        options: function(val) {
            this.dts = val;
        },
        selected: function(val) {
            this.sel = val;
        },
        dts: function(val) {
            this.retobj = val;
            this._filter(val);
        },
        sel: function(val) {
            var sels = this.findById(this.sel);
            this.selecteds = sels;
            this.changeLetterSelected(sels);
        },
        searchText: function(val){
        	if(this.isFuzzySearch){
        		this.selects();
        		if(val){
        			this.reloadEnd = false;
                	this.isLoading = true;
        		}else{
        			this.dts = JSON.parse(JSON.stringify(this.initOption));
        		}
        	}
        }
    },
    computed: {
        filteredOption() {
        	var name = this.optionName;
            if (this.retobj && !this.searchText) {
            	return this.retobj
            } else if (this.retobj && this.searchText){
            	if (!this.reloadEnd && this.isFuzzySearch) {
            		this.reloadData()
            	} else {
            		var searchText = this.isFuzzySearch ? '' : this.searchText;
            		return this.retobj.filter(option => {
                    	return option[name].match(new RegExp(searchText, 'i'))
                    })
            	}
            } else {
                return this.retobj
            }
        }
    },
    mounted() {
        if (this.api) {
            this.loadtype();
            this.isLoading = true;
        } else {
            this.retobj = this.dts;
            this._filter(this.dts);
        }
    },
    methods: {
        letterReload: function() {
            this.searchText = '';
            this.selects();
        },
        changeLetterSelected: function(data) {
            this.$emit('get-selecteds', data);
        },
        loadtype: function() {
            var that = this;
            syncData(that.api, that.requestType, that.requestData, function(res, err){
            	if(res){
            		that.isLoading = false;
            		if(Array.prototype.isPrototypeOf(res)){
            			that.dts = res;
            			that.initOption = JSON.parse(JSON.stringify(res));
            		}else{
            			that.dts = res.list;
            			that.initOption = JSON.parse(JSON.stringify(res.list));
            		}
            	}
            })
        },
        letterSelectedAll(arr) {
            var that = this;
            arr.forEach(function(item) {
                var flag = false;
                that.selecteds.forEach(function(el) {
                    if (el.ids === item.ids) {
                        flag = true;
                    }
                })
                if (!flag) {
                    that.getLetterSelected(item[that.optionId])
                }
            })
        },
        getLetterSelected: function(obj) {
            var sels = [];
            if (this.multiple === true) {
                var index = _.indexOf(this.sel, obj);
                if (index > -1) {
                    this.sel.splice(index, 1);
                } else {
                    sels = _.unionWith(this.sel, [obj], _.isEqual);
                    this.sel = sels;
                }
            } else {
                this.sel = obj;
            }
        },
        del: function(obj) {
            var index = _.indexOf(this.sel, obj);
            this.sel.splice(index, 1);
        },
        selects: function() {
            this.retobj = this.dts;
            this.char = '';
        },
        returnObject: function(py) {
            var dts = this.retData;
            this.retobj = dts[py.toLowerCase()];
            this.char = py.toLowerCase();
        },
        _filter: function(dts) {
            var name = this.optionName;
            var that = this;
            for (var i in dts) {
                var py = pinyin(dts[i][name], {
                    style: pinyin[this.style]
                });
                if (py[0]) {
                    var str = py[0].join();
                    if (str.length > 1) {
                        str = str[0].toLowerCase();
                    }
                    var Regx = /^[A-Za-z]+$/;
                    if (!Regx.test(str)) {
                        str = 'other';
                    }
                    dts[i]['py'] = str;
                    that.letterList.forEach(function(item) {
                        if (item.name.toLocaleLowerCase() === str) {
                            item.hasList = true;
                        }
                    })
                }
            }
            that.retData = _.groupBy(dts, 'py');
        },
        findById: function(arr) {
            var selected = [],
                that = this;
            var options = this.isFuzzySearch ? this.initOption : this.dts;
            if (this.multiple === true) {
                for (var i in arr) {
                    var obj = _.filter(options, function(o) {
                        return o[that.optionId] == arr[i]
                    });
                    selected = _.unionWith(selected, obj, _.isEqual);
                }
            } else {
                var obj = _.filter(this.dts, function(o) {
                    return o[that.optionId] == arr
                });
                if (obj.length > 0) {
                    selected = obj[0];
                }
            }
            return selected;
        },
        returnActive: function(el) {
            var active = false;
            if (this.multiple === true) {
                if (_.indexOf(this.sel, el) > -1) {
                    active = true
                }
            } else {
                if (this.sel === el) {
                    active = true
                }
            }
            return active;
        },
        returnHover: function(str) {
            var active = false;
            if (this.char === str.toLowerCase()) {
                active = true
            }
            return active;
        },
        returnOptionName: function(str){
        	if(str.indexOf('.')>-1){
        		return str.split('.')
        	}else{
        		return str
        	}
        },
        returnName: function(str,data){
        	 var arr = str.split('.');
             arr.forEach(function(e, index) {
                 if (e.indexOf('[]') > -1) {
                     e = e.replace('[]', '');
                     data = data[e];
                     data = data[0];
                 } else {
                     data = data[e];
                 }
             });
             return data;
        },
        reloadData: function(){
        	var that = this;
        	if(!this.reloadApi){
        		console.log('reloadApi未传');
        		return
        	}
        	var obj={};
        	obj[this.name] = this.searchText;
        	var url = applyTpl(that.reloadApi, obj);
        	syncData(url, that.reloadType, that.reloadReqData, function(res, err){
        		if(res){
    				that.reloadEnd = true;
    				that.isLoading = false;
    				that.letterList.forEach(function(item) {
    					item.hasList = false;
                    })
            		if(Array.prototype.isPrototypeOf(res)){
            			that.dts = res;
            		}else{
            			that.dts = res.list;
            		}
            	}
        	})
        }
    }
})