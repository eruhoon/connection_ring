<?php if(!defined('BASEPATH')) exit('No Direct Script Access Allowed');

class User_module extends CI_Controller {

	function __construct(){
		parent::__construct();
		$this->load->model('user_m');
		$this->load->helper('form');

		//$this->data['menu'] = 'user';
	}


	public function index(){
		$this->login();
	}

	/***************************************************************************
		LOGIN MODULE
			req : post->id, post->pw
			res : {
				result : true/false (BOOLEAN)
				msg : message (TEXT)
			}
	***************************************************************************/
	public function login(){
		## MESSAGE ##
		$response = array( 'result' => null, 'msg' => null );

		## INPUT ##
		$auth = array(
			'id' => $this->input->post('id'),
			'pw' => $this->input->post('pw'),
		);

		## QUERY ##
		$query_result = $this->user_m->login($auth);

		### LOGIN FAIL ###
		if(!$query_result){
			$response['result'] = false;
			$response['msg'] = '로그인실패.';
			echo json_encode($response);
			return;
		}

		### INPUT FORMAT ###
		$new_session_data = array(
			'uid' => $query_result->uid,
			'id' => $query_result->id,
			'username' => $query_result->name,
			'pw' => $query_result->pw,
			'logged_in'	=> TRUE
		);

		### REGISTER SESSION ###
		$this->session->set_userdata($new_session_data);
		
		### LOGIN SUCCESS ###
		$response['result'] = true;
		$response['msg'] = '로그인성공.';
		echo json_encode($response);
		return;
	}

	public function logout(){
		### DESTROY SESSION ###
		$this->session->sess_destroy();
		
		### LOGOUT SUCCESS ###
		$response['result'] = true;
		$response['msg'] = '로그아웃성공.';
		echo json_encode($response);
		return;
	}



	/***************************************************************************
		JOIN MODULE
			req : $id
			res : {
				result : true/false (BOOLEAN)
				msg : message (TEXT)
			}
	***************************************************************************/
	public function join(){
		$id = $this->input->post('id');
		
		## ID DUP ##
		$id_info = $this->user_m->get_info_from_id($id);
		if($id_info)
		{
			$response['result'] = false;
			$response['msg'] = '아이디가 중복됩니다.';
			echo json_encode($response);
			return;
		}

		## MESSAGE ##
		$response = array( 'result' => null, 'msg' => null );

		## INPUT ##
		$auth = array(
			'id' => $this->input->post('id'),
			'pw' => $this->input->post('pw'),
			'name' => $this->input->post('name')
		);

		## QUERY ##
		$query_result = $this->user_m->join($auth);
		
		## FAILED ##
		if(!$query_result)
		{
			$response['result'] = false;
			$response['msg'] = '가입실패.';
			echo json_encode($response);
			return;
		}

		## ADD WIDGET USELIST ##
		$uid = $query_result;
		

		## TUTORIAL ##
		$DEFAULT_MEMO = 1;
		$DEFAULT_CALENDAR = 2;
		$this->load->model('custom_m');
		$this->custom_m->add_uselist($uid, 1);
		$this->custom_m->add_uselist($uid, 2);

		## TUTORIAL - BOARD ##
		$this->load->model('dash_m');
		$dgid = $this->dash_m->create_dashboard($uid);
		if(!$dgid){
			$response['result'] = false;
			$response['msg'] = '초기화실패.';
			echo json_encode($response);
			return;
		}



		## RESULT ##
		$response['result'] = true;
		$response['msg'] = '가입성공.';
		echo json_encode($response);
		return;
	}



	/***************************************************************************
		EDIT
	***************************************************************************/
	public function edit(){
		$id = $this->input->post('id');
		$oldpw = md5($this->input->post('oldpw'));
		$newpw = $this->input->post('newpw');
		$name = $this->input->post('name');
		$id_info = $this->user_m->get_info_from_id($id);
		if(!$id_info){
			$response['result'] = false;
			$response['msg'] = '계정이 존재하지 않습니다.';
			echo json_encode($response);
			return;
		}
		if($id_info->pw != $oldpw){
			$response['result'] = false;
			$response['msg'] = '비밀번호가 일치하지 않습니다.';
			echo json_encode($response);
			return;
		}
		
		$new_user = array(
			'uid' => $id_info->uid,
			'pw' => $newpw,
			'name' => $name
		);
		$query_result = $this->user_m->edit($new_user);
		if(!$query_result){
			$response['result'] = false;
			$response['msg'] = '변경실패.';
			echo json_encode($response);
			return;
		}
		$response['result'] = true;
		$response['msg'] = '변경성공.';
		$response['msg'] .= $this->input->get('newpw');
		echo json_encode($response);
	}




	/***************************************************************************
		ID CHECK MODULE
			req : $id
			res : {
				result : true/false (BOOLEAN)
				msg : message (TEXT)
			}
	***************************************************************************/
	public function id_check($id){
		## MESSAGE ##
		$response = array( 'result' => null, 'msg' => null );

		## ID ##
		if(!$id) {
			$response['result'] = false;
			$response['msg'] = '아이디가 없습니다.';
			echo json_encode($response);
			return;
		};

		## QUERY ##
		$id_info = $this->user_m->get_info_from_id($id);
		
		## ID DUP ##
		if($id_info)
		{
			$response['result'] = false;
			$response['msg'] = '아이디가 중복됩니다.';
			echo json_encode($response);
			return;
		}
		
		## RESULT ##
		$response['result'] = true;
		$response['msg'] = '사용 가능한 아이디입니다.';
		echo json_encode($response);
		return;
	}

}


?>