/*******************************************************************************
	NETWORK OBJECT
*******************************************************************************/	
var Network = {
	

	/***************************************************************************
		INIT
	***************************************************************************/
	init: function(){
		Network.callGetCustomList();
	},


	

	/***************************************************************************
		AJAX CALL FUNCTION
	***************************************************************************/
	callGetCustomList: function(){
		$.ajax({
			type: 'POST',
			url: 'http://210.118.74.153/ring/index.php/custom/get_uselist',
			dataType: 'json',
			success: function(data, status, req){
				if(!data.result){
					alert(data.msg);
					return;
				}
				Network.onGetCustomList(data.res);
			}
		});
	},


	callGetDashInfo: function(_did){
		$.ajax({
			type: 'POST',
			data: {
				did: _did,
			},
			url: 'http://210.118.74.153/ring/index.php/dash_module/info/',
			dataType: 'json',
			success: function(data, status, req){
				if(!data.result){
					alert(data.msg);
					return;
				}
				console.log(data);
				Network.onGetDashInfo(data.res);
			}
		})
	},




	/***************************************************************************
		AJAX CALLBACK FUNCTION
	***************************************************************************/
	onGetCustomList: function(res){
		Dock.initAddDock(res);
	},

	onGetDashInfo: function(res){
		Dock.makeDashboardIcon(res)
	},
}
	