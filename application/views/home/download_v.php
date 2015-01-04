<div class="container">

	<div class="page-header">
		<h2><i class="fa fa-th"></i> Download Page </h2>
		<p> 너와 나의 연결고리 다운로드 페이지 입니다. </p>
	</div>
	<br/>


<button id="down" class="btn btn-lg btn-success">
	<i class="fa fa-download"></i> INSTALL <br>
	for <i class="fa fa-windows"></i>
</button>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<button id="down2" class="btn btn-lg btn-success">
	<i class="fa fa-download"></i> INSTALL <br>
	for <i class="fa fa-android"></i>
</button>



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

$('#down').on('click', function(){
	window.location.replace('http://210.118.74.92/ConnectionRing/setup.exe');
});

$('#down2').on('click', function(){
	window.location.replace('http://210.118.74.153/ring/item/connectionring.apk');
});



</script>