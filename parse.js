$(window).load(function() {
	

	$.get('files/futureproject.json', function(data) {
		console.log(data);

		var jsonObject = jQuery.parseJSON(data);

		console.log(jsonObject);

		var newdiv = document.createElement('li');
		newdiv.innerHTML =  "<p>The Result is: "  +  jsonObject.effort +" </p>" +
							"<p> "  +  jsonObject.aexp +" </p>" +
							"<p> "  +  jsonObject.cplx +" </p>";
		document.getElementById('results').appendChild(newdiv);   


		var rawdiv = document.createElement('p');
		rawdiv.innerHTML  = jsonObject.acap; 
		document.getElementById('raw-input').appendChild(rawdiv);
	});
});