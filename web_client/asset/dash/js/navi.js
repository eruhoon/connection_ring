var Navi = {
	
	/***************************************************************************
		VARIABLE
	***************************************************************************/
	target : $("div#navi"),
	mapscope : $("div#mapscope"),


	/***************************************************************************
		STATE DUMMY
	***************************************************************************/
	isMove : false,
	offsetX : 0,
	offsetY : 0,

	init : function(){
		Navi.resize();
		Navi.registerWindowEvent();
		Navi.registerMoveEvent();
	},

	widthRatio : function(){
		var dashWidth = Config.dashWidth;
		var width = Navi.target.width();
		var widthRatio = width / dashWidth;
		return widthRatio;
	},

	heightRatio : function(){
		var dashHeight = Config.dashHeight;
		var height = Navi.target.height();
		var heightRatio = height / dashHeight;
		return heightRatio;
	},

	resize: function(){
		var scopeWidth = window.innerWidth * Navi.widthRatio();
		var scopeHeight = window.innerHeight * Navi.heightRatio();
		Navi.mapscope.width(scopeWidth);
		Navi.mapscope.height(scopeHeight);
	},



	/***************************************************************************
		WINDOW EVENT RESISTER FUNCTION
	***************************************************************************/
	registerWindowEvent: function(){
		$(window).resize(Navi.resize);
	},


	registerMoveEvent: function(){

		/***********************************************************************
			MOUSEUP
		***********************************************************************/
		Navi.target.off("mousedown").on("mousedown", function(e){
			Dock.contextmenu.fadeOut('300');
		});

		/***********************************************************************
			MOUSEUP
		***********************************************************************/
		Navi.mapscope.off("mousedown").on("mousedown", function(e){
			Dock.contextmenu.fadeOut('300');
			// 1. Event Target Check
			if(!Navi.mapscope.is(e.target)) return;
			e.stopPropagation();

			// 2. Change State : Move
			Navi.isMove = true;
			$("body").css("cursor", "move");

			// 4. Save Mouse State
			Navi.offsetX = e.clientX;
			Navi.offsetY = e.clientY;
		});

		Navi.target.off("mousemove").on("mousemove", Config.mouse.onMove);
		Navi.mapscope.off("mousemove").on("mousemove", Config.mouse.onMove);
		Navi.target.off("mouseup").on("mouseup", Config.mouse.onUp);
		Navi.mapscope.off("mouseup").on("mouseup", Config.mouse.onUp);
	},


	/***************************************************************************
		RELOCATE
	***************************************************************************/
	relocate : function(){
		var scopeTop = Dash.viewTop * Navi.heightRatio();
		var scopeLeft = Dash.viewLeft * Navi.widthRatio();
		Navi.mapscope.css("left", scopeLeft);
		Navi.mapscope.css("top", scopeTop);
	}
}