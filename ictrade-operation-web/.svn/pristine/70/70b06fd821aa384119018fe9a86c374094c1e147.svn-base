Vue.component('checkbox-layer', {
    template: `<div class="checkbox-layer">
        <a class="select_industry" @click="showSelectLayer"><em class="select_tip">请选择所属行业</em><i class="icon-down_arrow1 g9"></i></a>
        <div class="industry_content dn">
            <div class="industry_list">
                <span class="company_type noselect" v-for="item in listData">
                    <input type="checkbox" class="check-box-input dn" :value="item.categoryId"/>
                    <label @click="selectItem($event)"><span :class="[{'check-box-red':  isSelected(item.categoryId)}, 'check-box-white']"></span> <i class="g3">{{ item.categoryName }}</i></label>
                    <div v-if="item.categoryId == '1008' && isSelected(item.categoryId)" class="inlineBlock">
                        <input name="localOtherAttr" maxlength="10" placeholder="请填写所属行业" :class="[{'v_error': showTips}, 'other_attr']" v-model="localOtherAttr" @blur="localOtherAttrBlur" @input="localOtherAttrInput"/>
                        <span v-show="showTips" class="tips_text">行业不能为空</span>
                    </div>
                </span>
            </div>
            <div class="industry_btn mt20 tc">
                <a class="btn_y" @click="confirm()">确认</a>
                <a class="btn_c" @click="cancel()">取消</a>
            </div>
        </div>
        <div class="select-area">
            <div v-for="categoryId in LocalSelectedData" :data-id="categoryId" class="area-item">{{ getCategoryName(categoryId) }}<span v-if="categoryId==1008">({{ localOtherAttr }})</span><i class="icon-close-min close" @click="delCategory(categoryId)"></i></div>
        </div>
    </div>`,
    props: {
        selectedData: Array,
        listDataUrl: String,
        otherAttr: String
    },
    data: function () {
        return {
            listData: [],
            tempSelectedData: [],
            showTips: false,
            LocalSelectedData: this.selectedData,
            LocalListDataUrl: this.listDataUrl,
            localOtherAttr: this.otherAttr
        }
    },
    watch: {
        LocalSelectedData: function () {
            this.$emit('selected-data-change', this.LocalSelectedData);
        },
        localOtherAttr: function () {
            this.$emit('attr-change', this.localOtherAttr);
        },
        otherAttr: function () {
            this.localOtherAttr = this.otherAttr;
        },
        selectedData: function () {
            this.LocalSelectedData = this.selectedData;
        }
    },
    methods: {
        //显示浮层
        showSelectLayer: function () {
            $('.industry_content').show();
        },
        //确认选择
        confirm: function () {
            if(this.tempSelectedData.indexOf(1008) != -1 && !this.localOtherAttr) {
                this.showTips = true;
                return;
            };

            $('.industry_content').hide();
            this.LocalSelectedData = this.tempSelectedData.concat();
        },
        //取消选择
        cancel: function () {
            $('.industry_content').hide();
            this.tempSelectedData = this.LocalSelectedData.concat();
        },
        //其他行业输入
        localOtherAttrInput: function () {
            if(this.localOtherAttr) {
                this.showTips = false;
            }
        },
        //其他行业失去焦点
        localOtherAttrBlur: function () {
            this.showTips = !this.localOtherAttr;
        },
        //选择/取消选择 单项
        selectItem: function (event) {
            var _this = this;
            var target = event.target;
            var currentId = Number($(target).closest('label').prev('input[type=checkbox]').val());

            $(target).closest('label').find('span').toggleClass('check-box-red');


            var index = _this.tempSelectedData.indexOf(currentId);
            if( index != -1) {
                _this.tempSelectedData.splice(index, 1);
            }else {
                _this.tempSelectedData.push(currentId);
                _this.tempSelectedData.sort();
            }

        },
        //是否选中
        isSelected: function (id) {
            var id = Number(id);
            return this.tempSelectedData.indexOf(id) != -1;
        },
        //id转换成行业名称
        getCategoryName: function (value) {
            var returnValue = '';

            $.each(this.listData, function (i, item) {
                if(value == item.categoryId) {
                    returnValue = item.categoryName;
                    return false;
                }
            });

            return returnValue;
        },
        //删除行业
        delCategory: function (categoryId) {
            var index = this.LocalSelectedData.indexOf(categoryId);
            this.LocalSelectedData.splice(index, 1);
            this.tempSelectedData = this.LocalSelectedData.concat();

            if(categoryId == 1008) {
                this.localOtherAttr = '';
            }
        }
    },
    created: function () {
        var _this = this;

        this.tempSelectedData = this.LocalSelectedData.concat();

        $.ajax({
            url: this.LocalListDataUrl,
            type: 'GET',
            success: function (data) {
                if(data) {
                    _this.listData = data;
                }
            },
            error: function (e) {
                console.error('接口错误,Error:' + e.responseText);
            }
        })
    }
});
