<?php require_once 'CR_Controller.php';

class Dash extends CR_Controller {

	function __construct(){
		parent::__construct();
		$this->load->model('user_m');
		$this->load->helper('form');
	}

	public function index(){
		$this->view();
	}

	public function view($board_no=0){
		$data = array(
			'id' => $this->session->userdata('id'),
			'key' => $this->session->userdata('pw'),
			'board_id' => $board_no,
			'is_home' => ($board_no==0)?true:false
		);
		$this->load->view('board/view_v', $data);
	}
}

?>