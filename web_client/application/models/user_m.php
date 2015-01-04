<?php if(!defined('BASEPATH')) exit('No Direct script access allowed');

class User_m extends CI_Model
{
	
	function __construct()
	{
		parent::__construct();
	}


	/***************************************************************************
		LOGIN /w id, pw @ table 'auth'
			$auth : Array(
				- username : id
				- password : pw
			)
		= success : $row (PHP_OBJECT)
			user_idx : INDEX (NUMBER)
			user_id : ID (STRING)
			user_pw : MD5(ID) (STRING)
			email : EMAIL (STRING)
			reg_time : DATE
		= failed : FALSE
	***************************************************************************/
	public function login($auth)
	{
		### INPUT DATA ###
		$login_user = array(
			'id' => strtolower($auth['id']),
			'pw' => md5($auth['pw'])
		);
		### EXECUTE QUERY ###
		$this->db->from('user')->where($login_user);
		$query = $this->db->get();
		### VALIDATION TEST ###
		if(!$query->num_rows()) return FALSE;
		### OUTPUT FORMAT ###
		return $query->row();
	}




	/***************************************************************************
		JOIN /w id, pw, name @ table 'user'
			$user : Array(
				- id : id (STRING)
				- pw : md5(pw) (STRING)
				- name : STRING
			)
		= success : TRUE
		= failed : FALSE
	***************************************************************************/
	public function join($auth)
	{
		### INPUT DATA ###
		$new_user = array(
			'id' => strtolower($auth['id']),
			'pw' => md5($auth['pw']),
			'name' => $auth['name']
		);
		### EXCUTE QUERY ###
		$result = $this->db->insert('user', $new_user);
		### VALIDATION TEST ###
		if(!$result) return FALSE;
		### OUTPUT FORMAT ###
		return $this->db->insert_id();
	}








	/***************************************************************************
		EDIT /w id, oldpw, newpw, name @ table 'user'
			$user : Array(
				- id : id (STRING)
				- newpw : md5(pw) (STRING)
				- pw : md5(pw) (STRING)
				- name : STRING
			)
		= success : TRUE
		= failed : FALSE
	***************************************************************************/
	public function edit($auth)
	{
		### INPUT DATA ###
		$uid = $auth['uid'];
		$new_user = array(
			'pw' => md5($auth['pw']),
			'name' => $auth['name']
		);

		### EXECUTE QUERY ###
		$this->db->where('uid', $uid);
		$result = $this->db->update('user', $new_user);
		### VALIDATION TEST ###
		if(!$result) return FALSE;
		### OUTPUT FORMAT ###
		return TRUE;
	}









	/***************************************************************************
		GET INFORMATION /key, value user_idx @ table 'auth'
			$key : KEY (STRING)
			$value : VALUE (STRING)
		= success : $row (PHP_OBJECT)
			user_idx : INDEX (NUMBER)
			user_id : ID (STRING)
			user_pw : MD5(ID) (STRING)
			email : EMAIL (STRING)
			reg_time : DATE
		= failed : FALSE
	***************************************************************************/
	private function get_info_kv($key, $value){
		### INPUT DATA ###
		$user_info = array(
			$key => $value
		);

		### EXECUTE QUERY ###
		$this->db->from('user');
		$this->db->where($user_info);
		$query = $this->db->get();

		### OUTPUT FORMAT ###
		return $query->row();
	}
	public function get_info($user_idx) { return $this->get_info_kv('uid', $user_idx); }
	public function get_info_from_id($user_id) { return $this->get_info_kv('id', $user_id); }

}

?>