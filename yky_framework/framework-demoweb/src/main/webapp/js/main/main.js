/**
 *
 */
//第一层目录
var menuLevel1 = [{
	"id" : "depSubmission",
	"title" : "防重复提交",
	"options" : {
		"iconCls" : "icon-save"
	},
	"fit" : true,
	"selected" : true
}, {
	"id" : "umeng",
	"title" : "友盟推送",
	"options" : {
		"iconCls" : "icon-save"
	},
	"fit" : true,
	"selected" : true
}];

//第二层目录
var menuLevel2 = {
	"umeng" : [{
		"id" : "menu_001",
		"title" : "友盟广播消息推送",
		"url" : "umeng/goBroadcastPage.htm"
	}, {
		"id" : "menu_002",
		"title" : "友盟设备发送消息",
		"url" : "umeng/goListBroadcastPage.htm"
	}],
	"depSubmission" : [{
		"id" : "menu_003",
		"title" : "防重复提交",
		"url" : "dupSubmission.htm"
	}]
};

$(document).ready(function() {
	var content = '<ul>';
	var firstMenuId  ;
	$(menuLevel1).each(function(index, value) {
		content += '<li class="block" onclick="addChildMenu(\'' + value["id"] + '\')" id="menu_' + value["id"] + '" isAddChild="false">';
		if(index==0){
			content += '<input checked="checked" type="checkbox" name="menu_input_' + value["id"] + '" id="menu_item_' + value["id"] + '" /> <label for="menu_item_' + value["id"] + '"><i aria-hidden="true" class="icon-users"></i>';
			firstMenuId = value["id"];
		}else{
			content += '<input type="checkbox" name="menu_input_' + value["id"] + '" id="menu_item_' + value["id"] + '" /> <label for="menu_item_' + value["id"] + '"><i aria-hidden="true" class="icon-users"></i>';
		}	
		
		content += value["title"] + '<span class="menu-span">124</span></label></li>';

	});
	content += '</ul>';
	$("#west").html(content);
	addChildMenu(firstMenuId);
});

function addChildMenu(id) {
	// alert($("#menu_" + id).attr("isAddChild"));
	if ("false" == $("#menu_" + id).attr("isAddChild")) {
		var content = '<ul class="options">';
		$(menuLevel2[id]).each(function(index, value) {
			content += '<li><a href="#" onclick=\'menuClick('+JSON.stringify(value)+')\'><i aria-hidden="true" class="icon-search"></i>' + value["title"] + '</a></li>';
		});
		content += '</ul>';
		$("#menu_" + id).html($("#menu_" + id).html() + content);
		$("#menu_" + id).attr("isAddChild", "true");
	}
}

function menuClick(value) {
	if ($('#center').tabs('exists', value["title"])) {
		$('#center').tabs('select', value["title"]);
	} else {
		var tab = $.extend({}, value);
		var content = '<iframe scrolling="auto" frameborder="0"  src="' + value["url"] + '" style="width:100%;height:100%;"></iframe>';
		tab["closable"] = true;
		tab["content"] = content;
		tab["tools"] = [{
			iconCls : 'icon-mini-refresh',
			handler : function() {
				$('#center').tabs('select', value["title"]);
				var tab = $('#center').tabs('getSelected');
				// get selected panel
				$('#center').tabs('update', {
					tab : tab,
					options : {
						href : value["url"] // the new content URL
					}
				});
			}
		}];
		$('#center').tabs('add', tab);
	}
}
