<?php if(!defined('BASEPATH')) exit('No Direct script access allowed');

class Dash_m extends CI_Model
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
	private function get_info_kv($key, $value, $list=false){
		### INPUT DATA ###
		$user_info = array(
			$key => $value
		);

		### EXECUTE QUERY ###
		$this->db->from('dash');
		$this->db->where($user_info);
		$query = $this->db->get();

		### OUTPUT FORMAT ###
		if($list==true)
			return $query->result();
		else
			return $query->row();
	}

	public function get_info($did){
		return $this->get_info_kv('did', $did);
	}

	public function get_all_agenda($uid){
		$this->db->from('component');
		$this->db->join('dashToComponent', 'dashToComponent.component_cid = component.cid');
		$this->db->join('dashGroup', 'dashGroup.dash_did = dashToComponent.dash_did');
		$this->db->join('dash', 'dash.did = dashToComponent.dash_did');
		$this->db->where('dashGroup.user_uid', $uid);
		$this->db->where('component.date !=', 'null');
		$this->db->where('component.date !=', '0000-00-00 00:00:00');
		$this->db->order_by('date', 'DESC');
		$query = $this->db->get();
		return $query->result();
	}


	public function create_dashboard($uid){
		$this->db->insert('dash', array(
			'dashname' => '새 대시보드',
			'imgsrc' => ON_DEFAULT_BOARD
		));
		$did = $this->db->insert_id();


		$this->db->insert('dashGroup', array(
			'dash_did' => $did,
			'user_uid' => $uid,
			'inviteid' => $uid,
			'response' => 'y'
		));
		$dgid = $this->db->insert_id();
		
		return $dgid;
	}



	public function get_all_dashlist_count(){
		$this->db->from('dash');
		$this->db->order_by('did', 'DESC');
		$query = $this->db->get();
		return $query->num_rows();
	}

	public function get_all_dashlist($offset='', $limit=''){
		$this->db->from('dash');
		$this->db->order_by('did', 'DESC');
		if($offset!='' || $limit !='') $this->db->limit($limit, $offset);
		$query = $this->db->get();
		return $query->result();
	}

	public function get_dashlist($uid, $offset='', $limit=''){
		$this->db->from('dashGroup');
		$this->db->join('dash', 'dashGroup.dash_did = dash.did');
		$this->db->where('user_uid', $uid);
		$this->db->where('response', 'y');
		$this->db->order_by('did', 'DESC');
		if($offset!='' || $limit !='') $this->db->limit($limit, $offset);
		$query = $this->db->get();
		return $query->result();
	}

	public function get_dashGroup($did){
		$this->db->from('dashGroup');
		$this->db->join('user', 'user.uid = dashGroup.user_uid');
		$this->db->where('dash_did', $did);
		$this->db->where('response', 'y');
		$this->db->order_by('user.id', 'ASC');
		$query = $this->db->get();
		return $query->result();
	}

	public function get_all_component($did){
		$this->db->from('component');
		$this->db->join('dashToComponent', 'dashToComponent.component_cid = component.cid');
		$this->db->where('dash_did', $did);
		$this->db->order_by('cid', 'ASC');
		$query = $this->db->get();
		return $query->result();
	}

	public function get_component($cid){
		$this->db->from('component');
		$this->db->where('component_cid', $cid);
		$query = $this->db->get();
		return $query->row();
	}

	public function get_icon($imgsrc, $w, $h){
		$imagefile = @file_get_contents($imgsrc);
		$mainImage = imagecreatefromstring($imagefile);
		if(!$w) $w = imagesx($mainImage);
		if(!$h) $h = imagesy($mainImage);
		$result = imagecreatetruecolor($w, $h);
		imagecopyresized($result, $mainImage, 0, 0, 0, 0, $w, $h, imagesx($mainImage), imagesy($mainImage));
		imagepng($result);
		imagedestroy($result);
	}

}

?>