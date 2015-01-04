
<div class="container">
	<div class="page-header">
		<h2><i class="fa fa-paint-brush"></i> Widget List </h2>
		<p> 
			개성있는 커스텀으로 대시보드를 꾸며보세요.
			<button class="btn btn-success pull-right" onclick="window.location.href='make_widget'">추가</button>
		</p>
	</div>
	<br/>

	<?php foreach($customlist as $customEntity): ?>
	<div class="col-xs-12 col-sm-6 col-md-4 col-lg-4">
		<div class="panel panel-success">
			<div class="panel-heading">
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
					<div class="thumbnail">
						<div class="media">
							<a class="pull-left" href="#" data-toggle="modal" data-target="#customInfo" onclick="viewDetail(<?=$customEntity->cuid;?>);">
								<img class="media-object img-rounded img-thumbnail img-responsive" src="<?=site_url('custom/layout_xml/'.$customEntity->cuid);?>?w=70&h=70" style="height:70px; width:70px;" onerror="this.src='<?=base_url('asset/img/onwidgeterror.png');?>'">
							</a>
							<div class="media-body">
								<div><h4 style="height: 20px; overflow-y:hidden;" class="media-heading"><?=($customEntity->customName)?$customEntity->customName:'[Untitle]';?></h4></div>
							</div>
							<p>
								<span><i class="fa fa-user"></i></span>
								<span><?=$customEntity->writer;?></span>
								<br>
								<span><i class="fa fa-download"></i></span>
								<span id="count<?=$customEntity->cuid;?>">
									<?=count($customEntity->userCustom);?>
								</span>
								<br>
								<?php if(count($customEntity->hasCustom)==true):?>
									<span id="state<?=$customEntity->cuid;?>" class="label label-success" data-state="1">사용중</span>
								<?php else:?>
									<span id="state<?=$customEntity->cuid;?>" class="label label-default" data-state="0">미사용</span>
								<?php endif;?>
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-body text-center">
				<button class="btn btn-xs btn-success" onclick="useit(<?=$customEntity->cuid;?>);">
					<?php if(count($customEntity->hasCustom)!=true):?>
						<span id="usebuttonlabel_<?=$customEntity->cuid;?>"><i class="fa fa-download"></i> 사용</span>
					<?php else:?>
						<span id="usebuttonlabel_<?=$customEntity->cuid;?>"><i class="fa fa-upload"></i> 사용해제</span>
					<?php endif;?>
				</button>
				<?php if($customEntity->writer_uid==$uid): ?>
					<button class="btn btn-xs btn-success" onclick="modifyWidget(<?=$customEntity->cuid;?>);">
						<span><i class="fa fa-pencil-square"></i> 수정</span>
					</button>
					<button class="btn btn-xs btn-danger" data-toggle="modal" data-target="#deleteInfo" onclick="selectWidget(<?=$customEntity->cuid;?>);">
						<span><i class="fa fa-trash"></i> 제거</span>
					</button>
				<?php endif; ?>
			</div>
		</div>
	</div>
	<?php endforeach; ?>
	<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
		<div class="text-center"><?php echo $pagination; ?></div>
	</div>
</div>

<!-- DELETE MODAL -->
<div class="modal fade" id="deleteInfo" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">커스텀 컴포넌트</h4>
			</div>
			<div class="modal-body">
				<p>삭제하시겠습니까?</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="test btn btn-danger" data-dismiss="modal" onclick="deleteWidget();">확인</button>
				<button type="button" class="test btn btn-default" data-dismiss="modal">취소</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<!-- MODAL -->
<div class="modal fade" id="customInfo" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">커스텀 컴포넌트</h4>
			</div>
			<div class="modal-body">
				<ul class="nav nav-tabs" id="myTab">
					<li class="active">
						<a href="#customLayout" data-toggle="tab">미리보기</a>
					</li>
					<li>
						<a href="#rxml" data-toggle="tab">RXML</a>
					</li>
					<li>
						<a href="#xml" data-toggle="tab">XML</a>
					</li>
				</ul>
				 
				<div class="tab-content">
					<div id="customLayout" class="tab-pane active" >
						<div class="text-center">
							<img id="layoutXML" class="text-center" style="display:inline-block; width:300px; height:300px">
							</img>
							<div id="thumbSpinner" class="spinner" style="padding-top: 100px">
								로딩 중입니다.<br>
								<br>
								<i class="fa fa-5x fa-spinner fa-spin"></i>
							</div>
						</div>
					</div>
					<div class="tab-pane" id="rxml">
						<pre id="editorRXML" style="height:300px">rxml</pre>
					</div>
					<div class="tab-pane" id="xml">
						<pre id="editorXML" style="height:300px">xml</pre>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="test btn btn-success" data-dismiss="modal">확인</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<!-- SPINNER -->
<div id="spinner" class="spinner" style="padding-top: 300px">
	로딩 중입니다.<br>
	<br>
	<i class="fa fa-5x fa-spinner fa-spin"></i>
</div>


<script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js" type="text/javascript" charset="utf-8"></script>
<script>

var selectId = null;

$(window).load(function(){
	$('#spinner').addClass('hide');
});

$('#layoutXML').load(function(){
	$('#thumbSpinner').addClass('hide');
});

var editorRXML = ace.edit("editorRXML");
editorRXML.setTheme("ace/theme/twilight");
editorRXML.getSession().setMode("ace/mode/xml");
editorRXML.getSession().setUseWrapMode(true);
editorRXML.getSession().setWrapLimitRange(0, 80);
editorRXML.setReadOnly(true);

var editorXML = ace.edit("editorXML");
editorXML.setTheme("ace/theme/twilight");
editorXML.getSession().setMode("ace/mode/xml");
editorXML.getSession().setUseWrapMode(true);
editorXML.getSession().setWrapLimitRange(0, 80);
editorXML.setReadOnly(true);

var selectWidget = function(_cuid){ selectId = _cuid }

var modifyWidget = function(cuid){
	window.location.replace('http://210.118.74.153/ring/index.php/home/modify_widget/'+cuid);
}

var deleteWidget = function(){
	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/custom/del_custom/",
		data:{
			cuid: selectId,
		},
		dataType: 'json',
		success: function(data, status, req){
			alert(data.msg);
			if(data.result!=true) return;
			location.reload();
		}
	})
}

var useit = function(_cuid){
	$('#spinner').removeClass('hide');
	if($('#state'+_cuid).data('state')==0){
		$.ajax({
			type: "POST",
			url: "http://210.118.74.153/ring/index.php/custom/add_uselist/",
			data:{
				cuid: _cuid
			},
			dataType: 'json',
			success: function(data, status, req){
				alert(data.msg);
				if(data.result!=true) return;
				$('#usebuttonlabel_'+_cuid).html('<i class="fa fa-upload"></i> 사용해제');
				$('#state'+_cuid).text('사용중');
				$('#state'+_cuid).addClass('label-success');
				$('#state'+_cuid).removeClass('label-default');
				$('#state'+_cuid).data('state', 1);
				var c = parseInt($('#count'+_cuid).text());
				$('#count'+_cuid).text(c+1);
			},
			complete: function(xhr, status){
				$('#spinner').addClass('hide');
			}
		});	
	}
	if($('#state'+_cuid).data('state')==1){
		$.ajax({
			type: "POST",
			url: "http://210.118.74.153/ring/index.php/custom/del_uselist/",
			data:{
				cuid: _cuid
			},
			dataType: 'json',
			success: function(data, status, req){
				alert(data.msg);
				if(data.result!=true) return;
				$('#usebuttonlabel_'+_cuid).html('<i class="fa fa-download"></i> 사용');
				$('#state'+_cuid).text('미사용');
				$('#state'+_cuid).removeClass('label-success');
				$('#state'+_cuid).addClass('label-default');
				$('#state'+_cuid).data('state', 0);
				var c = parseInt($('#count'+_cuid).text());
				$('#count'+_cuid).text(c-1);
			},
			complete: function(xhr, status){
				$('#spinner').addClass('hide');
			}

		});
	}
	
}

var viewDetail = function(cuid){
	$('#layoutXML').attr('src', "http://210.118.74.153/ring/index.php/custom/layout_xml/"+cuid);
	$('#spinner').removeClass('hide');
	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/custom/raw_xml/"+cuid,
		dataType: 'text',
		success: function(data, status, req){
			editorRXML.setValue(data);
		},
		complete: function(xhr, status){
			$('#spinner').addClass('hide');
		}
	});

	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/custom/xml/"+cuid,
		dataType: 'text',
		success: function(data, status, req){
			editorXML.setValue(data);
		},
		complete: function(xhr, status){
			$('#spinner').addClass('hide');
		}
	});

	$('#myTab a[href="#customLayout"]').tab('show');
}

</script>