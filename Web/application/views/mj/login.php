<!DOCTYPE html>
<html lang="en">
<head>

  <meta charset="utf-8">
  <title>AvocadoPanel</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">

  <!-- Styles -->
  <link href='<?php echo base_url();?>assets/css/chosen.css' rel='stylesheet' type="text/css">
  <link href="<?php echo base_url();?>assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
  <link href="<?php echo base_url();?>assets/css/theme/avocado.css" rel="stylesheet" type="text/css" id="theme-style">
  <link href="<?php echo base_url();?>assets/css/prism.css" rel="stylesheet/less" type="text/css">
  <link href='<?php echo base_url();?>assets/css/fullcalendar.css' rel='stylesheet' type="text/css">
  <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,400,600,300' rel='stylesheet' type='text/css'> 
  <style type="text/css">
    body { padding-top: 102px; }
  </style>
  <link href="<?php echo base_url();?>assets/css/bootstrap-responsive.css" rel="stylesheet">
  
  <!-- JavaScript/jQuery, Pre-DOM -->
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> 
  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
  <script src="<?php echo base_url();?>assets/js/charts/excanvas.min.js"></script>
  <script src="<?php echo base_url();?>assets/js/charts/jquery.flot.js"></script>
  <script src="<?php echo base_url();?>assets/js/jquery.jpanelmenu.min.js"></script>
  <script src="<?php echo base_url();?>assets/js/jquery.cookie.js"></script>
  <script src="<?php echo base_url();?>assets/js/avocado-custom-predom.js"></script>

  <!-- HTML5, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

  <script type="text/javascript">
  
  // 當畫面升成後執行
  $(function(){

    $("#btnLogin").click(function(){
      $.ajax({
        url : "<?php echo base_url();?>index.php/verify",
        dataType : "json",
        type : "post",
        cache : false,
        data : {
          username : $("#username").val(),
          password : $("#password").val()
        }
      }).done(function(result){
          if(result){
            if(result.status != undefined && result.status == "1" && result.startPath != undefined && result.startPath != ""){
              location.href = "<?php echo base_url();?>" + result.startPath;
            }else if (result.message != undefined && result.message != ""){
              // $("#test").html(result.message);
               alert(result.message);
            }else{
              alert("系統錯誤");
            }
          }else{
            alert("系統錯誤");
          }
      });
    });

    $("#btnRegister").click(function(){
      $('#login').hide();
      $('#register').show();
      

    });
    $("#cancle").click(function(){
      $('#login').show();
      $('#register').hide();
    });
    $("#btnregis").click(function(){
      $.ajax({
         url : "<?php echo base_url();?>index.php/register",
        dataType : "json",
        type : "post",
        cache : false,
        data : {
          registerName : $("#RegisterName").val(),
          registerEmail : $("#RegisterEmail").val(),
          registerUser : $("#RegisterUser").val(),
          registerPass : $("#RegisterPass").val()
        }
      }).done(function(account){
          if(account){
            if(account.status != undefined && account.status =="1" && account.startPath != undefined && account.startPath != ""){
              alert(account.message);
              location.href = "<?php echo base_url();?>" + account.startPath;
            }else if (account.message != undefined && account.message !=""){
              alert(account.message);
            } 

          }
      })
    });


  });

  </script>

</head>

<body>


     <div id="login" class="container">  

        <form class="form-signin form-horizontal" action="" >
        <div class="top-bar">
          <h3><i class="icon-leaf"></i> Avocado<b>Panel</b></h3>
        </div>
        <div class="well no-padding">

          <div class="control-group">
            <label class="control-label" for="inputName"><i class="icon-user"></i></label>
            <div class="controls">
              <input id="username" type="text" name="username" placeholder="Username" value="">
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="inputUsername"><i class="icon-key"></i></label>
            <div class="controls">
              <input id="password" type="password" name="password" placeholder="Password">
            </div>
          </div>

        <div class="padding">
          <button id="btnLogin" class="btn btn-primary" type="button">Sign in</button>
          <button id="btnRegister" class="btn" type="button">Register member</button>
          </div>
        </div>
      </form>

    </div> 


    <div id="register" class="container" style="display:none">  

        <form class="form-signin form-horizontal" action="register" method="post">
        <div class="top-bar">
          <h3><i class="icon-leaf"></i>申請會員</h3>
        </div>
        <div class="well no-padding" >
          <div class="control-group">
            <label class="control-label" for="inputUsername"><i class="icon-male"></i></label>
            <div class="controls">
              <input type="text" id="RegisterName" name="RegisterName" placeholder="姓名">
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="inputUsername"><i class="icon-envelope"></i></label>
            <div class="controls">
              <input type="text" id="RegisterEmail" name="RegisterEmail" placeholder="email">
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="inputName"><i class="icon-user"></i></label>
            <div class="controls">
              <input type="text" id="RegisterUser" name="RegisterUser" placeholder="帳號">
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="inputUsername"><i class="icon-key"></i></label>
            <div class="controls">
              <input type="password" id="RegisterPass" name="RegisterPass" placeholder="密碼">
            </div>
          </div>
          
          

        <div class="padding" style="text-align:center;">
          <button class="btn btn-primary" id="btnregis" type="button">新增</button>
          <button id="cancle" class="btn" type="button" >取消</button>
          
          </div>
        </div>
      </form>

    </div> 

</body>

	<!-- Javascript
	================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src='<?php echo base_url();?>assets/js/jquery.hotkeys.js'></script>
	<script src='<?php echo base_url();?>assets/js/calendar/fullcalendar.min.js'></script>
	<script src="<?php echo base_url();?>assets/js/jquery-ui-1.10.2.custom.min.js"></script>
	<script src="<?php echo base_url();?>assets/js/jquery.pajinate.js"></script>
	<script src="<?php echo base_url();?>assets/js/jquery.prism.min.js"></script>
	<script src="<?php echo base_url();?>assets/js/jquery.dataTables.min.js"></script>
	<script src="<?php echo base_url();?>assets/js/charts/jquery.flot.time.js"></script>
	<script src="<?php echo base_url();?>assets/js/charts/jquery.flot.pie.js"></script>
	<script src="<?php echo base_url();?>assets/js/charts/jquery.flot.resize.js"></script>
	<script src="<?php echo base_url();?>assets/js/bootstrap/bootstrap.min.js"></script>
	<script src="<?php echo base_url();?>assets/js/bootstrap/bootstrap-wysiwyg.js"></script>
	<script src="<?php echo base_url();?>assets/js/bootstrap/bootstrap-typeahead.js"></script>
  <script src="<?php echo base_url();?>assets/js/jquery.easing.min.js"></script>
  <script src="<?php echo base_url();?>assets/js/jquery.chosen.min.js"></script>
	<script src="<?php echo base_url();?>assets/js/avocado-custom.js"></script>
</html>