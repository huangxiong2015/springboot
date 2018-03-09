/**
 * Created by yikuyi on 2017/1/22.
 */
Vue.component('notification', {
    template : `<div> 
        <div class="notification fixed"
          v-if="show"
          :style="setStyle"
          transition="slide">
            <div class="notify-delete"
            v-if="!options.autoClose"
            @click="close()"></div>
            <div class="notify-content" v-html="options.content">
            </div>
          </div>
          <div class="notify-countdown"
          v-if="show && options.autoClose && options.countdownBar"
          :style="setTime"
          :class="barControl"></div>
        </div>`,
    props : {
        options: {
            type: Object,
            default: () => {
                return {}
            }
        },
        show: {
            type: Boolean,
            default: false
        }
    },
    data (){
        return {
            timers: [],
            barControl: '',
            isShow : this.show
        }
    },
    computed : {
        setStyle () {
            return {
                color: this.options.textColor || '#fff',
                background: this.options.backgroundColor || '#f33'
            }
        },
        setTime () {
            return {
                transition: `all ${(this.options.showTime / 1000) || 3}s linear`,
                background: this.options.barColor || '#f60'
            }
        }
    },
    methods : {
        countdown () {
            if (this.options.autoClose) {
                if (this.options.countdownBar) {
                    setTimeout(() => {
                        this.barControl = 'count-leave'
                    }, 10)
                }
                const t = setTimeout(() => {
                    this.close()
                }, this.options.showTime || 3000)
                this.timers.push(t)
            }
        },
        close () {
            this.$emit('is-notify');
        }
    },
    watch : {
        options () {
            this.barControl = ''
            this.timers.forEach((timer) => {
                window.clearTimeout(timer)
            })
            this.timers = []
            this.countdown()
        }
    }

})