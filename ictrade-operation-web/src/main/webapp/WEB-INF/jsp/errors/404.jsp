<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no">
  <link rel="shortcut icon" href="${ctx}/favicon.ico"/>
  <title>您所访问的页面不存在 </title>

  <link rel="stylesheet" type="text/css" href="${ctx }/css/common/base.css"/>     
  <link rel="stylesheet" type="text/css" href="${ctx }/css/common/error.css"/>
  <link rel="stylesheet" type="text/css" href="${ctx }/css/common/content.css"/>
  
</head>

<body>
<div class="wrapper auto hd">
	<div class="logo_head wd10 pb30">
		<a href="${ctx }/">
			<img src="${ctx }/images/static/logo2x.png" class="logo vm" width="200"></img>
		</a>
	</div>
</div>
<!--主体-->
<div>
	<div class="wrong_main pt30">
      <div class="wrong_ct auto ovh">
        <div class="wg_right ml30 mr30 pr30">
           <div class="text b "><img src="${ctx }/images/static/404.png" /></div>
           <div class="wr_ts f24 mt20 tc">哎哟，您访问的页面已丢失！</div>
           <div class="wr_ts f16 tc mt14">错误信息：连接失败</div>
           <div class="link_nav mt30">
           	  <a onclick="javascript:history.back(-1)" class="ovh mr20 l backhover"><span class="f14 back tc dib poi">返回上一页</span></a>
           	  <a href="${ctx }/" class="ovh mr20 l"><span class="f16 home tc dib">返回首页</span></a> 
           </div>
        </div>        
      </div>
    </div>
</div>
<!--页脚-->

<!---footer  start   ---->
<jsp:directive.include file="../include/main/footerNojs.jsp" />
<!---footer  end   ----> 
</body>

</html>