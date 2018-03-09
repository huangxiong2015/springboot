/**
 * 
 */

$(document).ready(function() {
	$("#ajaxSubmitButton").html("AJAX提交[" + ajaxCommitCount + "]次");
	$("#ajaxSubmitButton").addClass("easyui-linkbutton");
});

var ajaxCommitCount = 0;

function submitForm() {
	if ($('#dupSubmissionForm').form('validate')) {
		$('#dupSubmissionForm').submit();
	}
}

function ajaxSubmitForm() {
	if ($('#dupSubmissionForm').form('validate')) {
		$('#dupSubmissionForm').form('submit',{
		    onSubmit: function(param){
		    	param["isAjaxRequest"]=true;//增加提交字段
		    },
		    success:function(data){
		    	if(data.indexOf("请勿重复提交")>-1){
		    		alert("页面已过期，请勿重复提交！");
		    	}else{
		    		$("#ajaxSubmitButton").html("AJAX提交[" + (++ajaxCommitCount) + "]次");
		    	}
		    	
		    }
		}); 
	}
}

function clearForm() {
	$('#dupSubmissionForm').form('clear');
}
