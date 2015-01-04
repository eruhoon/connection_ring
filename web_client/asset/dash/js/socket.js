/*******************************************************************************
	SOCKET OBJECT
*******************************************************************************/
var Socket = {
	ws : null,
	wsHost: "210.118.74.153",
	wsPort: "8000",

	/***************************************************************************
		SOCKET INIT
	***************************************************************************/
	init: function(){
		Socket.ws = new WebSocket("ws://"+Socket.wsHost+":"+Socket.wsPort+"/");
		Socket.regiserSocketEvent();
	},

	regiserSocketEvent: function(){
		Socket.ws.onopen = function(e){
			Socket.callLogin();
		}

		Socket.ws.onmessage = function(p) {
			try{
				var res = JSON.parse(p.data);
				console.log("이벤트콜", res.type);
				console.log(res);
				
				switch(res.type){
					case 'login': Socket.onLogin(res); break;
					case 'logout': break;
					case 'logoutAction': Socket.onLogoutAction(res); break;
					case 'liveMessage': Socket.onLiveMessage(res); break;
					
					case 'getDashList': Socket.onGetDashList(res); break;
					case 'getDashComponentList': Socket.onGetDashComponentList(res); break;
					case 'getComponentList': Socket.onGetComponentList(res); break;
					case 'getInviteList': Socket.onGetInviteList(res); break;

					case 'dashNew': Socket.onDashNew(res); break;
					case 'dashNewAction': Socket.onDashNewAction(res); break;
					case 'dashUpdate': Socket.onDashUpdate(res); break;
					case 'dashUpdateAction': Socket.onDashUpdateAction(res); break;
					case 'dashDelete': Socket.onDashDelete(res); break;
					case 'dashDeleteAction': Socket.onDashDeleteAction(res); break;
					case 'dashInvite': Socket.onDashInvite(res); break;
					case 'inviteResponseTrue': Socket.onInviteResponseTrue(res); break;
					case 'inviteResponseFalse': Socket.onInviteResponseFalse(res); break;
					case 'componentNew': Socket.onComponentNew(res); break;
					case 'componentNewAction': Socket.onComponentNewAction(res); break;
					case 'componentUpdate': Socket.onComponentUpdate(res); break;
					case 'componentUpdateAction': Socket.onComponentUpdateAction(res); break;
					case 'componentDelete': Socket.onComponentDelete(res); break;
					case 'componentDeleteAction': Socket.onComponentDeleteAction(res); break;
					default: break;
				}
			}catch(e){

			}
		}
	},


	/***************************************************************************
		CALL SOCKET FUNCTION
	***************************************************************************/
	callLogin: function(){
		var req = {
			type: "login",
			id: auth.id,
			pw: auth.key,
			target: "web",
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callLogout: function(){
		var req = {
			type: "logout",
			id: auth.id,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callLiveMessage: function(){
		var req = {
			type: "liveMessage",
			id: auth.id,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callGetDashList: function(){
		var req = {
			type : "getDashList",
			id : auth.id,
			target : "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callGetComponentList: function(_did){
		var req = {
			type: "getComponentList",
			did: _did,
			id: auth.id,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callDashNew: function(_dashname, _imgsrc){
		var req = {
			type: "dashNew",
			id: auth.id,
			dashname: _dashname,
			imgsrc: _imgsrc,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callDashDelete: function(_did){
		var req = {
			type:"dashDelete",
			did: _did,
			id: auth.id,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callDashUpdate: function(_did, _dashname, _imgsrc, _msg){
		var req = {
			type: "dashUpdate",
			id: auth.id,
			did: _did,
			dashname: _dashname,
			imgsrc: _imgsrc,
			target: "web",
			msg: _msg
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callDashInvite: function(_did, _fid, _dname, _imgsrc){
		var req = {
			type: "dashInvite",
			did: _did,
			id: auth.id,
			dashname: _dname,
			imgsrc: _imgsrc,
			fid: _fid,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callGetInviteList: function(){
		var req = {
			type: "getInviteList",
			id: auth.id,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callInviteResponse: function(_did, _fid, _dashname, _inviteRes){
		var req = {
			type: "inviteResponse",
			did: _did,
			id: auth.id,
			dashname: _dashname,
			fid: _fid,
			Response: _inviteRes,
			target: "web",
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callGetDashComponentList: function(){
		var req = {
			type: "getDashComponentList",
			id: auth.id,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callComponentNew: function(_did, _title, _cuid, _content, _x, _y, _date){
		var dateFormat = (_date!=null)?Config.getDatetime(_date):null
		if(dateFormat=="0000-00-00 00:00:00") dateFormat = null;
		var req = {
			type: "componentNew",
			id: auth.id,
			did: _did,
			cuid: _cuid,
			typec: (dateFormat)?'calendar':'memo',
			sub: _title,
			content: _content,
			x: _x,
			y: _y,
			date: dateFormat,
			target: "web",
		}
		Socket.ws.send(JSON.stringify(req));
	},

	callComponentUpdate: function(_did, _cid, _title, _content, _x, _y, _date, _msg){
		_date = new Date(_date);
		var dateFormat = (_date!=null)?Config.getDatetime(_date):null
		if(dateFormat=="0000-00-00 00:00:00") dateFormat = null;
		var req = {
			type: "componentUpdate",
			id: auth.id,
			did: _did,
			cid: _cid,
			typec: (dateFormat)?'calendar':'memo',
			sub: _title,
			content: _content,
			x: _x,
			y: _y,
			date: dateFormat,
			msg: _msg,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));

	},

	callComponentDelete: function(_did, _cid){
		var req = {
			type: "componentDelete",
			id: auth.id,
			did: _did,
			cid: _cid,
			target: "web"
		}
		Socket.ws.send(JSON.stringify(req));
	},

	/***************************************************************************
		CALLBACK SOCKET FUNCTION
	***************************************************************************/

	onLogin: function(res){
		if(res.return != "True"){
			alert("로그인 오류입니다.");
			window.close();
			return;
		}
		Socket.callGetDashList();
		Socket.callGetInviteList();
		if(!auth.isHome){
			Socket.callGetComponentList(Dash.id);
		}
	}, 

	onLogout: function(res){
		if(res.return != "True"){ alert("로그아웃 오류입니다."); return; }
		alert("성공적으로 로그아웃 되었습니다.");
	},

	onLiveMessage: function(res){
		Socket.callLiveMessage();
	},

	onLogoutAction: function(res){
		alert('동일아이디로 로그인되어 클라이언트를 종료합니다');
		window.close();
	},

	onGetDashList: function(res){
		if(res.return != "True"){ alert("대쉬보드 로딩 오류입니다."); return; }
		Dock.initDashboardDock(res.dashList);
	},

	onGetComponentList: function(res){
		if(res.return != "True"){
			alert("컴포넌트 로딩 오류입니다."); 
			window.location.replace('http://210.118.74.153/ring/index.php/dash/view');
			return;
		}
		for(var idx=0; idx<res.componentList.length; idx++){
			res.componentList[idx].cuid=res.componentList[idx].custom_cuid;
		}
		Dash.initComponentList(res.componentList);
	},

	onGetDashComponentList: function(res){
		//console.log(JSON.parse(res.componentList));
	},

	onDashNew: function(res){
		if(res.return != "True"){ alert("대쉬보드 추가 오류입니다."); return; }	
		alert("대시보드가 추가되었습니다.");
	},

	onDashNewAction: function(res){
		Dock.makeDashboardIcon(res);
	},

	onDashUpdate: function(res){
		if(res.return != "True"){ alert("대쉬보드 변경 오류입니다."); return; }	
		alert("대시보드 설정이 변경되었습니다.");	
	},

	onDashUpdateAction: function(res){
		Dock.updateDash(res);
	},

	onDashDelete: function(res){
		if(res.return != "True"){ alert("대쉬보드 제거 오류입니다."); return; }	
		alert("대시보드가 제거되었습니다.");		
	},

	onDashDeleteAction: function(res){
		Dock.deleteDash(res);
	},

	onDashInvite: function(res){
		if(res.return == "True"){
			alert("초대 메세지를 보냈습니다.");
		}
		else if(res.return == "Invite"){
			Socket.callGetInviteList();
		}
		else{
			alert("초대를 실패했습니다.");
		}
	},

	onGetInviteList: function(res){
		if(res.return != "True"){ alert("초대목록을 로딩하는데 실패했습니다."); return; }
		if(res.inviteList.length==0) return;
		Dock.showInviteList(res.inviteList);
	},

	onInviteResponseTrue: function(res){
		if(res.return != "True"){ alert("응답을 실패했습니다."); return; }	
		//alert("수락했습니다.");
		Network.callGetDashInfo(res.did);
	},

	onInviteResponseFalse: function(res){
		if(res.return != "True"){ alert("응답을 실패했습니다."); return; }	
		//alert("거절했습니다.");	
	},

	onComponentNew: function(res){
		if(res.return != "True"){ alert("컴포넌트 추가 오류입니다."); return; }
		//alert("컴포넌트가 추가되었습니다.");
	},

	onComponentNewAction: function(res){
		if(Dash.id != res.did) return;
		Dash.makeComponentObject(res);
	},

	onComponentUpdate: function(res){
		if(res.return != "True"){ alert("컴포넌트 변경 오류입니다."); return; }
		//alert("컴포넌트가 변경되었습니다.");
	},

	onComponentUpdateAction: function(res){
		if(Dash.id != res.did) return;
		Component.update(res);
	},

	onComponentDelete:function(res){
		if(res.return != "True"){ alert("컴포넌트 삭제 오류입니다."); return; }
		//alert("컴포넌트가 삭제되었습니다.");	
	},

	onComponentDeleteAction: function(res){
		if(Dash.id != res.did) return;
		Component.delComponent(res);
	},

}
