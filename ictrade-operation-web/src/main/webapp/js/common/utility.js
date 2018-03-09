
/*获取当前url参数*/
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null){
		return decodeURI(r[2]);
	}else{
		return "";
	}
} 

/**
 * 解析url后附加的数据
 * @param  {Boolean} isHash 为true时解析location.hash, false则解析location.search
 * @return {object}         返回键值对
 */
function parseUrlParam (isHash) {
    var query,
        data = {};
    if (isHash) {
        query = window.location.hash;
        query = query.replace(/^\#/,'');
    } else {
        query = window.location.search;
        query = query.replace(/^\?/,'');
    }
    if (!query) return false;
    query = query.split('&');
    query.forEach(function (ele) {
        var tmp = ele.split('=');
        data[ decodeURIComponent( tmp[0] ) ] = decodeURIComponent( tmp[1] === undefined ? '' : tmp[1] );
    });
    return data;
}
/**
 * 将信息序列化为query string
 * @param  {string} info 商品信息字符串, 格式为 Name,鸡翅;Id,2323
 * @return {string}      序列化后的字符串, 形如 Name=%E9%B8%A1%E7%BF%85&Id=2323
 */
function serializeMetaInfo (info) {
    var pairs = (info || '').replace(/^;|;$/g,'').replace(/^\s+|\s+$/g, ''),
        arr = [];
    if (pairs === '') return '';
    pairs = pairs.split(';');
    pairs.forEach(function (pair) {
        pair = pair.split(',');
        arr.push( encodeURIComponent($.trim(pair[0] )) + '=' + encodeURIComponent( $.trim( pair[1] ) ) );
    });
    return arr.join('&');
}
/**
 * 模板函数
 * @param  {string} tpl   模板, 括号中的东西会被替换掉(包括括号),依据括号内的名
 *                        称来寻找对象中对应的值
 *                        格式为 商品名称:{Name}, 商品ID: {Id}
 * @param  {array of object or object} value 用来生成模板的值, 可以是包含对象的
 *                        数组或者是对象
 * @return {string}
 */
function applyTpl (tpl, value) {
    var str = '',
        values = Array.isArray(value) ? value : [value];
    values.forEach(function (el) {
        str += tpl.replace(/{([^}]+)}/g, function ($0, $1) {
            if ($1 === 'RAW_DATA') {
                return encodeURIComponent( JSON.stringify(el) );
            } else {
                return el[$1] == null ? '': el[$1];
            }
        });
    });
    return str;
}
/**
 * 将对象序列化为query string
 * @param  {object} data 要序列化的对象, 如 {Name:"鸡翅",Id:2323}
 * @return {string}      序列化之后的结果,形如 Name=%E9%B8%A1%E7%BF%85&Id=2323
 */
function serialize (data) {
    var res = [],key;
    for (key in data) {
        if (data.hasOwnProperty(key)) {
            res.push( encodeURIComponent( key ) + '=' + encodeURIComponent( data[key] ));
        }
    }
    return res.join('&');
}
/**
 * 将array转换成Json String格式
 * @param  {array} 要序列化的对象,如[{"name":"code","value":"2323"},.....}]
 * @return  {object} data 序列化之后的结果, 如 "{code:"2323"}"
 */
function arrayToJson(formArray){
    var dataArray = {};
    $.each(formArray,function(){
        if(dataArray[this.name]){
            if(!dataArray[this.name].push){
                dataArray[this.name] = [dataArray[this.name]];
            }
            dataArray[this.name].push(this.value || '');
        }else{
            dataArray[this.name] = this.value || '';
        }
    });
    return JSON.stringify(dataArray);
}
/**
 * 判断对象是否为空
 * @param  {object}  obj
 * @return {Boolean}     为空则返回true, 否则false
 */
function isEmptyObj (obj) {
    var key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) {
            return false;
        }
    }
    return true;
}

function toObj( query ){
    var data = {};
    query = query.split('&');
    query.forEach(function (ele) {
        var tmp = ele.split('=');
        data[ decodeURIComponent( tmp[0] ) ] = decodeURIComponent( tmp[1] === undefined ? '' : tmp[1] );
    });
    return data;
}

//判断值是否为null
var isNull = function(text){
    if(text == null){
        text =" ";
    }return text;
};

//格式化时间
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

//返回url参数
var getUrlParameter = function(param){
    var pageUrl = window.location.search.substring(1);
    var urlVariables = pageUrl.split("&");
    for(var i = 0; i < urlVariables.length; i++){
        var parameterName = urlVariables[i].split("=");
        if(parameterName[0] ==  param){
            return parameterName[1];
        }
    }
    return null;
};

(function(window){
    //准备模拟对象、空函数等
    var LS, noop = function(){}, document = window.document, notSupport = {set:noop,get:noop,remove:noop,clear:noop,each:noop,obj:noop,length:0};

    //优先探测userData是否支持，如果支持，则直接使用userData，而不使用localStorage
    //以防止IE浏览器关闭localStorage功能或提高安全级别(*_* 万恶的IE)
    //万恶的IE9(9.0.11）)，使用userData也会出现类似seesion一样的效果，刷新页面后设置的东西就没有了...
    //只好优先探测本地存储，不能用再尝试使用userData
    (function(){
        // 先探测本地存储 2013-06-06 马超
        // 尝试访问本地存储，如果访问被拒绝，则继续尝试用userData，注： "localStorage" in window 却不会返回权限错误
        // 防止IE10早期版本安全设置有问题导致的脚本访问权限错误
        if( "localStorage" in window ){
            try{
                LS = window.localStorage;
                return;
            }catch(e){
                //如果报错，说明浏览器已经关闭了本地存储或者提高了安全级别
                //则尝试使用userData
            }
        }

        //继续探测userData
        var o = document.getElementsByTagName("head")[0], hostKey = window.location.hostname || "localStorage", d = new Date(), doc, agent;

        //typeof o.addBehavior 在IE6下是object，在IE10下是function，因此这里直接用!判断
        //如果不支持userData则跳出使用原生localStorage，如果原生localStorage报错，则放弃本地存储
        if(!o.addBehavior){
            try{
                LS = window.localStorage;
            }catch(e){
                LS = null;
            }
            return;
        }

        try{ //尝试创建iframe代理，以解决跨目录存储的问题
            agent = new ActiveXObject('htmlfile');
            agent.open();
            agent.write('<s' + 'cript>document.w=window;</s' + 'cript><iframe src="/favicon.ico"></iframe>');
            agent.close();
            doc = agent.w.frames[0].document;
            //这里通过代理document创建head，可以使存储数据垮文件夹访问
            o = doc.createElement('head');
            doc.appendChild(o);
        }catch(e){
            //不处理跨路径问题，直接使用当前页面元素处理
            //不能跨路径存储，也能满足多数的本地存储需求
            //2013-03-15 马超
            o = document.getElementsByTagName("head")[0];
        }

        //初始化userData
        try{
            d.setDate(d.getDate() + 36500);
            o.addBehavior("#default#userData");
            o.expires = d.toUTCString();
            o.load(hostKey);
            o.save(hostKey);
        }catch(e){
            //防止部分外壳浏览器的bug出现导致后续js无法运行
            //如果有错，放弃本地存储
            //2013-04-23 马超 增加
            return;
        }
        //开始处理userData
        //以下代码感谢瑞星的刘瑞明友情支持，做了大量的兼容测试和修改
        //并处理了userData设置的key不能以数字开头的问题
        var root, attrs;
        try{
            root = o.XMLDocument.documentElement;
            attrs = root.attributes;
        }catch(e){
            //防止部分外壳浏览器的bug出现导致后续js无法运行
            //如果有错，放弃本地存储
            //2013-04-23 马超 增加
            return;
        }
        var prefix = "p__hack_", spfix = "m-_-c",
            reg1 = new RegExp("^"+prefix),
            reg2 = new RegExp(spfix,"g"),
            encode = function(key){ return encodeURIComponent(prefix + key).replace(/%/g, spfix); },
            decode = function(key){ return decodeURIComponent(key.replace(reg2, "%")).replace(reg1,""); };
        //创建模拟对象
        LS= {
            length: attrs.length,
            isVirtualObject: true,
            getItem: function(key){
                //IE9中 通过o.getAttribute(name);取不到值，所以才用了下面比较复杂的方法。
                return (attrs.getNamedItem( encode(key) ) || {nodeValue: null}).nodeValue||root.getAttribute(encode(key));
            },
            setItem: function(key, value){
                //IE9中无法通过 o.setAttribute(name, value); 设置#userData值，而用下面的方法却可以。
                try{
                    root.setAttribute( encode(key), value);
                    o.save(hostKey);
                    this.length = attrs.length;
                }catch(e){//这里IE9经常报没权限错误,但是不影响数据存储
                }
            },
            removeItem: function(key){
                //IE9中无法通过 o.removeAttribute(name); 删除#userData值，而用下面的方法却可以。
                try{
                    root.removeAttribute( encode(key) );
                    o.save(hostKey);
                    this.length = attrs.length;
                }catch(e){//这里IE9经常报没权限错误,但是不影响数据存储
                }
            },
            clear: function(){
                while(attrs.length){
                    this.removeItem( attrs[0].nodeName );
                }
                this.length = 0;
            },
            key: function(i){
                return attrs[i] ? decode(attrs[i].nodeName) : undefined;
            }
        };
        //提供模拟的"localStorage"接口
        if( !("localStorage" in window) )
            window.localStorage = LS;
    })();

    //二次包装接口
    window.LS = !LS ? notSupport : {
            set : function(key, value){
                //fixed iPhone/iPad 'QUOTA_EXCEEDED_ERR' bug
                if( this.get(key) !== undefined )
                    this.remove(key);
                LS.setItem(key, value);
                this.length = LS.length;
            },
            //查询不存在的key时，有的浏览器返回null，这里统一返回undefined
            get : function(key){
                var v = LS.getItem(key);
                return v === null ? undefined : v;
            },
            remove : function(key){ LS.removeItem(key);this.length = LS.length; },
            clear : function(){ LS.clear();this.length = 0; },
            //本地存储数据遍历，callback接受两个参数 key 和 value，如果返回false则终止遍历
            each : function(callback){
                var list = this.obj(), fn = callback || function(){}, key;
                for(key in list)
                    if( fn.call(this, key, this.get(key)) === false )
                        break;
            },
            //返回一个对象描述的localStorage副本
            obj : function(){
                var list={}, i=0, n, key;
                if( LS.isVirtualObject ){
                    list = LS.key(-1);
                }else{
                    n = LS.length;
                    for(; i<n; i++){
                        key = LS.key(i);
                        list[key] = this.get(key);
                    }
                }
                return list;
            },
            length : LS.length
        };
    //如果有jQuery，则同样扩展到jQuery
    if( window.jQuery ) window.jQuery.LS = window.LS;
})(window);


function popup(data){
    var me = this;
    if (!this._wrapper || !$('.alert').length) {
        this._wrapper = $('<div />');
        this._wrapper.addClass('alert alert-dismissible');
        this._wrapper.html('<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><h4></h4><p></p>');
        this._wrapper.appendTo(document.body);
    }
    this._wrapper.find('h4').html(data.title);
    this._wrapper.find('p').html(data.msg);
    switch (data.type){
        case "success":
            me._wrapper.addClass('alert-success');
            break;
        case "info":
            me._wrapper.addClass('alert-info');
            break;
        case "warning":
            me._wrapper.addClass('alert-warning');
            break;
        case "failed":
            me._wrapper.addClass('alert-danger');
            break;
        default :
            me._wrapper.addClass('alert-info');
            break;
    }
    me._wrapper.addClass('pos-fix').fadeIn(data.time? 500 : data.time);
    if(data.time){
        me._wrapper.fadeOut(data.time);
    }
}

Array.prototype.unique = function(){
    var a = {};
    for(var i = 0; i < this.length; i++){
        if(typeof a[this[i]] == "undefined")
            a[this[i]] = 1;
    }
    this.length = 0;
    for(var i in a)
        this[this.length] = i;
    return this;
}

//循环检索控件
var checkRequired = function (btn) {
    var i=0;
    var j=0;
    $("input[required = required]").each(function(a, b){
        if(b.value!=''){
            i++;
        }else {
            j++;
        }
    });
    $(".city-picker[required = required] > select").each(function(a, b){
        if(b.value!=''){
            i++;
        }else {
            j++;
        }
    });
    if(j <= 0){
        $(btn).removeAttr('disabled');
    }else{
        $(btn).attr('disabled', 'disabled');
    }
}

Array.prototype.unique = function (isStrict) {//去重
    if (this.length < 2)
        return [this[0]] || [];
    var tempObj = {}, newArr = [];
    for (var i = 0; i < this.length; i++) {
        var v = this[i];
        var condition = isStrict ? (typeof tempObj[v] != typeof v) : false;
        if ((typeof tempObj[v] == "undefined") || condition) {
            tempObj[v] = v;
            newArr.push(v);
        }
    }
    return newArr;
}

function formValidate(formID, rules, submitHandler, messages) {
    $(formID).validate({
        success: function (label) {
            $(label).parent().removeClass("has-error");
        },
        showErrors: function (errorMap, errorList) {
            if (errorList.length) {
                var error = $(errorList).get(0).element;
                var type = (typeof error.attributes.type != "undefined" ) ? error.attributes.type.nodeValue : "";
                var errP = $(error).parent();
                if (type == "checkbox" || type == "radio" || type == "file") {
                    errP=$(error).parents(".checkVal");
                }
                errP.addClass("has-error");
            }
            this.defaultShowErrors();
        },
        errorElement: "span",
        errorClass: "help-block",
        errorPlacement: function (error, element) {
            var type = (typeof $(element)[0].attributes.type != "undefined" ) ? $(element)[0].attributes.type.nodeValue : "";
            if (type == "checkbox" || type == "radio" || type == "file") {
                error.appendTo($(element).parents(".checkVal"));
                $(element).parents(".checkVal").addClass("has-error");
            } else {
                error.appendTo($(element).parent());
                $(element).parent().addClass("has-error");
            }
        },
        rules:  rules,
        messages: messages,
        submitHandler: submitHandler
    });

}

//解决乘法精度丢失问题
function numMulti(num1, num2) { 
	var baseNum = 0; 
	try { 
		baseNum += num1.toString().split(".")[1].length; 
	} catch (e) { 
	} 
	try { 
		baseNum += num2.toString().split(".")[1].length; 
	} catch (e) { 
	} 
	
	return Number(num1.toString().replace(".", "")) * Number(num2.toString().replace(".", "")) / Math.pow(10, baseNum); 
};

//通过商品详情链接获取商品id
function getProductId(url, name){
	if(!url){
		return;
	}
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var c = url.split('?')[1],r;
	if(c){
		r=c.match(reg);
	}
	if (r != null){
		return decodeURI(r[2]);
	}else{
		var str1 = url.split('product/')[1];
		var str2 = str1?str1.split('.htm')[0]:'';
		if(str2){
			return str2;
		}else{
			return "";
		}			
	}
}