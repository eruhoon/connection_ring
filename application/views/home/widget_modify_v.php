<div class="container">
	<div class="page-header">
		<h2><i class="fa fa-paint-brush"></i> Widget Modify </h2>
		<p> 개성있는 커스텀으로 대시보드를 꾸며보세요. </p>
	</div>
	
	<div class="input-group">
		<span class="input-group-addon">제목</span>
		<input id="title" type="text" class="form-control" placeholder="title" value="<?=$title;?>">
	</div>
	<pre id="editor" style="height:300px"><?=htmlspecialchars($contents)?></pre>

	<nav class="navbar-default" role="bottom-navigation">
		<div>
			<div class="nav navbar-form navbar-right">
				<button type="button" class="btn btn-success" data-toggle="modal" data-target="#makeCustom">작성</button>
			</div>
		</div>
	</nav>

</div>

<!-- MODAL -->
<div class="modal fade" id="makeCustom" tabindex="-1" role="dialog" aria-labelledby="addDashboardLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">커스텀 컴포넌트 작성</h4>
			</div>
			<div class="modal-body">
				변경하시겠습니까?
			</div>
			<div class="modal-footer">
				<button id="submit" type="button" class="btn btn-success">확인</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
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
var edited = false;

$(window).on('beforeunload', function(event) {
	if(edited){
		var s = "You have unsaved changes. Really leave?";
		event = event || window.event;
		if (event) {
			event.returnValue = s;
		}
		return s;
	}
});


$(window).load(function(){
	$('#spinner').addClass('hide');
})

var editor = ace.edit("editor");
editor.setTheme("ace/theme/twilight");
editor.getSession().setMode("ace/mode/xml");
editor.getSession().setUseWrapMode(true);
editor.getSession().setWrapLimitRange(0, 80);
editor.getSession().on("change", function(e){
	edited = true;
});


$('#submit').on('click', function(e){
	$('#spinner').removeClass('hide');
	$.ajax({
		type: "POST",
		data: {
			cuid : <?=$cuid;?>,
			name : $('#title').val(),
			contents : editor.getValue()
		},
		url: "http://210.118.74.153/ring/index.php/custom/modify_custom/",
		dataType: 'json',
		success: function(data, status, req){
			alert(data.msg);
			if(data.result == true){
				edited = false;
				window.location.replace("../widget");
			}
		},
		complete: function(xhr, status){
			$('#spinner').addClass('hide');
		}
	});	
});

</script>