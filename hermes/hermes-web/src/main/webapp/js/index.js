var origContent = "";
var isInit = false;
var hashCode="";
function loadContent(hash) {
	if (hash != "") {
		if (origContent == "") {
			origContent = $('#pageContentFrame').html();
		}
		loadPage(hash);
	} else if (origContent != "") {
		$('#pageContentFrame').html(origContent);
	}
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
	$("#pageContentFrame").html(data)
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