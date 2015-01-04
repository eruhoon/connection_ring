/*******************************************************************************
	DASHBOARD OBJECT
*******************************************************************************/
var Dash = {

	/***************************************************************************
		COMPONENT
	***************************************************************************/
	target : $("div#dashboard"),

	/***************************************************************************
		CONSTANT
	***************************************************************************/
	id : 0,

	/***************************************************************************
		VARIABLE
	***************************************************************************/
	selectedComponent : null,
	background : Config.defaultDashBackground,
	componentList : [],
	viewLeft : 0,
	viewTop : 0,


	/***************************************************************************
		STATE DUMMY
	***************************************************************************/
	isMove : false,
	offsetX : 0,
	offsetY : 0,


	/***************************************************************************
		INIT FUNCTION
	***************************************************************************/
	init : function(){
		Dash.id = auth.did;
		
		// WINDOW SIZE SETTING
		Dash.target.css("background-image", "url("+Dash.background+")");
		Dash.target.css("width", Config.dashWidth);
		Dash.target.css("height", Config.dashHeight);
		Dash.relocate();

		// EVENT REGISTER
		Dash.registerMouseEvent();
		Dash.registerWindowEvent();
	},


	/***************************************************************************
		INIT ALL COMPONENT
	***************************************************************************/
	initComponentList: function(componentList){
		for(var idx=0; idx<componentList.length; idx++){
			Dash.makeComponentObject(componentList[idx]);
		}
	},



	/***************************************************************************
		ADDITION FUNCTION
	***************************************************************************/
	makeComponentObject: function(componentObj){
		var newComponent = Component.getNewComponent();
		
		newComponent.cid = componentObj.cid;
		newComponent.title = componentObj.sub;
		newComponent.memo = componentObj.content;
		newComponent.x = parseInt(componentObj.x);
		newComponent.y = parseInt(componentObj.y);
		newComponent.date = componentObj.date;
		Dash.componentList.push(newComponent);
		
		Component.applyCustom(newComponent, componentObj.cuid);

		Dash.target.append(newComponent.target);
		Navi.target.append(newComponent.naviTarget);
	},

	/***************************************************************************
		BACKGROUND FUNCTION
	***************************************************************************/
	setBackground : function(url){
		Dash.background = url;
		Dash.target.css("background-image", "url("+url+"), url("+Config.defaultDashBackground+")");
	},

	/***************************************************************************
		RESIZE FUNCTION
	***************************************************************************/
	resize: function(){
		if(Dash.viewLeft + window.innerWidth > Config.dashWidth) Dash.viewLeft = Config.dashWidth - window.innerWidth;
		if(Dash.viewTop + window.innerHeight > Config.dashHeight) Dash.viewTop = Config.dashHeight - window.innerHeight;
		Dash.relocate();
		Navi.relocate();
	},

	/***************************************************************************
		WINDOW EVENT RESISTER FUNCTION
	***************************************************************************/
	registerWindowEvent: function(){
		$(window).resize(Dash.resize);
	},


	/***************************************************************************
		MOUSE EVENT RESISTER FUNCTION
	***************************************************************************/
	registerMouseEvent: function(){

		/***********************************************************************
			MOUSEDOWN
		***********************************************************************/
		Dash.target.off("mousedown").on("mousedown", function(e){
			Dock.contextmenu.fadeOut('300');
			Dock.componentContextmenu.fadeOut('300');

			// 1. Event Target Check
			if(!Dash.target.is(e.target)) return;
			if(e.which == 2) { e.preventDefault(); }
		
			// 2. Change State : Move
			Dash.isMove = true;
			$("body").css("cursor", "move");

			// 3. Save Mouse State
			Dash.offsetX = e.clientX;
			Dash.offsetY = e.clientY;
		});
		Dash.target.off("mousemove").on("mousemove", Config.mouse.onMove);
		Dash.target.off("mouseup").on("mouseup", Config.mouse.onUp);
	},


	/***************************************************************************
		POSITION FUNCTION
			setViewLeft
			setViewTop
			relocate
	***************************************************************************/
	setViewLeft : function(left){
		var maxLeft = Config.dashWidth - window.innerWidth;
		if(left < 0) left = 0;
		if(left > maxLeft) left = maxLeft;
		Dash.viewLeft = left;
	},

	setViewTop : function(top){
		var maxTop = Config.dashHeight - window.innerHeight;
		if(top < 0) top = 0;
		if(top > maxTop) top = maxTop;
		Dash.viewTop = top;
	},

	relocate : function(){
		Dash.target.css("left", -Dash.viewLeft);
		Dash.target.css("top", -Dash.viewTop);
	},




	



	/***************************************************************************
		GET AVAILABLE POSITION
	***************************************************************************/
	getNewX: function(){
		var margin = 100;
		return Dash.viewLeft+Math.floor(Math.random()*(window.innerWidth-margin));
	},
	getNewY: function(){
		var margin = 100;
		return Dash.viewTop+Math.floor(Math.random()*(window.innerHeight-margin));
	},

}