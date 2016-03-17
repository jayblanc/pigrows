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
					<th>Derni&eagrave;re Modification</th>
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
						<td width="100px">
							<a class="btn btn-default btn-sm" href="${context}/api/html/devices/${key}/${folder.name}" role="button"><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></a>
							<a class="btn btn-default btn-sm" href="${context}/api/pictures/${key}/${folder.name}" role="button"><span class="glyphicon glyphicon-download" aria-hidden="true"></span></a>
						</td>
					</tr>
				</#items>
			</tbody>
		</table>
	<#else>
		<p>Il n'y a aucun dossier d'images pour ce dispositif</p>
	</#list>
</div>

<#include "/common/foot.ftl">  