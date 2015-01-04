<?php if(!defined('BASEPATH')) exit('No Direct Script Access Allowed');

class Dash_module extends CI_Controller {

	function __construct(){
		parent::__construct();
		$this->load->model('dash_m');
	}


	public function index(){
	}

	public function userlist($did){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$response['result'] = false;
			$response['msg'] = '조회 실패 - 로그인 상태가 아닙니다.';
			echo json_encode($response);
			return;
		}

		$dashgroup = $this->dash_m->get_dashGroup($did);
		$result = array();
		for($idx=0; $idx < count($dashgroup); $idx++){
			$entity = array(
				'uid' => $dashgroup[$idx]->uid,
				'id' => $dashgroup[$idx]->id
			);
			array_push($result, $entity);
		}
		$response['result'] = true;
		$response['res'] = $result;
		print_r(json_encode($response));
	}

	public function memolist($did){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$response['result'] = false;
			$response['msg'] = '조회 실패 - 로그인 상태가 아닙니다.';
			echo json_encode($response);
			return;
		}

		$memolist = $this->dash_m->get_all_component($did);
		
		$response['result'] = true;
		$response['res'] = $memolist;
		print_r(json_encode($response));
	}

    public function rss($id, $board_no){
        echo $id.'\'s '.$board_no.'rss';
    }

    public function info(){
    	## MSG ##
    	$response = array( 'result' => null, 'msg' => null, 'res' => null);

    	## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$response['result'] = false;
			$response['msg'] = '조회 실패 - 로그인 상태가 아닙니다.';
			echo json_encode($response);
			return;
		}

		## POST DATA ##
		$did = $this->input->post('did');

		$dash_info = $this->dash_m->get_info($did);
		$response['result'] = true;
		$response['msg'] = '조회 성공';
		$response['res'] = $dash_info;
		print_r(json_encode($response));
    }

    public function get_icon(){
    	header ('Content-type: image/png');
    	$src = $this->input->get('src');
		$w = $this->input->get('w');
		$h = $this->input->get('h');
		$this->dash_m->get_icon($src, $w, $h);
    }
}


?>