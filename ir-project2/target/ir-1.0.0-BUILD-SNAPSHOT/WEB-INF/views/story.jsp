<%@ include file="layout/header.jsp"%>

<%@ include file="modules/search.jsp" %>
<div id="story" class="block span11">
<h1>${story.titel}</h1>
<div id="text">
	<table class="table table-striped table-hover" id="${story.idnummer}">
		<tr>
			<th class="field-title">Text</th>
			<td class="field-tekst">${story.tekst}</td>
		</tr>
		<tr>
			<th class="field-title">Region</th>
			<td class="field-regio"><c:out value="${story.regio}"/></td>
		</tr>
		<tr>
			<th class="field-title">Story type</th>
			<td class="field-regio"><c:out value="${story.type}"/></td>
		</tr>
		<tr>
			<th class="field-title">Language</th>
			<td class="field-regio"><c:out value="${story.taal}"/></td>
		</tr>
		<tr>
			<th class="field-title">Keywords</th>
			<td class="field-regio"><c:out value="${story.trefwoorden}"/></td>
		</tr>
		<tr>
			<th class="field-title">ATU description</th>
			<td id="atu" class="field-atu"><c:out value="${story.ATUOmschrijving}"/></td>
		</tr>
		<tr>
			<th class="field-title">ID</th>
			<td class="field-id"><c:out value="${story.idnummer}"/></td>
		</tr>
		<tr>
			<th class="field-title">Subgenre</th>
			<td class="field-subgenre"><c:out value="${story.subgenre}"/></td>
		</tr>
		<tr>
			<th class="field-title">Date</th>
			<td class="field-subgenre"><c:out value="${story.datering}"/></td>
		</tr>
	</table>
</div>
<div id="images"></div>
</div>

<%@ include file="layout/footer.jsp"%>