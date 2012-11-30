$(document).ready(function() {
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
