<div class="container">

	<div class="page-header">
		<h2><i class="fa fa-th"></i> Dashboard List </h2>
		<p> 자신의 대시보드 리스트가 나열됩니다. </p>
	</div>
	<br/>

	<?php foreach($dashlist as $dashEntity): ?>
	<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
		<div class="panel panel-success">
			<div class="panel-heading">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<div class="thumbnail">
						<div class="media">
							<a class="pull-left" onclick="window.open('<?=site_url('dash/view/'.$dashEntity->did);?>','','resizable=1,scrollbars=0,width=1280,height=720,location=no,status=no,toolbar=no')" href="#">
							
								<img class="media-object img-rounded img-thumbnail img-responsive" src="<?=$dashEntity->imgsrc;?>" style="height:60px; width:60px;" onerror="this.src='<?=base_url('asset/img/onboarderror.png');?>'">
							</a>
							<div class="media-body">
								<h4 class="media-heading"><?=$dashEntity->dashname;?></h4>
							</div>
							<p>
								<span><i class="fa fa-users"></i> </span>
								<?=count($dashEntity->dashgroup);?>
								<span><i class="fa fa-inbox"></i> </span>
								<?=count($dashEntity->dashcomponent);?>
							</p>

						</div>
					</div>
				</div>
			</div>
			<div class="panel-body text-center">
				<button class="btn btn-xs btn-success" data-toggle="modal" data-target="#memolistInfo" onclick="viewMemoList(<?=$dashEntity->did;?>);">메모조회</button>
				<button class="btn btn-xs btn-success" data-toggle="modal" data-target="#userlistInfo" onclick="viewUserList(<?=$dashEntity->did;?>);">사용자목록</button>
			</div>
		</div>
	</div>
	<?php endforeach; ?>
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div class="text-center"><?php echo $pagination; ?></div>
	</div>

	<!-- USER LIST MODAL -->
	<div class="modal fade" id="userlistInfo" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:200px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h4 class="modal-title"><i class="fa fa-users"></i> 사용자 목록</h4>
				</div>
				<div class="modal-body">
					<p id="userlist" class="text-center">
					</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" data-dismiss="modal">확인</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->

	<!-- MEMO LIST MODAL -->
	<div class="modal fade" id="memolistInfo" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h4 class="modal-title"><i class="fa fa-gears"></i> 메모 목록</h4>
				</div>
				<div class="modal-body">
					<p id="memolist">
					</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" data-dismiss="modal">확인</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
</div>


<!-- SPINNER -->
<div id="spinner" class="spinner" style="padding-top: 300px">
	로딩 중입니다.<br>
	<br>
	<i class="fa fa-5x fa-spinner fa-spin"></i>
</div>


<script>
$(window).load(function(){
	$('#spinner').addClass('hide');
})

var viewUserList = function(did){
	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/dash_module/userlist/"+did,
		dataType: 'json',
		success: function(data, status, req){
			if(data.result!=true){
				alert(data.msg);
				return;
			}
			var text = '';
			for(var idx=0; idx<data.res.length; idx++){
				text += "<h4>"+data.res[idx].id+"</h4><hr>";
			}
			$('#userlist').html(text);
		}
	});
}

var viewMemoList = function(did){
	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/dash_module/memolist/"+did,
		dataType: 'json',
		success: function(data, status, req){
			if(data.result!=true){
				alert(data.msg);
				return;
			}
			var text = '';
			if(data.res.length==0){
				text += "<p>메모가 존재하지 않습니다.</p>";
			}
			for(var idx=0; idx<data.res.length; idx++){
				console.log(data.res[idx]);
				text += "<div class=\"media\"><div class=\"pull-left\"><img src=\"http://210.118.74.153/ring/index.php/custom/layout_xml/"+data.res[idx].custom_cuid+"\" onerror=\"this.src='<?=base_url('asset/img/onwidgeterror.png');?>'\" class=\"media-object img-rounded img-thumbnail img-responsive\" style=\"width:60px; height:60px;\"/>";
				text += "</div><div class=\"media-body\"><h4>";
				text += data.res[idx].content+"</h4>";
				
				if(data.res[idx].date != "0000-00-00 00:00:00"){
					var date = new Date(data.res[idx].date);
					text += ("<small>"
						+date.getFullYear()+"-"
						+(date.getMonth()+1)+"-"
						+date.getDate()+"</small>");
				}
				
				text += "</h4></div></div>";
				text += "<hr>";
			}
			$('#memolist').html(text);
		}
	});	
}

</script>