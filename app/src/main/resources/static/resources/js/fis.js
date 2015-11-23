/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var stations = $.get('stations.json', function(data){
    $("#station").typeahead({
		source:	data,
		showHintOnFocus: true,
		items: 10,
		minLength: 1,
		afterSelect: function(active){
			//alert(active.id);
			$("#stationId").val(active.id);
		}
	});
},'json');

$(function(){
	$(document).on('click', '#stationList a', function(){
		$("#stationId").val($(this).attr('href').substr(1));
		$("#station").val($(this).text());
		$("#stationList").collapse('hide');
	});
});

var trainRoutes = $.get('trainRoutes.json', function(data){
    $("#trainNumber").typeahead({
		source:	data,
		showHintOnFocus: true,
		items: 10,
		minLength: 1,
		displayText: function(item){
			return item.id + ': ' + item.stops[0].station.name + ' - ' + item.stops[item.stops.length - 1].station.name;
		},
		afterSelect: function(active){
			//alert(active.id);
			$("#trainRouteId").val(active.id);
		}
	});
},'json');

$(function(){
	$(document).on('click', '#trainRouteList a', function(){
		$("#trainRouteId").val($(this).attr('href').substr(1));
		$("#trainRoute").val($(this).text());
		$("#trainRouteList").collapse('hide');
	});
});