
var vm = new Vue({
        el: '#categoryEdit',
    data: {
    	categoryImageUrlBig: '',
    	categoryImageUrlSmall: '',
    	categoryName: '',
    	categoryTypeId: '',
    	sequenceNum: '',
    	categoryId: getQueryString('id'),
        },
        watch : {
        	
        }
   });
  var categoryId = getQueryString('id');
  if(categoryId){
	  syncData(ykyUrl.database + "/v1/category/findById?categoryId=" + categoryId , "get", null, function (data, err) {
          if (data) { 
        	  vm.categoryId = data.categoryId;
        	  vm.categoryImageUrlBig = data.categoryImageUrlBig?data.categoryImageUrlBig: '';
        	  vm.categoryImageUrlSmall = data.categoryImageUrlSmall?data.categoryImageUrlSmall: '';
        	  vm.categoryName = data.categoryName;
        	  vm.categoryTypeId = data.categoryTypeId;
        	  vm.sequenceNum = data.sequenceNum;
          } 
      });
}
  
//	checkAbledButton();
//验证
formValidate('#manu-from', {}, function () {
//序列化form表单数据
var formArry = $('#manu-from').serializeObject(); 
formArry.categoryId = vm.categoryId;
formArry.categoryImageUrlBig = vm.categoryImageUrlBig;
formArry.categoryImageUrlSmall = vm.categoryImageUrlSmall;
syncData(ykyUrl.database + "/v1/category/updateById", "post", formArry, function (data, err) {
	if (data) {
        layer.msg('修改成功！');
        window.location.href=ykyUrl._this + "/category.htm";
    }
});
});

$('.btn-concle').on('click', function(){
	 history.go(-1);
});

var uploaderBig = createUploader({
	buttonId: "uploadBtnBig", 
	uploadType: "categoryUpload.categoryTypeUpload", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "jpg,png",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: true, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.fileBigName = file.name;
				vm.categoryImageUrlBig = _yetAnotherFileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploaderBig.init(); 
var uploaderSmall = createUploader({
	buttonId: "uploadBtnSmall", 
	uploadType: "categoryUpload.categoryTypeUpload", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "jpg,png",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: true, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.fileSmallName = file.name;
				vm.categoryImageUrlSmall = _yetAnotherFileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploaderSmall.init(); 
