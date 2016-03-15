<#include "/common/head.ftl">
<#include "/macros/pages.ftl">

<div class="starter-template">
	<h2 class="page-header">Images du dispositif <small><a href="${context}/api/html/devices/${key}">${key}</a></small> dossier <small>${folder}</small></h2>
	<#list pictures>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>Nom</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<#items as picture>
					<tr>
						<td>${picture?counter + ((page-1)*size)}</td>
						<td>${picture.name}</td>
						<td width="100px">
							<a class="btn btn-default btn-sm" href="${context}/api/pictures/${key}/${folder}/${picture.name}" role="button"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
							<a class="btn btn-default btn-sm" href="${context}/api/pictures/${key}/${folder}/${picture.name}?fd=true" role="button"><span class="glyphicon glyphicon-download" aria-hidden="true"></span></a>
						</td>
					</tr>
				</#items>
			</tbody>
		</table>
	<#else>
		<p>Il n'y a aucun dossier d'images pour ce dispositif</p>
	</#list>
	<nav>
		<#if nbpages gt 1>
			<#assign base = "${context}/api/html/devices/${key}/${folder}" >
			<@pages size=nbpages p=page base=base/>
		</#if>
	</nav>
</div>

<#include "/common/foot.ftl">  