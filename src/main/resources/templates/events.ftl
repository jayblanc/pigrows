<#include "/common/head.ftl">

<div class="starter-template">
	<h2 class="page-header">Événements</h2>
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
		<p>Il n'y a aucun évènement disponible</p>
	</#list>
	<nav>
		<#if nbpages gt 1>
			<#assign base = "${context}/api/html/events" >
			<@pages size=nbpages p=page base=base/>
		</#if>
	</nav>
</div>

<#include "/common/foot.ftl">  