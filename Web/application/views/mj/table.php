
		<div id="edit"  class="row-fluid" style="display:none">
			<div class="span12">
				<div class="top-bar">
					<h3><i class="icon-cog"></i> 用戶編輯</h3>
				</div>
				<div class="well no-padding" id="editpage">
					<form  action="editact" method="post" class="form-horizontal">
							<input type="hidden" id="num" name="num" value="">
						<div class="control-group">
							<label  for="inputInline"><i class="icon-check"></i>用戶名稱</label>
							<div class="controls">
								<input type="text" id="name" name="name" value="">
							</div>
						</div>
						<div class="control-group">
							<label  for="inputInline"><i class="icon-check"></i>帳號信箱</label>
							<div class="controls">
								<input type="text" id="email" name="email" value="">
							</div>
						</div>
						<div class="control-group">
							<label  for="inputInline"><i class="icon-check"></i>帳號名稱</label>
							<div class="controls">
								<input type="text" id="username" name="username" value="">
							</div>
						</div>
						<div class="control-group">
							<label  for="inputInline"><i class="icon-check"></i>帳號密碼</label>
							<div class="controls">
								<input type="text" id="password" name="password" value="">
							</div>
						</div>		
						<div align="center">
							<button type="submit" class="btn btn-primary" > Save</button>
							<button id="cancle" type="button" class="btn" > Cancel</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div id="table" class="row-fluid" style="display:show">
			
			<div  class="span12">

				<div class="top-bar">
					<h3><i class="icon-eye-open"></i>會員表</h3>
				</div>

				<div class="well no-padding" style="display:show">
					
					<table class="data-table">
						<thead>
							<tr>
								<th>user</th>
								<th>email</th>
								<th>username</th>
								<th class="center">password</th>
								<th class="center">button</th>
							</tr>
						</thead>
						<tbody id='testTbody'>
							<?php  foreach($member as $key => $row){ ?>
							<tr class="odd gradeX">
								<td><?php echo $row["name"]; ?></td>
								<td><?php echo $row["email"]; ?></td>
								<td><?php echo $row["username"]; ?></td>
								<td class="center"><?php echo $row["password"]; ?></td>
								<td class="center"><button type="button" id="btnEdit____<?php echo $row["username"];?>" name="btnEdit" class="btn btn-info">編輯</button>
												   <button type="button" id="btnDelete____<?php echo $row["username"];?>" name="btnDelete" class="btn btn-danger">刪除</button></td>
							</tr>
							<?php } ?>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
<script type="text/javascript">
			$(document).ready(function(){
		      refreshTable();
		    });

		    function refreshTable(){
		        $('#rooms').load('room', function(){
		           setTimeout(refreshTable, 10000);
		        });
		    }


		</script>

		<script>
			var member = <?php echo json_encode($member); ?>;
			$(function(){
				 $.ajax({
			        url : thisBaseUrl + "index.php/getmember",
			        dataType : "json",
			        type : "post",
			        cache : false,
			        data : {}
			      }).done(function(result){
			   //        var appendText = "";
						// for(var i in member){
						// 	var thisLoopValue = member[i];
						// 	appendText += '<tr class="odd gradeX">';
						// 	appendText += '<td>'+thisLoopValue.name+'</td>';
						// 	appendText += '<td>'+thisLoopValue.email+'</td>';
						// 	appendText += '<td>'+thisLoopValue.username+'</td>';
						// 	appendText += '<td class="center">'+thisLoopValue.password+'</td>';
						// 	appendText += '<td class="center">';
						// 	appendText += '<button type="button" id="btnEdit__'+thisLoopValue.num+'" name="btnEdit" class="btn btn-info" >編輯</button>';
						// 	appendText += '<button type="button" id="btnDelete__'+thisLoopValue.num+'" name="btnDelete" class="btn btn-danger">刪除</button></td>';
						// 	appendText += '</tr>';
						// }
						// $("#testTbody").html(appendText);

						$("[name='btnEdit']").click(function(){
							var thisId = $(this).attr("id");
							var thisIdArray = thisId.split("____");
							$('#table').hide();
							$('#edit').show();
							$('#username').val(thisIdArray[1]);
							$.ajax({
							        url : "<?php echo base_url();?>index.php/member_web",
							        dataType : "json",
							        type : "post",
							        cache : false,
							        data : {
							        	username : thisIdArray[1]
							        }
							      }).done(function(member){
							      		console.log(member);
							          $("#name").val(member.name);
							          $("#email").val(member.email);
							          $("#username").val(member.username);
							          $("#password").val(member.password);
							      });
							    });


							// location.href = thisBaseUrl + "index.php/member/" + thisIdArray[1];
						});

						$("[name='btnDelete']").click(function(){
							var thisId = $(this).attr("id");
							var thisIdArray = thisId.split("__");
							memberconf(thisIdArray[1]);
						});
						$("#cancle").click(function(){
							$('#table').show();
							$('#edit').hide();
							
						});

			      

				
			});

		</script>

		

