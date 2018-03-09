
/**
 * 分页组件  调用： <pagination :query-params="queryParams" @page-to="putQuery" :show-pagesize-change="showPagesizeChange" ></pagination>
 * @param queryParams   //请求接口参数
 * @param queryParams.pageSize  //每页数
 * @param queryParams.cur       //当前页
 * @param queryParams.all       //所有页数
 * @param queryParams.defaultStatus //监测参数变化标识
 * @param showPagesizeChange    //是否显示‘每页显示条数’
 * @param  @pageTo 与父组件通信方法
 * @since 2017-07-21
 * @author yansha@yikuyi.com
 */
Vue.component('pagination', {
    template: `  <div v-if="all >= 1">
                        <span v-if="showPagesizeChange" class="pull-left" style="margin-left: 20px;">
                            每页显示
                            <select class="form-control" @change="setPageNum()" v-model="num" style="display: inline-block;  width: 70px;  height: 30px; line-height: 30px;">
                                <option v-for="size in pageSizeList" :value="size">{{size}}</option>
                            </select>
                            条
                        </span>
                        <span v-if="showTotal" class="pull-left" style="margin-left: 20px;">共{{total}}条记录</span>
                        <div   v-if="all >= 1"  class="pull-right">
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
    </div>`,
    replace: true,
    props:{
        queryParams:Object,
        showPagesizeChange: {
            type: Boolean,
            default: false
        },
        showTotal: {
            type: Boolean,
            default: false
        }
    },
    data: function () {
        return {
            page: this.queryParams.page ? this.queryParams.page:1,        //当前页数
            curPage: 1,     //绑定跳转页数
            num: this.queryParams && this.queryParams.size ?    //绑定每页数
                this.queryParams.size : this.queryParams &&
                this.queryParams.pageSize ? this.queryParams
                        .pageSize : 10,

            query: this.queryParams ? this.queryParams : {},    //传递参数
            all: this.queryParams.all ? this.queryParams.all : 0,   //总页数
            pageSizeList: [10, 20, 50, 100]     //显示条数数组
        }
    },
    watch: {
        page : function(val, oldVal) {
            this.query.page = val;
            this.curPage=val;
            this.query.defaultStatus = !this.query.defaultStatus;
            this.$emit('page-to', this.query);
        },
        'queryParams.all': function (val) {
            this.all=val;
        },
        'queryParams.page':function (val) {
            this.page=val;
        }
    },
    methods:{
        //页码点击事件
        btnClick : function(index){
            if(index != this.page){
                this.page = this.curPage = index;
            }
        },
        tunrnPage : function () {
            this.page = this.curPage;
        },
        setPageNum: function() {
            this.query.size ? this.query.size = this.num :  '';
            this.query.pageSize ? this.query.pageSize =this.num : '';
            this.page = 1;
            this.query.defaultStatus = !this.query.defaultStatus;
            this.$emit('page-to', this.query);
        }
    },
    computed:{
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
});
