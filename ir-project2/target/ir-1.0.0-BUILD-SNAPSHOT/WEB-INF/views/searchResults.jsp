<%@ include file="layout/header.jsp"%>
<%@ include file="modules/search.jsp"%>

<div class="span3 facets">
	<c:forEach var="facet" items="${facets}">
		<div id="${facet.title}" class="facet block">
			<span class="title">${facet.title}</span>
			${facet.allOptions}
			<a class="more">[more]</a>
		</div>
	</c:forEach>
</div>
<div class="span8 results">
	<div id="active-filters" class="facet block">
		<h5>Active filters</h5>
		<p>Your search query was: ${query} (${count} results)</p>
		<ul class="inline">
			<c:forEach var="filter" items="${filters}">
				<li class="item">${filter}</li>
			</c:forEach>
		</ul>
	</div>
	<div id="search-results" class="block">
		<h5>Search results</h5>
		<table class="table table-striped table-hover">
			<c:forEach var="result" items="${results}">
				<tr>
					<td><a
						href="${pageContext.request.contextPath}/story/${result.idnummer}"
						class="result">
							<div class="field-titel">
								<c:out value="${result.titel}" />
							</div>
							<div class="field-tekst">
								<c:out value="${result.shortTekst}" />
							</div>
					</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>
<div class="clearfix"></div>



<%@ include file="layout/footer.jsp"%>