<?php if(!defined('BASEPATH')) exit('No Direct Script Access Allowed');

ini_set('display_errors', true);

class CR_Controller extends CI_Controller {

	public $data = null;

	function __construct()
	{
		parent::__construct();
	}

	public function index(){
		$this->error();
	}

	public function _remap($method, $params = array())
	{
		if(method_exists($this, $method)){
			$this->load->view('layout_frame/header_v');	
			call_user_func_array(array($this, $method), $params);
			$this->load->view('layout_frame/footer_v');
		}
		else{
			$this->error();
		}
		
	}

	public function error()
	{
		$this->load->view('layout_frame/40x_v');
	}
}


?>