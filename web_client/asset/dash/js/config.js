var Config = {
	/***************************************************************************
		DASHBOARRD
	***************************************************************************/
	defaultDashBackground : "http://210.118.74.153/ring/asset/img/boardbg-default.jpg",
	dashWidth : 3200,
	dashHeight : 1800,
	

	/***************************************************************************
		DOCK
	***************************************************************************/
	defaultDockIconSize : 49,
	defaultDockIconMargin : 2,
	defaultDashboardDockIcon : "http://210.118.74.153/ring/asset/img/onboarderror.png",
	defaultAddDockIcon : "http://210.118.74.153/ring/asset/img/onwidgeterror.png",
	defaultDockIconSource : "http://kyosu.mooo.com/gallery/img.php?src=/resources/img/avatar/61.png",
	defaultDockIconSource_change : "http://210.118.74.153/ring/asset/img/button_addition-default.png",
	defaultDockIconSource_add : "http://210.118.74.153/ring/asset/img/button_newdash-default.png",
	defaultDockIconSource_invite : "http://210.118.74.153/ring/asset/img/button_invite-default.png",
	defaultDockIconSource_cancel : "http://210.118.74.153/ring/asset/img/button_cancel-default.png",


	/***************************************************************************
		GLOBAL FUNCTION
	***************************************************************************/
	cssUrl: function(url){
		return "url("+url+")";
	},

	getDatetime: function(d){
		if(!d) return null;
		function pad(n){ return n<10 ? '0'+n : n}
		return d.getFullYear()+'-'
			+ pad(d.getMonth()+1)+'-'
			+ pad(d.getDate()) +' '
			+ pad(d.getHours())+':'
			+ pad(d.getMinutes())+':'
			+ pad(d.getSeconds())
	},


	/***************************************************************************
		CLEAR SELECTION FUNCTION
	***************************************************************************/
	clearSelection: function(){
		if (window.getSelection) {
			if (window.getSelection().empty) {  // Chrome
				window.getSelection().empty();
			} else if (window.getSelection().removeAllRanges) {  // Firefox
				window.getSelection().removeAllRanges();
			}
		} else if (document.selection) {  // IE
			document.selection.empty();
		}
	},

	/***************************************************************************
		GENERAL MOUSE EVNET
	***************************************************************************/
	mouse : {
		onMove : function(e){
			
			// DashBoard
			if(Dash.isMove) {
				Config.clearSelection();
				Dash.setViewLeft(Dash.viewLeft - (e.clientX - Dash.offsetX));
				Dash.setViewTop(Dash.viewTop - (e.clientY - Dash.offsetY));

				Dash.offsetX = e.clientX;
				Dash.offsetY = e.clientY;
			}

			// Navigator
			if(Navi.isMove){
				Config.clearSelection();
				e.stopPropagation();
				Dash.setViewLeft(Dash.viewLeft + (e.clientX - Navi.offsetX) / Navi.widthRatio());
				Dash.setViewTop(Dash.viewTop + (e.clientY - Navi.offsetY) / Navi.heightRatio());

				Navi.offsetX = e.clientX;
				Navi.offsetY = e.clientY;
			}

			Dash.relocate();
			Navi.relocate();
		},

		onUp: function(e){
			if(Dash.isMove) {
				Dash.isMove = false;
				$("body").css("cursor", "auto");
	
				Dash.relocate();
				Navi.relocate()
			}

			if(Navi.isMove) {
				e.stopPropagation();

				Navi.isMove = false;
				$("body").css("cursor", "auto");          

				Dash.relocate();
				Navi.relocate();
			}
		}
	},
}