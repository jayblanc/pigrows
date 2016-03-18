<#include "/common/head.ftl">

<div class="starter-template">
	<h2 class="page-header">Configuration Globale</h2>
	<form method="post" action="${context}/api/html/config">
		<div class="form-group">
			<label for="emails">Addresses Email de Notification (s&eacute;par&eacute;s par des virgules)</label>
			<input type="email" class="form-control" id="emails" name="notification.recipients" value="${.data_model["notification.recipients"]}">
		</div>
		<div class="form-group">
			<label for="smtpserver">Server SMTP</label>
			<input type="text" class="form-control" id="smtpserver" name="smtp.host value="${.data_model["smtp.host"]}">
		</div>
		<div class="form-group">
			<label for="smtpport">Port SMTP</label>
			<input type="text" class="form-control" id="smtpport" name="smtp.port" value="${.data_model["smtp.port"]}">
		</div>
		<div class="form-group">
			<label for="smtpuser">Utilisateur SMTP (vide si pas d'authentification)</label>
			<input type="text" class="form-control" id="smtpuser" name="smtp.username" value="${.data_model["smtp.username"]}">
		</div>
		<div class="form-group">
			<label for="smtppassword">Mot de Passe SMTP</label>
			<input type="password" class="form-control" id="smtppassword" name="smtp.password" value="${.data_model["smtp.password"]}">
		</div>
		<button type="submit" class="btn btn-default">Mettre &agrave; jour</button>
	</form>
</div>

<#include "/common/foot.ftl">