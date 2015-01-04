<link rel="stylesheet" type="text/css" href="<?=base_url('asset/dash/css/style.css')?>"/>
<link rel="stylesheet" type="texx/css" href="<?=base_url('asset/dash/datepicker/css/datepicker.css');?>"/>
<script src="<?=base_url('asset/dash/datepicker/js/bootstrap-datepicker.js');?>"></script>

<!--
################################################################################
##/######## ###  ############################################################/##
##/####### # # # ############################################################/##
##/###### ##  ## ### === ## #  ## #  # ### ##################################/##
##/##### ### ### ## #####  ####  #### ## ####################################/##
##/#### ######## ##    # ##### ######  ######################################/##
##/################################# ########################################/##
##/##########################################################################/##
##/##################   #### ################################################/##
##/################# ###### ########## ######### ############################/##
##/################ ######  ####   ##=###   ##   ###    ###  ####   #########/##
##/################ ### # ## ## #### #### #### ### ## # # ## #### ###########/##
##/################   ## ### # #### ###   ### ### ### # ## ## #   ###########/##
##/##########################################################################/##
################################################################################
-->

<!--
################################################################################
	DASHBOARD
################################################################################
-->
<div id="dashboard" class="dashboard">


<?php if(!$board_id): ?>




<div class="component notice" style="left:200px; top:200px; width:238px; height:175px">
<div><i class="fa fa-2x fa-tags"> 안녕하세요!</i></div>
<div class="text-center" style="padding-top:30px">
	너와나의 연결고리에<br>
	오신걸 환영합니다.
</div>
</div>



<div class="component notice" style="left:600px; top:70px; width:238px; height:175px">
<div><i class="fa fa-2x fa-tags"> 처음이세요?!</i></div>
<div style="padding-top:10px">'+'버튼을 눌러 <br> 컴포넌트를 추가해주세요!!</div>
</div>



<div class="component notice" style="left:400px; top:500px; width:238px; height:175px">
<div><i class="fa fa-2x fa-tags"> 대시보드는?</i></div>
<div style="padding-top:10px">컴포넌트를 가진 그룹의 단위를 대시보드라고 칭합니다!!</div>
</div>



<div class="component notice" style="left:800px; top:400px; width:238px; height:175px">
<div><i class="fa fa-2x fa-tags"> 컴포넌트란?</i></div>
<div style="padding-top:10px">대시보드 내에 존재하는 위젯들을 컴포넌트라고 합니다!! <br> 간단한 <code>XML</code>문장을 통해 자신의 컴포넌트를 제작할 수도 있어요!</div>
</div>





<?php endif; ?>




</div>



<!--
################################################################################
	DASHBOARD DOCK SECTION
################################################################################
-->
<div id="dashboardDock" class="dock top-fixed">
	<div id="dashDockList" class="docklist"> </div>
	<div id="changeDock" class="dockicon"> </div>
</div>




<!--
################################################################################
	ADD DOCK SECTION
################################################################################
-->
<div id="addDock" class="dock top-fixed hidden">
	<div id="addDash" class="dockicon" data-toggle="modal" data-target="#addDashboard"> </div>
	<div id="invite" class="dockicon" data-toggle="modal" data-target="#inviteFriend"> </div>
	<div id="addDockList" class="docklist"> </div>
	<div id="cancelDock" class="dockicon"> </div>
</div>





<!--
################################################################################
	DASHBOARD CONTEXT MENU
################################################################################
-->
<div id="contextmenu" class="contextmenu" style="width:100px; display: none;">
	<div class="btn-group-vertical btn-group-sm">
		<span id="contextDashname" class="input-group-addon">테스트보드</span>
		<button id="showDetail" type="button" class="btn btn-default">보드정보수정</button>
		<button id="context_deleteDash" type="button" class="btn btn-danger">삭제</button>
	</div>
</div>




<!--
################################################################################
	COMPONENT CONTEXT MENU
################################################################################
-->
<div id="componentcontextmenu" class="contextmenu" style="width:100px; display: none;">
	<div class="btn-group-vertical btn-group-sm">
		<span id="contextComponentname" class="input-group-addon">컴포넌트 메뉴</span>
		<button id="context_deleteComponent" type="button" class="btn btn-danger">삭제</button>
	</div>
</div>






<!--
################################################################################
	MODAL VEIW - UPDATE DASHBOARD
################################################################################
-->
<div class="modal fade" id="modal_viewDetail" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:400px"><div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 class="modal-title">대시보드 수정</h4>
		</div>
		<div class="modal-body"><div class="form-group">
			<div class="controls"><div class="input-group">
				<span class="input-group-addon"><i class="fa fa-tag"></i></span>
				<input type="text" class="form-control" id="update_dashName" placeholder="대시보드 이름">
			</div></div>
			<br>
			<div class="controls"><div class="input-group">
				<span class="input-group-addon"><i class="fa fa-photo"></i></span>
				<input type="text" class="form-control" id="update_dashImgsrc" placeholder="이미지 주소">
			</div></div>
		</div></div>
		<div class="modal-footer">
			<button id="callDashUpdate" type="button" class="btn btn-success">확인</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
		</div>
	</div></div>
</div>






<!--
################################################################################
	MODAL VEIW - INVITE
################################################################################
-->
<div class="modal fade" id="inviteFriend" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:300px"><div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 class="modal-title">친구 초대</h4>
		</div>
		<div class="modal-body"><div class="form-group">
			<label class="control-label">아이디</label>
			<div class="controls"><div class="input-group">
				<span class="input-group-addon"><i class="fa fa-tag"></i></span>
				<input type="text" class="form-control" id="input_fid" placeholder="친구 아이디">	
			</div></div>
		</div></div>
		<div class="modal-footer">
			<button id="callInvite" type="button" class="btn btn-success">확인</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
		</div>
	</div></div>
</div>





<!--
################################################################################
	MODAL VEIW - INVITE RESPONSE LIST
################################################################################
-->
<div class="modal fade" id="modal_inviteFriendResponse" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:300px">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 class="modal-title">친구 초대 응답</h4>
		</div>
		<div id="modalList_inviteDashList" class="modal-body">
			
		</div>
	</div>
	</div>
</div>












<!--
################################################################################
	MODAL VEIW - ADD DASHBOARD
################################################################################
-->
<div class="modal fade" id="addDashboard" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true" >
	<div class="modal-dialog" style="width:400px"><div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 class="modal-title">대시보드 추가</h4>
		</div>
		<div class="modal-body"><div class="form-group">
			<div class="controls"><div class="input-group">
				<span class="input-group-addon"><i class="fa fa-tag"></i></span>
				<input type="text" class="form-control" id="input_dashName" placeholder="대시보드 이름">
			</div></div>
			<br>
			<div class="controls"><div class="input-group">
				<span class="input-group-addon"><i class="fa fa-photo"></i></span>
				<input type="text" class="form-control" id="input_dashImgsrc" placeholder="이미지 주소">
			</div></div>
		</div></div>
		<div class="modal-footer">
			<button id="callDashNew" type="button" class="btn btn-success">확인</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
		</div>
	</div></div>
</div>


<!--
################################################################################
	MODAL VEIW - DEL DASHBOARD
################################################################################
-->
<div class="modal fade" id="modal_dashDelete" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true" >
	<div class="modal-dialog" style="width:400px"><div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			<h4 class="modal-title">대시보드 삭제</h4>
		</div>
		<div class="modal-body">
			<p>보드를 삭제하시겠습니까?</p>
		</div>
		<div class="modal-footer">
			<button id="callDashDelete" type="button" class="btn btn-success">확인</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
		</div>
	</div></div>
</div>












<!--
################################################################################
	DOCK HELPER
################################################################################
-->
<div id="dockHelper" class="docknamehelper top-fixed hidden">
	<span>Dock Helper</span>
</div>


<!-- CONFIG SECTION -->





<!--
################################################################################
	NAVIGATOR
################################################################################
-->
<div id="navi"  class="navigator bottom-fixed right-fixed">
	<div id="mapscope" class="minimap"></div>
</div>



<!--
################################################################################
	CONFIG
################################################################################
-->
<script>
var auth = {
	id: "<?=$id;?>",
	key: "<?=$key;?>",
	did: "<?=$board_id?>",
	isHome: "<?=$is_home?>"
}
</script>

<script src="<?=base_url('asset/dash/js/config.js');?>"></script>
<script src="<?=base_url('asset/dash/js/socket.js');?>"></script>
<script src="<?=base_url('asset/dash/js/network.js');?>"></script>
<script src="<?=base_url('asset/dash/js/dock.js');?>"></script>
<script src="<?=base_url('asset/dash/js/component.js');?>"></script>
<script src="<?=base_url('asset/dash/js/dash.js');?>"></script>
<script src="<?=base_url('asset/dash/js/navi.js');?>"></script>
<script src="<?=base_url('asset/dash/js/main.js');?>"></script>


