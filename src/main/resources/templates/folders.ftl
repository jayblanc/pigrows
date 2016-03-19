<#include "/common/head.ftl">

<div class="starter-template">
	<h2 class="page-header">Images du dipositif <small>${key}</small></h2>
	<#list folders>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>Nom</th>
					<th>Nb Images</th>
					<th>Taille</th>
					<th>Derni&egrave;re Modification</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
		  		<#items as folder>
					<tr>
						<td>${folder?counter}</td>
						<td>${folder.name}</td>
						<td>${folder.nbFiles}</td>
						<td>${formatsize(folder.size)}</td>
						<td>${folder.lastModification}</td>
						<td width="150px">
							<a class="btn btn-default btn-sm" href="${context}/api/html/devices/${key}/${folder.name}" role="button" title="Parcourir les images du dossier"><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></a>
							<a class="btn btn-default btn-sm" href="${context}/api/pictures/${key}/${folder.name}" role="button" title="T&eacute;l&eacute;charger une archive des images du dossier"><span class="glyphicon glyphicon-compressed" aria-hidden="true"></span></a>
							<a class="btn btn-danger btn-sm" onclick="$('#purgeForm').attr('action', '${context}/api/html/devices/${key}/${folder.name}/purge');$('#confirmModal').modal('show')" role="button" title="Purger toutes les images"><span class="glyphicon glyphicon-trash"></span></a>
						</td>
					</tr>
				</#items>
			</tbody>
		</table>
	<#else>
		<p>Il n'y a aucun dossier d'images pour ce dispositif</p>
	</#list>
</div>

<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<form method="post" id="purgeForm">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">Effacer toutes les images du dossier ?</h4>
				</div>
				<div class="modal-body">
					<p>Confirmez vous vouloir <b>EFFACER TOUTES LES IMAGES DU DOSSIER</b> ?<br/>
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