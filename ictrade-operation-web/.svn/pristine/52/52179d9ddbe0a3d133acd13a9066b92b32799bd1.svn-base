
var signatureData = {};
var currentGetSignatureTime = Date.parse(new Date()); // 上一次获取Signature的时间
//var generatePostObjectSignatureUrl = "/ictrade-admin-web/oss.htm?action=generatePostObjectKeyByType&uploadType=";// 获取文件直传的路径，如果重写了获取方法需更改此变量

//根据文件上传类型获取文件直传的Signature(认证申请)
function getSignatures(uploadType,key,isImage) {
	var flag = true;
	//uploadPort +
	
	var generatePostObjectSignatureUrl = ykyUrl.database + "/v1/oss/upload?uploadType=";  // 获取文件直传的路径，如果重写了获取方法需更改此变量

	var now = Date.parse(new Date());
//	if (!signatureData["host"] || (now - currentGetSignatureTime) > (60 * 1000)) // 如果获取Signature时间大于1分钟，那么重新获取
//	{
		currentGetSignatureTime = now;
		$.aAjax({
			type : 'GET',
			url : generatePostObjectSignatureUrl + uploadType + "&key="+key +"&isImage="+isImage , //
			dataType : 'json',
			async : false,
			data : JSON.stringify({}),
			contentType : "application/json",
			xhrFields: {
	            withCredentials: true
	        },
	        crossDomain: true,
			success : function(data) {
				_fileUrl = data.url;
				signatureData = data;
				
			},
			error : function(data) {
				flag = false;
				layer.close(index);
				alert(data.status + " : " + data.statusText + " : " + data.responseText);
			}
		});
//	}
	return flag;
};

