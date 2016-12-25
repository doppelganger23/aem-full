var refreshScript = refreshScript || {};
var myHost = "localhost:4502";
var contentPath = $('#viewSelect').attr('data');

window.onload = function() {
	refreshScript.view('type')
};

refreshScript.view = function(selector) {
  var target = $('#target');
  $.ajax({
    type: 'GET',
    url: 'http://' + myHost + contentPath + '.' + selector + '.html',
    success: function(data) {
      var parser = new DOMParser()
      var doc = parser.parseFromString(data, "text/html");
      var eventsHtml = doc.getElementById('mywrapper').innerHTML;

	  target.replaceWith(eventsHtml);
    }
  });
  if (selector == 'topic') {
  	$('#topicButton').addClass('active');
  	$('#typeButton').removeClass('active');
  };
  if (selector == 'type'){
	$('#typeButton').addClass('active');
 	$('#topicButton').removeClass('active');
  };
};