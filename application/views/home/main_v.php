
<div class="container">


	<div class="page-header">
		<h2>
			<i class="fa fa-users"></i>
			<span><?=htmlspecialchars($name);?>(<?=htmlspecialchars($id);?>)님 환영합니다.</span>	
		</h2>
	</div>
	<br/>

	<div class="col-sm-12 col-md-12 col-lg-8">
		<div>
			<div class="panel panel-success">
				<div class="panel-heading"><i class="fa fa-th"></i> 내 보드</div>
				<div class="panel-body">
					<?php $maxCount = (count($dashlist)<8)?count($dashlist):8; ?>
					<?php for($idx=0; $idx<$maxCount; $idx++): ?>
						<div>
							<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3">
								<div class="thumbnail">
									<a onclick="window.open('<?=site_url('dash/view/'.$dashlist[$idx]->did);?>','','resizable=1,scrollbars=0,width=1280,height=720,location=no,status=no,toolbar=no')" href="#">
										<img class="media-object img-responsive img-rounded img-thumbnail" src="<?=$dashlist[$idx]->imgsrc;?>" onerror="this.src='<?=base_url('asset/img/onboarderror.png');?>'" style="height:70px; width:70px;">
									</a>
									<div class="caption text-center">
										<h4>
											<?php if(mb_strlen($dashlist[$idx]->dashname)>6): ?>
												<?=mb_substr($dashlist[$idx]->dashname, 0, 5).'...'; ?>
											<?php else: ?>
												<?=$dashlist[$idx]->dashname ?>
											<?php endif; ?>
										</h4>
										<p>
											<span><i class="fa fa-users"></i> </span>
											<?=count($dashlist[$idx]->dashgroup);?>
											<span><i class="fa fa-inbox"></i> </span>
											<?=count($dashlist[$idx]->dashcomponent);?>
										</p>
									</div>
								</div>
						  	</div>
						</div>
					<?php endfor; ?>
					<?php if(count($dashlist)>8): ?>
					<div class="text-center">
						<a href="<?=site_url('home/dash');?>">more...</a>
					</div>
					<?php endif; ?>
				</div>
			</div>
		</div>
		<div>
			<div class="panel panel-success">
				<div class="panel-heading"><i class="fa fa-check"></i> 할 일 </div>
				<div class="panel-body">
					<?php foreach($agendalist as $agendaEntity): ?>
						<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
							<div class="thumbnail">
								<div class="media" style="overflow-y:hidden; height:60px;">
									<a class="pull-left" onclick="window.open('<?=site_url('dash/view/'.$agendaEntity->did);?>','','resizable=1,scrollbars=0,width=1280,height=720,location=no,status=no,toolbar=no')" href="#">
										<img class="media-object img-responsive img-rounded img-thumbnail" src="http://210.118.74.153/ring/index.php/dash_module/get_icon?src=<?=$agendaEntity->imgsrc; ?>&w=60&h=60" style="width:60px; height:60px;" alt="...">
									</a>
									<div class="media-body">
										<h4 class="media-heading"><?=date_format(new Datetime($agendaEntity->date), 'Y-m-d')?></h4>
										<?=$agendaEntity->content ?>
									</div>
								</div>
							</div>
						</div>
					<?php endforeach; ?>
				</div>
			</div>
		</div>
	</div>

	<div class="col-sm-12 col-md-12 col-lg-4" style="height:100%">
		<div class="panel panel-success">
			<div class="panel-heading"><i class="fa fa-calendar"></i> Timeline</div>
			<div class="panel-body">
				<div style="height:700px; overflow-y:scroll">
				<?php foreach($historylist as $historyEntity): ?>
					<div class="well">
						<div><?=$historyEntity->message;?></div>
						<div class="text-right">
							<small><?=$historyEntity->date;?></small>
						</div>
					</div>
					
				<?php endforeach; ?>
				</div>
			</div>
		</div>
	</div>

</div>