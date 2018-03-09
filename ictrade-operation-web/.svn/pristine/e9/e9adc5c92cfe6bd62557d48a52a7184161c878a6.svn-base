var vm = '';
$(function(){
      vm = new Vue({
        el: '#recommend-list',
        data: { 
            url : ykyUrl.info + "/v1/recommendations/goodslist", //访问数据接口 
            queryParams : {           //请求接口参数 
            	pageSize: 50,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                categoryId:'20021',
                status:''
            },
            gridColumns: [          //表格列                
                {key: 'orderSeq', name: '位置', align: 'center',width:'50px',
                	render:function(h,params){
                		var statu = params.row.status,
            				seq = params.row.orderSeq,
            				str;
                		if(statu == 'USER_PUT' || statu == 'SYSTEM_PUT'){
                			str = seq;
                		}else{
                			str = "";
                		}
                		return h('span',{},str);
                	}},
                {key: 'contentMap', name: '型号', align: 'center', child:[
	                     {
	                         key: "modelName",
	                         name: "",
	                         cutstring: true,
	                     } 
	                 ]
	                },
                 {key: 'contentMap', name: '分类', align: 'center',cutstring: true, default:'-', child:[
		                {
		                    key: "category3Name",
		                    name: "",
		                    cutstring: true,
		                } 
		            ]
	               },
                
                {key: 'contentMap', name: '区域', align: 'center',cutstring: true, default:'-', child:[
		                {
		                    key: "name",
		                    name: "",	
		                    cutstring: true,
		                } 
		            ]
                	},
                {key: 'lastUpdateDate', name: '更新时间', align: 'center', cutstring: true, default:'-'},
                {key: 'publishDate', name: '推荐时间', align: 'center',cutstring: true, default:'-'},
                {key: 'creatorName', name: '创建人', align: 'center', cutstring: true, default:'-'},
                {key: 'status', name: '状态', align: 'center', width: '80px', default:'-',
                	text:{
	                    SYSTEM_PUT:{
	                    	value : '系统推荐',
	                    	color : 'green'
	                    },
	                    USER_PUT:{
	                    	value : '手工维护'
	                    },
	                    DELETED:{
	                    	value : '-'
	                    }
	                }
                },
                {key: 'operates', name: '操作', align: 'center', width: '150px', 
                	render:function(h,params){
                		var that = this;
                		var id = params.row.recommendationId,
                			statu = params.row.status,
                			seq = params.row.orderSeq,
                			index = params.index,
                			contraStatu = statu == 'SYSTEM_PUT' ? 'USER_PUT' : 'SYSTEM_PUT',
        					changStr = statu == 'SYSTEM_PUT' ? '关闭' : '启用',
                			arr = [];
                		//编辑
                		var editBtn = h('a',{
                			attrs:{
                				href:ykyUrl._this + '/recommend/edit.htm?recommendationId=' + id
                			},
                			style:{
                				paddingRight:'5px'
                			}
                		},'编辑');
                		//启用 关闭
                		var changBtn = h('a',{
                			attrs:{
                				href:'javascript:;'
                			},
                			style:{
                				paddingRight:'5px'
                			},
                			on:{
                				click:function(){
                					changeStatus(id,contraStatu)
                				}
                			}
                		},changStr);
                		//推荐
                		var recommendBtn = h('a',{
                			attrs:{
                				href:'javascript:;'
                			},
                			style:{
                				paddingRight:'5px'
                			},
                			on:{
                				click:function(){
                					recommendPrd(id,seq);
                				}
                			}
                		},'推荐');
                		if(statu == 'USER_PUT' || statu == 'SYSTEM_PUT'){
                			arr.push(editBtn);
                			arr.push(changBtn);
                		}else{
                			arr.push(recommendBtn);
                		}
                		return h('span',{},arr);
                	},
                }
            ], 
            pageflag : true,    //是否显示分页
            refresh: false,     //重载 
            showModal:false,
            modalHead:'确认推荐',
            recommendId:'',
            orderSeq:1,
            seqList:[1,2,3,4,5],
            modalStyle:{width: '500px'}
        },
        methods:{
        	modalOk:function(){//推荐确认
        		this.showModal = false;
        		var id = this.recommendId,seq = this.orderSeq;
        		var data = {
        				orderSeq:seq,
        				recommendationId:id
        		}
        		var index = layer.load(1);
        		syncData(ykyUrl.info + '/v1/recommendations/goods/SYSTEM_PUT','PUT',data,function(res,err){
        			if(err == null){
        				vm.refresh = !vm.refresh;
        			}
    				layer.close(index);
        		})
        	}
        }
    });
});
function changeStatus(id, status){//修改状态
	var index = layer.load(1);
	syncData(ykyUrl.info + '/v1/recommendations/' + id + '/' + status, 'PUT', null , function (res , err) {//页面加载前调用方法 		
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);         
        layer.close(index);
    });   
}
function recommendPrd(id,seq){//打开推荐弹窗
	vm.showModal = true;
	vm.recommendId = id;
	//vm.orderSeq = seq?seq:'1';
}