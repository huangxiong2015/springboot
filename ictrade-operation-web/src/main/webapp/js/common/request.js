var Authorization = $("#pageToken").val();
function retMsg(status, errmsg) {
    var msg = errmsg || "服务器异常！";
    if(status === 404){
        msg = "访问不存在！"+ errmsg;
    }else if(status === 400){
        msg = "参数异常！"+ errmsg;
    }else if(status === 401){
        msg = "您无权限！"+ errmsg;
    }else if(status === 428){
        msg = "未登录！"+ errmsg;
    }
    return msg;
}

$.extend({       
	aAjax: function(option){
		var defualts = {
					beforeSend: function(request) {
		                request.setRequestHeader("Authorization", Authorization);
		            },
		            contentType:"application/json",
		            xhrFields: {
		                withCredentials: true
		            },
		            crossDomain: true,
			  	    success: function(){},
			  	    error: function(){},
				}
		var opts = $.extend({}, defualts, option);
		
		$.ajax(opts);
	}
});
//获取数据
var syncData = function(api, type, params, callback,isAsync){
    type = type.toUpperCase();
    var newParams = null;
    if(params != null){
        if(type == "POST" || type == "PUT" || type == "PATCH"){
            newParams = JSON.stringify(params);
        }
        else if(type == "GET" || type == "DELETE"){
            newParams = $.param(params);
        }
    }

    $.aAjax({
        url:  api,// + '?ts='+new Date().getTime(),
        contentType: "application/json;charset=UTF-8",
        type 		: type,
        data        : newParams,
        dataType	: "json",
        async       : isAsync == false ? false : true,
        success:function(response, msg, res){
            //var newData = JSON.parse(data || '{}');
            //callback(true, data);
            if(res.status === 200){
                callback(response, null);
            }else{
                callback(null , response);
            }
        },
        error:function (xhr, textStatus, errorThrown) {
            //异常处理 401 404 400 500
        	if(xhr.status === 200){
        		 callback(xhr.responseText, null);
        	}else{
        		var errMsg= xhr.responseJSON && xhr.responseJSON.errMsg ? xhr.responseJSON.errMsg : xhr.responseJSON && xhr.responseJSON.errCode ? xhr.responseJSON.errCode : '';
	            var msg = retMsg(xhr.status, errMsg);
	            layer.msg(msg);
	            callback(null , msg);
        	}
        }
    })
};
/**delete请求 批量删除
 * @param obj       对象
 * @param url       api链接
 * @param params    参数
 * @param callback  回调
 */
var multiDelete = function(api, type, params, callback){ 
	 $.aAjax({
	        url:  api,// + '?ts='+new Date().getTime(),
	        contentType: "application/json;charset=UTF-8",
	        type 		: type,
	        data        :  JSON.stringify(params),
	        dataType	: "json",
	        success:function(response, msg, res){
	            //var newData = JSON.parse(data || '{}');
	            //callback(true, data);
	            if(res.status === 200){
	                callback(response, null);
	            }else{
	                callback(null , response);
	            }
	        },
	        error:function (xhr, textStatus, errorThrown) {
	            //异常处理 401 404 400 500
	        	if(xhr.status === 200){
	        		 callback(xhr.responseText, null);
	        	}else{
	        		var errMsg= xhr.responseJSON && xhr.responseJSON.errMsg ? xhr.responseJSON.errMsg : xhr.responseJSON && xhr.responseJSON.errCode ? xhr.responseJSON.errCode : '';
		            var msg = retMsg(xhr.status, errMsg);
		            layer.msg(msg);
		            callback(null , msg);
	        	}
	        }
	    })
};
/**get请求
 * @param obj       对象
 * @param url       api链接
 * @param params    参数
 * @param callback  回调
 */
function httpGet(obj ,url, params, callback){
    obj.$http.get(url, {
        params : params,
        headers: {"Authorization": Authorization},
    }).then((response) => {//页面加载前调用方法
        if(response.status === 200){
            callback(response.data, null);
        }else{
            callback(null , response.body);
        }
    }, (response) => {
        //异常处理 401 404 400 500
        var msg = retMsg(response.status, response.body)
        layer.msg(msg);
        callback(null , msg);
        console.log('fail' + response.status + "," + response);
    })
}
/**post请求
 * @param obj       对象
 * @param url       api链接
 * @param params    参数
 * @param callback  回调
 */
function httpPost(obj ,url, params, callback){
    obj.$http.post(url, {
        params : params,
        before: function(request) {
            request.setRequestHeader("Authorization", Authorization);
        },
    }).then((response) => {//页面加载前调用方法
        console.log("success");
        if(response.status === 200){
            callback(response.data, null);
        }else{
            layer.msg(response.body);
            callback(null , response.body);
        }
    }, (response) => {
        //异常处理 401 404 400 500
        var msg = retMsg(response.status, response.body);
        layer.msg(msg);
        callback(null , msg);
        console.log('fail' + response.status + "," + response);
    })
}