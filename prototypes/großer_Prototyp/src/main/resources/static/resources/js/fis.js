/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var data = $.get('stations.json', function(data){
    $("#station").typeahead({
		source:	data,
		showHintOnFocus: true,
		items: 'all',
		afterSelect: function(active){
			//alert(active.id);
			$("#stationId").val(active.id);
		}
	});
},'json');