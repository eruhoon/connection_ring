$("#logout").on("click", function(e){
	$.ajax({
		type: "POST",
		url: "http://210.118.74.153/ring/index.php/user_module/logout",
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


$("#launch").on('click', function(e){
	window.open(
		'http://210.118.74.153/ring/index.php/dash/',
		'',
		'resizable=1,scrollbars=0,width=1280,height=720,location=no,status=no,toolbar=no')
});