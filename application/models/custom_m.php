<?php if(!defined('BASEPATH')) exit('No Direct script access allowed');

class Custom_m extends CI_Model
{
	
	function __construct()
	{
		parent::__construct();
	}


	/***************************************************************************
		GET XML /w cuid
		= success : source code (TEXT)
		= failed : FALSE
	***************************************************************************/
	public function getXML($cuid){
		$this->load->helper('file');

		$result = read_file(CUSTOM_DIR.$cuid.'.xml');
		
		if(!$result){
			echo '로딩 실패';
			return FALSE;	
		} 
		return $result;
	}

	public function getXMLObject($cuid){
		$rawXML = $this->getXML($cuid);
		if(!$rawXML) return FALSE;

		$result = new SimpleXMLElement($rawXML);

		return $result;
	}


	/***************************************************************************
		GET PARSED XML /w cuid	
	***************************************************************************/
	public function getParsedXML($cuid){
		$constantList = array();
		$wpConstantList = array();

		$rawXMLObject = $this->getXMLObject($cuid);
		if($rawXMLObject->getName() != 'component'){
			return;
		}
		
		## GET CONSTANTS ##
		if($rawXMLObject->constants){
			foreach($rawXMLObject->constants->var as $variable){
				$varNode = array(
					"key" => (string)$variable['key'],
					"value" => (string)$variable['value']
				);
				array_push($constantList, $varNode);
			}
		}
		
		## GET WEBPARSER CONSTANTS ##
		if($webparser = $rawXMLObject->webparser){
			$url = (string)$webparser['url'];
			$urlContents = implode('', file($url)); 
			$regex = (string)$webparser['regex'];
			$wpNo = 1;
			preg_match_all('/'.$regex.'/sU', $urlContents, $result);
			for($idx=0; $idx<count($result[0]); $idx++){
				for($ridx=0; $ridx<count($result); $ridx++){
					$varNode = array(
						"key" => 'WP'.$wpNo,
						"value" => htmlspecialchars($result[$ridx][$idx])
					);
					$wpNo++;
					array_push($constantList, $varNode);
				}
			}
		}

		unset($rawXMLObject->constants);
		unset($rawXMLObject->webparser);
		$resultXml = $rawXMLObject->saveXML();
		foreach($constantList as $cNode){
			$resultXml = str_replace('##'.$cNode['key'].'##', $cNode['value'], $resultXml);

		}
		return print_r($resultXml, true);
	}


	/***************************************************************************
		GET LAYOUT XML
	***************************************************************************/
	public function getLayoutXML($cuid, $cWidth=0, $cHeight=0){
		$mainImage = null;
		$width = null;
		$height = null;

		if($cache = @file_get_contents(base_url('custom/thumbs/'.$cuid.'.png'))){
			$mainImage = imagecreatefrompng(base_url('custom/thumbs/'.$cuid.'.png'));
			imagesavealpha($mainImage, true);
		}
		else{
			$component = new SimpleXMLElement($this->custom_m->getParsedXML($cuid));
			if($component->getName()!='component') return;
			$width = ($component['width'])?(int)$component['width']:100;
			$height = ($component['height'])?(int)$component['height']:100;

			$mainImage = imagecreatetruecolor($width, $height);
			$black = imagecolorallocate($mainImage, 0, 0, 0);
			imagecolortransparent($mainImage, $black);

			$memoflag = true;
			$calendarflag = true;
			foreach($component as $xmlChildObject){
				switch($xmlChildObject->getName()){
					case 'layer': {
						## BACKGROUND ##
						$layerImage = imagecreatetruecolor(100, 100);		
						if($background = $xmlChildObject->background){
							if($background['image']){
								$imagefile = @file_get_contents($background['image']);
								if($imagefile){
									imagedestroy($layerImage);
									$layerImage = imagecreatefromstring($imagefile);
									imagesavealpha($layerImage, true);
								}
							}
							else if($background['color']){
								imagedestroy($layerImage);
								$layerImage = imagecreatetruecolor(100, 100);
								$colorSet = $this->hex2rgb($background['color']);
								$color = imagecolorallocate($layerImage, $colorSet[0], $colorSet[1], $colorSet[2]);
								imagefill($layerImage, 0, 0, $color);
							}
						}
						## POSITION ##
						if($position = $xmlChildObject->position){
							imagecopyresized(
								$mainImage,
								$layerImage,
								(int)$position['x'],
								(int)$position['y'],
								0,
								0,
								(int)$position['width'],
								(int)$position['height'],
								imagesx($layerImage),
								imagesy($layerImage)
							);
						}
						break;
					}
					case 'memo': {
						if(!$memoflag) break;
						$memoflag = false;

						$x = 0; $y = 0; $w = 0; $h = 0;
						## POSITION ##
						if($position = $xmlChildObject->position){
							$x = (int)$position['x'];
							$y = (int)$position['y'];
							$w = (int)$position['width'];
							$h = (int)$position['height'];
						}

						## TEXTAREA ##
						if($textarea = $xmlChildObject->textarea){
							if($position = $textarea->position){
								imagestring($mainImage, 1, (int)$x+(int)$position['x'], (int)$y+(int)$position['y'], 'TEXT AREA', imagecolorallocate($mainImage, 0, 0, 0));
							}
						}

						## AGENDA ##
						if($agenda = $xmlChildObject->agenda){
							if($position = $agenda->position){
								imagestring($mainImage, 1, (int)$x+(int)$position['x'], (int)$y+(int)$position['y'], 'AGENDA', imagecolorallocate($mainImage, 0, 0, 0));
							}	
						}

						## BUTTON MEMO ##
						foreach($xmlChildObject->button_memo as $button){
							if($position = $button->position){
								$posx = (int)$position['x'];
								$posy = (int)$position['y'];

								if($button['src_normal']){
									$imagefile = @file_get_contents($button['src_normal']);
									if($imagefile){
										imagedestroy($layerImage);
										$layerImage = imagecreatefromstring($imagefile);
										imagesavealpha($layerImage, true);
										imagecopyresized(
											$mainImage,
											$layerImage,
											(int)$x+$posx,
											(int)$y+$posy,
											0,
											0,
											(int)$position['width'],
											(int)$position['height'],
											imagesx($layerImage),
											imagesy($layerImage)
										);
									}
								}
								imagestring($mainImage, 1, (int)$x+$posx, (int)$y+$posy, 'BUTTON', imagecolorallocate($mainImage, 0, 0, 0));	
							}
						}
						break;
					}
					case 'calendar': {
						if(!$calendarflag) break;
						$calendarflag = false;

						$x = 0; $y = 0; $w = 0; $h = 0;
						## POSITION ##
						if($position = $xmlChildObject->position){
							$x = (int)$position['x'];
							$y = (int)$position['y'];
							$w = (int)$position['width'];
							$h = (int)$position['height'];
						}

						## YEAR ##
						if($yearObject = $xmlChildObject->year){
							//print_r($yearObject);
							$posx = 0; $posy = 0; $posw = 0; $posh = 0;
							if($position = $yearObject->position){
								$posx = (int)$position['x'];
								$posy = (int)$position['y'];
								$posw = (int)$position['width'];
								$posh = (int)$position['height'];
							}
							$src = $yearObject['src'];
							$offset = (int)$yearObject['offset'];
							$imagefile = @file_get_contents($yearObject['src']);
							if($imagefile){
								$layerImage = imagecreatefromstring($imagefile);
								imagesavealpha($layerImage, true);
								for($idx = 0; $idx<4; $idx++){
									imagecopyresized(
										$mainImage,
										$layerImage,
										(int)$x+$posx+$posw/4*$idx,
										(int)$y+$posy,
										imagesx($layerImage)/10*$idx,
										0,
										$posw/4,
										$posh/(imagesx($layerImage)/10)*($posw/4),
										imagesx($layerImage)/10,
										imagesy($layerImage)
									);
								}
							}
							imagestring($mainImage, 1, (int)$x+$posx, (int)$y+$posy, 'YEAR', imagecolorallocate($mainImage, 0, 0, 0));
						}

						## MONTH ##
						if($monthObject = $xmlChildObject->month){
							$posx = 0; $posy = 0; $posw = 0; $posh = 0;
							if($position = $monthObject->position){
								$posx = (int)$position['x'];
								$posy = (int)$position['y'];
								$posw = (int)$position['width'];
								$posh = (int)$position['height'];
							}
							$src = $monthObject['src'];
							$offset = (int)$monthObject['offset'];
							$imagefile = @file_get_contents($monthObject['src']);
							if($imagefile){
								$layerImage = imagecreatefromstring($imagefile);
								imagesavealpha($layerImage, true);
								for($idx = 0; $idx<2; $idx++){
									imagecopyresized(
										$mainImage,
										$layerImage,
										(int)$x+$posx+$posw/2*$idx,
										(int)$y+$posy,
										imagesx($layerImage)/10*$idx,
										0,
										$posw/2,
										$posh/(imagesx($layerImage)/10)*($posw/2),
										imagesx($layerImage)/10,
										imagesy($layerImage)
									);
								}
							}
							imagestring($mainImage, 1, (int)$x+$posx, (int)$y+$posy, 'MONTH', imagecolorallocate($mainImage, 0, 0, 0));
						}

						## DATE ##
						if($dateObject = $xmlChildObject->date){
							$posx = 0; $posy = 0; $posw = 0; $posh = 0;
							if($position = $dateObject->position){
								$posx = (int)$position['x'];
								$posy = (int)$position['y'];
								$posw = (int)$position['width'];
								$posh = (int)$position['height'];
							}
							$column = (int)$dateObject['column'];
							$offsetX = (int)$dateObject['offset_x'];
							$offsetY = (int)$dateObject['offset_y'];
							$imagefile = @file_get_contents($dateObject['src']);
							$imagefile_sat = @file_get_contents($dateObject['saturdaysrc']);
							$imagefile_sun = @file_get_contents($dateObject['sundaysrc']);
							if($imagefile){
								if($imagefile_sun) $imagefile_sun = $imagefile;
								if($imagefile_sat) $imagefile_sat = $imagefile;
								$layerImage = imagecreatefromstring($imagefile);
								$layerImage_sun = imagecreatefromstring($imagefile_sun);
								$layerImage_sat = imagecreatefromstring($imagefile_sat);
								imagesavealpha($layerImage, true);
								$idx = 0;
								if($column==0) $column=7;
								for($ridx = 0; $idx<30; $ridx++){
									for($cidx = 0; $cidx<$column; $cidx++){
										$idx = $ridx*$column+$cidx;
										if($idx>30) break;
										imagecopyresized(
											$mainImage,
											$layerImage,
											(int)$x+$posx+$posw/$column*$cidx,
											(int)$y+$posy+$posh/$column*$ridx,
											imagesx($layerImage)/10*$cidx,
											0,
											$posw/$column*2/5,
											$posh/$column*2/5,
											imagesx($layerImage)/10,
											imagesy($layerImage)
										);
										imagecopyresized(
											$mainImage,
											$layerImage,
											(int)$x+$posx+$posw/$column*$cidx+$posw/$column*2/5,
											(int)$y+$posy+$posh/$column*$ridx,
											imagesx($layerImage)/10*$cidx,
											0,
											$posw/$column*2/5,
											$posh/$column*2/5,
											imagesx($layerImage)/10,
											imagesy($layerImage)
										);
									}
								}
							}
							imagestring($mainImage, 1, (int)$x+$posx, (int)$y+$posy, 'DATE', imagecolorallocate($mainImage, 0, 0, 0));
						}

						## BUTTON CALENDAR ##
						foreach($xmlChildObject->button_cal as $button){
							if($position = $button->position){
								$posx = (int)$position['x'];
								$posy = (int)$position['y'];

								if($button['src_normal']){
									$imagefile = @file_get_contents($button['src_normal']);
									if($imagefile){
										imagedestroy($layerImage);
										$layerImage = imagecreatefromstring($imagefile);
										imagecolortransparent($layerImage);
										imagesavealpha($layerImage, true);
										imagecopyresized(
											$mainImage,
											$layerImage,
											(int)$x+$posx,
											(int)$y+$posy,
											0,
											0,
											(int)$position['width'],
											(int)$position['height'],
											imagesx($layerImage),
											imagesy($layerImage)
										);
									}
								}
								imagestring($mainImage, 1, (int)$x+$posx, (int)$y+$posy, 'BUTTON', imagecolorallocate($mainImage, 0, 0, 0));	
							}
						}
						break;
					}
					default: break;
				}
			}
			imagepng($mainImage, CUSTOM_DIR.'thumbs/'.$cuid.'.png');
		}
		if(!$cWidth) $cWidth = imagesx($mainImage);
		if(!$cHeight) $cHeight = imagesy($mainImage);

		$result = imagecreatetruecolor($cWidth, $cHeight);
		imagecolortransparent($result);
		imagecopyresized($result, $mainImage, 0, 0, 0, 0, $cWidth, $cHeight, imagesx($mainImage), imagesy($mainImage));
		imagepng($result);
		imagedestroy($result);
	}

	private function hex2rgb($hex) {
		$hex = str_replace("#", "", $hex);

		if(strlen($hex) == 3) {
			$r = hexdec(substr($hex,0,1).substr($hex,0,1));
			$g = hexdec(substr($hex,1,1).substr($hex,1,1));
			$b = hexdec(substr($hex,2,1).substr($hex,2,1));
		} else {
			$r = hexdec(substr($hex,0,2));
			$g = hexdec(substr($hex,2,2));
			$b = hexdec(substr($hex,4,2));
		}
		$rgb = array($r, $g, $b);
		return $rgb;
	}



	/***************************************************************************
		GET INFORMATION /key, value user_idx @ table 'auth'
			$key : KEY (STRING)
			$value : VALUE (STRING)
		= success : $row (PHP_OBJECT)
			customName : CUSTOM NAME (STRING)
			customsrc : SRC (STRING)
		= failed : FALSE
	***************************************************************************/
	private function get_info_kv($key, $value){
		### INPUT DATA ###
		$user_info = array(
			$key => $value
		);

		### EXECUTE QUERY ###
		$this->db->from('custom');
		$this->db->where($user_info);
		$query = $this->db->get();

		### OUTPUT FORMAT ###
		return $query->row();
	}
	public function get_info($cuid) { return $this->get_info_kv('cuid', $cuid); }
	public function get_writer($cuid) {
		$this->db->from('custom');
		$this->db->join('user', 'user.uid = custom.writer_uid');
		$this->db->where('cuid', $cuid);
		$query = $this->db->get();
		return $query->row()->id;
	}

	public function get_all_customlist($offset='', $limit=''){
		$this->db->from('custom');
		$this->db->order_by('cuid', 'DESC');
		if($offset!='' || $limit !='') $this->db->limit($limit, $offset);
		$query = $this->db->get();
		return $query->result();
	}

	public function get_userCustom($cuid){
		$this->db->from('userCustom');
		$this->db->join('user', 'user.uid = userCustom.user_uid');
		$this->db->where('custom_cuid', $cuid);
		$this->db->order_by('user.id', 'ASC');
		$query = $this->db->get();
		return $query->result();
	}

	public function hasCustom($uid, $cuid){
		$this->db->from('userCustom');
		$this->db->where(array(
			'custom_cuid' => $cuid,
			'user_uid' => $uid
		));
		$query = $this->db->get();
		return $query->row();
	}

	public function get_defaultXML(){
		$code_content = file_get_contents(base_url('custom/default_memo.xml'));
		return $code_content;
	}


	/***************************************************************************
		REGISTER XML /w $custom
			$custom : Array (
				- writer_uid : id (STRING)
				- customName : STRING
			)
		= success : TRUE
		= failed : FALSE
	***************************************************************************/
	public function registerXML($custom){
		$customDB = array(
			'writer_uid' => $custom['uid'],
			'customName' => $custom['name']
		);
		$result = $this->db->insert('custom', $customDB);
		
		if(!$result) return FALSE;

		return $this->db->insert_id();
	}


	/***************************************************************************
		DELETE CUSTOM COMPONENT /w $custom
			$custom : Array (
				- writer_uid : id (STRING)
				- customName : STRING
			)
		= success : TRUE
		= failed : FALSE
	***************************************************************************/
	public function modifyXML($custom){
		$this->db->where('cuid', $custom['cuid']);
		$result = $this->db->update('custom', array(
			'customName' => $custom['name']
		));

		if(!$result) return FALSE;
		return TRUE;
	}

	/***************************************************************************
		DELETE CUSTOM COMPONENT /w $custom
			$custom : Array (
				- writer_uid : id (STRING)
				- customName : STRING
			)
		= success : TRUE
		= failed : FALSE
	***************************************************************************/
	public function deleteCustom($cuid){
		$this->db->from('custom');
		$this->db->where(array(
			'cuid' => $cuid
		));
		$result = $this->db->delete();

		if(!$result) return FALSE;
		return TRUE;
	}


	/***************************************************************************
		GET USELIST /w $uid
		= success : USELIST (ARRAY)
		= failed : FALSE
	***************************************************************************/
	public function get_uselist($uid){
		$this->db->from('userCustom');
		$this->db->join('custom', 'custom.cuid = userCustom.custom_cuid');
		$this->db->where('user_uid', $uid);
		$this->db->order_by('custom_cuid', 'ASC');
		$query = $this->db->get();
		return $query->result();
	}



	/***************************************************************************
		ADD USELIST /w $custom
			$custom : Array (
				- user_uid : id (STRING)
				- custom_cuid : cuid (STRING)
			)
		= success : TRUE
		= failed : FALSE
	***************************************************************************/
	public function add_uselist($uid, $cuid){
		if($this->hasCustom($uid, $cuid)) return TRUE;
		$newList = array(
			'user_uid' => $uid,
			'custom_cuid' => $cuid
		);
		$result = $this->db->insert('userCustom', $newList);
		if(!$result) return FALSE;
		return $this->db->insert_id();
	}

	/***************************************************************************
		DELETE USELIST /w $custom
			$custom : Array (
				- user_uid : id (STRING)
				- custom_cuid : cuid (STRING)
			)
		= success : TRUE
		= failed : FALSE
	***************************************************************************/
	public function del_uselist($uid, $cuid){
		if(!$this->hasCustom($uid, $cuid)) return TRUE;

		$this->db->from('userCustom');
		$this->db->where(array(
			'custom_cuid' => $cuid,
			'user_uid' => $uid
		));
		$result = $this->db->delete();

		if(!$result) return FALSE;
		return TRUE;
	}


}

?>