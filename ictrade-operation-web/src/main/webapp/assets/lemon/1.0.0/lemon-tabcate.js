/**
 * create by roy.he@yikuyi.com at 2017/10/11
 */
var lemonTabcate = Vue.extend({
    template: `
        <div>
            <div class="lemmon-tab-box">
                <ul class="lemmon-tab">
                    <li>
                        <label :for="'tab_all'">
                            <input type="checkbox" v-model="firstCheckNull" value="不限" id="tab_all" @change="checkNullLevelFirst($event)">
                            不限
                        </label>
                    </li>
                    <li v-for="(tabs, index) in dataList">
                        <label :for="'tab_'+index">
                            <input type="checkbox" v-model="tabs.cate" :id="'tab_'+index" @change="checkLevelFrist(tabs)">
                            {{tabs[name]}}
                        </label>
                    </li>
                </ul>
                <ul class="lemon-tab-leveltw lemmon-tab" v-if="levelTwoData.children && levelTwoData.children.length > 0">
                    <li>
                        <label for="tab_leveltw_all">
                            <input type="checkbox" value="不限" v-model="levelTwoData.checkNull" id="tab_leveltw_all" @change="checkNullLevenlTwo($event)">
                            不限
                        </label>
                    </li>
                    <li v-for="(tab, index) in levelTwoData.children">
                        <label :for="tab[id]">
                            <input type="checkbox" :id="tab[id]" v-model="tab.cate" @change="ckeckLevelTwo(levelTwoData, tab)">
                            {{tab[name]}}
                        </label>
                    </li>
                </ul>
                <ul class="lemon-tab-levelth lemmon-tab" v-if="levelThreeData.children && levelThreeData.children.length > 0">
                    <li>
                        <label for="tab_levelth_all">
                            <input type="checkbox" v-model="levelThreeData.checkNull" value="不限" id="tab_levelth_all" @change="checkNullLevenlThree($event)">
                            不限
                        </label>
                    </li>
                    <li v-for="(tab, index) in levelThreeData.children">
                        <label>
                            <input type="checkbox" :value="tab[id]" v-model="tab.cate" @change="ckeckLevelThree(levelTwoData, levelThreeData, tab)">
                            {{tab[name]}}
                        </label>
                    </li>
                </ul>
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
            checkedList: [],
            levelTwoData: {},
            levelThreeData: {},
            firstCheckNull: false
        }
    },
    created() {
        var $this = this;
        $this.checkedList = $this.initData ? $this.initData:[];
        httpGet($this, $this.api, null, function(res, err) { //页面加载前调用方法
            if (res) {
                $this.dataList = res;
                $this.tabChecked = res[0];
                //不限初始化
                if($this.checkedList.length>0&&$this.checkedList[0].id =="*/*/*"){
            		$this.firstCheckNull = true;
            	}
                $this.checkedList.forEach(function(list) {
                    var ids = list.id.split('/');
                    $this.dataList.forEach(function(item) {
                        if (item[$this.id] == ids[0]) {
                            item.cate = true
                        }
                    })
                })
            }
        });
    },
    watch:{
        checkedList: function(val){
            this.changeChecked(val);
        }
    },
    methods: {
        changeChecked: function(data) {
            this.$emit('get-checkeds', data);
        },
        checkNullLevelFirst(e){//一级分不限点击事件
            var obj = {
                id: '*/*/*',
                name: '不限'
            };
            var $this = this;
            $this.levelTwoData = {}
            $this.levelThreeData = {}
            if(e.target.checked){
                $this.checkedList = [];
                $this.dataList.forEach(function(item){
                    if(item.cate){
                        item.cate = false
                    }
                    if(item.children && item.children.length > 0){
                        item.children.forEach(function(el){
                            if(el.cate){
                                el.cate = false;
                            }
                            if(el.children && el.children.length > 0){
                                el.children.forEach(function(ts){
                                    if(ts.cate){
                                        el.cate = false;
                                    }
                                })
                            }
                        })
                    }

                })
                $this.checkedList.push(obj)
            }else{
                $this.checkedList.forEach(function(item, i){
                    if(item.id === obj.id){
                        $this.checkedList.splice(i, 1);
                    }
                })
            }
        },
        checkLevelFrist(item){
            var $this = this;
            if(item.cate){
                $this.levelTwoData = item
                $this.levelThreeData = {}
                $this.firstCheckNull = false;
                $this.checkedList.forEach(function(item, i){
                    if(item.id === '*/*/*'){
                        $this.checkedList.splice(i, 1);
                    }
                })
                var obj = {
                    id: item[$this.id]+'/*/*',
                    name: item[$this.name]
                }
                $this.checkedList.push(obj)
            }else{
                item.checkNull = false;
                item.children.forEach(function(el){
                    if(el.cate) el.cate = false
                    el.checkNull = false;
                    if(el.children && el.children.length > 0){
                        el.children.forEach(function(ts){
                            if(ts.cate) ts.cate = false
                        })
                    }
                })
                $this.levelTwoData = {}
                $this.levelThreeData = {}
                for(var i = 0; i < $this.checkedList.length; i++){
                    var ids = $this.checkedList[i].id.split('/');
                    if(ids[0] == item[$this.id]){
                        $this.checkedList.splice(i, 1);
                        i = i -1;
                    }
                }
            }
        },
        checkNullLevenlTwo(e){
            var $this = this;
            if(e.target.checked){
                $this.levelTwoData.checkNull = true;
                var obj = {
                    id: $this.levelTwoData[$this.id]+'/*/*',
                    name: $this.levelTwoData[$this.name]
                }
                for(var i = 0; i < $this.checkedList.length; i++){
                    var ids = $this.checkedList[i].id.split('/');
                    if(ids[0] == $this.levelTwoData[$this.id]){
                        $this.checkedList.splice(i, 1);
                        i = i -1;
                    }
                }
                $this.levelTwoData.children.forEach(function(item){
                    item.cate = false;
                    if(item.children && item.children.length > 0){
                        item.children.forEach(function(el){
                            el.cate = false;
                        })
                    }
                })
                $this.checkedList.push(obj);
                $this.levelThreeData = {}
            }else{
                $this.levelTwoData.checkNull = false;
            }
        },
        ckeckLevelTwo(a,b){
            var $this = this;
            if(b.cate){
                $this.levelTwoData.checkNull = false;
                $this.levelThreeData = b;
                var obj = {
                    id: a[$this.id]+'/'+b[$this.id]+'/*',
                    name: b[$this.name]
                }
                $this.checkedList.forEach(function(item, i){
                    var ids = item.id.split('/');
                    if(ids[0] == a[$this.id] && ids[1] == '*' && ids[2] == '*'){
                        $this.checkedList.splice(i, 1);
                    }
                });
                $this.checkedList.push(obj)
            }else{
                b.checkNull = false;
                b.children.forEach(function(el){
                    if(el.cate) el.cate = false
                    el.checkNull = false;
                })
                $this.levelThreeData = {};
                var levelFristList = []
                for(var i = 0; i < $this.checkedList.length; i++){
                    var ids = $this.checkedList[i].id.split('/');
                    if(ids[0] == a[$this.id]){
                        levelFristList.push($this.checkedList[i])
                    }
                    if(ids[0] == a[$this.id] && ids[1] == b[$this.id]){
                        $this.checkedList.splice(i, 1);
                        i = i -1;
                    }
                }
                $this.dataList.forEach(function(item) {
                    if(item[$this.id] == a[$this.id]){
                        if(levelFristList.length === 1){
                            var obj = {
                                id: a[$this.id]+'/*/*',
                                name: a[$this.name]
                            }
                            $this.checkedList.push(obj)
                            // item.cate = false;
                            // $this.levelTwoData = {};
                        }
                        if(item.children && item.children.length > 0){
                            item.children.forEach(function(el){
                                el.checkNull = false;
                                $this.levelThreeData = {};
                                if(el[$this.id] == b[$this.id]) el.cate = false;
                            })
                        }
                    }
                })
            }
        },
        checkNullLevenlThree(e){
            var $this = this;
            if(e.target.checked){
                $this.levelThreeData.checkNull = true;
                var obj = {
                    id: $this.levelTwoData[$this.id]+'/'+$this.levelThreeData[$this.id]+'/*',
                    name: $this.levelThreeData[$this.name]
                }
                for(var i = 0; i < $this.checkedList.length; i++){
                    var ids = $this.checkedList[i].id.split('/');
                    if(ids[1] == $this.levelThreeData[$this.id]){
                        $this.checkedList.splice(i, 1);
                        i = i -1;
                    }
                }
                $this.levelThreeData.children.forEach(function(item){
                    item.cate = false;
                })
                $this.checkedList.push(obj);
            }else{
                $this.levelThreeData.checkNull = false;
            }
        },
        ckeckLevelThree(a,b,c){
            var $this = this;
            if(c.cate){
                b.checkNull = false;
                var obj = {
                    id: a[$this.id]+'/'+b[$this.id]+'/'+c[$this.id],
                    name: c[$this.name]
                }
                for(var i = 0; i < $this.checkedList.length; i++){
                    var ids = $this.checkedList[i].id.split('/');
                    if(ids[0] == a[$this.id] && ids[1] == b[$this.id] && ids[2] == '*'){
                        $this.checkedList.splice(i, 1);
                        i = i -1;
                    }
                }
                $this.checkedList.push(obj)
            }else{
                var leveltwoList = [],
                    levelFristList = []
                for(var i = 0; i < $this.checkedList.length; i++){
                    var ids = $this.checkedList[i].id.split('/');
                    if(ids[0] == a[$this.id] ){
                        levelFristList.push($this.checkedList[i]);
                    }
                    if(ids[1] == b[$this.id] ){
                        leveltwoList.push($this.checkedList[i]);
                    }
                    if(ids[0] == a[$this.id] && ids[1] == b[$this.id] && ids[2] == c[$this.id]){
                        $this.checkedList.splice(i, 1);
                        i = i -1;
                    }
                }
                $this.dataList.forEach(function(item) {
                    if(item[$this.id] == a[$this.id]){
                        if(levelFristList.length === 1){
                            // item.cate = false;
                            // $this.levelTwoData = {}
                        }
                        if(item.children && item.children.length > 0){
                            item.children.forEach(function(el){
                                if(el[$this.id] == b[$this.id]) {
                                    if(leveltwoList.length === 1){
                                        var obj = {
                                            id: a[$this.id]+'/'+b[$this.id]+'/*',
                                            name: b[$this.name]
                                        }
                                        $this.checkedList.push(obj)
                                        // el.cate = false;
                                        // $this.levelThreeData = {}
                                    }
                                    if(el.children && el.children.length > 0) {
                                        el.children.forEach(function(ts){
                                            if(ts[$this.id] == c[$this.id]) ts.cate = false
                                        })
                                    }
                                }
                            })
                        }
                    }
                })
            }
        },
        removeChecked(i) {
            var $this = this;
            var checkedItem = $this.checkedList[i];
            var ids = checkedItem.id.split('/');
            if(ids[0] == '*'){
                $this.firstCheckNull = false;
            }else if(ids[0] != '*' && ids[1] == '*'){
                $this.dataList.forEach(function(item) {
                    item.checkNull = false;
                    if(item[$this.id] == ids[0]) item.cate = false;
                })
            }else if(ids[0] != '*' && ids[1] != '*' && ids[2] == '*'){
                var levelFristList = []
                $this.checkedList.forEach(function(item){
                    if(item.id.includes(ids[0])){
                        levelFristList.push(item);
                    }
                })
                $this.dataList.forEach(function(item) {
                    if(item[$this.id] == ids[0]){
                        if(levelFristList.length === 1){
                            item.cate = false;
                            $this.levelTwoData = {};
                        }
                        if(item.children && item.children.length > 0){
                            item.children.forEach(function(el){
                                el.checkNull = false;
                                $this.levelThreeData = {};
                                if(el[$this.id] == ids[1]) el.cate = false;
                            })
                        }
                    }
                })
            }else if(ids[0] != '*' && ids[1] != '*' && ids[2] != '*'){
                var leveltwoList = [],
                    levelFristList = []
                $this.checkedList.forEach(function(item){
                    if(item.id.includes(ids[1])){
                        leveltwoList.push(item);
                    }
                    if(item.id.includes(ids[0])){
                        levelFristList.push(item);
                    }
                })
                $this.dataList.forEach(function(item) {
                    if(item[$this.id] == ids[0]){
                        if(levelFristList.length === 1){
                            item.cate = false;
                            $this.levelTwoData = {}
                        }
                        if(item.children && item.children.length > 0){
                            item.children.forEach(function(el){
                                if(el[$this.id] == ids[1]) {
                                    if(leveltwoList.length === 1){
                                        el.cate = false;
                                        $this.levelThreeData = {}
                                    }
                                    if(el.children && el.children.length > 0) {
                                        el.children.forEach(function(ts){
                                            if(ts[$this.id] == ids[2]) ts.cate = false
                                        })
                                    }
                                }
                            })
                        }
                    }
                })
            }
            $this.checkedList.splice(i, 1);
        }

    }
})

Vue.component('lemonTabcate', lemonTabcate);