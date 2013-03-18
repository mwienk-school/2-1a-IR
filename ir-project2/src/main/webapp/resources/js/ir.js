function getURLParameter(name) {
    return decodeURI(
        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
    );
}

function showNext(list) {
	var total   = list.children("li").length;
	var visible = list.children("li:visible").length;
	list
	  .find('li').hide().end()
	  .find('li:lt(' + (visible + 5) + ')').show();
	if(visible + 5 >= total) {
		list.siblings('a.more').hide();
	}
}

$(document).ready(function(){
	$('div#extreem').hide();
	$('div#active-filter-extreem').hide();
	
	// hide options
	$('ul.entries').find('li').hide().end();
	
	// show initial set
	$('ul.entries').each(function() {
		showNext($(this));
	});

	// clicking on the 'more' link:
	$('a.more').click(function(e) {
	  e.preventDefault();
	  showNext($(this).siblings('ul.entries'));
	});

	
	if(getURLParameter("extreme") == 'on') {
		$('button#toggle-extreme').removeClass("btn-danger").addClass("btn-success");
		$('div#extreem').show();
		$('div#active-filter-extreem').show();
	}

	$("button#toggle-extreme").click(function() {
		if(getURLParameter("extreme") == 'on') {
			window.location = jQuery.param.querystring(window.location.href, 'extreme=off');
		} else {
			window.location = jQuery.param.querystring(window.location.href, 'extreme=on');
		}
	});
	
	$(".facet li.item a").click(function() {
		var facet   = $(this).attr('data-filter');
		var option  = $(this).attr('id');
		var separator = ["$", ","]; // Facet, Option
		var index   = window.location.pathname.lastIndexOf("/");
		var base    = window.location.pathname.substring(0,index);
		var extreme = getURLParameter("extreme");
		var filters = window.location.pathname.substring(index + 1);
		var filterArray = new Array();
		if(filters != "") {
			filterArray = filters.split(separator[0]);
		}
	
		var newFacet = true; // Keep track of the current facet's state
		for (var i = 0; i < filterArray.length; i++) {
		    if(filterArray[i].indexOf(facet) == 0) {
		    	// Facet is in use, do actions
		    	newFacet = false;
		    	option = option.replace(/ /g,"%20");
		    	
		    	var options = filterArray[i].substring(filterArray[i].indexOf(":") + 1).split(separator[1]);
		    	var newOption = true; // Keep track of the current option's state
		    	for(var n = 0; n < options.length; n++) {
		    		if(options[n] == option) {
			    		// Option is in use, disable it
		    			options.splice(n,1);
		    			newOption = false;
		    		}
		    	}
		    	if(newOption) {
		    		// Add new option to already used facet filter
		    		options.push(option);
		    	}
		    	options = options.join(separator[1]);
		    	if(options != "") {
		    		filterArray[i] = filterArray[i].substring(0,filterArray[i].indexOf(":") + 1) + options;
		    	} else {
		    		filterArray.splice(i, 1);
		    	}
		    }
		}
		if(newFacet) {
			// Facet isn't in use, append selected option (and facet) to the filters array
			filterArray.push(facet + ":" + option);
		}
		if((extreme) != 'on') {
			extreme = 'off';
		}
		window.location = base + '/' + filterArray.join(separator[0]) + "?extreme=" + extreme;
	});
	
	if($('#story').length > 0) {
		var title = $('#atu').text();
		getWikipediaImages("en", title);
		title = $('#story h1').text();
		getWikipediaImages("nl", title);
	}
});

function getWikipediaImages(lang, title) {
	var titles = new Array();
	if(lang == "nl") {
		$.getJSON("http://nl.wikipedia.org/w/api.php?action=opensearch&search=" + title + "&format=json&callback=?", function(data){
			titles = titles.concat(data[1]);
			console.log(titles);
			getImages(lang, titles);
		});
	} else {
		titles[0] = title;
		getImages(lang, titles);
	}
}

function getImages(lang, titles) {
	$.each(titles, function(index, value) {
		console.log(value);
		$.getJSON(
				"http://" + lang + ".wikipedia.org/w/api.php?action=query&format=json&callback=?&redirects",
				{titles : value, prop : "images"}, function(data){
				$.each(data.query.pages, function(item) {
					console.log(this);
					$.each(this.images, function(item) {
						console.log(this.title);
						var filename = this.title.replace(/ /g, '_');
						filename = filename.replace("File:", "");
						filename = filename.replace("Bestand:", "");
						if(filename.endsWith("svg")) { return; }
						var digest = hex_md5(filename);
						var folder = digest[0] + '/' + digest[0] + digest[1] + '/' + encodeURI(filename);
						var url = 'http://upload.wikimedia.org/wikipedia/commons/' + folder;
						$("<img/>").attr("src", url).appendTo("#images");
					});
				});
			});
	});
}