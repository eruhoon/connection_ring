$("#submit").on("click", function(e){
	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/user_module/login",
		data: {
			id: $('#userId').val(),
			pw: $('#userPass').val()
		},
		dataType:'JSON',
		success: function(data, status, req){
			if(!data.result){
				alert(data.msg);
				return;
			}
			alert(data.msg);
			window.location.href = "http://210.118.74.153/ring/";
		},
	});
});


$('#join').on('click', function(e){
	window.location.href = "http://210.118.74.153/ring/index.php/user/join";
});