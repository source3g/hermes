$(document).ready(function() {
	$.ajaxSetup({
		cache : false
	});
});


function showPage(data) {
	$("#pageContentFrame").html(data)
}
function showError(data) {
	 alert("出错了");
}


