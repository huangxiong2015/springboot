$(function(){
	placeholder();
  	checkOnlyNumber();
});

//初始化只能输入数字的验证
function checkOnlyNumber(){
  	//数字输入框只能输入数字（不带小数点）
  	$(".onlynumber").keydown(function(event){
  		return onlyDecimalKeyDown(event,false);
  	});
  	$(".onlynumber").keyup(function(){
  		onlyDecimalByReplace(this,false);
  	});
  	$(".onlynumber").bind("paste",function(){
  		var obj = this;
  		setTimeout(function(){
  			onlyDecimalByReplace(obj,false);
  		}, 500);
  	});
  	//数字输入框只能输入数字（带小数点）
  	$(".onlydecimal").keydown(function(event){
  		return onlyDecimalKeyDown(event,true);
  	});
  	$(".onlydecimal").keyup(function(){
  		onlyDecimalByReplace(this,true);
  	});
  	$(".onlydecimal").bind("paste",function(){
  		var obj = this;
  		setTimeout(function(){
  			onlyDecimalByReplace(obj,true);
  		}, 500);
  	});
}

//数字输入框只能输入数字（键盘按下）
function onlyDecimalKeyDown(event,isDecimal){
	var target = event.srcElement;
	if(target==null)target = event.target;
	//控制字符不能输入
	if(!(event.keyCode>=48 && event.keyCode<=57)
			//字母不能输入
			&& !(event.keyCode>=96 && event.keyCode<=117)
			//如果不是浮点数则不能输入.
			&& (!isDecimal || event.keyCode!=190)
			//允许输入数字和,
			&& event.keyCode!=188 && event.keyCode>46){
		if(layer)layer.tips('只能输入数字', $(target),{
			tips: [1, '#3595CC'],
			time: 4000
		});
		return false;
	}
	else return true;
}

//数字输入框只能输入数字（替换复原）
function onlyDecimalByReplace(obj,isDecimal){
	var reg = null;
	if(isDecimal){
		reg = new RegExp("[^0-9.,]","g");
	}else{
		reg = new RegExp("[^0-9,]","g");
	}
	if(reg.test(obj.value))obj.value=obj.value.replace(reg,'');
}

String.prototype.replaceAll = function(con,rep)  
{  
	var reg = new RegExp(con,"g");
    return this.replace(reg,rep);
} 
//placeholder的IE兼容性问题 
var placeholder = function(){ 
    $('input[placeholder],textarea[placeholder]').each(function(){
      //取得提示文字    
      var that = $(this),
      text = that.attr('placeholder');    
      that.attr("placeholder","");
      //移动
      var div = $("<div/>");
      if(that.css("float")){
    	  div.css("float",that.css("float"))
      }
      if(that.css("display")){
    	 // div.css("display",that.css("display"))
    	  div.css("display","inline-block")
      }
      var cssWidth = 0;
      if(that.css("width")){
    	  cssWidth = parseInt(that.css("width").substring(0,that.css("width").indexOf("px")));
      }
      var cssPLeft = 0;
      if(that.css("padding-left")){
    	  cssPLeft = parseInt(that.css("padding-left").substring(0,that.css("padding-left").indexOf("px")));
      }
      var cssPRight = 0;
      if(that.css("padding-right")){
    	  cssPRight = parseInt(that.css("padding-right").substring(0,that.css("padding-right").indexOf("px")));
      }
      var cssBLeft = 0;
      if(that.css("border-left-width")){
    	  cssBLeft = parseInt(that.css("border-left-width").substring(0,that.css("border-left-width").indexOf("px")));
      }
      var cssBRight = 0;
      if(that.css("border-right-width")){
    	  cssBRight = parseInt(that.css("border-right-width").substring(0,that.css("border-right-width").indexOf("px")));
      }
	  div.css("width",cssWidth+cssPLeft+cssPRight+cssBLeft+cssBRight+"px");
	  that.css("width",cssWidth+"px");
      if(that.css("margin-left")){
    	  div.css("margin-left",that.css("margin-left"));
    	  that.css("margin-left","0px");
      }
      if(that.css("margin-top")){
    	  div.css("margin-top",that.css("margin-top"));
    	  that.css("margin-top","0px");
      }
      if(that.css("margin-bottom")){
    	  div.css("margin-bottom",that.css("margin-bottom"));
    	  that.css("margin-bottom","0px");
      }
      that.before(div);
      that.appendTo(div);
      //创建
      var inputHolder = $("<div/>");
      inputHolder.text(text);
      that.before(inputHolder);
      //设定css
      
      //inputHolder.css("font-size",that.css("font-size"));
      inputHolder.css("font-size","12px");
      inputHolder.css("line-height",that.outerHeight()+"px");
      inputHolder.css("padding-left","5px");
      inputHolder.css("vertical-align","middle");
      inputHolder.css("position","absolute");
      inputHolder.css("color","#cccccc");
      if(that.val()!=""){
    	  inputHolder.hide();
      }
      //点击获取焦点
      inputHolder.click(function(){
    	  inputHolder.hide();
    	  that.focus();
      });
      that.bind("focus",function(){
    	  inputHolder.hide();
      })
      that.bind("blur",function(){
    	  if(that.val()==""){
	    	  inputHolder.show();
    	  }else{
    		  inputHolder.hide();
    	  }
      });
    });   
}

var HtmlUtil = {
    /*1.用浏览器内部转换器实现html转码*/
    htmlEncode:function (html){
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement ("div");
        //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
        (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
        //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
        var output = temp.innerHTML;
        temp = null;
        return output;
    },
    /*2.用浏览器内部转换器实现html解码*/
    htmlDecode:function (text){
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement("div");
        //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
        temp.innerHTML = text;
        //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
        var output = temp.innerText || temp.textContent;
        temp = null;
        return output;
    }
};