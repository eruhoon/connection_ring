/*******************************************************************************
	DOCUMENT ONLOAD
*******************************************************************************/
$(document).on('ready', function(e){
	Dash.init();
	Socket.init();
	Network.init();
	Dock.init();
	Navi.init();
});
