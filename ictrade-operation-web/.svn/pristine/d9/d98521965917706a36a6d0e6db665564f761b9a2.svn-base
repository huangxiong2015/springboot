function getParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(decodeURI(r[2]));
	return null;
}
/**
 * 分页
 * @param url 请求URL（可以带参数）
 */
function loadPage(url)
{
	var totalRecords= $("#total").val();//总记录数
	var size= $("#pageSize").val();//每页条数
	var sendUrl="?page=";
	var t = url.indexOf('?'); 
	if (t >= 0)sendUrl="&page=";
var totalRecords = totalRecords;//总数量
var totalPage = Math.ceil(totalRecords / size);//总页数
var pageNo = getParameter('page');
		if (!pageNo) {
			pageNo = 1;
		}
		kkpager.generPageHtml({
			pno : pageNo,
			total : totalPage,//总页码
			totalRecords : totalRecords,//总数据条数
			hrefFormer : 'pager_test',//链接前部
			hrefLatter : '.html', //链接尾部
			getLink : function(n) {
				var ss = (parseInt(n));
				return url +sendUrl + ss;
			}
		});
}