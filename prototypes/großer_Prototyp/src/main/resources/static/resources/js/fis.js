/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var data = $.get('stations.json', function(data){
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