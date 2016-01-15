/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* station typeahead */
var stations = $.get('../stations.json', function(data){
    $("#station").typeahead({
		source:	data,
		showHintOnFocus: true,
		items: 10,
		minLength: 1,
		afterSelect: function(active){
			//alert(active.id);
			$("#stationId").val(active.id);
			window.location.replace("./"+active.id);
		}
	});
},'json');

/* select content when clikcing on the input */
$(function(){
	$(document).on('click', '#station, #trainRoute', function(){
		$(this).select();
	});
});

/* click event on a station list link */
$(function(){
	$(document).on('click', '#stationList a', function(){
		$("#stationId").val($(this).attr('href').substr(1));
		$("#station").val($(this).text());
		$("#stationList").collapse('hide');
	});
});

/* train route typeahead */
var trainRoutes = $.get('../trainRoutes.json', function(data){
    $("#trainRoute").typeahead({
		source:	data,
		showHintOnFocus: true,
		items: 10,
		minLength: 1,
		displayText: function(item){
			return item.name + ': ' + item.begin.name + " – " + item.end.name;
		},
		afterSelect: function(active){
			$("#trainRouteId").val(active.id);
			window.location.replace("./"+active.id);			
		}
	});
},'json');

/* click event on a train route list link */
$(function(){
	$(document).on('click', '#trainRouteList a', function(){
		$("#trainRouteId").val($(this).attr('href').substr(1));
		$("#trainRoute").val($(this).text());
		$("#trainRouteList").collapse('hide');
	});
});

/* stationList */
$(function(){
	$('#stationList').on('show.bs.collapse hide.bs.collapse', function(){
		$("#stationMenu").toggleClass('glyphicon-menu-down');
		$("#stationMenu").toggleClass('glyphicon-menu-up');
	});
});

/* filter options */
$(function(){
	$('#filterOptions').on('show.bs.collapse hide.bs.collapse', function(){
		$("#filterMenu").toggleClass('glyphicon-menu-down');
		$("#filterMenu").toggleClass('glyphicon-menu-right');
	});
});

/* test timespan */
$(function(){
	$('#testTime').on('click', function(){
		$("input[name='start']").val('10:00');
		$("input[name='end']").val('12:00');
	});
});

/* auto reload */
// Zeit aktualisieren (aller 5s)
setInterval(function(){
	$.ajax({
		type	: 'GET',
		cache	: false,
		async	: true,
		url		: '/currentTime',
		success	: function(data) {
			$("#time").empty().append(data);
		}
	});
}, 5000);

// Tabelle aktualisieren
setInterval(function(){
	var form = $("#filter");
	var reloadSelected = $("input[name='reload']:checked");
	var reloadValue = "off";
	if (reloadSelected.length > 0){
		reloadValue = reloadSelected.val();
	}
	
	switch (reloadValue){
		case 'update':
			$("#start").val("");
			$("#end").val("");
			// no break
		case 'current':
			$.ajax({
				type	: 'POST',
				cache	: false,
				async	: true,
				url		: form.attr('action'),
				data	: form.serialize(),
				success	: function(data) {
					$("#traintable").empty().append(data);
					$('html, body').animate({
						scrollTop: $("#traintable").offset().top
					}, 1000);
				}
			});
			break;
		default : return;
	}
			
	$("#start").val($("#_newStart").val());
	$("#end").val($("#_newEnd").val());
}, 20000);