<#include "/common/head.ftl">

<div class="starter-template">
	<h2 class="page-header">Dispositifs</h2>
	<#list devices>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>Cl&eacute;</th>
					<th>Nom</th>
					<th>Description</th>
					<th>Derni&egrave;re Activit&eacute;</th>
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
						<form action="${context}/api/html/devices/${device.key}/purge" method="post">
							<td width="200px">
								<a class="btn btn-default btn-sm" href="${context}/api/html/devices/${device.key}" role="button" title="Parcourir les images"><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></a>
								<a class="btn btn-default btn-sm" href="${context}/api/pictures/${device.key}" role="button" title="T&eacute;l&eacute;charger une archive de toutes les images"><span class="glyphicon glyphicon-compressed" aria-hidden="true"></span></a>
								<a class="btn btn-default btn-sm" role="button" title="Configurer le dispositif"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span></a>
								<a class="btn btn-danger btn-sm" onclick="$('#purgeForm').attr('action', '${context}/api/html/devices/${device.key}/purge');$('#confirmModal').modal('show')" role="button" title="Purger toutes les images"><span class="glyphicon glyphicon-trash"></span></a>
							</td>
						</form>
					</tr>
				</#items>
			</tbody>
		</table>
	<#else>
		<p>Il n'y a aucun dispositif d&eacute;clar&eacute;</p>
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
					<h4 class="modal-title" id="createDeviceLabel">D&eacute;clarer un nouveau dispositif</h4>
				</div>
				<div class="modal-body">
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
					<button type="submit" class="btn btn-primary">Cr&eacute;er</button>
				</div>
			</form>  
		</div>
	</div>
</div>
<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<form method="post" id="purgeForm">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">Effacer toutes les images ?</h4>
				</div>
				<div class="modal-body">
					<p>Confirmez vous vouloir <b>EFFACER TOUTES LES IMAGES DU DISPOSITIF</b> ?<br/>
					<small>Cette action ne peut pas &ecirc;tre annul&eacute;e.</small></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
					<button type="submit" class="btn btn-danger">Confirmer</button>
				</div>
			</form>
		</div>
	</div>
</div>

<#include "/common/foot.ftl">  