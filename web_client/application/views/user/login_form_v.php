<div style="height:60px;"></div>
<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4 col-sm-offset-4 col-md-offset-4 col-lg-offset-4">
	<img src="<?=base_url('asset/img/logo.png');?>" style="width:100%" class="img-responsive"/>
</div>
<div style="height:60px;"></div>
<div class="container">
	<div class="col-xs-12 col-sm-12 col-md-6 col-md-offset-3 col-lg-6 col-lg-offset-3">
		<div class="form-group">
			<label class="control-label" for="input01">
				<span><i class="fa fa-users"></i></span> 아이디
			</label>
			<input id="userId" type="text" class="form-control" name="id" placeholder="아이디">
			<div style="height:10px"></div>
			<label class="control-label" for="input01">
				<span><i class="fa fa-key"></i></span> 패스워드
			</label>
			<input id="userPass" type="password" class="form-control" name="pw" placeholder="패스워드">
		</div>
		<div class="text-center">
			<button id="submit" class="btn btn-primary"> 확인 </button>
			<button id="join" class="btn btn-default"> 회원가입 </button>
		</div>
	</div>
<div>

<script src="<?=base_url('asset/js/login.js');?>"></script>