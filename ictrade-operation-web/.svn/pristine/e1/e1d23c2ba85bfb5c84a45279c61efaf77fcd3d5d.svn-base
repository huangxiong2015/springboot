'use strict';
/**
 * Created by roy.he@yikuyi.com on 2017/4/21.
 */

Vue.component('left-menu', {
    template: `
        <ul :class="'sidebar-menu'">
            <li
                v-for="(level1, $index) in menuList"
                :class="'treeview ' + [level1.active ?'active':'']"
            >
                <a
                    :href="level1.menuUrl !== '' && level1.menuUrl ? level1.menuUrl : 'javascript:void(0);'"
                    :target="level1.istarget ? '_blank' : '_self'"
                >
                    <i :class="'fa icon-'+level1.menuId"></i>
                    <span>{{level1.menuName}}</span>
                    <i v-if="level1.subList && level1.subList.length > 0" :class="'fa fa-angle-left pull-right'"></i>
                </a>
                <ul
                    v-if="level1.subList && level1.subList.length > 0"
                    :class="'treeview-menu'"
                >
                    <li
                        v-for="(level2, $index) in level1.subList"
                        :class="'treeview ' + [level2.active ?'active':'']"
                    >
                        <a
                            :href="level2.menuUrl !== '' && level2.menuUrl ? level2.menuUrl : 'javascript:void(0);'"
                            :target="level2.istarget ? '_blank' : '_self'"
                        >
                            <i :class="'fa icon-'+level2.menuId"></i>
                            <span>{{level2.menuName}}</span>
                            <i v-if="level2.subList && level2.subList.length > 0" :class="'fa fa-angle-left pull-right'"></i>
                        </a>
                        <ul v-if="level2.subList && level2.subList.length > 0" :class="'treeview-menu'">
                            <li v-for="level3 in level2.subList" :class="'treeview ' + [level3.active ?'active':'']">
                                <a
                                    :href="level3.menuUrl !== '' && level3.menuUrl ? level3.menuUrl : 'javascript:void(0);'"
                                    :target="level3.istarget ? '_blank' : '_self'"
                                >
                                    <i :class="'fa icon-'+level3.menuId"></i>
                                    <span>{{level3.menuName}}</span>
                                    <i v-if="level3.subList && level3.subList.length > 0" :class="'fa fa-angle-left pull-right'"></i>
                                </a>
                                <ul
                                    v-if="level3.subList && level3.subList.length > 0"
                                    :class="'treeview-menu'"
                                >
                                    <li
                                        v-for="(level4, $index) in level3.subList"
                                        :class="'treeview ' + [level4.active ?'active':'']"
                                    >
                                        <a
                                            :href="level4.menuUrl !== '' && level4.menuUrl ? level4.menuUrl : 'javascript:void(0);'"
                                            :target="level4.istarget ? '_blank' : '_self'"
                                        >
                                            <i :class="'fa icon-'+level4.menuId"></i>
                                            <span>{{level4.menuName}}</span>
                                            <i v-if="level4.subList && level4.subList.length > 0" :class="'fa fa-angle-left pull-right'"></i>
                                        </a>
                                        <ul v-if="level4.subList && level4.subList.length > 0" :class="'treeview-menu'">
                                            <li v-for="level5 in level4.subList" :class="'treeview ' + [level5.active ?'active':'']">
                                                <a
                                                    :href="level5.menuUrl !== '' && level5.menuUrl ? level5.menuUrl : 'javascript:void(0);'"
                                                    :target="level5.istarget ? '_blank' : '_self'"
                                                >
                                                    <i :class="'fa icon-'+level5.menuId"></i>
                                                    <span>{{level5.menuName}}</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>
    `,
    props: {
        api: String,
        params: Object
    },
    data() {
        return {
            menuList: []
        }
    },
    created: function() {
    	this.initData()
    },
    methods: {
    	initData(){
    		var $this = this;
            httpGet($this, $this.api, $this.params, function(res, err) { //页面加载前调用方法
                if (res) {
                    for (var i = 0; i < res.length; i++) {
                    	if(res[i].menuUrl.indexOf(location.origin) > -1){
                    		if (location.href.indexOf(res[i].menuUrl.slice(location.origin.length, res[i].menuUrl.length-4))> 0) {
                                res[i].active = true;
                            }
                    	}else{
                    		if (location.href.indexOf(res[i].menuUrl)> 0) {
                                arrs[i].active = true;
                                item.active = true;
                            }
                    	}
                        if (res[i].subList && res[i].subList.length >
                            0) {
                            $this.menuHightLight(res[i].subList,
                                res[i])
                        }
                    }
                    $this.menuList = res;
                    $this.$nextTick(function(){
                    	$.AdminLTE.layout.fix() 
                    })
                }
            });
            
    	},
        menuHightLight(arrs, item) {
            for (var i = 0; i < arrs.length; i++) {
            	if(arrs[i].menuUrl.indexOf(location.origin) > -1){
            		if (location.href.indexOf(arrs[i].menuUrl.slice(location.origin.length, arrs[i].menuUrl.length-4))> 0) {
                        arrs[i].active = true;
                        item.active = true;
                    }
            	}else{
            		if (location.href.indexOf(arrs[i].menuUrl)> 0) {
                        arrs[i].active = true;
                        item.active = true;
                    }
            	}
                

                if (arrs[i].subList && arrs[i].subList.length > 0) {
                    this.menuHightLight(arrs[i].children, arrs[i])
                }
            }
        }
    }
});
