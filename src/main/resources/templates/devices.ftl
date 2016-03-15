<#include "/common/head.ftl">

<div class="starter-template">
	<h2 class="page-header">Dispositifs</h2>
	<#list devices>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>Clé</th>
					<th>Nom</th>
					<th>Description</th>
					<th>Dernière Activité</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
		  		<#items as device>
					<tr>
						<td>${device?counter}</td>
						<td>${device.key}</td>
						<td>${device.name}</td>
						<td>${device.description}</td>
						<td>${device.lastActivity?number_to_datetime}</td>
						<td width="100px">
							<a class="btn btn-default btn-sm" href="${context}/api/html/devices/${device.key}" role="button"><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></a>
							<a class="btn btn-default btn-sm" href="${context}/api/pictures/${device.key}" role="button"><span class="glyphicon glyphicon-download" aria-hidden="true"></span></a>
						</td>
					</tr>
				</#items>
			</tbody>
		</table>
	<#else>
		<p>Il n'y a aucun dispositif déclaré</p>
	</#list>
	<p>
		<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createDeviceModal">
			Ajouter un dispositif
		</button>
	</p>
</div>

<!-- Modal -->
<div class="modal fade" id="createDeviceModal" tabindex="-1" role="dialog" aria-labelledby="createDeviceLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form class="form-horizontal" method="post" action="${context}/api/html/devices">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="createDeviceLabel">Déclarer un nouveau dispositif</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<label for="inputkey" class="col-sm-2 control-label">Clé</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="inputkey" placeholder="Key" name="key">
						</div>
					</div>
					<div class="form-group">
						<label for="inputname" class="col-sm-2 control-label">Nom</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="inputname" placeholder="Name" name="name">
						</div>
					</div>
					<div class="form-group">
						<label for="inputdescr" class="col-sm-2 control-label">Description</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="inputdescr" placeholder="Description" name="description">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
					<button type="submit" class="btn btn-primary">Créer</button>
				</div>
			</form>  
		</div>
	</div>
</div>

<#include "/common/foot.ftl">  