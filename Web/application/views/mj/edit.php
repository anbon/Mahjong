
		<!-- Live Stats -->
		<div class="row-fluid">
			<div class="span12">

				<!-- Forms: Top Bar -->
				<div class="top-bar">
					<h3><i class="icon-cog"></i> 公告編輯</h3>
				</div>
				<!-- / Forms: Top Bar -->

				<!-- Forms: Content -->
				<div class="well no-padding" id="editpage">

					<!-- Forms: Form -->
					<!-- <form  action="editact" method="post" class="form-horizontal">
						<div class="control-group">
							<label  for="inputInline"><i class="icon-check"></i>帳號名稱</label>
							<div class="controls">
								<input type="text" name="username" value="">
							</div>
						</div>
						<div class="control-group">
							<label  for="inputInline"><i class="icon-check"></i>帳號密碼</label>
							<div class="controls">
								<input type="text" name="password" value="">
							</div>
						</div>		
						<div align="center">
							<button type="submit" class="btn btn-primary" > Save</button>
							<button type="button" class="btn" onclick="document.location.href='<?php echo base_url(); ?>index.php/announcelist'"> Cancel</button>
						</div>
					</form>  -->
				</div>
			<script>
			
			var member = <?php echo json_encode($member); ?>;
			$(function(){

				
			          var appendText = "";
						for(var i in member){
							var thisLoopValue = member[i];
							appendText += '<form  action="editact" method="post" class="form-horizontal">';
							appendText += '<input type="hidden" name="num" value="'+thisLoopValue.num+'">'
							appendText += '<div class="control-group">';
							appendText += '<label  for="inputInline"><i class="icon-check"></i>帳號名稱</label>';
							appendText += '<div class="controls">';
							appendText += '<input type="text" name="username" value="'+thisLoopValue.username+'">';
							appendText += '</div>';
							appendText += '</div>';
							appendText += '<div class="control-group">';
							appendText += '<label  for="inputInline"><i class="icon-check"></i>帳號密碼</label>';
							appendText += '<div class="controls">';
							appendText += '<input type="text" name="password" value="'+thisLoopValue.password+'">';
							appendText += '</div>';
							appendText += '</div>';
							appendText += '<div align="center">';
							appendText += '<button type="submit" class="btn btn-primary" > Save</button>';
							appendText += '<button type="button" class="btn" id="btn__cancle" name=btn__cancle > Cancel</button>';
							appendText += '</div>';
							appendText += '</form> ';
						}
						$("#editpage").html(appendText);

						$("[name='btn__cancle']").click(function(){
							location.href = thisBaseUrl + "index.php/table/";
						});
			     

				
			});

		</script>

				<!-- / Forms: Content -->

			</div>
		</div>


		

	