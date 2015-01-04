<nav class="navbar navbar-default" role="navigation">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" style="color:#FFF" href="<?=site_url('home/');?>">
			<i class="fa fa-link"></i> 너와나의 연결고리
		</a>
	</div>

	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse navbar-ex1-collapse navbar-inverse">

		<form class="navbar-form navbar-right">
			<button id="launch" class="btn btn btn-success"> LAUNCH </button>
			<button id="logout" class="btn btn btn-danger">LOG-OUT</button>	
		</form>	

		<ul class="nav navbar-nav navbar-right">
			<li <?php if($menu=='my_page'):?>class="active"<?php endif;?>>
				<a href="<?=site_url('home/');?>">My Page</a>
			</li>
			<li <?php if($menu=='dash'):?>class="active"<?php endif;?>>
				<a href="<?=site_url('home/dash');?>">Board</a>
			</li>
			<li <?php if($menu=='widget'):?>class="active"<?php endif;?>>
				<a href="<?=site_url('home/widget');?>">Widget</a>
			</li>
			<li <?php if($menu=='documentation'):?>class="active"<?php endif;?>>
				<a href="<?=site_url('home/docs');?>">Docs</a>
			</li>
			<li <?php if($menu=='download'):?>class="active"<?php endif;?>>
				<a href="<?=site_url('home/download');?>">Download</a>
			</li>
		</ul>

		<form class="navbar-form navbar-right">

		</form>
	</div><!-- /.navbar-collapse -->
</nav>