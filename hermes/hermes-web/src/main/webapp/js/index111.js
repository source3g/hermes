$(document).ready(function() {

	// ajax menu checkbox
	/*
	 * $('#is-ajax').click(function(e){
	 * $.cookie('is-ajax',$(this).prop('checked'),{expires:365}); });
	 * $('#is-ajax').prop('checked',$.cookie('is-ajax')==='true' ? true :
	 * false);
	 */

	// disbaling some functions for Internet Explorer
	/*
	 * if($.browser.msie) { $('#is-ajax').prop('checked',false);
	 * $('#for-is-ajax').hide(); $('#toggle-fullscreen').hide();
	 * $('.login-box').find('.input-large').removeClass('span10'); }
	 */

	// highlight current / active link
	$('ul.main-menu li a').each(function() {
				if ($($(this))[0].href == String(window.location))
					$(this).parent().addClass('active');
			});

	// establish history variables
	var History = window.History, // Note: We are using a capital H instead of
	// a lower h
	State = History.getState(), $log = $('#log');

	// bind to State Change
	History.Adapter.bind(window, 'statechange', function() { // Note: We are
				// using
				// statechange
				// instead of
				// popstate
				var State = History.getState(); // Note: We are using
				// History.getState() instead of
				// event.state
				$.ajax({
							url : State.url,
							success : function(msg) {
								$('#content').html($(msg).find('#content')
										.html());
								$('#loading').remove();
								$('#content').fadeIn();
								docReady();
							}
						});
			});

	// ajaxify menus
	$('a.ajax-link').click(function(e) {
		if ($.browser.msie)
			e.which = 1;
		if (e.which != 1 || !$('#is-ajax').prop('checked')
				|| $(this).parent().hasClass('active'))
			return;
		e.preventDefault();
		if ($('.btn-navbar').is(':visible')) {
			$('.btn-navbar').click();
		}
		$('#loading').remove();
		$('#content')
				.fadeOut()
				.parent()
				.append('<div id="loading" class="center">Loading...<div class="center"></div></div>');
		var $clink = $(this);
		History.pushState(null, null, $clink.attr('href'));
		$('ul.main-menu li.active').removeClass('active');
		$clink.parent('li').addClass('active');
	});

	// animating menus on hover
	$('ul.main-menu li:not(.nav-header)').hover(function() {
				$(this).animate({
							'margin-left' : '+=5'
						}, 300);
			}, function() {
				$(this).animate({
							'margin-left' : '-=5'
						}, 300);
			});
	$("#pageContentFrame").load(function() {
				var mainheight = $(this).contents().find("body").height() + 30;
				$(this).height(mainheight);
			});
		// other things to do on document ready, seperated for ajax calls
});
