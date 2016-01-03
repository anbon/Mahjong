<?php
class Lmodel extends CI_Model {

	public function __construct(){
        $this->load->database();
    }

    public function verify($user,$pass){
    	$query=$this->db->get_where('mj_member',array('username' => $user , 'password' => $pass ) );
    	$row=$query->row_array();
    	if($user != null && $pass != null && $row["username"] == $user && $row["password"] == $pass){
    		if($row["level"]=='0'){
                return 0;
            }else{
                return $row["num"];
            }
    	}else{
            return '-1';
        }
    }

    public function writetoken($token,$username){
        date_default_timezone_set("Asia/Shanghai");
        $datetime=date("Y-m-d h:i:s");
        $data =array('token'=> $token,'datetime'=>$datetime);
        $this->db->update('mj_member', $data, array('username' => $username));
    }

    public function get_member(){
        $sql = "SELECT * FROM `mj_member`";
        $r = $this->db->query($sql);
        $ts = $r->result_array(); 
        return $ts;
    }

    public function get_edit($username){
        $base =base_url();
        $sql = "SELECT mj.num,mj.name,mj.email,mj.username,mj.password,mj.gender,mj.age,CONCAT('{$base}' ,CONCAT('assets/photo/',mj.photo)) photo ,mj.level ,
                CASE 
                WHEN mj.rating= '0' THEN '0' 
                ELSE ROUND((mj.rating/mj.ratingpeople),1)
                END 
                AS rate 

                FROM `mj_member` mj WHERE `username`= '{$username}'";
        $r = $this->db->query($sql);
        $ts = $r->row();
        if($ts==""){
            return 'NULL';
        }
        return $ts;
    }
    public function editact($name,$email,$username,$password){
        $data =array(   
            'name'=>$name,
            'email'=>$email,
            'password'=>$password
        );
        $this->db->update('mj_member', $data, array('username' => $username));
    }
    public function register($registerName,$registerEmail,$registerUser,$registerPass){
        $this->db->select_max('num');
        $query=$this->db->get('mj_member');
        $row = $query->row_array();
        $max=$row["num"];
        $data =array(        
            'name'=>$registerName,
            'email'=>$registerEmail,
            'username'=>$registerUser,
            'password'=>$registerPass,
            'photo'=>'0.jpg'
        );
        $this->db->insert('mj_member',$data);  
    }
    public function delete($num){
        $this->db->delete('mj_member',array('num' => $num));
    }
    public function createchat($userid,$name,$base,$unit,$circle,$time,$location,$people,$type,$cigarette,$rule){
        date_default_timezone_set("Asia/Shanghai");
        $date=date("Y-m-d H:i:s");
        $alive = date('Y-m-d H:i:s',strtotime($date . "+30 minutes"));
        $this->db->select_max('num');
        $query=$this->db->get('mj_chatroom');
        $row = $query->row_array();
        $max=$row["num"]+1;
        $data= array(
            'num'=>$max,
            'name'=>$name,
            'base'=>$base,
            'unit'=>$unit,
            'circle'=>$circle,
            'time'=>$time,
            'location'=>$location,
            'people'=>$people,
        	'type'=>$type,
            'cigarette'=>$cigarette,
            'datetime'=>$alive,
            'rule'=>$rule
        );
        $this->db->insert('mj_chatroom',$data);

        // $query=$this->db->get_where('mj_member',array('token' => $token ) );
        // $row=$query->row_array();
        // $userid=$row["num"];

        $data =array(
            'chatID'=>$max,
            'memID'=>$userid,
            'level'=>'2',
            'time'=>$alive
        );
        $this->db->insert('mj_mem_chat',$data);
        $query=$this->db->get_where('mj_chatroom',array('num' => $max ) );
        $amount=$query->num_rows();
        if($amount>0){
            return $max;
        }else return 0;

    }
    public function get_rooms(){
        $sql = "SELECT num,name,(SELECT COUNT(*)  FROM `mj_mem_chat` WHERE `mj_mem_chat`.`chatID` = `mj_chatroom`.`num` AND `mj_mem_chat`.`level` !=0 ) AS count FROM mj_chatroom";
        $r = $this->db->query($sql);
        $ts = $r->result_array(); 
        return $ts;
    }
    public function getChatroom($room_id){
        $sql = "SELECT * FROM `mj_chatroom` WHERE `num`= '{$room_id}' ";
        $r = $this->db->query($sql);
        $ts = $r->row();
        return $ts;
    }
    public function useramount($num){
        $query=$this->db->get_where('mj_mem_chat',array('chatID' => $num ,'level' => '1' ) );
        $amount=$query->num_rows()+1;
        return $amount;
    }
    public function searchcreater($num){
    	$sql = "SELECT * FROM `mj_member` WHERE `num` IN (SELECT `memID` FROM `mj_mem_chat` WHERE `chatID` = '{$num}' AND `level` = '2')";
    	$r = $this->db->query($sql);
    	$ts = $r->row();
    	return $ts;
    }
    public function searchmate($num){
    	$sql = "SELECT * FROM `mj_member` WHERE `num` IN (SELECT `memID` FROM `mj_mem_chat` WHERE `chatID` = '{$num}' AND `level` = '1')";
    	$r = $this->db->query($sql);
    	$ts = $r->row();
    	return $ts;
    }
    public function editpage($RegisterUser,$RegisterEmail){
        $query=$this->db->get_where('mj_member', array('username' => $RegisterUser));
        $num1=$query->num_rows();
        $query=$this->db->get_where('mj_member', array('email' => $RegisterEmail));
        $num2=$query->num_rows();
        return $num1+$num2;
    }
    public function applyChat($user_ID,$room_ID){
        $query=$this->db->get_where('mj_mem_chat', array('chatID' => $room_ID ,'memID' => $user_ID ) );
        $exist=$query->num_rows();
        if($exist==1){
            return '-1';
        }else{
            $query=$this->db->get_where('mj_mem_chat', array('chatID' => $room_ID , 'level ' =>1));
            $num=$query->num_rows()+1;
            $data=array(
                'chatID'=>$room_ID,
                'memID'=>$user_ID,
                'level'=>'0'
            );
            $this->db->insert('mj_mem_chat',$data);
            return $num;
        }
    }
    public function waitChat($user_ID,$room_id){
        $query=$this->db->get_where('mj_mem_chat',array('chatID' => $room_id ,'memID'=> $user_ID) );
        $num=$query->num_rows();
        if($num=='1'){
            $row = $query->row_array();
            $level=$row["level"];
                
            $query=$this->db->get_where('mj_mem_chat',array('chatID' => $room_id ,'level'=> '1') );
            $num=$query->num_rows();

            $query = $this->db->get_where('mj_chatroom',array('num' => $chatID ) );
            $row = $query->row_array();
            $people = $row["people"];
            if($num==$people){
                $this->db->delete('mj_mem_chat',array('chatID' => $room_ID , 'level' => '0' ) );
            }

                           
        }else {
           $level='-1';
        }
        return $level;
        
    }

    public function SearchChat($room_ID){
        $sql = "SELECT M.num,M.name,M.gender,M.age,CONCAT('{$base}' ,CONCAT('assets/photo/',M.photo)) photo  ,CASE 
                WHEN M.rating= '0' THEN '0' 
                ELSE ROUND((M.rating/M.ratingpeople),1)
                END 
                AS rate  ,min(O.time) FROM `mj_mem_chat` O LEFT JOIN mj_member M ON O.memID = M.num WHERE `O`.`chatID` = '1' AND `O`.`level`=1";
        $r = $this->db->query($sql);
        $ts = $r->row();
        return $ts;
    }

    public function VerifyChat($status,$room_ID,$user_ID){
        
        if($status==1){
            $query=$this->db->get_where('mj_mem_chat',array('chatID' => $room_id ,'memID'=> $user_ID) );
            $num=$query->num_rows();
            if($num=='1'){
                $data = array(
                    'level'=>'1',
                    );
                $this->db->update('mj_mem_chat', $data, array('chatID' => $room_id ,'memID'=> $user_ID) );

                return '1';
            }else {
                return '0';
            }
        }else{
            $query=$this->db->get_where('mj_mem_chat',array('chatID' => $room_id ,'memID'=> $user_ID) );
            $num=$query->num_rows();
            if($num==1){
                $this->db->delete('mj_mem_chat',array('chatID' => $room_id ,'memID'=> $user_ID));
            }
            return '1';
        }

    }

    public function rating($user_ID,$Rated_ID,$rating){



        $query = $this->db->get_where('mj_rating',array('User' => $user_ID ) );
        $row = $query->row_array();
        if($row["Rated"]==$Rated_ID){
            return '0';
        }else{
            $data = array(
                'User' => $user_ID,
                'Rated' => $Rated_ID
                );
            $this->db->insert('mj_rating',$data);

            $query = $this->db->get_where('mj_member',array('num' => $Rated_ID ) );
            $row = $query->row_array();
            $rating = $row["rating"]+$rating;
            $ratingpeople = $row["ratingpeople"]+1;
            $data = array(
                'rating'=>$rating,
                'ratingpeople'=>$ratingpeople
                );
            $this->db->update('mj_member', $data, array('num' => $userid));
            return '1';
        }
    }
    public function blocklist($user_ID){
    	$sql = "SELECT * FROM `mj_member` WHERE `num` IN (SELECT `memID` FROM `mj_blocklist` WHERE `blocker` = '{$user_ID}' ) ";
    	$r = $this->db->query($sql);
    	$ts = $r->row();
    	return $ts;
    }
    public function blockadd($user_ID,$block_ID){

    	$data =array(
    			'blocker'=>$user_ID,
    			'memID'=>$block_ID
    	);
    	$this->db->insert('mj_blocklist',$data);
    }
    public function blockdelete($user_ID,$block_ID){

    	$this->db->delete('mj_blocklist',array('blocker' => $user_ID , 'memID' => $block_ID));
    }

    public function seed_alive($user_ID){
        date_default_timezone_set("Asia/Shanghai");
        $date=date("Y-m-d H:i:s");
        $alive = date('Y-m-d H:i:s',strtotime($date . "+30 minutes"));
        $data=array(
            'datetime'=>$alive
            );
        $this->db->update('mj_member',$data,array('num' => $user_ID));

        $query=$this->db->get_where('mj_mem_chat',array('memID' => $user_ID));
        $row=$query->row_array();
        $level=$row["level"];
        $chatID=$row["chatID"];
        if($level==2){
        $data=array(
            'time'=>$alive
            );
        $this->db->update('mj_mem_chat',$data,array('memID' =>$user_ID));
         $data=array(
            'datetime'=>$alive
            );
        $this->db->update('mj_chatroom',$data,array('num' =>$chatID));
        }

        return '1';
    }
    public function deleteseed(){

        $sql = "SELECT * FROM (SELECT num,name,datetime,(SELECT COUNT(*) FROM `mj_mem_chat` WHERE `mj_mem_chat`.`chatID` = `mj_chatroom`.`num` AND `mj_mem_chat`.`level` !=0 ) AS count FROM mj_chatroom) AS t WHERE count < '1' OR datetime < NOW()";
        // $sql = "SELECT * FROM `mj_mem_chat` WHERE `chatID` IN ( SELECT `chatID` FROM( SELECT `chatID` FROM `mj_mem_chat` WHERE `time`>NOW() ) AS t )";
        $r = $this->db->query($sql);
        $ts = $r->row();
        if($ts!=''){
        $seed = $ts->num;
        $this->db->delete('mj_chatroom',array('num' => $seed));
        $this->db->delete('mj_mem_chat',array('chatID' => $seed));
        }
    }


    public function dis_one($lng1,$lat1,$lng2,$lat2){
        //将角度转为狐度
        $radLat1=deg2rad($lat1);//deg2rad()函数将角度转换为弧度
        $radLat2=deg2rad($lat2);
        $radLng1=deg2rad($lng1);
        $radLng2=deg2rad($lng2);
        $a=$radLat1-$radLat2;
        $b=$radLng1-$radLng2;
        $s=ROUND((2*asin(sqrt(pow(sin($a/2),2)+cos($radLat1)*cos($radLat2)*pow(sin($b/2),2)))*6378.137),1);
        return $s;
    }
    public function token_ID($user_token){
        $query=$this->db->get_where('mj_member',array('token' => $user_token));
        $row=$query->row_array();
        $num=$row["num"];
        return $num;
    }
    public function writelocation($num,$location_x,$location_y){
         $data =array(
            'location_x'=>$location_x,
            'location_y'=>$location_y
            );
        $this->db->update('mj_member', $data, array('num' => $num));
    }
    public function location_x($num){
        $query=$this->db->get_where('mj_member',array('num' => $num));
        $row=$query->row_array();
        $location_x=$row["location_x"];
        return $location_x;
    }
    public function location_y($num){
        $query=$this->db->get_where('mj_member',array('num' => $num));
        $row=$query->row_array();
        $location_y=$row["location_y"];
        return $location_y;
    }

    
    public function dis($location_x,$location_y){
        $base =base_url();


        $sql = "SELECT C.num 'RoomNum' , C.name 'RoomName' ,C.base,C.unit,C.circle,C.time,C.location,C.people,C.type,C.cigarette,C.rule,
                ROUND((2*ASIN(SQRT(POW(SIN((radians('{$location_y}')-radians(U.location_y))/2),2)+COS(radians('{$location_y}'))*COS(radians(U.location_y))*POW(SIN( (radians('{$location_x}')-radians(U.location_x) )/2),2)))*6378.137),1) as dis
                , U.num 'Unum' , U.name 'Uname' , CONCAT('{$base}' ,CONCAT('assets/photo/',U.photo)) photo , O.level
                FROM mj_chatroom C LEFT JOIN mj_mem_chat O ON O.chatID = C.num JOIN  `mj_member` U ON U.num = O.memID
                WHERE O.time>NOW() ORDER BY level DESC , dis ASC , RoomNum ASC";
        $r = $this->db->query($sql);

        // $data = array();
        // foreach ($r->result_array() as $row){
        //     if( array_key_exists($row['RoomNum'],$data) ){
        //         array_push( $data[$row['RoomNum'] ]['users'], array("Unum" => $row['Unum'] , "Uname" => $row['Uname'] , "photo" => $row['photo'] , "level" => $row['level'] ) );
        //     }else{
        //         $data[ $row['RoomNum'] ] = array(
        //             "RoomName" => $row['RoomName'],
        //             "base" => $row['base'],
        //             "unit" => $row['unit'],
        //             "circle" => $row['circle'],
        //             "time" => $row['time'],
        //             "location" => $row['location'],
        //             "people" => $row['people'],
        //             "type" => $row['type'],
        //             "cigarette" => $row['cigarette'],
        //             "dis" => $row['dis'],
        //             "users" => array("Unum" => $row['Unum'] , array("Uname" => $row['Uname'] , "photo" => $row['photo'] , "level" => $row['level']) )
        //         );
        //     }
        // }

        $data = array();
        $ids = array();
        foreach ($r->result_array() as $row){
            if( in_array($row['RoomNum'],$ids ) ){
                $index = array_search($row['RoomNum'], $ids);
                array_push( $data[ $index ]['users'] , array("Unum" => $row['Unum'] , "Uname" => $row['Uname'] , "photo" => $row['photo'] , "level" => $row['level'] ) );
            }else{
                $obj = array(
                    "RoomNum" => $row['RoomNum'],
                    "RoomName" => $row['RoomName'],
                    "base" => $row['base'],
                    "unit" => $row['unit'],
                    "circle" => $row['circle'],
                    "time" => $row['time'],
                    "location" => $row['location'],
                    "people" => $row['people'],
                    "type" => $row['type'],
                    "cigarette" => $row['cigarette'],
                    "dis" => $row['dis'],
                    "users" => array("0" =>array("Unum" => $row['Unum'] , "Uname" => $row['Uname'] , "photo" => $row['photo'] , "level" => $row['level'] ))
                );
                array_push($data, $obj);
                array_push($ids, $row['RoomNum']);
            }
        }

        return $data;

    }
    public function User_info($user_ID,$gender,$name,$age){
        $data = array(
            'gender' => $gender,
            'name' => $name,
            'age' => $age
            );
        $this->db->update('mj_member',$data,array('num' => $user_ID) );

        return '1';
    }
    public function search_photo($user_ID){
        $query=$this->db->get_where('mj_member',array('num' => $user_ID) );
        $row=$query->row_array();
        $photo=$row["photo"];
        return $photo;   
    }
    public function User_photo($user_ID,$date){
        
        $data =array(
            'photo'=> $user_ID.'_'.$date.'.jpg'
            );
        $this->db->update('mj_member',$data,array('num' => $user_ID));
    }


    public function Search_ID($user_ID){
        $base =base_url();
        $sql = "SELECT num,name,email,username,password,gender,age,  CONCAT('{$base}' ,CONCAT('assets/photo/',mj_member.photo)) photo ,level ,ROUND((`mj_member`.`rating`/`mj_member`.`ratingpeople`),1) as rate FROM `mj_member` WHERE `num`= '{$user_ID}'";
        $r = $this->db->query($sql);
        $ts = $r->row();
        if($ts==""){
            return 'NULL';
        }else{
        return $ts;
        }
    }



    

}
