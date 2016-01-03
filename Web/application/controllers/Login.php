<?php
require_once 'ServerAPI.php';

class Login extends CI_Controller {

	public function __construct(){
    	parent::__construct();
        $this->load->model('Lmodel');
    }

    public function login(){
    	$this->load->view('mj/login');
    }

    public function verify(){

        $serverAPI = new ServerAPI("25wehl3uwk9bw", "o50nPHUuRaanG");

        $result = array("status" => "-999", "message" => "", "startPath" => "" , "data" => "");
        $username = isset($_POST["username"]) ? $_POST["username"] : "";
        $password = isset($_POST["password"]) ? $_POST["password"] : "";
        if($username==''||$password==''){
            $result["message"] = "登入請確實填寫";
        }else{
            
            $user = $this->Lmodel->verify($username,$password);

            $token = $serverAPI->getToken($user,$username,"1");
            $getdecode =json_decode($token,true);
            $gettoken = $getdecode['token'] ;
            $this->Lmodel->writetoken($gettoken,$username);
            if($user != "-1"){
                @session_start();
                $_SESSION["user"] = $user;
                $result["message"]= $gettoken;
                $result["status"] = "1";
                $result["startPath"] = "index.php/table";
                $result["data"] = $this->Lmodel->get_edit($username) ;
            }else if($user == "-1"){
                $result["status"] = "-1";
                $result["message"] = "資料不正確";
            }else{
                $reult["message"] = "帳號審核中";
            }
        }
        echo json_encode($result);
    }

    public function success(){
        date_default_timezone_set("Asia/Shanghai");
        $date=date("Y-m-d H:i:s");
        $tomorrow = date('Y-m-d H:i:s',strtotime($date . "+30 minutes"));
        if($tomorrow<$date){
            echo $date;
        }else echo $tomorrow;

        $this->Lmodel->deleteseed();
        $this->load->view('mj/success');
    }
    public function seed(){
        $account= array("status" => "-999" ,"message" => "" );
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        $location_x = isset($_POST["location_x"]) ? $_POST["location_x"] : "0";
        $location_y = isset($_POST["location_y"]) ? $_POST["location_y"] : "0";
        if($location_y==0||$location_x==0){
            $account["status"]=0;
        }else{
            $status=$this->Lmodel->seed_alive($user_ID);
            if($status==1){
                $account["status"] = $status;
                $this->Lmodel->writelocation($user_ID,$location_x,$location_y);
                $new_location_x=$this->Lmodel->location_x($user_ID);
                $new_location_y=$this->Lmodel->location_y($user_ID);

                
                $account["message"]=$this->Lmodel->dis($new_location_x,$new_location_y);
            }else {$account["status"]=0;}
        }
        
        echo json_encode($account);
    }
    public function table(){
        $data['room']=$this->Lmodel->get_rooms();
        $data['member']=$this->Lmodel->get_member(); 
        $this->load->view('templates/header');       
        $this->load->view('mj/table',$data);
        $this->load->view('mj/table2',$data);
        $this->load->view('templates/footer');
    }
    public function room(){
        $data['room']=$this->Lmodel->get_rooms();
        $this->Lmodel->deleteseed();
        $this->load->view('mj/table2',$data);
    }

    public function getmember(){
        echo json_encode($this->Lmodel->get_member());
    }

    public function edit(){
        $account= array("status" => "-999" ,"message" => "");
        $username = isset($_POST["username"]) ? $_POST["username"] : 0;
        // $location_x = isset($_POST["location_x"]) ? $_POST["location_x"] : 0;
        // $location_y = isset($_POST["location_y"]) ? $_POST["location_y"] : 0;

        $account["message"]=$this->Lmodel->get_edit($username);
        if($account["message"]=='NULL'){
            $account["status"]= '0';
        }else {
            $account["status"]= '1';
        }
        // if($location_x==0||$location_y==0){
        //     $account["dis"]='NULL';
        // }else{
        //     $account["dis"]=$this->Lmodel->dis_one($location_x,$location_y,$account["message"]->location_x,$account["message"]->location_y);
        // }
        echo json_encode($account);
    }
    public function edit_web(){
        $username = isset($_POST["username"]) ? $_POST["username"] : 0;
        echo json_encode($this->Lmodel->get_edit($username));
    }

    public function editact(){
        
        $name = isset($_POST["name"]) ? $_POST["name"] : "";
        $email = isset($_POST["email"]) ? $_POST["email"] : "";
        $username = isset($_POST["username"]) ? $_POST["username"] : "";
        $password = isset($_POST["password"]) ? $_POST["password"] : "";
        alert($name);
        $this->Lmodel->editact($name,$email,$username,$password);
        echo "<script>location.href='".base_url("index.php/table")."'; </script>";
    }
    public function delete($num){
        $this->Lmodel->delete($num);
        echo "<script>location.href='".base_url("index.php/table")."'; </script>";
    }
    public function register(){
        $account= array("status" => "-999" ,"message" => "" ,"startPath" => "" );
        $registerName = isset($_POST["registerName"]) ? $_POST["registerName"] : "";
        $registerEmail = isset($_POST["registerEmail"]) ? $_POST["registerEmail"] : "";
        $registerUser = isset($_POST["registerUser"]) ? $_POST["registerUser"] : "";
        $registerPass = isset($_POST["registerPass"]) ? $_POST["registerPass"] : "";
        if($registerName==''||$registerEmail==''||$registerUser==''||$registerPass==''){
            $account["message"]="註冊請確實填寫";
        }else{
            $precence=$this->Lmodel->editpage($registerUser,$registerEmail);
            if($precence=='1'||$precence=='2'){
                $account['message']="帳號或信箱已存在";
            }else{
                $this->Lmodel->register($registerName,$registerEmail,$registerUser,$registerPass);
                $account['status']="1";
                $account['message']="註冊成功";
                $account['startPath']="index.php/login";
            }
        }
        echo json_encode($account);
    }
    public function createchat(){
        $account= array("status" => "-999" ,"RoomNum" => "");
        $userid = isset($_POST["userid"]) ? $_POST["userid"] : "";
        $name = isset($_POST["name"]) ? $_POST["name"] : "";
        $base = isset($_POST["base"]) ? $_POST["base"] : "";
        $unit = isset($_POST["unit"]) ? $_POST["unit"] : "";
        $circle = isset($_POST["circle"]) ? $_POST["circle"] : "";
        $time = isset($_POST["time"]) ? $_POST["time"] : "";
        $location = isset($_POST["location"]) ? $_POST["location"] : "";
        $people = isset($_POST["people"]) ? $_POST["people"] : "";
        $type = isset($_POST["type"]) ? $_POST["type"] : "";
        $cigarette = isset($_POST["cigarette"]) ? $_POST["cigarette"] : "";
        $rule = isset($_POST["rule"]) ? $_POST["rule"] : "";
        
        $chatroomCreate=$this->Lmodel->createchat($userid,$name,$base,$unit,$circle,$time,$location,$people,$type,$cigarette,$rule);
        

        if($chatroomCreate=='0'){
            $account["status"]='0';
        }else {
            $account["status"]='1';
            $account["RoomNum"]=$chatroomCreate;
        }
        echo json_encode($account);
    }
    public function getChatroom(){
    	$room_id = isset($_POST["room_id"]) ? $_POST["room_id"] :"";
    	echo json_encode($this->Lmodel->getChatroom($room_id));
    }
    public function userChatroomQuery(){
        $account= array("amount" => "-999" ,"creater" => "" , "mate" => "");
        $num = isset($_POST["num"]) ? $_POST["num"] : "";
        $account['amount'] = $this->Lmodel->useramount($num);
        $account['creater'] = $this->Lmodel->searchcreater($num);
        $account['mate'] = $this->Lmodel->searchmate($num);
        
        echo json_encode($account);
    }
    public function applyChat(){
        $intoChat = array("status" => "-999" , "message" => "" , );
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        $room_ID = isset($_POST["room_ID"]) ? $_POST["room_ID"] : "";

        if($user_ID==''||$room_ID==''){
            $intoChat["message"]="請輸入使用者及房號";
            $intoChat["status"]='0';
        }else{
            $intoChat["status"] =$this->Lmodel->applyChat($user_ID,$room_ID);
            if($intoChat["status"]==4){
                $intoChat["message"]="房間已滿";
                $intoChat["status"]='0';
            }else if($intoChat["status"]=='-1'){
                $intoChat["status"]='0';
                $intoChat["message"]="已申請";
            }else{
                $intoChat["message"]="房間未滿";
                $intoChat["status"]='1';
            }

        }
        echo json_encode($intoChat);
    }
    public function waitChat(){
        $account = array("status" => "-999" , "message" => "" , );
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        $room_ID = isset($_POST["room_ID"]) ? $_POST["room_ID"] : "";
        $position = $this->Lmodel->waitChat($user_ID,$room_ID);
        if($position=='-1'){
            $account["status"]='0';
        }else{
            $account["status"]='1';
            if($position==0){
                $account["message"] = '等待中';
            }else{
                $account["message"] = '已加入';
            }
        }

        echo json_encode($account);
    }

    public function SearchChat(){
        $account = array("status" => "-999" , "message" => "" , );
        
        $room_ID = isset($_POST["room_ID"]) ? $_POST["room_ID"] : "";

        $account["message"]=$this->Lmodel->SearchChat($room_ID);
        if($account["message"]==''){
            $account["status"]='0';
        }else {
            $account["status"]='1';
        }
        echo json_encode($account);

    }
    public function VerifyChat(){
        $account = array("status" => "-999" , "message" => "" , );
        $status = isset($_POST["room_ID"]) ? $_POST["room_ID"] : "";
        $room_ID = isset($_POST["room_ID"]) ? $_POST["room_ID"] : "";
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        if($status==1||$status==0){
            $account["status"]=$this->Lmodel->VerifyChat($status,$room_ID,$user_ID);
        }else{
            $account["status"]='0';
            $account["message"]='狀態錯誤';
        }
        echo json_encode($account);

    }

    public function rating(){
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        $Rated_ID = isset($_POST["Rated_ID"]) ? $_POST["Rated_ID"] : "";
        $rating = isset($_POST["rating"]) ? $_POST["rating"] : "";
        echo json_encode($this->Lmodel->rating($user_ID,$Rated_ID,$rating));
    }  
    public function blocklist(){
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        $blocklist = $this->Lmodel->blocklist($user_ID);
        echo json_encode($blocklist);

    }
    public function blockadd(){
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        $block_ID = isset($_POST["block_ID"]) ? $_POST["block_ID"] : "";
        $this->Lmodel->blockadd($user_token,$block_name);

    }
    public function blockdelete(){
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : "";
        $block_ID = isset($_POST["block_ID"]) ? $_POST["block_ID"] : "";
		$this->Lmodel->blockdelete($user_ID,$block_name);
    }
    public function position(){
        echo $this->Lmodel->dis_one(31.2014966,121.40233369999998,31.22323799999999,121.44552099999998);
    }
    public function User_info(){
        $account = array("status" => "-999");

        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : ""; 
        
        $gender = isset($_POST["gender"]) ? $_POST["gender"] : "";
        $name = isset($_POST["name"]) ? $_POST["name"] : "";
        $age = isset($_POST["age"]) ? $_POST["age"] : "";


        if($user_ID!=""||$gender!=""||$name!=""||$age!=""){
        $success = $this->Lmodel->User_info($user_ID,$gender,$name,$age);

        $account["status"] = $success;
        }else $account["status"] = '0';
        echo json_encode($account);
    }

    public function Search_ID(){
        $account= array("status" => "-999" ,"message" => "");
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : 0;

        $account["message"]=$this->Lmodel->Search_ID($user_ID);
        if($account["message"]=='NULL'){
            $account["status"]= '0';
        }else {
            $account["status"]= '1';
        }
        echo json_encode($account);


    }
    public function User_photo(){
        $account= array("status" => "-999" , "message" => "");
        $user_ID = isset($_POST["user_ID"]) ? $_POST["user_ID"] : 0;
        date_default_timezone_set("Asia/Shanghai");
        $date= date("Y_m_d_H_i_s");;
        if($user_ID==''){
            $account["status"]='0';
            $account["message"]='ID_NULL';
        }else{
        // $photo = print_r($_FILES["file"]);
        // $this->Lmodel->test($user_ID,$photo);
            if ($_FILES["file"]["error"] > 0){
                $account["status"]='0'; 
            // 　echo "Error: " . $_FILES["file"]["error"];
            }else{
                $tmp_name = $_FILES["file"]["tmp_name"];
                // $name = $_FILES["file"]["name"];
                move_uploaded_file($tmp_name,"assets/photo/".$user_ID."_".$date.".jpg");
                chmod("assets/photo/".$user_ID."_".$date.".jpg", 777);
                $photo = $this->Lmodel->search_photo($user_ID);
                if($photo!='0.jpg'){
                    $photo = 'assets/photo/'.$photo;
                    unlink($photo);
                }
                $this->Lmodel->User_photo($user_ID,$date);
                $account["status"]='1';
            }
        }
        $url=$this->Lmodel->search_photo($user_ID);
        $location=base_url().'assets/photo/'.$url;
        $account["message"]=$location;
        echo json_encode($account);

    }
    public function upload(){
        date_default_timezone_set("Asia/Shanghai");
        $date= date("Y_m_d_H_i_s");;
        echo $date;
        $this->load->view("mj/upload");
    }

    public function uploading(){
        echo print_r($_FILES["file"]);
                    date_default_timezone_set("Asia/Shanghai");
                    $date= date("Y_m_d_H_i_s");;

                    $tmp_name = $_FILES["file"]["tmp_name"];
                    $name = $_FILES["file"]["name"];
                    move_uploaded_file($tmp_name,"assets/photo/".$name."_".$date.".jpg");
                
            
        
    }



}