$(document).ready(function() {
	$.ajaxSetup({
		cache : false
	});
});


function showInfo(data) {
	$("#pageContentFrame").html(data)
}

