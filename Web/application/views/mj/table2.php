<div id="rooms" class="row-fluid" style="display:show">
			
			<div  class="span12">

				<div class="top-bar">
					<h3><i class="icon-eye-open"></i>房間表</h3>
				</div>

				<div class="well no-padding" style="display:show">
					
					<table class="data-table">
						<thead>
							<tr>
								<th>num</th>
								<th>name</th>
								<th>count</th>
							</tr>
						</thead>
						<tbody id='testTbody'>
							<?php  foreach($room as $key => $row){ ?>
							<tr class="odd gradeX">
								<td><?php echo $row["num"]; ?></td>
								<td><?php echo $row["name"]; ?></td>
								<td><?php echo $row["count"]; ?></td>
							</tr>
							<?php } ?>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		