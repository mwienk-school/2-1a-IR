<form action="${pageContext.request.contextPath}/search" method="POST">
	<div class="control-group">
		<div class="controls">
			<div class="input-prepend">
				<input class="input-xxlarge focusedinput" id="queryfield"
					name="query" type="text" placeholder="Type your search query.." />
				<input type="hidden" name="extreem" value="${param.extreme}" />
				<button class="btn btn-inverse querybutton" type="submit">
					<i class="icon-search icon-white"></i> <i>Search</i>
				</button>
			</div>
		</div>
	</div>
</form>