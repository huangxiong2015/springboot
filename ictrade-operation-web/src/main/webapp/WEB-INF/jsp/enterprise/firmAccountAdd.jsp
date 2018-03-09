<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->
<head> 
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
	<title>企业账户新增</title>
	<%-- <link rel="stylesheet" href="${ctx}/css/app/account_Manage.css"/> --%>
	<link rel="stylesheet" href="${ctx}/css/common/component.css"/>
	<link rel="stylesheet" href="${ctx}/css/app/firmAccountDetail.css"/>
	<style>
	.cur{cursor:pointer;}
	i.radio {margin: 0px;}
	.ui-qual .input-text{width:145px;}
	</style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<!---index header  start   ---->
	<jsp:directive.include file="../include/main/header.jsp" />
	<!---index header  end   ---->
	 <!---index nav  start   ---->
	<jsp:directive.include file="../include/main/menu.jsp" />
	<!---index nav  end   ---->

	  <!-- 右边内容显示 -->
	  <div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<a href="#" class="header_tab pitch_tab">新增企业信息</a>
		</section>
	    <!-- Main content -->
		<section ms-controller="list" class="content container-fluid ms-controller">
		<input type="hidden" id="id" value="${id }" />
		<input type="hidden" id="userId" value="${userId }" />
		<input type="hidden" id="contactUserName" value=""/>
		<input type="hidden" id="contactUserMail" value=""/>
		<!-- <input type="hidden" id="mail" name="mail" :duplex="@toHtml(@list.mail)" /> -->
		<form action="#" id="infoForm">
			<h2 class="input-title">企业信息</h2>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>公司名称：</label>
				</div>
				<div class="col-xs-11">
				   <input class="col_input_1" type="hidden" id="registeAddr" :duplex="@toHtml(@list.registeAddr)">
					<input class="col_input_1" type="hidden" id="busiLisType" :duplex="@toHtml(@list.busiLisType)">
					<input class="col_input_1" type="text" id="Entname" name="Entname">
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>公司类型：</label>
				</div>
				<div class="col-xs-11 c5e">
					<select id="corCategory" name="corCategory" ms-duplex="@list.map.CORPORATION_CATEGORY_ID" class="companyType f12 pl10 g3">
						<option value="">请选择</option>
						<option :for="(index,ele) in @corCategoryList" :attr="{value:ele.categoryId}" :text="ele.categoryName"></option>
					</select>
				</div>
			</div>
			<div class="row mt20 dn">
				<div class="col-xs-1 tr"></div>
				<div class="col-xs-11">
					<input class="col_input_1" type="text" id="corCategoryOther"
						name="corCategoryOther" :duplex="@corCategoryOther" placeholder="请填写公司类型">
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>所属行业：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input id="industryCategory" name="industryCategory" type="hidden" :duplex="@toHtml(@list.map.INDUSTRY_CATEGORY_ID)" />
					<!-- industryList   categoryId -->
					<div id="company_genre" class="company_genre l mt10">
						<div class="dib pl5 f12 company_type mb5" :for="(index,ele) in @industryList">
							<span class="check-box-white">
								<input type="checkbox" :attr="{value:ele.categoryId}"/>
							</span>
							<i class="ml10 g3">{{ele.categoryName}}</i>
						</div>
					</div>
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label>公司地址：</label>
				</div>
				<div class="col-xs-11 c5e">
					<select id="Entprovince" name="Entprovince" style="width: 100px;" ms-controller='provinceList' ms-duplex="@activeProvince" data-duplex-changed="@changeProvince();">
						<option value="">省/直辖市</option>
						<option :for="(index,ele) in @list" :attr="{value:ele.id}" :text="ele.name"></option>
					</select>

					<select id="Entcity" name="Entprovince" style="width: 100px; margin-left: 15px" ms-controller='cityList' ms-duplex="@activeCity" data-duplex-changed="@changeCity();">
						<option value="">市</option>
						<option :for="(index,ele) in @list" :attr="{value:ele.id}" :text="ele.name"></option>
					</select>
					<select id="Entcountry" name="Entprovince" style="width: 100px; margin-left: 15px" ms-controller=countryList ms-duplex="@activeCountry" >
						<option value="">区/县</option>
						<option :for="(index,ele) in @list" :attr="{value:ele.id}" :text="ele.name"></option>
					</select>
				</div>
				<div class="col-xs-1 tr">
				</div>
				<div class="col-xs-11 c5e">
					<!-- lss-address  -->
					<input class="col_input_1" type="text" id="Entaddress" name="Entaddress" maxlength="250">
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label>邓氏编码：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="dCode" name="dCode"/>
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label>公司官网：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="webSite" name="webSite" maxlength="50" / >
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label>YKY客户编码：</label>
				</div>
				<div class="col-xs-11 c5e">
					<span class="lh32">/</span>
				</div>
			</div>
			<!-- <div class="row mt20">
				<div class="col-xs-1 tr">
					<label>公司简介：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="description" name="description" maxlength="200" :duplex="@toHtml(@list.description)" / >
				</div>
			</div> -->			
			<div class="area">
				<h2 class="input-title">企业资质</h2>
				
				<!-- 资质开始 -->
				<div id="qual" class="ui-qual">
					<div class="input-group">
						<label class="input-text"><span class="must">*</span>公司注册地：</label>
						<div class="input-content qual-type"></div>
					</div>
					<div class="input-group dalu_type">
						<label class="input-text"><span class="must">*</span>营业执照类型：</label>
						<div class="input-content">
							<label class="radio-item">
								<input type="radio" name="a">
								<i class="radio radio-selected" data-name="3-TO-1"></i>三证合一营业执照（自2015年10月1日起登记机关颁发的含统一信用代码的营业执照）
							</label>
							<label class="radio-item">
								<input type="radio" name="a">
								<i class="radio" data-name="COMMON"></i>普通营业执照
							</label>
						</div>
					</div>
					<div class="qual-list">
						<!-- <div class="input-group qual-item">
							<label class="input-text"><span class="must">*</span>营业执照影印件：</label>
							<div class="input-content">
								<div class="uploadImg">
									<img src="" alt="">
									<button class="upload-btn">点此上传</button>
								</div>
								<div class="uploadCz">
									<p class="tips">文件格式：.jpg .jpeg .png .bmp .pdf，文件大小：5M以内<br>
						上传的文件需加盖公司公章或财务章</p>
									<button class="ck-btn">参考样本</button>
								</div>
							</div>
						</div> -->
					</div>
				</div>								
	        	<!-- 资质结束 -->
			</div>
			<%-- <div class="ui-qual">
				<h2 class="input-title">子账号管理</h2>	
				<div class="input-group qual-item">
					<label class="input-text"><span class="must">*</span>企业认证授权书：</label>
					<div class="input-content">
						<div class="uploadImg uploadImgLoa">
							<img id="img8" class="LOA_PIC" src="" alt="">
							<span id="uploadBtnLoa" class="upload-btn tc" style="cursor: pointer;">点此上传</span>
						</div>
						<div class="uploadCz">
							<p class="tips">文件格式：.jpg .jpeg .png .bmp .pdf，文件大小：5M以内<br>
								上传的文件需加盖公司公章或财务章</p>
							<span class="ck-btn">参考样本</span>
						</div>
					</div>
				</div>			
				<div class="row_ mb20 ml30">
	                  		<label class="mr10 l"><i class="red pr5">*</i>企业认证授权书</label>
	                  		<div class="upload_img_ico upload_img_ico_hk_8 l">
		                         <span class="icon-file-pdf img_ico_hk_8 dn" alt=""></span>
		                         <span class="dn HKfileName_8 file_Name">上传文件</span>
		                    </div>
		                    <div>
	                  			<img :visible="@picsList.loaPicPdf==false" data-type="loaPic" class="upload_img upload_img_hk_8 l" id="uploadImg8" :attr="{src: @picsList.loaPic ? @picsList.loaPic : '${ctx }/images/default_logo.png'}" pdf-url=""  data-url="" alt="" ms-click="@showVoucherPic('showImg8')">
		                    </div>
		                    <div class="dn"><img :attr="{src: @picsList.loaPic ? @picsList.loaPic : '${ctx }/images/default_logo.png'}" id="showImg8" alt="" ></div>
		                    <div :visible="@picsList.loaPicPdf" class="dd2 pdffile mt10 dib l">
								<a :attr="{href: @picsList.loaPic ? @picsList.loaPic : ''}" target="_blank">
									<img width="32px" height="32px" alt="" src="${ctx }/images/pdf_icon.png">
								</a>
								 <span class="dib pdfName" type="text" id="pdfNameTwo" :attr="{title: @picsList.loaPicPdf_NAME}">{{@picsList.loaPicPdf_NAME}}</span>
							</div>
	                        <div class="upload_prompt l mt10 rel">
		                        <div class=" ml10">支持文件格式.jpg .jpeg .png .bmp .pdf,文件大小不能超过5MB！</div>
	                        	<!-- <div class="upload_b mt10 cur" ms-click="@showVoucherPic('c8')">参考样本</div> -->
	                        	<div class="please_up please_top please_ dn" id="uploaderror8">
		                               <div class="hint-bubble hint-bubble-left"><span class="g6 f12">请上传</span></div>
		                        </div>
		                        <div class="dn"><img src="${ctx }/images/account/businessCertificate.jpg" id="c8" alt="" ></div>

	                            <a class="blue dib download" href="${ctx}/resources/download/企业授权委托书.doc">下载《企业授权委托书》模版</a>
	                            <div class=" upload_b mt10 cur ml5" id="img8">上传文件</div>
	                        </div>
	                	</div>
	                	<p class="cl"></p>
                	</div> --%>		
			<div id="contactInfo" class="area">
				<h2 class="input-title">联系人信息</h2>
				<div class="row mt20">
					<div class="col-xs-1 tr">
						<label><span class="cRed">*</span>联系人姓名：</label>
					</div>
					<div class="col-xs-11 c5e">
						<input class="col_input_1" type="text" id="name" name="name" maxlength="20" / >
					</div>
				</div>
				<div class="row mt20">
					<div class="col-xs-1 tr">
						<label><span class="cRed">*</span>手机号码：</label>
					</div>
					<div class="col-xs-11 c5e">
						<input class="col_input_1" type="text" id="mobile" name="mobile"/ >
					</div>
				</div>
				<div class="row mt20">
					<div class="col-xs-1 tr">
						<label><span class="cRed">*</span>邮箱：</label>
					</div>
					<div class="col-xs-11 c5e">
						<input class="col_input_1" type="text" id="mail" name="mail" / >
					</div>
				</div>				
				<div class="row mt20">
					<div class="col-xs-1 tr">
						<label>联系QQ：</label>
					</div>
					<div class="col-xs-11 c5e">
						<input class="col_input_1" type="text" id="qq" name="qq" / >
					</div>
				</div>
				<div class="row mt20">
					<div class="col-xs-1 tr">
						<label>职位：</label>
					</div>
					<div class="col-xs-11 c5e">
						<select id="personalTitle" name="personalTitle" ms-duplex="@contactInfo.personalTitle" >
							<option value="">请选择</option>
							<option :for="(index,ele) in @positionList" :text="ele.name" :attr="{value:ele.id}"></option>
						</select>						
					</div>
				</div>
				<div class="row mt20">
					<div class="col-xs-1 tr">
						<label>联系地址：</label>
					</div>
					<div class="col-xs-11 c5e">
						<div id="ld" class="ui-area"></div>						
					</div>
					<div class="col-xs-1 tr"></div>
					<div class="col-xs-11 c5e mt5">
						<input name="address" id="address" type="text" maxlength="250"/>
					</div>
				</div>
			</div>								
		
			<h2 class="input-title">修改日志</h2>
			<div class="row mt20 mb10">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>修改说明：</label>
				</div>
				<div class="col-xs-11 c5e">
					<textarea id="reason" class="col_input_1" name="reason" rows="5" cols="80"></textarea>
				</div>
			</div>
			<div class="row mt20 mb10">
				<div class="col-xs-1 tr">
					<label>客服备注：</label>
				</div>
				<div class="col-xs-11 c5e">
					<textarea id="comments" class="col_input_1" name="comments" rows="5" cols="80"></textarea>
				</div>
			</div>
			<div class="row mt20 mb10">				
				<div class="col-xs-11 c5e cRed" style="padding-left:160px;">
					该部分数据与用户前台数据一致，请谨慎修改
				</div>
			</div>
			<div id="infoSuc"  class="infoSuc tc" style="display:none">
				<p class="g3 f16"><span class="icon-right-c"></span>资料修改成功</p>
			</div>
		</section>
		<!-- /.content -->
		<div style="margin-left:10%;padding-bottom:150px;margin-top:35px;">
			<a href="javascript:void(0);" onclick="save();" style="background: #b1191a;color: #fff;padding: 10px 15px;border-radius: 3px;">保存</a>
		 	<!-- <a href="javascript:void(0);" onclick="refresh();" class="reset_btn">取消</a> -->
		 </div>
	 	</form>
	</div>
	<input type="hidden" value="${userId}" id="applyUserId">
	<script type="text/javascript">
		//var selectCatid = "286,287";
		var selectCatid = "225,226";
	</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/main/footer.jsp" />
	<!---footer  end   ---->
	<script type="text/javascript" src="${ctx}/js/lib/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/js/common/add.validate.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/jquery.base64.js"></script>
	<script type="text/javascript" src="${ctx}/js/oss/oss_zz.js"></script>
	<script type="text/javascript" src="${ctx}/js/common/component.js"></script>
	<script type="text/javascript" src="${ctx}/js/enterprise/enterpriseAdd.js"></script>
</div>
<!-- ./wrapper -->
</body>
</html>
