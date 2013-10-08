var origContent = "";
var isInit = false;
var hashCode = "";
var pageMap = new Array();
function loadContent(hash) {
	if (hash != "") {
		//alert(hash);
		if (hash.indexOf("html_") > -1) {
		//	alert("有了");
			var html=getByHash(hash);
		//	alert(html);
			if(html!=null){
				$('#pageContentFrame').html(html);
			}
			return;
		}
		if (origContent == "") {
			origContent = $('#pageContentFrame').html();
		}
		$("#pageContentFrame").load(hash);
	} else if (origContent != "") {
		$('#pageContentFrame').html(origContent);
	}
}


function activeMenu(content){
	$("#"+content).parents(".collapse").collapse("show");
	$("#"+content).addClass("active");
}

function loadPage(url) {
	//alert(url);
	//$.history.load(url);
	//alert(url);
	//alert(url);
	window.location=url;
}

function refresh(){
	window.location.replace(window.location.href);
	//alert(ctx);
}

$(document).ready(function() {
	if (!isInit) {
		$.history.init(loadContent);
		isInit = true;
	}
	$.ajaxSetup({
		cache : false
	});
});

function showContentInfo(data) {
	//$("#pageContentFrame").html(data);
	var hash = "html_" + new Date().getTime();
	//alert(hash);
	pushHtml(hash,data);
	// alert(hash);
	$.history.load(hash);
	if (pageMap.length > 10) {
		pageMap.shift();
	}
}

function getByHash(hash){
	for (var i=0;i<pageMap.length;i++){
		if(pageMap[i][hash]!=null){
			return pageMap[i][hash];
		}
	}
	return null;
}

function pushHtml(hash, html) {
	var a=new Array();
	a[hash] = html;
	//alert("a[hash]"+a[hash]);
	pageMap.push(a);
}

function showError(data) {
	alert("出错了");
}

function initPage(currentPageNo, totalPageCount) {
	$('#pageOk').click(function() {
		var pageNoToGo = $('#pageNoToGo').val();
		goToPage(pageNoToGo);
	});
	if (totalPageCount == 1 || totalPageCount == 0) {
		$("#firstPage").addClass("active");
		$("#frontPage").addClass("active");
		$("#nextPage").addClass("active");
		$("#lastPage").addClass("active");
	} else if (currentPageNo == 1) {
		$("#firstPage").addClass("active");
		$("#frontPage").addClass("active");
		$("#nextPage").removeClass("active");
		$("#lastPage").removeClass("active");

		$('#nextPage').click(function() {
			goToPage(currentPageNo + 1);
		});
		$("#lastPage").click(function() {
			goToPage(totalPageCount);
		});

	} else if (currentPageNo == totalPageCount) {
		$("#firstPage").removeClass("active");
		$("#frontPage").removeClass("active");
		$("#nextPage").addClass("active");
		$("#lastPage").addClass("active");

		$("#firstPage").click(function() {
			goToPage(1);
		});
		$("#frontPage").click(function() {
			goToPage(currentPageNo - 1);
		});
	} else {
		$("#firstPage").click(function() {
			goToPage(1);
		});
		$("#frontPage").click(function() {
			goToPage(currentPageNo - 1);
		});
		$("#nextPage").click(function() {
			goToPage(currentPageNo + 1);
		});
		$("#lastPage").click(function() {
			goToPage(totalPageCount);
		});
	}
}
