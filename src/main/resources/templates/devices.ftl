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
						<td width="200px">
							<a class="btn btn-default btn-sm" href="${context}/api/html/devices/${device.key}" role="button" title="Parcourir les images"><span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span></a>
							<a class="btn btn-default btn-sm" href="${context}/api/pictures/${device.key}" role="button" title="T&eacute;l&eacute;charger une archive de toutes les images"><span class="glyphicon glyphicon-compressed" aria-hidden="true"></span></a>
							<a class="btn btn-default btn-sm" onclick="$('#configModal_${device.key}').modal('show')" role="button" title="Configurer le dispositif"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span></a>
							<a class="btn btn-danger btn-sm" onclick="$('#purgeForm').attr('action', '${context}/api/html/devices/${device.key}/purge');$('#confirmModal').modal('show')" role="button" title="Purger toutes les images"><span class="glyphicon glyphicon-trash"></span></a>
						</td>
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
<#list devices as device>
<div class="modal fade" id="configModal_${device.key}" tabindex="-1" role="dialog" aria-labelledby="configDeviceLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form class="form-horizontal" method="post" id="configForm_${device.key}" action="${context}/api/html/devices/${device.key}/config">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="configDeviceLabel">Configuration du dispositif ${device.key}</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-sm-5"></div>
						<div class="col-sm-3 text-center"><b>Maitre</b></div>
						<div class="col-sm-3 text-center"><b>Esclave</b></div>
					</div>
					<div class="form-group">
						<label for="quality" class="col-sm-5 control-label">Qualit&eacute; <small>(0 &agrave; 100)</small></label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="quality" name="master_quality" value="${device.masterConfig.quality}">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="quality" name="slave_quality" value="${device.slaveConfig.quality}">
						</div>
					</div>
					<div class="form-group">
						<label for="sharpness" class="col-sm-5 control-label">Finesse <small>(-100 &agrave; 100)</small></label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="sharpness" name="master_sharpness" value="${device.masterConfig.sharpness}">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="sharpness" name="slave_sharpness" value="${device.slaveConfig.sharpness}">
						</div>
					</div>
					<div class="form-group">
						<label for="contrast" class="col-sm-5 control-label">Contraste <small>(-100 &agrave; 100)</small></label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="contrast" name="master_contrast" value="${device.masterConfig.contrast}">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="contrast" name="slave_contrast" value="${device.slaveConfig.contrast}">
						</div>
					</div>
					<div class="form-group">
						<label for="brightness" class="col-sm-5 control-label">Brillance <small>(0 &agrave; 100)</small></label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="brightness" name="master_brightness" value="${device.masterConfig.brightness}">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="brightness" name="slave_brightness" value="${device.slaveConfig.brightness}">
						</div>
					</div>
					<div class="form-group">
						<label for="saturation" class="col-sm-5 control-label">Saturation <small>(-100 &agrave; 100)</small></label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="saturation" name="master_saturation" value="${device.masterConfig.saturation}">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="saturation" name="slave_saturation" value="${device.slaveConfig.saturation}">
						</div>
					</div>
					<div class="form-group">
						<label for="ev" class="col-sm-5 control-label">EV <small>(-10 &agrave; 10)</small></label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="ev" name="master_ev" value="${device.masterConfig.ev}">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="ev" name="slave_ev" value="${device.slaveConfig.ev}">
						</div>
					</div>
					<div class="form-group">
						<label for="iso" class="col-sm-5 control-label">ISO <small>(100 &agrave; 800)</small></label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="iso" name="master_iso" value="${device.masterConfig.iso}">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="iso" name="slave_iso" value="${device.slaveConfig.iso}">
						</div>
					</div>
					<div class="form-group">
						<label for="exposure" class="col-sm-5 control-label">Exposition</label>
						<div class="col-sm-3">
							<select class="form-control" id="exposure" name="master_exposure">
								<option ${(device.masterConfig.exposure == "AUTO")?then('selected','')}>AUTO</option>
								<option ${(device.masterConfig.exposure == "NIGHT")?then('selected','')}>NIGHT</option>
								<option ${(device.masterConfig.exposure == "BACKLIGHT")?then('selected','')}>BACKLIGHT</option>
								<option ${(device.masterConfig.exposure == "SPORTS")?then('selected','')}>SPORTS</option>
								<option ${(device.masterConfig.exposure == "SNOW")?then('selected','')}>SNOW</option>
								<option ${(device.masterConfig.exposure == "BEACH")?then('selected','')}>BEACH</option>
								<option ${(device.masterConfig.exposure == "VERYLONG")?then('selected','')}>VERYLONG</option>
								<option ${(device.masterConfig.exposure == "FIXEDFPS")?then('selected','')}>FIXEDFPS</option>
								<option ${(device.masterConfig.exposure == "ANTISHAKE")?then('selected','')}>ANTISHAKE</option>
								<option ${(device.masterConfig.exposure == "FIREWORKS")?then('selected','')}>FIREWORKS</option>
							</select>
						</div>
						<div class="col-sm-3">
							<select class="form-control" id="exposure" name="slave_exposure">
								<option ${(device.slaveConfig.exposure == "AUTO")?then('selected','')}>AUTO</option>
								<option ${(device.slaveConfig.exposure == "NIGHT")?then('selected','')}>NIGHT</option>
								<option ${(device.slaveConfig.exposure == "BACKLIGHT")?then('selected','')}>BACKLIGHT</option>
								<option ${(device.slaveConfig.exposure == "SPORTS")?then('selected','')}>SPORTS</option>
								<option ${(device.slaveConfig.exposure == "SNOW")?then('selected','')}>SNOW</option>
								<option ${(device.slaveConfig.exposure == "BEACH")?then('selected','')}>BEACH</option>
								<option ${(device.slaveConfig.exposure == "VERYLONG")?then('selected','')}>VERYLONG</option>
								<option ${(device.slaveConfig.exposure == "FIXEDFPS")?then('selected','')}>FIXEDFPS</option>
								<option ${(device.slaveConfig.exposure == "ANTISHAKE")?then('selected','')}>ANTISHAKE</option>
								<option ${(device.slaveConfig.exposure == "FIREWORKS")?then('selected','')}>FIREWORKS</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="wb" class="col-sm-5 control-label">Balance des blancs</label>
						<div class="col-sm-3">
							<select class="form-control" id="wb" name="master_wb">
								<option ${(device.masterConfig.wb == "OFF")?then('selected','')}>OFF</option>
								<option ${(device.masterConfig.wb == "AUTO")?then('selected','')}>AUTO</option>
								<option ${(device.masterConfig.wb == "SUN")?then('selected','')}>SUN</option>
								<option ${(device.masterConfig.wb == "CLOUD")?then('selected','')}>CLOUD</option>
								<option ${(device.masterConfig.wb == "SHADE")?then('selected','')}>SHADE</option>
								<option ${(device.masterConfig.wb == "TUNGSTEN")?then('selected','')}>TUNGSTEN</option>
								<option ${(device.masterConfig.wb == "FLUORESCENT")?then('selected','')}>FLUORESCENT</option>
								<option ${(device.masterConfig.wb == "INCANDESCENT")?then('selected','')}>INCANDESCENT</option>
								<option ${(device.masterConfig.wb == "FLASH")?then('selected','')}>FLASH</option>
								<option ${(device.masterConfig.wb == "JORIZON")?then('selected','')}>JORIZON</option>
							</select>
						</div>
						<div class="col-sm-3">
							<select class="form-control" id="wb" name="slave_wb">
								<option ${(device.slaveConfig.wb == "OFF")?then('selected','')}>OFF</option>
								<option ${(device.slaveConfig.wb == "AUTO")?then('selected','')}>AUTO</option>
								<option ${(device.slaveConfig.wb == "SUN")?then('selected','')}>SUN</option>
								<option ${(device.slaveConfig.wb == "CLOUD")?then('selected','')}>CLOUD</option>
								<option ${(device.slaveConfig.wb == "SHADE")?then('selected','')}>SHADE</option>
								<option ${(device.slaveConfig.wb == "TUNGSTEN")?then('selected','')}>TUNGSTEN</option>
								<option ${(device.slaveConfig.wb == "FLUORESCENT")?then('selected','')}>FLUORESCENT</option>
								<option ${(device.slaveConfig.wb == "INCANDESCENT")?then('selected','')}>INCANDESCENT</option>
								<option ${(device.slaveConfig.wb == "FLASH")?then('selected','')}>FLASH</option>
								<option ${(device.slaveConfig.wb == "JORIZON")?then('selected','')}>JORIZON</option>
							</select>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
					<button type="submit" class="btn btn-primary">Mettre &agrave; jour</button>
				</div>
			</form>  
		</div>
	</div>
</div>
</#list>
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