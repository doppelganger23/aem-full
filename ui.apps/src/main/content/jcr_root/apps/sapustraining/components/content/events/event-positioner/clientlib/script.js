window.onload = function() {
   viewScript.viewTypes();
};

var viewScript = viewScript || {};

viewScript.viewTypes = function() {
    $('#topicLayout').hide();
	$('#typeLayout').show();
    $('#typeButton').addClass('active');
    $('#topicButton').removeClass('active');
};

viewScript.viewTopics = function() {
    $('#typeLayout').hide();
    $('#topicLayout').show();
    $('#topicButton').addClass('active');
    $('#typeButton').removeClass('active');
};