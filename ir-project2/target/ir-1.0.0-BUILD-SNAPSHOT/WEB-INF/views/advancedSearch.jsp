<%@ include file="layout/header.jsp"%>
<div class="block">
	<h1>Advanced search</h1>
	<p>Please type your search query in the following fields, disabled fields aren't indexed and therefore cannot be used.</p>
</div>
<form action="${pageContext.request.contextPath}/advanced?extreme=${param.extreme}" method="POST">
	<div class="control-group block">
		<div class="controls">
			<div class="input-prepend">
				<ul>
					<li>
						<label for="idnummer">ID Number</label> 
						<input class="input-large focusedinput" id="idnummer" name="idnummer" type="text" placeholder="idnummer" />
					</li>
					<li>
						<label for="type">Story type</label>
						<input class="input-large focusedinput" id="type" name="type" type="text" placeholder="verhaaltype" />
					</li>
					<li>
						<label for="query">Description / Title</label>
						<input class="input-large focusedinput" id="query" name="query" type="text" placeholder="omschrijving" />
					</li>
					<li>
						<label for="notulist">Writer</label>
						<input class="input-large focusedinput" id="notulist" name="notulist" type="text" placeholder="notulist" disabled />
					</li>
					<li>
						<label for="taal">Language</label>
						<input class="input-large focusedinput" id="taal" name="taal" type="text" placeholder="taal" />
					</li>
					<li>
						<label for="schriftbron">Source</label>
						<input class="input-large focusedinput" id="schriftbron" name="schriftbron" type="text" placeholder="schriftbron" disabled />
					</li>
					<li>
						<label for="regio">Region</label>
						<input class="input-large focusedinput" id="regio"	name="regio" type="text" placeholder="plaats van vertellen" />
					</li>
					<li>
						<label for="kloekenummer">Kloekenumber</label> 
						<input class="input-large focusedinput" id="kloekenummer" name="kloekenummer" type="text" placeholder="kloekenummer" disabled />
					</li>
					<li>
						<label for="verteller">Storyteller</label> 
						<input class="input-large focusedinput" id="verteller" name="verteller"	type="text" placeholder="verteller" disabled />
					</li>
					<li>
						<label for="datering">Date</label> 
						<input class="input-large focusedinput" id="datering" name="datering" type="text" placeholder="datering" />
					</li>
					<li>
						<label for="literair">Literary</label>
						<input class="input-large focusedinput" id="literair" name="literair" type="text" placeholder="literair" />
					</li>
					<li>
						<label for="subgenre">Subgenre</label>
						<input class="input-large focusedinput" id="subgenre" name="subgenre" type="text" placeholder="subgenre" />
					</li>
					<li>
						<label for="motieven">Motives</label>
						<input class="input-large focusedinput" id="motieven" name="motieven" type="text" placeholder="motieven" disabled />
					</li>
					<li>
						<label for="samenvatting">Summary</label> 
						<input class="input-large focusedinput" id="samenvatting" name="samenvatting" type="text" placeholder="samenvatting" disabled/>
					</li>
					<li>
						<label for="trefwoorden">Keywords</label> 
						<input class="input-large focusedinput" id="trefwoorden" name="trefwoorden" type="text" placeholder="trefwoorden" />
					</li>
					<li>
						<label for="namen">Names</label>
						<input class="input-large focusedinput" id="namen" name="namen"	type="text" placeholder="namen" disabled/>
					</li>
					<li>
						<label for="opmerkingen">Notes</label> 
						<input class="input-large focusedinput" id="opmerkingen" name="opmerkingen" type="text" placeholder="opmerkingen" disabled />
					</li>
					<li>
						<label for="corpus">Corpus</label> 
						<input class="input-large focusedinput" id="corpus" name="corpus" type="text" placeholder="corpus" disabled />
					</li>
					<li>
						<label for="aardbron">Source type</label> 
						<input class="input-large focusedinput" id="aardbron" name="aardbron" type="text" placeholder="aardbron" disabled />
					</li>
					<li class="button"><input type="hidden" name="extreme"
						value="${param.extreme}" />
						<button class="btn btn-inverse querybutton" type="submit">
							<i class="icon-search icon-white"></i> <i>Search</i>
						</button></li>
				</ul>
			</div>
		</div>
	</div>
</form>

<%@ include file="layout/footer.jsp"%>