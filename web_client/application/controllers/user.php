<?php require_once 'CR_Controller.php';

class User extends CR_Controller {

    function __construct(){
        parent::__construct();
        $this->load->model('user_m');
        $this->load->helper('form');

        $this->data['menu'] = 'user';
    }

    public function index(){
        $this->login();
    }

    public function login(){
        $this->load->view('user/login_form_v');
    }

    public function join(){
        $result = false;
        if(!$result){
            $this->load->view('user/join_v');
            return;
        }
    }
}

?>