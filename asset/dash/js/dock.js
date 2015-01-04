/*******************************************************************************
	DOCK OBJECT
*******************************************************************************/
var Dock = {

	/***************************************************************************
		COMPONENT
	***************************************************************************/
	dashboardDock : $("div#dashboardDock"),
	addDock : $("div#addDock"),
	dashListSection : $("div#dashDockList"),
	addListSection : $("div#addDockList"),

	changeDockButton: $("div#changeDock"),
	cancelDockButton: $("div#cancelDock"),
	addDashboardButton : $("div#addDash"),
	inviteButton : $("div#invite"),

	dockHelperSection : $("div#dockHelper"),
	nameLabel : $("div#dockHelper span"),

	target : $("div#addDock"),
	contextmenu: $("#contextmenu"),
	componentContextmenu: $("#componentcontextmenu"),

	/***************************************************************************
		VARIABLE
	***************************************************************************/
	dashList : [],
	customList : [],
	list : [],
	dockIconSize: Config.defaultDockIconSize,
	dockSize: Config.defaultDockIconSize + 2 * Config.defaultDockIconMargin,

	/***************************************************************************
		STATE
	***************************************************************************/
	isAddMode: false,
	selectedDash: null,
	numInviteList: 0,







	/***************************************************************************
		INIT ALL COMPONENT
	***************************************************************************/
	init : function(){
		Dock.initDashboardDock();
		Dock.initAddDock();
		Dock.initFunctionButton();
		Dock.registerClickEvent();
		Dock.registerMoveEvent();
		Dock.resize();
	},

	/***************************************************************************
		INIT DASHBOARD DOCK
		USING @ Socket.onGetDashList
	***************************************************************************/
	initDashboardDock: function(dashList){
		if(!dashList) return;
		for(var idx = 0; idx<dashList.length; idx++){
			Dock.makeDashboardIcon(dashList[idx]);   
		}

	},

	/***************************************************************************
		INIT DASHBOARD DOCK
	***************************************************************************/
	initAddDock: function(customList){
		if(!customList) return;
		for(var idx = 0; idx<customList.length; idx++){
			Dock.makeAddIcon(customList[idx]);
		}
	},

	/***************************************************************************
		DASHBOARD-DOCK ICON MAKE MODULE
	***************************************************************************/
	makeDashboardIcon : function(dashObj){
		var newDash = {
			did: dashObj.did,
			dname: dashObj.dashname,
			icon: dashObj.imgsrc,
			target : $(document.createElement("div")),
			link : "http://210.118.74.153/ring/index.php/dash/view/"+dashObj.did,
		};
		Dock.dashList.push(newDash);
		Dock.dashboardIconFactory(newDash);
		Dock.dashListSection.append(newDash.target);

		if(Dock.isAddMode) { Dock.toggleDock(); }
		Dock.resize();
	},

	/***************************************************************************
		INIT ADD ICON MAKE MODULE
	***************************************************************************/
	makeAddIcon: function(customObj){
		var newCustom = {
			cuid: customObj.cuid,
			cuname: customObj.customName,
			target: $(document.createElement("div")),
			icon: "http://210.118.74.153/ring/index.php/custom/layout_xml/"+customObj.cuid,
		};
		Dock.customList.push(newCustom);
		Dock.addIconFactory(newCustom);
		Dock.addListSection.append(newCustom.target);

		Dock.resize();
	},






	/***************************************************************************
		DASHBOARD ICON FACTORY
	***************************************************************************/
	dashboardIconFactory: function(dashObj) {
		var dom = dashObj.target;
		dom.addClass("dockicon");
		dom.css("background-image", "url(http://210.118.74.153/ring/index.php/dash_module/get_icon/?src="+dashObj.icon+"), url("+Config.defaultDashboardDockIcon+")");

		dom.off("contextmenu").on("contextmenu", function(e){
			e.preventDefault();
			Dock.contextmenu.fadeIn(300);
			Dock.contextmenu.css('left', e.clientX);
			Dock.contextmenu.css('top', e.clientY);

			$('#contextDashname').text(dashObj.dname);

			Dock.selectedDash = dashObj;
			return false;
		});

		dom.off("click").on("click", function(e){
			if(!dom.is(e.target)) return;
			location.href = dashObj.link;
		});

		Dock.registerNametagHelper(dom, dashObj.dname);
	},

	/***************************************************************************
		ADD ICON FACTORY
	***************************************************************************/
	addIconFactory: function(customObj) {
		var dom = customObj.target;
		dom.addClass("dockicon");
		dom.css("background-image", "url("+customObj.icon+"), url("+Config.defaultAddDockIcon+")");

		dom.off("click").on("click", function(e){
			if(!dom.is(e.target)) return;

			var did = Dash.id;
			var title = "Default Component @WEB";
			var cuid = customObj.cuid;
			var content = "Default content";
			var x = Dash.getNewX();
			var y = Dash.getNewY();
			var date = null;
			Socket.callComponentNew(did, title, cuid, content, x, y, date);
		});

		Dock.registerNametagHelper(dom, customObj.cuname);
	},









	/***************************************************************************
		RESIZE MODULE
	***************************************************************************/
	resize : function(){
		// 0. Constants
		var iconSize = Dock.dockIconSize;
		var dockHeight = Dock.dockSize;
		var changeIconSize = Dock.dockIconSize;
		var addIconSize = Dock.dockIconSize;
		var cancelIconSize = Dock.dockIconSize;
		
		var dashDockWidth = (Dock.dashList.length + 1)*Dock.dockSize + 10;
		var addDashWidth = (Dock.customList.length + 3)*Dock.dockSize + 30;
		var dashMarginLeft = -dashDockWidth / 2;
		var addMarginLeft = -addDashWidth / 2;


		// 1. Dashboard Dock Resize
		Dock.dashboardDock.css("margin-left", dashMarginLeft);
		Dock.dashboardDock.height(dockHeight);
		Dock.dashboardDock.width(dashDockWidth);
		
		// 2. Add Dock Resize
		Dock.addDock.css("margin-left", addMarginLeft);
		Dock.addDock.height(dockHeight);
		Dock.addDock.width(addDashWidth);

		// 3. Button List Resize
		var dockList = $("div.dockicon");
		for(var didx = 0; didx < dockList.length; didx++){
			$(dockList[didx]).css("height", iconSize);
			$(dockList[didx]).css("width", iconSize);
			$(dockList[didx]).css("background-size", iconSize+"px "+iconSize+"px");
			$(dockList[didx]).css("margin", Config.defaultDockIconMargin);
		}

		// 4. Dock Helper Resize
		Dock.dockHelperSection.css("top", dockHeight);
		
	},








	/***************************************************************************
		DOCK FORMAT INITIATE
	***************************************************************************/
	initFunctionButton: function(){
		
		Dock.changeDockButton.css("background-image", "url("+Config.defaultDockIconSource_change+")");
		Dock.addDashboardButton.css("background-image", "url("+Config.defaultDockIconSource_add+")");

		Dock.inviteButton.css("background-image", "url("+Config.defaultDockIconSource_invite+")");
		Dock.cancelDockButton.css("background-image", "url("+Config.defaultDockIconSource_cancel+")");


		/***********************************************************************
			MODAL BUTTON
		***********************************************************************/
		$('#callDashNew').on("click", function(e){
			var dashname = $('#input_dashName').val();
			var imgsrc = $('#input_dashImgsrc').val();
			Socket.callDashNew(dashname, imgsrc);
			$('#addDashboard').modal('hide');
		});

		$('#callInvite').on("click", function(e){
			$('#inviteFriend').modal('hide');
			if(auth.isHome) {
				alert("홈페이지에서는 사용하실수 없습니다.");
				return;
			}
			var did=Dash.id;
			var dname = null;
			var imgsrc = null;
			for(var idx=0; idx<Dock.dashList.length; idx++){
				if(did == Dock.dashList[idx].did){
					dname = Dock.dashList[idx].dname;
					imgsrc = Dock.dashList[idx].icon;
				}
			}
			var fid=$('#input_fid').val();
			Socket.callDashInvite(did, fid, dname, imgsrc);
		});

		$('#callDashUpdate').on("click", function(e){
			var did = Dock.selectedDash.did;
			var dashname = $('#update_dashName').val();
			var imgsrc = $('#update_dashImgsrc').val();
			$('#update_dashName').val('');
			$('#update_dashImgsrc').val('');
			$('#modal_viewDetail').modal('hide');
			var msg = 'dashname';
			if(Dock.selectedDash.icon != $('#update_dashImgsrc').val()){
				msg = 'imgsrc';
			}
			Socket.callDashUpdate(did, dashname, imgsrc, msg);
		});

		$('#callDashDelete').on("click", function(e){
			var did = Dock.selectedDash.did;
			$('#modal_dashDelete').modal('hide');
			Socket.callDashDelete(did);
		});

		/***********************************************************************
			CONTEXT BUTTON
		***********************************************************************/
		$('#showDetail').on("click", function(e){
			Dock.contextmenu.fadeOut('300');
			$('#modal_viewDetail').modal('show');
			$('#update_dashName').val(Dock.selectedDash.dname);
			$('#update_dashImgsrc').val(Dock.selectedDash.icon);
		});

		$('#context_deleteDash').on("click", function(e){
			Dock.contextmenu.fadeOut('300');
			$('#modal_dashDelete').modal('show');
		});
	},






	/***************************************************************************
		MOUSE EVENT RESISTER FUNCTION
	***************************************************************************/
	registerMoveEvent: function(){
		Dock.target.off("mousemove").on("mousemove", Config.mouse.onMove);
		Dock.target.off("mouseup").on("mouseup", Config.mouse.onUp);
	},

	registerClickEvent: function(){

		// DASHBOARD ADDITION BUTTON
		Dock.registerNametagHelper(Dock.addDashboardButton, "대시보드");

		// INVITE BUTTON
		Dock.registerNametagHelper(Dock.inviteButton, "친구 초대");

		// ADD MODE BUTTON
		Dock.registerNametagHelper(Dock.changeDockButton, "컴포넌트 추가");
		Dock.changeDockButton.off("click").on("click", function(e){
			Dock.contextmenu.fadeOut('300');
			if(!Dock.changeDockButton.is(e.target)) return;
			Dock.toggleDock();
		});

		// CANCEL BUTTON
		Dock.registerNametagHelper(Dock.cancelDockButton, "취소");
		Dock.cancelDockButton.off("click").on("click", function(e){
			if(!Dock.cancelDockButton.is(e.target)) return;
			Dock.toggleDock();
		});
	},


	/***************************************************************************
		NAME TAG HELPER RESISTER FUNCTION
	***************************************************************************/
	registerNametagHelper: function(iconObj, text){
		iconObj.off("mouseover").on("mouseover", function(e){
			if(!iconObj.is(e.target)) return;
			Dock.nameLabel.text(text);
			Dock.dockHelperSection.removeClass("hidden");
		});

		iconObj.off("mouseout").on("mouseout", function(e){
			if(!iconObj.is(e.target)) return;
			Dock.dockHelperSection.addClass("hidden");
		});
	},




	/***************************************************************************
		TOGGLE DOCK
	***************************************************************************/
	toggleDock: function(){
		if(Dock.isAddMode){
			Dock.isAddMode = false;
			Dock.dashboardDock.removeClass("hidden");
			Dock.addDock.addClass("hidden");
		}else{
			Dock.isAddMode = true;
			Dock.dashboardDock.addClass("hidden");
			Dock.addDock.removeClass("hidden");
		}	
	},






	/***************************************************************************
		UPDATE DASH @ socket.onDashDelete
	***************************************************************************/
	updateDash: function(dashObj){
		for(var idx = 0; idx < Dock.dashList.length; idx++){
			if(Dock.dashList[idx].did == dashObj.did){
				var d = Dock.dashList[idx];
				d.icon = dashObj.imgsrc;
				d.dname = dashObj.dashname;
				Dock.dashboardIconFactory(d);
				break;
			}
		}
		if(Dock.isAddMode) { Dock.toggleDock(); }
		Dock.resize();
	},

	/***************************************************************************
		DELETE DASH @ socket.onDashDelete
	***************************************************************************/
	deleteDash: function(dashObj){
		for(var idx = 0; idx < Dock.dashList.length; idx++){
			if(Dock.dashList[idx].did == dashObj.did){
				Dock.dashList[idx].target.remove();
				var delObj = Dock.dashList.splice(idx, 1);
				delObj = null;
				break;
			}
		}
		if(Dock.isAddMode) { Dock.toggleDock(); }
		Dock.resize();
	},

	/***************************************************************************
		INVITE FRIEND RESPONSE @ socket.onGetInviteList
	***************************************************************************/
	showInviteList: function(inviteObj){
		console.log(inviteObj);
		$('#modalList_inviteDashList').empty();
		Dock.numInviteList = inviteObj.length;
		for(var idx=0; idx<inviteObj.length; idx++){
			var did = inviteObj[idx].dash_did;
			var dname = inviteObj[idx].dashname;
			var imgsrc = inviteObj[idx].imgsrc;
			var btnAgree = $(document.createElement('button')).addClass('btn btn-success').text('수락');
			var btnDisagree = $(document.createElement('button')).addClass('btn btn-default').text('거절');
			var menu = $(document.createElement('div')).addClass('panel panel-default')
				.attr('id', 'inviteList_'+did)
				.append(
				$(document.createElement('div')).addClass('panel-body').append(
					$(document.createElement('div')).addClass('media').append(
						$(document.createElement('a')).addClass('pull-left').append(
							$(document.createElement('img'))
							.addClass('media-object img-thumbnail img-resposive img-rounded')
							.attr('src', 'http://210.118.74.153/ring/index.php/dash_module/get_icon/?src='+imgsrc)
							.css('height', 60)
							.css('width', 60)
						),
						$(document.createElement('div')).addClass('media-body').append(
							$(document.createElement('h4')).addClass('media-heading').text(dname),
							$(document.createElement('div')).addClass('btn-group').append(
								btnAgree,
								btnDisagree
							)
						)
					)
				)
			).appendTo('#modalList_inviteDashList');

			btnAgree.attr({
				'data-did': did,
				'data-dname': dname,
			});

			btnAgree.on('click', function(e){
				Socket.callInviteResponse($(this).data('did'), null, $(this).data('dname'), 'True');
				Dock.numInviteList--;
				if(!Dock.numInviteList) {
					$("#modal_inviteFriendResponse").modal('hide');
				}
				else {
					$('#inviteList_'+$(this).data('did')).fadeOut('300', function(){
						$('#inviteList_'+$(this).data('did')).remove();		
					});
				}
			});

			btnDisagree.attr({
				'data-did': did,
				'data-dname': dname,
			});
			btnDisagree.on('click', function(e){
				Socket.callInviteResponse($(this).data('did'), null, $(this).data('dname'), 'False');
				Dock.numInviteList--;
				if(!Dock.numInviteList) {
					$("#modal_inviteFriendResponse").modal('hide');
				}
				else {
					$('#inviteList_'+$(this).data('did')).fadeOut('300', function(){
						$('#inviteList_'+$(this).data('did')).remove();		
					});
				}
			});

		}
		$("#modal_inviteFriendResponse").modal('show');
	},

}

