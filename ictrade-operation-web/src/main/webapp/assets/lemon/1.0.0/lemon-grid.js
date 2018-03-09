/*简单表格*/
Vue.component('child-grid', {
    template: `
        <div class="tabcon">
            <div :class="col.key"
                v-for="(col , k) in column"
            >
                {{col.name}}
                <cell :column="col" :entry="entry"></cell>
            </div>
        </div>`,
    props: {
        column: Array,
        entry: Object
    }
});
Vue.component('children-grid', {
    template: `<div>
        <div v-for="(child , k) in entry">
            <div  v-for="(col , k) in column" >{{col.name}}
            	<cell :column="col" :entry="child"></cell>
            </div>
        </div>
    </div> `,
    props: {
        column: Array,
        entry: Array
    }
});

// 截取字符串
Vue.component('cut-string', {
    template: `
        <span :title="str?str:column.default" :class="'cutstring'" :style="{color: column.textColor && retBool(column.textColor.condition, entry)?column.textColor.color: '', padding:column.padding }" v-html="(str===0 || str)?retHtml(str):column.default "></span>
    `,
    props: {
        str: [String, Number],
        column: Object,
        entry: Object,
    },
    methods: {
        retVal: function(str, data) {
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
        retBool: function(show, data) {
            var $this = this;
            if (show) {
                if (typeof(show) == 'boolean') {
                    return show;
                }
                var retShow = show.replace(/{([^}]+)}/g,
                    function($0, $1) {
                        var val = '';
                        if ($1.indexOf('.') > -1) {
                            val = $this.retVal($1, data);
                        } else {
                            val = data[$1];
                        }
                        return val == null ? '' : val;
                    });
                return eval(retShow);
            }
        },
        retHtml: function(str) {
            str = '' + str;
            if(str.indexOf('<')<=-1){
                str = str.replace(/ /g, "&nbsp;");
            }
            return str;
        }
    }
})

Vue.component('grid-cell', {
    template: `<div>
            <span v-if="column.type && column.type=='image'">  <img :src="entry[column.key] ? entry[column.key] :  column.default" :title="column.title ? column.title : column.name"/></span>
            <div v-else-if="column.type && column.type=='array' && !column.endString" v-for="item in entry[column.key]" :style="{color: column.textColor && retBool(column.textColor.condition, entry)?column.textColor.color: ''}">{{item && item !== '' && item !== null? item : '--'}}</div>
            <div v-else-if="column.type && column.type=='array' && column.endString" v-for="item in entry[column.key]" :style="{color: column.textColor && retBool(column.textColor.condition, entry)?column.textColor.color: ''}">{{item && item !== '' && item !== null? item : '--'}} {{column.endString}}</div>
            <div v-else-if="column.type && column.type=='index'" v-for="(i, $index) in entry[column.key]" :style="{color: column.textColor && retBool(column.textColor.condition, entry)?column.textColor.color: '' }" >{{$index+1}} +</div>
            <cut-string
                v-else-if="column.cutstring"
                :str="colValue"
                :column="column"
                :entry="entry"
            >
            </cut-string>
            <span v-else :style="{color: column.textColor && retBool(column.textColor.condition, entry)?column.textColor.color: ''}" v-html="(colValue===0 || colValue) ? retHtml(colValue) :column.default" ></span>
        </div>`,
    props: {
        column: Object,
        entry: Object,
    },
    computed: {
        colValue: function () {
            return this.formatter(this.entry, this.entry[this.column.key])
        },
        formatter: function () {
            return this.column.formatter || function (rowData, value) {
                    return value
                }
        }
    },
    methods: {
        retVal: function(str, data) {
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
        retBool: function(show, data) {
            var $this = this;
            if (show) {
                if (typeof(show) == 'boolean') {
                    return show;
                }
                var retShow = show.replace(/{([^}]+)}/g,
                    function($0, $1) {
                        var val = '';
                        if ($1.indexOf('.') > -1) {
                            val = $this.retVal($1, data);
                        } else {
                            val = data[$1];
                        }
                        return val == null ? '' : val;
                    });
                return eval(retShow);
            }
        },
        retHtml: function(str) {
            str = '' + str;
            if(str.indexOf('<')<=-1){
                str = str.replace(/ /g, "&nbsp;");
            }
            return str;
        }
    }
});

Vue.component('cell-render', {
    functional: true,
    props: {
        column: Object,
        entry: Object,
        index: Number
    },
    render: function (h, ctx) {
        var params = {
            row: ctx.props.entry,
            index: ctx.props.index
        };
        if (ctx.props.column) {
            params.column = ctx.props.column;
        }
        return ctx.props.column.render(h, params);
    }
})

Vue.component('cell', {
    template: `<span>
        <a v-if="column.href && column.text && column.cutstring"
            :href="column.href ? getUrl(column.href, entry)  : 'javascript:'" 
            :style="{color: column.text[entry[column.key]] && column.text[entry[column.key]].color ? column.text[entry[column.key]].color : '', background: column.text[entry[column.key]] && column.text[entry[column.key]].bgcolor ? column.text[entry[column.key]].bgcolor : '' }"
        >
            <cut-string
                :str="column.text[entry[column.key]] && column.text[entry[column.key]].value ? column.text[entry[column.key]].value :  colValue"
                :column="column"
                	:entry="entry"
            >
            </cut-string> 
        </a>
		<a v-else-if="column.href && column.text"
            :href="column.href ? getUrl(column.href, entry)  : 'javascript:'" 
            :style="{color: column.text[entry[column.key]] && column.text[entry[column.key]].color ? column.text[entry[column.key]].color : '', background: column.text[entry[column.key]] && column.text[entry[column.key]].bgcolor ? column.text[entry[column.key]].bgcolor : '' }"
        >
		     {{column.text[entry[column.key]] && column.text[entry[column.key]].value ? column.text[entry[column.key]].value :  entry[column.key] }} 
	    </a>
	    <span v-else-if="column.text && column.cutstring"  :style="{color:  column.text[entry[column.key]] && column.text[entry[column.key]].color  ? column.text[entry[column.key]].color : '', background: column.text[entry[column.key]] && column.text[entry[column.key]].bgcolor ? column.text[entry[column.key]].bgcolor: '' }">
            <cut-string
                :str="column.text[entry[column.key]] && column.text[entry[column.key]].value ? column.text[entry[column.key]].value :  colValue"
                :column="column"
                	:entry="entry"
            >
            </cut-string> 
	    </span>
        <span v-else-if="column.text"  :style="{color:  column.text[entry[column.key]] && column.text[entry[column.key]].color  ? column.text[entry[column.key]].color : '', background: column.text[entry[column.key]] && column.text[entry[column.key]].bgcolor ? column.text[entry[column.key]].bgcolor: '' }">
	          {{column.text[entry[column.key]] && column.text[entry[column.key]].value ? column.text[entry[column.key]].value :  entry[column.key]?entry[column.key]:column.default }}
	    </span>
	     <a v-else-if="column.href && column.pattern" :href="column.href ? getUrl(column.href, entry)  : 'javascript:'" > {{entry[column.key] | func(column.pattern.method, column.pattern.format, column.pattern.isThousand)}}</a>
         <a v-else-if="column.href"  :href="column.href ? getUrl(column.href, entry)  : 'javascript:'" > <grid-cell :column="column" :entry="entry"></grid-cell></a>
	     <span v-else-if="column.pattern">{{entry[column.key] | func(column.pattern.method, column.pattern.format, column.pattern.isThousand)}}</span>  
	     <span v-else-if="column.callback && column.cutstring" :class="'cutstring'" :title="callfunc(entry, column.callback)" v-html="callfunc(entry, column.callback)" > </span>
	     <span v-else-if="column.callback" v-html="callfunc(entry, column.callback)" > </span>
         <cut-string
             v-else-if="column.cutstring"
             :str="colValue"
             :column="column"
             :entry="entry"
         >
         </cut-string>
	     <span v-else><grid-cell :column="column" :entry="entry"></grid-cell></span>
     </span>`,
    props: {
        column: Object,
        entry: Object,
    },
    data: function() {
        return {
            callParams: [],
            title: '提示',
            content: '内容',
            callFn: Object,
            showModal: false
        }
    },
    computed: {
        colValue: function () {
            return this.formatter(this.entry, this.entry[this.column.key]?this.entry[this.column.key]:this.column.default)
        },
        formatter: function () {
            return this.column.formatter || function (rowData, value) {
                    return value
                }
        }
    },
    methods: {
        callfunc: function(data, func) {
            var $this = this;
            var re = /{([^}]+)}/;
            //$this.callParams = [];
            func.params.forEach(function(e, index) {
                if (typeof(e) == 'string' && re.test(e)) {
                    e = e.replace(/{([^}]+)}/, function($0, $1) {
                        return $1;
                    });
                    var val = '';
                    if (e.indexOf('.') > -1) {
                        val = $this.retVal(e, data);
                    }else if(e==='rowData'){
                        val = data;
                    } else {
                        val = data[e];
                    }
                    $this.callParams[index] = val;
                } else {
                    $this.callParams[index] = e;
                }
            })
            this.callIndex = data['id'];
            if (func.action) {
                this.callFn = eval(func.action);
            }
            if (func.confirm) {
                var modal = func.confirm;
                this.content = modal.content;
                this.title = modal.title;
                this.showModal = true;
            } else {
                return this.callFn.call(this, data['id'], $this.callParams)
            }
        },
        retVal: function(str, data) {
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
        }
    }
});
Vue.component('modal', {
    template: `<transition name="modal">
        <div class="modal-mask">
            <div class="modal-wrapper">
                <div class="modal-container"  :style="modalStyle ? modalStyle : ''">
                    <div class="modal-header">
                        <button type="button" class="close" v-on:click="$emit('close')"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title"><slot name="header">Header Slot</slot></h4>
                    </div>
                    <div class="modal-body">
                        <slot>Body Slot</slot>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" v-on:click="clickOk">确认</button>
                        <button type="button" class="btn btn-default" v-on:click="clickCancel">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </transition>`,
    props: {
        action: String,
        modalStyle: Object
    },
    methods: {
        clickOk: function() {
            this.$emit('click-ok', this.action);
        },
        clickCancel: function() {
            this.$emit('click-cancel');
        }
    }
})
Vue.component('rowspan-table', function(resolve, reject) {
    resolve({
        template: `
               <div class="table-container">
                <div :class="fixedWidth ? 'fixed-width' : ''">
                    <div class="overflow">
                        <table class="table table-bordered table-hover mb20" :style="fixedWidth">
                            <thead class="table-thead">
                                <tr >
                                    <th  v-for="key in columns" :style="key.hide && (key.hide||  key.hide=='true') ? 'display:none' : ''" :width="key.width ? key.width : ''">
                                      {{ key.name }} <span v-if="key.orderBy" @click="orderby(key)" class="col-orderby"><i class="fa fa-arrows-v"> </i></span>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                            <tr v-if="isLoading">
	                        	<td :colspan="showColumns" align="center"><div class="tabcon">数据加载中...</div></td>
	                        </tr>
                            <tr v-if="!datas.length && !isLoading">
                            	<td :colspan="showColumns" align="center"><div class="tabcon">{{defaultTip}}</div></td>
                            </tr>
                            <tr v-if="datas.length && !isLoading" v-for="(entry, $index) in datas"
                                @click="clickRow($index, $event)"
                                :class="'tr-' + $index" :data-id="entry['id']"
                            >
                                <input type="hidden" :value="entry['id']" name="id">
                                <input type="hidden" :value="$index" name="index">
                                <td
                                        v-for="(column, index) in columns"
                                        :data-row="$index"
                                        :data-col="index"
                                        :class="column.class ? ' td-' + column.key +  ' ' + column.class : ' td-' + column.key"
                                        :style="column.hide  && (column.hide || column.hide=='true') ? 'display:none' : column.align ? 'text-align:' + column.align  : ''"
                                > 
                                    <!--是否包含子级-->
                                    <table class="table" v-if="column.children &&　column.children.length > 0">
                                        <tbody>
                                            <tr  v-for="(order, n) in entry[column.key]">
                                                <td v-for="(col, k) in column.children">
                                                    <div v-if="col.children">
                                                        <children-grid :column="col.children" :entry="order[col.key]" ></children-grid>
                                                    </div>
                                                    <div v-else-if="col.child && order[col.key]">
                                                        <child-grid :column="col.child" :entry="order[col.key]"  ></child-grid>
                                                    </div>
                                                    <div class="tabcon" v-else-if="n != (entry[column.key].length - 1)">
                                                        <span :class="col.padding && col.padding.length > 0 ? 'tabcon-label': ''">{{col.name}}：</span>
                                                        <a
                                                            v-if="col.href && col.cutstring"
                                                            :href="col.href ? getUrl(col.href, entry)  : 'javascript:'"
                                                        >
                                                            <cut-string
                                                                :str="order[col.key]? order[col.key] :  col.default"
                                                                :column="col"
                                                                :entry="col"
                                                            >
                                                            </cut-string>
                                                        </a>
                                                        <a v-else-if="col.href" :href="col.href ? getUrl(col.href, entry)  : 'javascript:'">{{order[col.key]? order[col.key] :  col.default}}</a>
                                                        <cut-string
                                                            v-else-if="col.cutstring"
                                                            :str="order[col.key] ? order[col.key] :  col.default"
                                                            :column="col"
                                                            	:entry="col"
                                                        >
                                                        </cut-string>
                                                        <span v-else>{{order[col.key] ? order[col.key] :  col.default}}</span>
                                                    </div>
                                                    <div class="tabcon" v-else-if="n == (entry[column.key].length - 1)">
                                                        <span :class="col.padding && col.padding.length > 0 ? 'tabcon-label': ''">{{col.name}}：</span>
                                                        <a
                                                            v-if="col.href && col.cutstring"
                                                            :href="col.href ? getUrl(col.href, entry)  : 'javascript:'"
                                                            :title="order[col.key] ? order[col.key] :  col.default"
                                                        >
                                                            <cut-string
                                                                :str="order[col.key] ? order[col.key] :  col.default"
                                                                :column="col"
                                                                	:entry="col"
                                                            >
                                                            </cut-string>
                                                        </a>
                                                        <a v-else-if="col.href" :href="col.href ? getUrl(col.href, entry)  : 'javascript:'">{{order[col.key] ? order[col.key] :  col.default}} </a>
                                                        <cut-string
                                                            v-else-if="col.cutstring"
                                                            :str="order[col.key] ? order[col.key] :  col.default"
                                                            :column="col"
                                                            	:entry="col"
                                                        >
                                                        </cut-string>
                                                        <span v-else>{{order[col.key] ? order[col.key] :  col.default}}</span>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <table class="table" v-else-if="column.child &&　column.child.length > 0">
                                        <tbody>
                                            <tr  v-if="entry[column.key]">
                                                <td v-if="column.child.length > 0">
                                                    <child-grid :column="column.child" :entry="entry[column.key]"></child-grid>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                     <!--不包含子级-->
                                    <div v-else  class="tabcon">
                                        <span  class="check-span" v-show="checkflag"  v-if="column.key==='selected'">
                                          <input type="checkbox" class="chk_ input_check" :id="'checkbox_' + $index" :value="entry" v-model="ickeck"  @click="checkBtn" :disabled="retBool(column.disabled, entry)" > <label :for="'checkbox_' + $index"></label>
                                        </span>
                                        <span  v-else-if="column.key==='index'" :title="$index + 1+(page-1)*size">
                                            {{$index + 1+(page-1)*size}}
                                    	</span>
                                        <span  v-else-if="column.key==='operate'">
                                           <span v-if="column.key==='operate' && (column.items && column.items.length > 0)">
                                                <a
                                                    v-for="(item, i) in column.items"
                                                    v-show="retBool(item.show, entry)"
                                                    :href="item.href ? getUrl(item.href, entry)  : 'javascript:'" :target="item.target ? item.target : '_self'"
                                                    :class="item.className ? item.className :''"
                                                    @click="item.callback ? callfunc(entry, item.callback) : ''"  >
                                                   {{item.text}}
                                                </a>

                                            </span>
                                        </span>
                                        <cell-render v-else-if="column.render" :column="column" :entry="entry" :index="$index"></cell-render>
                                        <cell  v-else :column="column" :entry="entry"></cell>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="row-table-pagination">
                        <span v-show="checkflag" class="pull-left check-span" style="margin-right: 20px;">
                            <input type="checkbox" id="checkbox_a1" class="chk_al1 input_check" v-model="isAll" @click="checkAllBtn"><label for="checkbox_a1"></label>
                            <span class="table-checkall-text" style="margin-left:5px;">全选</span>
                             <input type="hidden" id="checkedIds" v-model="ickeck">
                        </span>
                        <span v-if="showPagesizeChange" class="pull-left cur-page-show" style="margin-right: 20px;">
                            每页显示
                            <select class="form-control" @change="setPageNum()" v-model="num" style="display: inline-block;  width: 70px;  height: 30px; line-height: 30px;">
                                <option v-for="size in pageSizeList" :value="size">{{size}}</option>
                            </select>
                            条
                        </span>
                        <span v-if="showTotal" class="pull-left page-total-record">共{{total}}条记录</span>
                        <div  v-show="pageflag" v-if="all >= 1"  class="pull-right">
                            <div id="pager" class="page-turn pull-right">
                                <div class="">
                                    <span class="total-text"><i class="num">{{ page }}</i>
                                                 / <i class="total-page">{{all}}</i>页</span>
                                    <span class="goPageBox">&nbsp;转到</span>
                                    <span> <input type="text" class="page-input form-control input-sm" v-model="curPage"></span>
                                    <span> 页 </span>
                                    <span><input type="button" class="btn btn-light input-sm page-btn" @click="tunrnPage(page)" value="确定"></span>
                                </div>
                            </div>
                            <ul class="pagination  no-margin pull-right ">
                                <li>
                                    <a v-if="showFirst" href="javascript:" @click="page--">上一页</a>
                                    <span v-else-if="!showFirst">上一页</span>
                                </li>
                                <li v-for="index in indexes"  :class="{ 'active': page == index}">
                                    <a @click="btnClick(index)" href="javascript:">{{ index }}</a>
                                <li>
                                    <a v-if="showLast" @click="page++" href="javascript:">下一页</a>
                                    <span v-else-if="!showLast">下一页</span>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <modal v-if="showModal"
                           @close="showModal = false"
                           @click-ok="modalOk"
                           @click-cancel="toggleModal"
                           :modal-style="modalStyle"
                    >
                        <h3 slot="header">{{title}}</h3>
                        <span>{{ content }}</span>
                    </modal>
                </div>
            </div>`,
        replace: true,
        props: {
            columns: Array,
            pageflag: Boolean,
            checkflag: Boolean,
            queryParams: {
                type: Object,
                default(){
                    return {}
                }
            },
            api: String,
            refresh: {
                type: Boolean,
                default: false
            },
            showTotal: {
                type: Boolean,
                default: false
            },
            showPagesizeChange: {
                type: Boolean,
                default: false
            },
            fixedWidth: String,
            initDatas:{
                type:Array,
                default(){
                    return []
                }
            },
            defaultTip:{
                type:String,
                default(){
                    return '没有查询到相关数据'
                }
            },
            selectedItem:{
                type:Array,
                default(){
                    return[]
                }
            }
        },
        data: function() {
            return {
                datas: [],
                page: this.queryParams && this.queryParams.page ? this.queryParams.page : 1,
                curPage: 1,
                size: this.queryParams && this.queryParams.size ?
                    this.queryParams.size : this.queryParams &&
                    this.queryParams.pageSize ? this.queryParams
                            .pageSize : 10,
                num: this.queryParams && this.queryParams.size ?
                    this.queryParams.size : this.queryParams &&
                    this.queryParams.pageSize ? this.queryParams
                            .pageSize : 10,
                all: 0,
                pageSizeList: [10, 20, 50, 100],
                ickeck:  this.selectedItem ? this.selectedItem : [],
                isAll: false,
                query: this.queryParams ? this.queryParams : {},
                showModal: false,
                modalStyle: {
                    width: '500px'
                },
                title: '提示',
                content: '内容',
                callFn: Object,
                callIndex: '',
                callParams: [],
                total: '',
                checkClick:false,
                orderBy:'desc',
                allCount:0,
                showColumns:this.columns.length,
                isLoading:false,
            }
        },
        created(){
            if(!this.api && this.initDatas.length){
                this.datas = this.initDatas
            }
        },
        mounted() {
            if (!this.query.page || this.query.page <= 0) {
                this.query.page = 1
            }
            if(this.api && !this.initDatas.length){
                this.ajax(this.query);
            }
            
        },
        watch: {
            api: function(val){
                this.ajax(this.query);
            },
            page: function(val) {
                //this.$emit('page-to', val, this.query);
                this.query.page = val;
                this.ajax(this.query);
                this.ickeck=[];
            },
            isAll: function(val, oldVal) {
                var disabled='',arr = [],that = this,isDisabled=true;
                if(this.columns[0].key==='selected'&&this.columns[0].disabled){
                    disabled= this.columns[0].disabled;
                }
                if (val == true) {
                    this.datas.forEach(function(data) {
                        if(disabled){
                            isDisabled = that.retBool(disabled, data);
                            if(!isDisabled){
                                arr.push(data);
                            }
                        }else{

                            arr.push(data);
                        }
                    })
                    this.ickeck = _.uniq(this.ickeck.concat(
                        arr));
                    this.allCount=this.ickeck.length;
                }
                else if(val== false && this.checkClick===true) {
                    this.ickeck = [];
                }
            },
            all: function (val) {
                if (this.all!=0 && this.query.page > val) {
                    this.query.page = this.page = this.curPage = this.all;
                } else if (this.query.page < 1) {
                    this.query.page = this.page = this.curPage = 1;
                }
                //this.ajax(this.query);
            },
            'query.defaultStatus': function(val) { //监测query参数变化
                //this.$emit('build-query', this.query);//调用父页面build-query对应的方法
                this.size=this.query.size;
                if (this.all!=0 && this.query.page > this.all) {
                    this.query.page=this.all;
                }
                this.ajax(this.query);
                this.ickeck=[];
                var $this = this;
                $this.showColumns = 0;
                $this.columns.forEach(function(item){
                	if(!item.hide){
                		$this.showColumns +=1
                	}
                })
                console.log('监测query');
            },
            refresh: function() {
                //this.showLoading = true;
                this.ajax(this.query);
            },
            ickeck: function (val, oldVal) {
                var datas=this.datas,icheck=this.ickeck,count=this.allCount;
                icheck= _.uniq(icheck);
                if(count>0 && icheck.length>=count){
                    this.isAll=true;
                }else{
                    this.isAll=false;
                }
            }
        },
        methods: {
            getChecked:function () {
                return this.ickeck;
            },
            orderby: function (col) {
                if(this.orderBy==='asc'){
                    this.orderBy='desc';
                }else{
                    this.orderBy='asc';
                }
                this.query['orderBy'] = col.key + ' ' +this.orderBy;
                this.ajax(this.query);
            },
            clickRow: function(index, e) {
                // console.log(this.datas[index]);
                // this.$emit('click-tr', e, this.datas[index]);//请求父组件方法
            },
            //页码点击事件
            btnClick: function(index) {
                if (index != this.page) {
                    this.page = this.curPage = index;
                }
            },
            tunrnPage: function() {
                if (this.curPage > this.all) {
                    this.page = this.curPage = this.all;
                } else if (this.curPage < 1) {
                    this.page = this.curPage = 1;
                } else {
                    this.page = this.curPage
                }
                this.page = this.curPage;
            },
            checkAllBtn: function () {
                this.checkClick = true;
            },
            checkBtn: function () {
                this.checkClick = false;
            },
            setPageNum: function() {
                this.size = this.num;
                this.query.size ? this.query.size = this.num : '';
                this.query.pageSize ? this.query.pageSize = this.num : '';
                this.query.defaultStatus = !this.query.defaultStatus;
            },
            ajax: function(val) {
                var $this = this,all=0;
                if (this.all!=0 && val.page > this.all) {
                    this.page = this.curPage = this.all;
                } else if (val.page < 1) {
                    this.page = this.curPage = 1;
                }else{
                    this.page = this.curPage = val.page;
                }
                this.isLoading = !this.isLoading;
                httpGet($this, $this.api, val, function(res, err) { //页面加载前调用方法
                	$this.isLoading = !$this.isLoading;
                	if (res) {
                        if(res.constructor === Object){
                            if (res.list) {
                                $this.total = res.total;
                                $this.datas = res.list;
                                $this.size=res.pageSize?res.pageSize:res.size?res.size:$this.size;
                                all= Math.ceil(res.total / $this.size);
                                $this.all=all>200?200:all;
                            } else {
                                if(!res.content){
                                    $this.datas = [];
                                }else{
                                    $this.datas = res.content;
                                }
                                $this.size=res.pageSize?res.pageSize:res.size?res.size:$this.size;
                                all= Math.ceil(res.totalElements / $this.size);
                                $this.all=all>200?200:all;
                            }
                        }else if(res.constructor === Array){
                            $this.datas = res;
                        }
                    }else{
                        $this.datas = [];
                    }
                });
            },
            toggleModal: function() { //显示隐藏；cancel调用方法
                this.showModal = !this.showModal;
            },
            modalOk: function() { //ok调用方法
                this.toggleModal();

                this.$emit(this.callFn, this.callIndex, this.callParams);
                this.callFn.call(this, this.callIndex, this.callParams); //调用自定义函数
            },
            callfunc: function(data, func) {
                var $this = this;
                var re = /{([^}]+)}/;
                $this.callParams = [];
                func.params.forEach(function(e, index) {
                    if (typeof(e) == 'string' && re.test(e)) {
                        e = e.replace(/{([^}]+)}/, function($0, $1) {
                            return $1;
                        });
                        var val = '';
                        if (e.indexOf('.') > -1) {
                            val = $this.retVal(e, data);
                        }else if(e==='rowData'){
                            val = data;
                        } else {
                            val = data[e];
                        }
                        $this.callParams[index] = val;
                    } else {
                        $this.callParams[index] = e;
                    }
                })
                this.callIndex = data['id'];
                if (func.action) {
                    if(func.isInternal) {
                        this.callFn = func.action;
                    }else{
                        this.callFn = eval(func.action);
                    }
                }
                if (func.confirm) {
                    var modal = func.confirm;
                    this.content = modal.content;
                    this.title = modal.title;
                    this.showModal = true;
                } else {
                    if(func.isInternal){
                        this.$emit(this.callFn, data['id'], $this.callParams);
                    }else{
                        this.callFn.call(this, data['id'], $this.callParams)
                    }
                }
            },
            retVal: function(str, data) {
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
            getUrl: function(url, data) {
                var $this = this;
                var retUrl = url.replace(/{([^}]+)}/g, function($0, $1) {
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
            retBool: function(show, data) {
                var $this = this;
                if (show) {
                    if (typeof(show) == 'boolean') {
                        return show;
                    }
                    var retShow = show.replace(/{([^}]+)}/g, function($0, $1) {
                        var val = '';
                        if ($1.indexOf('.') > -1) {
                            val = $this.retVal($1, data);
                        } else {
                            val = data[$1];
                        }
                        return val == null ? '' : val;
                    });
                    return eval(retShow);
                }
            }
        },
        computed: {
            indexes: function() {
                var list = [];
                //计算左右页码
                var mid = 5; //中间值
                var page = parseInt(this.page);
                var left = Math.max(page - mid, 1);
                var right = Math.max(page + 10 - mid - 1, 10);
                if (right > this.all) {
                    right = this.all
                }
                while (left <= right) {
                    list.push(left);
                    left++;
                }
                return list;
            },
            showLast: function() {
                return this.page != this.all;
            },
            showFirst: function() {
                return this.page != 1;
            }
        }
    })
})