<#include "/common/head.ftl">
<#include "/macros/pages.ftl">

<div class="starter-template">
	<h2 class="page-header">&Eacute;v&eacute;nements</h2>
	<#list events>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>Date</th>
					<th>Dispositif</th>
					<th>Type</th>
					<th>Message</th>
				</tr>
			</thead>
			<tbody>
				<#items as event>
					<tr>
						<td>${event?counter + ((page-1)*size)}</td>
						<td>${event.date}</td>
						<td>${event.key}</td>
						<td>${event.type}</td>
						<td>${event.message}</td>
					</tr>
				</#items>
			</tbody>
		</table>
	<#else>
		<p>Il n'y a aucun &eacute;v&egrave;nement disponible</p>
	</#list>
	<nav>
		<#if nbpages gt 1>
			<#assign base = "${context}/api/html/events" >
			<@pages size=nbpages p=page base=base/>
		</#if>
	</nav>
	<#if events?size gt 0>
		<button type="button" data-toggle="modal" data-target="#confirmModal" class="btn btn-primary">Purger tous les &eacute;v&egrave;nements</button>
	</#if>
</div>

<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<form action="${context}/api/html/events/purge" method="post">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">Effacer tous les &eacute;v&egrave;nements ?</h4>
				</div>
				<div class="modal-body">
					<p>Confirmez vous vouloir <b>EFFACER TOUS LES EVENEMENTS</b> ?<br/>
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