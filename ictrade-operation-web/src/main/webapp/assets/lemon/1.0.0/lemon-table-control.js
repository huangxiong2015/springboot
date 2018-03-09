Vue.component('table-control', {
    template : `<div>
            <table class="table table-bordered ck-table form-inline">
               <thead>
                <tr><th ><span class="text-red">*</span>型号</th><th><span class="text-red">*</span>品牌</th><th><span class="text-red">*</span>数量</th><th><span class="text-red">*</span>期望交货期</th><th>客户目标价</th><th>备注</th></tr>
               </thead>
               <tbody  v-if="indexes.length>0">
                <tr class="" v-for="index in indexes" >
                    <td ><input type="text" class="form-control" :id="'styleCode_' + index" :name="modelName + '[' + index + '][styleCode]'" :value="datas[index] && datas[index].styleCode ? datas[index].styleCode : ''" required/> </td>
                    <td >
                        <select type="text" class="form-control" :id="'brand_' + index" :name="modelName + '[' + index + '][brand]'" :value="datas[index] && datas[index].brand ? datas[index].brand : '0'" required >
                            <option value="0" > 品牌1</option>
                            <option value="1" > 品牌2</option>
                        </select>
                    </td>
                    <td class="col-md-2">
                        <input type="number" class="col-md-6 form-control" :id="'num_' + index" :name="modelName + '[' + index + '][num]'"   :value="datas[index] && datas[index].num ? datas[index].num : ''" required/>
                        <select type="text" class="col-md-6 form-control" :id="'numUnit_' + index" :name="modelName + '[' + index + '][numUnit]'"  :value="datas[index] && datas[index].numUnit ? datas[index].numUnit : 'PCS'" required>
                            <option > PCS</option>
                        </select>
                    </td>
                    <td><input type="date" class="form-control" :id="'payData_' + index" :name="modelName + '[' + index + '][payData]'"  :value="datas[index] && datas[index].payData ? datas[index].payData : ''" required/> </td>
                    <td>
                        <input type="text" class="form-control" :id="'price_' + index" :name="modelName + '[' + index + '][price]'"  :value="datas[index] && datas[index].price ? datas[index].price : ''" />
                        <select type="text" class="form-control" :id="'priceUnit_' + index" :name="modelName + '[' + index + '][priceUnit]'" :value="datas[index] && datas[index].priceUnit ? datas[index].priceUnit : 'RMB'" >
                            <option > RMB</option>
                        </select>
                    </td>
                    <td><input type="text" class="form-control" :id="'remark_' + index" :name="modelName + '[' + index + '][remark]'" :value="datas[index] && datas[index].remark ? datas[index].remark : ''" />
                        <a href="javascript:void(0);" class="btn copy-btn" @click="copyControl(index, $event)"><i class="fa fa-copy font18"></i></a>
                        <a href="javascript:void(0);" class="btn del-btn" @click="delControl(index, $event)"><i class="fa fa-trash font18"></i></a>   
                    </td>
                </tr>
                </tbody>
            </table>
            <button type="button" class="btn btn-block btn-default btn-lg" @click="addControl"><i class="fa fa-plus"></i></button>
        </div>`,
    props:{
        modelName: String ,
        models : Array
    },
    data(){
        return {
            datas : this.models,
            indexes : [0]
        }
    },
    watch : {
        models : function (val, old) {
            this.datas = val;
        }
    },
    methods : {
        addControl : function () {
            this.$emit("putmodel");
            this.$nextTick(function () {
                var n = this.indexes[this.indexes.length - 1] + 1;
                this.indexes.push(n);
            });
        },
        copyControl : function(index, e) {
            var t = this.indexes[this.indexes.length-1] + 1;
            this.indexes.push(t);
            this.$emit("putmodel");
            this.$nextTick(function () {
                this.datas.push(this.models[index]);
            });
        },
        delControl : function(index, e) {
            var t  = this.getIndex(index);
            this.$emit("putmodel");
            this.$nextTick(function () {
                this.indexes.splice(t, 1);
            });
        },
        getIndex : function (index) {
            for( var i =0; i<this.indexes.length; i++){
                if(index == this.indexes[i])
                    return i
            }
        }
    }
});
