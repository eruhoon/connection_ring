<?php require_once 'CR_Controller.php';

class Home extends CR_Controller {

	//public $data = null;
	//public $uid = null;

	function __construct()
	{
		parent::__construct();

	}

	public function index(){
		$this->main();
	}





	public function main(){
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$this->load->view('user/login_form_v.php');
			return;
		}
		$this->load->view('layout_frame/navbar_v', array('menu' => 'my_page'));

		$this->load->model('dash_m');
		$dashlist = $this->dash_m->get_dashlist($uid);
		for($idx=0; $idx<count($dashlist); $idx++){
			## GET GROUP LIST ##
			$dashlist[$idx]->dashgroup = $this->dash_m->get_dashGroup($dashlist[$idx]->did);
			## GET COMPONENT LIST ##
			$dashlist[$idx]->dashcomponent = $this->dash_m->get_all_component($dashlist[$idx]->did);
		}
		$agendalist = $this->dash_m->get_all_agenda($uid);

		$this->load->model('history_m');
		$historylist = $this->history_m->get_all_historylist($uid);
		for($idx=0; $idx<count($historylist); $idx++){
			## GET MESSAGE ##
			$historylist[$idx]->content = json_decode($historylist[$idx]->content);
			$historylist[$idx]->message = null;
			switch($historylist[$idx]->content->action){
				case 'dashnameUpdate':
					$historylist[$idx]->message = 
						'<span class="label label-success">'.$historylist[$idx]->name.'</span><small>님이 대시보드 이름을 </small><span class="label label-success">'.$historylist[$idx]->dashname.'</span><small>로 변경했습니다.</small>';
					break;
				case 'imgsrcUpdate':
					$historylist[$idx]->message = 
						'<span class="label label-success">'.$historylist[$idx]->name.'</span><small>님이 </small><span class="label label-success">'.$historylist[$idx]->dashname.'</span><small>의 아이콘을 생성했습니다.</small>';
					break;
				case 'componentNew':
					$historylist[$idx]->message = 
						'<span class="label label-success">'.$historylist[$idx]->name.'</span><small>님이 </small><span class="label label-success">'.$historylist[$idx]->dashname.'</span><small>에 컴포넌트를 생성했습니다.</small>';
					break;
				case 'componentDate':
					if($historylist[$idx]->content->date == 'NaN-NaN-NaN NaN:NaN:NaN') break;
					$historylist[$idx]->message = 
						'<span class="label label-success">'.$historylist[$idx]->name.'</span><small>님이 </small><span class="label label-success">'.$historylist[$idx]->dashname.'</span><small>의 '.
						'컴포넌트에 </small><span class="label label-success">'.date_format(new Datetime($historylist[$idx]->content->date), 'Y-m-d').'</span><small> 일정을 등록했습니다.</small>';
					break;
				case 'componentDelete':
					$historylist[$idx]->message = 
						'<span class="label label-success">'.$historylist[$idx]->name.'</span><small>님이 </small><span class="label label-success">'.$historylist[$idx]->dashname.'</span><small>의 컴포넌트를 삭제했습니다.</small>';
					break;
				case 'componentMove':

					$historylist[$idx]->message = 
						'<span class="label label-success">'.$historylist[$idx]->name.'</span><small>님이 </small><span class="label label-success">'.$historylist[$idx]->dashname.'</span><small>의 컴포넌트를 이동했습니다.</small>';
					break;
				case 'componentContent':
					$historylist[$idx]->message = 
						'<span class="label label-success">'.$historylist[$idx]->name.'</span><small>님이 </small><span class="label label-success">'.$historylist[$idx]->dashname.'</span><small>의 컴포넌트의 내용을 </small><span class="label label-success">'.$historylist[$idx]->content->content.'</span><small> 으로 변경했습니다.</small>';
					break;
				default:
					$historylist[$idx]->message = '기본응답입니다.';
					break;
			}
		}


		$this->load->view('home/main_v', array(
			'uid' => $uid,
			'id' => $this->session->userdata('id'),
			'key' => $this->session->userdata('pw'),
			'name' => $this->session->userdata('username'),

			'dashlist' => $dashlist,
			'historylist' => $historylist,
			'agendalist' => $agendalist
		));
	}





	public function dash(){
		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$this->load->view('user/login_form_v.php');
			return;
		}
		$this->load->view('layout_frame/navbar_v', array('menu' => 'dash'));

		## GET DASHBOARD ##
		$this->load->model('dash_m');
		$dashlist = $this->dash_m->get_dashlist($uid);
		$total_rows = count($dashlist);
		$per_page = 4;
		$uri_segment = 4;
		$pagination_set = $this->_get_pagination(site_url('home/dash/page/'), '', $total_rows, $per_page, $uri_segment);

		if($pagination_set['start'] > $total_rows){ $list_result = FALSE; }
		else if($pagination_set['start'] < 0){ $list_result = FALSE; }
		else { $list_result = $this->dash_m->get_dashList($uid, $pagination_set['start'], $pagination_set['limit']); }

		
		for($idx=0; $idx<count($list_result); $idx++){
			## GET GROUP LIST ##
			$list_result[$idx]->dashgroup = $this->dash_m->get_dashGroup($list_result[$idx]->did);
			## GET COMPONENT LIST ##
			$list_result[$idx]->dashcomponent = $this->dash_m->get_all_component($list_result[$idx]->did);
		}
		
		$this->load->view('home/dash_v', array(
			'uid' => $uid,
			'id' => $this->session->userdata('id'),
			'key' => $this->session->userdata('pw'),
			'name' => $this->session->userdata('username'),

			'dashlist' => $list_result,
			'pagination' => $pagination_set['pagination']

		));
	}






	public function widget(){
		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$this->load->view('user/login_form_v.php');
			return;
		}
		$this->load->view('layout_frame/navbar_v', array('menu' => 'widget'));

		## GET CUSTOM ##
		$this->load->model('custom_m');
		$total_rows = count($this->custom_m->get_all_customlist());
		$per_page = 8;
		$uri_segment = 4;
		$pagination_set = $this->_get_pagination(site_url('home/widget/page/'), '', $total_rows, $per_page, $uri_segment);
		if($pagination_set['start'] > $total_rows){ $list_result = FALSE; }
		else if($pagination_set['start'] < 0){ $list_result = FALSE; }
		else{
			$list_result = $this->custom_m->get_all_customlist($pagination_set['start'], $pagination_set['limit']);
		}

		## GET DETAIL INFO ##
		for($idx=0; $idx<count($list_result); $idx++){
			$cuid = $list_result[$idx]->cuid;
			## GET WRITER ##			
			$list_result[$idx]->writer = $this->custom_m->get_writer($cuid);
			## PEOPLE PER CUSTOM ##
			$list_result[$idx]->userCustom = $this->custom_m->get_userCustom($cuid);
			## CUSTOM USING ##
			$list_result[$idx]->hasCustom = $this->custom_m->hasCustom($uid, $cuid);
		}

		/*echo'<pre>';
		print_r($list_result);
		echo'</pre>';*/

		## DISPLAY ##
		$this->load->view('home/widget_v', array(
			'uid' => $uid,
			'id' => $this->session->userdata('id'),
			'key' => $this->session->userdata('pw'),
			'name' => $this->session->userdata('username'),

			'customlist' => $list_result,
			'pagination' => $pagination_set['pagination']
		));
	}


	public function docs(){
		$this->load->view('layout_frame/navbar_v', array('menu' => 'documentation'));
		$this->load->view('home/docs_v', array());

	}



	public function download(){
		$this->load->view('layout_frame/navbar_v', array('menu' => 'download'));
		$this->load->view('home/download_v', array());
	}






	public function make_widget(){
		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$this->load->view('user/login_form.php');
			return;
		}

		$this->load->model('custom_m');
		$this->load->view('layout_frame/navbar_v', array('menu' => 'widget'));
		$this->load->view('home/widget_make_v', array(
			'contents' => $this->custom_m->get_defaultXML()
		));
	}






	public function modify_widget($cuid){
		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$this->load->view('user/login_form.php');
			return;
		}

		$this->load->helper('alert');
		$this->load->model('custom_m');
		if($this->custom_m->get_info($cuid)->writer_uid!=$uid){
			alert('권한이 없습니다.');
			http_redirect(site_url('home/custom'));
			return;
		}

		$this->load->view('layout_frame/navbar_v', array('menu' => 'widget'));
		$this->load->view('home/widget_modify_v', array(
			'cuid' => $cuid,
			'title' => $this->custom_m->get_info($cuid)->customName,
			'contents' => $this->custom_m->getXML($cuid)
		));
	}






	private function _get_pagination($base_url, $suffix, $total_rows, $per_page, $uri_segment)
	{
		
		### INCLUDE MODULE ###	
		$this->load->library('pagination');
		
		### INIT PAGINATION ###
		$config['base_url'] = $base_url;
		$config['suffix'] = $suffix;
		$config['total_rows'] = $total_rows;
		$config['per_page'] = $per_page;
		$config['uri_segment'] = $uri_segment;
		$config['full_tag_open'] = '<ul class="pagination pagination-lg">';
		$config['full_tag_close'] = '</ul>';
		$config['num_tag_open'] = '<li>';
		$config['num_tag_close'] = '</li>';

		$config['cur_tag_open'] = '<li class="active"><a>';
		$config['cur_tag_close'] = '</a></li>';
		$config['first_link'] = 'First';
		$config['first_tag_open'] = '<li>';
		$config['first_tag_close'] = '</li>';
		$config['last_link'] = 'Last';
		$config['last_tag_open'] = '<li>';
		$config['last_tag_close'] = '</li>';
		$config['prev_link'] = '«';
		$config['prev_tag_open'] = '<li>';
		$config['prev_tag_close'] = '</li>';
		$config['next_link'] = '»';
		$config['next_tag_open'] = '<li>';
		$config['next_tag_close'] = '</li>';
		$this->pagination->initialize($config);
		### PAGING PROCESS ###
		$page = $this->uri->segment($uri_segment, 1);
		if($page > 1){
			$start = ($page/$config['per_page']) * $config['per_page'];
		}
		else{
			$start = ($page-1) * $config['per_page'];
		}
		$limit = $config['per_page'];
		### SET DATA ###
		$result = array(
			'pagination' => $this->pagination->create_links(),
			'start' => $start,
			'limit' => $limit
		);
		### RETURN ###
		return $result;
	}

}


?>