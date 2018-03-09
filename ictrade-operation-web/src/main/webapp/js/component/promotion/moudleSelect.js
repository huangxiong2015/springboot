Vue.component('moudletype', {
    template: `<select class="form-control" v-model="addType">
                <option value="BANNER">Banner</option>
                <option value="COUPON">优惠券</option>
                <option value="PRODUCT_LIST">商品列表</option>
                <option value="CUSTOM">自定义</option>
            </select>`,
    data() {
        return {
            addType: "BANNER"
        }
    },
    watch: {
        addType: function(newVal, oldVal) {
            this.getMoudleType();
        }
    },
    methods: {
        getMoudleType: function() {
            this.$emit('get-type', this.addType);
        }
    }
});