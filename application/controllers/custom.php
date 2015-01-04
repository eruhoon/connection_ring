<?php if(!defined('BASEPATH')) exit('No Direct Script Access Allowed');

class Custom extends CI_Controller {

	function __construct(){
		parent::__construct();
		$this->load->model('custom_m');
		$this->load->helper('file');
		//$this->data['menu'] = 'user';
	}




	public function index(){
	}




	public function raw_xml($cuid){
		header('Content-type: text/xml;');
		echo "<?xml version='1.0' encoding='UTF-8'?>\n";
		echo $this->custom_m->getXML($cuid);
	}




	public function xml($cuid){
		header('Content-type: text/xml');
		echo $this->custom_m->getParsedXML($cuid);
	}




	public function layout_xml($cuid){
		//header ('Content-type: image/png');
		$w = $this->input->get('w');
		$h = $this->input->get('h');
		$this->custom_m->getLayoutXML($cuid, $w, $h);
	}



	public function get_uselist(){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		
		if($id = $this->input->post('id')){
			$this->load->model('user_m');
			$uid = @$this->user_m->get_info_from_id($id)->uid;
		}

		if(!$uid){
			$response['result'] = false;
			$response['msg'] = '조회 실패 - 로그인 상태가 아닙니다.';
			echo json_encode($response);
			return;
		}

		## DB ##
		$db_result = $this->custom_m->get_uselist($uid);
		$response['result'] = true;
		$response['res'] = $db_result;
		$response['msg'] = '조회 성공.';
		echo json_encode($response);
		return;
	}




	public function add_uselist(){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## INPUT ##
		$uid = $this->session->userdata('uid');
		$cuid = $this->input->post('cuid');

		## DB ##
		$db_result = $this->custom_m->add_uselist($uid, $cuid);
		if(!$db_result){
			$response['result'] = false;
			$response['msg'] = '작업 실패 - DB 작업 에러.';
			echo json_encode($response);
			return;
		}

		$response['result'] = true;
		$response['msg'] = '등록되었습니다.';
		echo json_encode($response);
	}




	public function del_uselist(){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## INPUT ##
		$uid = $this->session->userdata('uid');
		$cuid = $this->input->post('cuid');

		## DB ##
		$db_result = $this->custom_m->del_uselist($uid, $cuid);
		if(!$db_result){
			$response['result'] = false;
			$response['msg'] = '작업 실패 - DB 작업 에러.';
			echo json_encode($response);
			return;
		}

		$response['result'] = true;
		$response['msg'] = '해제되었습니다.';
		echo json_encode($response);
	}




	public function add_custom(){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## FORM TEST ##
		if(!$this->input->post()){
			$response['result'] = false;
			$response['msg'] = '업로드 실패 - 잘못된 폼 입니다.';
			echo json_encode($response);
			return;
		}

		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$response['result'] = false;
			$response['msg'] = '업로드 실패 - 로그인 상태가 아닙니다.';
			echo json_encode($response);
			return;
		}

		## INPUT ##
		$name = $this->input->post('name');
		$contents = $this->input->post('contents');

		## DB ##
		$custom = array(
			'uid' => $uid,
			'name' => $name,
			'contents' => $contents
		);
		$db_result = $this->custom_m->registerXML($custom);
		if(!$db_result){
			$response['result'] = false;
			$response['msg'] = '업로드 실패 - DB 작업 에러.';
			echo json_encode($response);
			return;
		}

		## WRITE FILE ##
		$cuid = $db_result;
		$file_result = write_file(CUSTOM_DIR.'/'.$cuid.".xml", $contents);
		if(!$file_result) {
			$response['result'] = false;
			$response['msg'] = '업로드 실패 - 파일 작업 에러';
			echo json_encode($response);
			return;
		}

		$response['result'] = true;
		$response['msg'] = '성공적으로 등록되었습니다.';
		echo json_encode($response);
	}




	public function modify_custom(){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## FORM TEST ##
		if(!$this->input->post()){
			$response['result'] = false;
			$response['msg'] = '변경 실패 - 잘못된 폼 입니다.';
			echo json_encode($response);
			return;
		}

		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$response['result'] = false;
			$response['msg'] = '변경 실패 - 로그인 상태가 아닙니다.';
			echo json_encode($response);
			return;
		}

		## INPUT ##
		$cuid = $this->input->post('cuid');	
		$name = $this->input->post('name');
		$contents = $this->input->post('contents');

		## DB ##
		$custom = array(
			'cuid' => $cuid,
			'name' => $name
		);
		$db_result = $this->custom_m->modifyXML($custom);
		if(!$db_result){
			$response['result'] = false;
			$response['msg'] = '변경 실패 - DB 작업 에러.';
			echo json_encode($response);
			return;
		}

		## WRITE FILE ##
		$file_result = write_file(CUSTOM_DIR.'/'.$cuid.".xml", $contents);
		if(!$file_result) {
			$response['result'] = false;
			$response['msg'] = '변경 실패 - 파일 작업 에러';
			echo json_encode($response);
			return;
		}

		unlink(CUSTOM_DIR.'/thumbs/'.$cuid.'.png');


		$response['result'] = true;
		$response['msg'] = '성공적으로 변경되었습니다.';
		echo json_encode($response);

	}






	public function del_custom(){
		## MSG ##
		$response = array( 'result' => null, 'msg' => null );

		## FORM TEST ##
		if(!$this->input->post()){
			$response['result'] = false;
			$response['msg'] = '삭제 실패 - 잘못된 폼 입니다.';
			echo json_encode($response);
			return;
		}

		## LOGIN TEST ##
		$uid = $this->session->userdata('uid');
		if(!$uid){
			$response['result'] = false;
			$response['msg'] = '업로드 실패 - 로그인 상태가 아닙니다.';
			echo json_encode($response);
			return;
		}

		## INPUT ##
		$cuid = $this->input->post('cuid');

		## DUP TEST ##
		if($this->custom_m->get_info($cuid)->writer_uid!=$uid){
			$response['result'] = false;
			$response['msg'] = '삭제 실패 - 권한이 없습니다.';
			echo json_encode($response);
			return;
		}

		## DB ##
		$db_result = $this->custom_m->deleteCustom($cuid);
		if(!$db_result){
			$response['result'] = false;
			$response['msg'] = '업로드 실패 - DB 작업 에러.';
			echo json_encode($response);
			return;
		}

		$response['result'] = true;
		$response['msg'] = '삭제되었습니다.';
		echo json_encode($response);
	}

}


?>