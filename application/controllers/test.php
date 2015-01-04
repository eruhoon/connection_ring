<?php

class test extends CI_Controller {

	function __construct(){
		parent::__construct();
		$this->load->helper('form');

		//$this->data['menu'] = 'user';
	}


	public function index(){
		$this->test();
	}

	public function test(){
		
		$this->load->helper('alert');
		$this->load->view('test_v');
	}

}

?>