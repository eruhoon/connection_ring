<div style="height:60px;"></div>
<div id="container">
	<div class="col-xs-12 col-sm-12 col-md-6 col-md-offset-3 col-lg-6 col-lg-offset-3">
		<div class="page-header">
			<h2><i class="fa fa-users"></i> 회원 가입 </h2>
		</div>

		<div class="form-group">
			<label class="control-label" for="#userId">
				<span><i class="fa fa-users"></i></span> 아이디
			</label>
			<input id="userId" type="text" class="form-control" placeholder="아이디">
			
			<div style="height:10px"></div>
			<label class="control-label" for="#userPass">
				<span><i class="fa fa-key"></i></span> 패스워드
			</label>
			<input id="userPass" type="password" class="form-control" placeholder="패스워드">

			<div style="height:10px"></div>
			<label class="control-label" for="#userName">
				<span><i class="fa fa-tags"></i></span> 이름
			</label>
			<input id="userName" type="text" class="form-control" placeholder="패스워드">			
		</div>
		<div class="text-center">
			<button id="submit" class="btn btn-primary"> 확인 </button>
			<button id="cancel" class="btn btn-default"> 취소 </button>
		</div>
	</div>
</div>

<script>

$('#submit').on('click', function(e){
	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/user_module/join",
		data: {
			id: $('#userId').val(),
			pw: $('#userPass').val(),
			name: $('#userName').val(),
		},
		dataType: 'json',
		success: function(data, status, req){
			if(!data.result){
				alert(data.msg);
				return;
			}
			alert(data.msg);
			window.location.href = "http://210.118.74.153/ring";
		}
	})
});


$('#cancel').on('click', function(e){
	window.location.replace("http://210.118.74.153/ring"); 
});

</script>