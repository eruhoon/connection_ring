<?php if(!defined('BASEPATH')) exit('No Direct script access allowed');

class History_m extends CI_Model
{
	
	function __construct()
	{
		parent::__construct();
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
		$this->db->from('history');
		$this->db->where($user_info);
		$query = $this->db->get();

		### OUTPUT FORMAT ###
		return $query->row();
	}
	public function get_info($history_idx) { return $this->get_info_kv('hid', $history_idx); }


	public function get_all_historylist($uid) {
		$this->db->from('history');
		$this->db->join('dashGroup', 'dashGroup.dash_did = history.dash_did');
		$this->db->join('dash', 'dashGroup.dash_did = dash.did');
		$this->db->join('user', 'user.uid = history.user_uid');
		$this->db->where('dashGroup.user_uid', $uid);
		$this->db->order_by('hid', 'DESC');
		$this->db->limit(100, 0);
		$query = $this->db->get();
		return $query->result();
	}




}

?>