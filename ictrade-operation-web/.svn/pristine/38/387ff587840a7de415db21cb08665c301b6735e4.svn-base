/**
 * create by roy.he@yikuyi.com at 2017/10/11
 */
var lemmonTab = Vue.extend({
    template: `
        <div>
            <div class="lemmon-tab-box">
                <ul class="lemmon-tab">
                    <li v-for="(tabs, index) in dataList" class="cate-item">
                        <a href="javascript:void(0);" @click="currentIndex = index; tabChecked = tabs" :class="{active:tabChecked == tabs}" :title="tabs[name]">{{tabs[name]}}</a>
                    </li>
                </ul>
                <div class="lemmon-tab-cont" v-for="(tabs, index) in dataList" v-show="currentIndex === index">
                    <div class="lemon-tab-item" v-if="tabs[children]">
                        <label><input type="checkbox" v-model="tabs.cate" @change="checkAllChange(tabs,tabs[children], tabs.cate)">不限</label>
                    </div>
                    <div class="lemon-tab-item" v-if="tabs[children]" v-for="(tab, i) in tabs[children]">
                        <label><input type="checkbox" :value="tab[id]"  :title="tab[name]" v-model="tab.cate" @change="checkboxChange(tabs, tab, tab.cate)">{{tab[name]}}</label>
                    </div>
                </div>
            </div>
            <div class="lemmon-checked">
                当前已选：<span v-for="(item, index) in checkedList">{{item.name}} <i class="fa fa-close" @click="removeChecked(index)"></i></span>
            </div>
        </div>
    `,
    props: {
        api: String,
        id: String,
        children: String,
        name: String,
        initData: Array,
    },
    data() {
        return {
            dataList: [], //初始数据源
            currentIndex: 0, //默认当前显示第一个选项卡
            tabChecked: {},
            checkedList: [],
        }
    },
    watch:{
    	checkedList:function(val){
    		this.$emit('get-data',val);
    	}
    },
    created() {
        var $this = this;
        $this.checkedList = $this.initData;

        httpGet($this, $this.api, null, function(res, err) { //页面加载前调用方法
            if (res) {
                $this.dataList = res;
                $this.tabChecked = res[0];
                $this.checkedList.forEach(function(list) {
                    var ids = list.id.split('/');
                    $this.dataList.forEach(function(item) {
                        if (item[$this.children] && item[$this.children].length > 0) {
                            item[$this.children].forEach(function(el) {
                                if (el[$this.id] == ids[1]) {
                                    $this.checkboxChange(item, el, true);
                                }
                            })
                        }
                    })
                })

            }
        });
    },
    methods: {
        checkAllChange(items, arr, check) {
            var $this = this;
            arr.forEach(function(item) {
                $this.checkboxChange(items, item, check)
            })
            $this.getSelected($this.checkedList);
        },
        getSelected(data) {
            this.$emit('get-selecteds',data);
        },
        checkboxChange(items, item, check) {
            var $this = this;
            item.cate = check;
            if (check) { //选中，添加选中数据
                items.cate = true;
                var obj = {};
                obj.id = items[$this.id] + '/' + item[$this.id];
                obj.name = items[$this.name] + '/' + item[$this.name];
                $this.checkedList.push(obj);
                $this.checkedList = _.uniqBy($this.checkedList, 'id');
                for (var i = 0; i < items[$this.children].length; i++) {
                    if (!items[$this.children][i].cate) {
                        items.cate = false;
                    }
                }
            } else { //取消选中，移除选中数据
                items.cate = false;
                $this.checkedList.forEach(function(el, i) {
                    var ids = el.id.split('/');
                    ids.forEach(function(id) {
                        if (id == item[$this.id]) {
                            $this.checkedList.splice(i, 1);
                        }
                    })
                }, this);
            }
            $this.getSelected($this.checkedList);
        },
        removeChecked(i) {
            var $this = this;
            var checkedItem = $this.checkedList[i];
            var ids = checkedItem.id.split('/');
            $this.dataList.forEach(function(item) {
                if (item[$this.id] == ids[1]) {
                    item.cate = false;
                }

                if (item[$this.children] && item[$this.children].length > 0) {
                    item[$this.children].forEach(function(el) {
                        if (el[$this.id] == ids[1]) {
                            el.cate = false;
                            item.cate = false;
                        }
                    })
                }
            })

            $this.checkedList.splice(i, 1);
            $this.getSelected($this.checkedList);
        }
    }
})

Vue.component('lemmonTab', lemmonTab);