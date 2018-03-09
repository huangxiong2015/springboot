/**
 * Created by yansha on 2017/8/11.
 */

Vue.component('step', {
    template:`<div :class="wrapClasses" :style="styles">
        <div class="lemon-steps-tail"><i></i></div> 
        <div class="lemon-steps-head">
             <div class="lemon-steps-head-inner">
                <span v-if="!icon && currentStatus != 'finish' && currentStatus != 'error'">{{ stepNumber }}</span>
                <span v-else :class="iconClasses" ></span>
             </div> 
        </div> 
        <div class="lemon-steps-main">
                <div class="lemon-steps-title">{{ title }}</div>
                <slot>
                    <div v-if="content" class="lemon-steps-content">{{ content }}</div>
                </slot>
        </div> 
        <div class="lemon-steps-tail-right"><i class="fa fa-angle-right"></i></div>
    </div>`,
    props:{
        status: {
            validator (value) {
                return common.oneOf(value, ['wait', 'process', 'finish', 'error']);
            }
        },
        title: {
            type: String,
            default: ''
        },
        content: {
            type: String
        },
        icon: {
            type: String
        },
        iconClass:{
            type: String
        }
    },
    data(){
        return{
            prefixCls: 'lemon-steps',
            iconPrefixCls : 'fa',
            stepNumber: '',
            nextError: false,
            total: 1,
            currentStatus: ''
        }
    },
    computed:{
        wrapClasses(){
            return [
                `${this.prefixCls}-item`,
                `${this.prefixCls}-status-${this.currentStatus}`,
                {
                    [`${this.prefixCls}-custom`]: !!this.icon,
                    [`${this.prefixCls}-next-error`]: this.nextError
                }
            ];
        },
        iconClasses () {
            let icon = '';
            if (this.icon) {
                icon = this.icon;
            }  else {
                if (this.currentStatus == 'finish') {
                    icon = 'fa fa-check';
                } else if (this.currentStatus == 'error') {
                    icon = 'fa fa-close';
                }
            }
            return [
                `${this.prefixCls}-icon`,
                {
                    [`${icon}`]: icon != ''
                }
                /*`${this.iconPrefixCls}`,
                 {
                 [`${this.iconPrefixCls}-${icon}`]: icon != ''
                 }*/
            ];
        },
        styles () {
            return {
                width: `${1/this.total*100}%`
            };
        }
    },
    watch: {
        status (val) {
            this.currentStatus = val;
            if (this.currentStatus == 'error') {
                this.$parent.setNextError();
            }
        }
    }

});

Vue.component('steps', {
    template:`<div :class="classes">
        <slot></slot>
    </div>`,
    props:{
        current: {
            type: Number,
            default: 0
        },
        status: {
            validator (value) {
                return common.oneOf(value, ['wait', 'process', 'finish', 'error']);
            },
            default: 'process'
        },
        size: {
            validator (value) {
                return common.oneOf(value, ['small']);
            }
        },
        direction: {
            validator (value) {
                return common.oneOf(value, ['horizontal', 'vertical']);
            },
            default: 'horizontal'
        },
        borderColor:{
            type: String,
            default: ''
        },
        borderType:{
            type: String,
            default: ''
        }
    },
    data(){
        return{
            prefixCls:'lemon-steps'
        }
    },
    computed: {
        classes () {
            return [
                `${this.prefixCls}`,
                `${this.prefixCls}-${this.direction}`,
                {
                    [`${this.prefixCls}-${this.size}`]: !!this.size
                },
                `${this.borderColor}`,
                `${this.borderType}`
            ];
        }
    },
    mounted () {
        this.updateChildProps(true);
        this.setNextError();
        this.updateCurrent(true);
    },
    methods: {
        updateChildProps (isInit) {
            const total = this.$children.length;
            this.$children.forEach((child, index) => {
                child.stepNumber = index + 1;
                if (this.direction === 'horizontal') {
                    child.total = total;
                }
                // 如果已存在status,且在初始化时,则略过
                // todo 如果当前是error,在current改变时需要处理
                if (!(isInit && child.currentStatus)) {
                    if (index == this.current) {
                        if (this.status != 'error') {
                            child.currentStatus = 'process';
                        }
                    } else if (index < this.current) {
                        child.currentStatus = 'finish';
                    } else {
                        child.currentStatus = 'wait';
                    }
                }
                if (child.currentStatus != 'error' && index != 0) {
                    this.$children[index - 1].nextError = false;
                }
            });
        },
        setNextError () {
            this.$children.forEach((child, index) => {
                if (child.currentStatus == 'error' && index != 0) {
                    this.$children[index - 1].nextError = true;
                }
            });
        },
        updateCurrent (isInit) {
            // 防止溢出边界
            if (this.current < 0 || this.current >= this.$children.length ) {
                return;
            }
            if (isInit) {
                const current_status = this.$children[this.current].currentStatus;
                if (!current_status) {
                    this.$children[this.current].currentStatus = this.status;
                }
            } else {
                this.$children[this.current].currentStatus = this.status;
            }
        }
    },
    watch: {
        current () {
            this.updateChildProps();
        },
        status () {
            this.updateCurrent();
        }
    }
});