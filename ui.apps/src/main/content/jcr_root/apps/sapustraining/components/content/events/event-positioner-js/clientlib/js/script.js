 window.onload = function(){
    jScript.view('type');
 }

 var jScript = jScript || {};

 jScript.view = function(param){
     var XHR = ("onload" in new XMLHttpRequest()) ? XMLHttpRequest : XDomainRequest;
     var xhr = new XHR();

     var servletUrl = filterInnerHtml(document.getElementById("servlet-url-holder").innerHTML);
     if(servletUrl == ""){
        servletUrl = "http://localhost:4502/bin/getEvents";
     }

     xhr.open("GET", servletUrl);
     xhr.onload = function (){
        var obj = JSON.parse(xhr.responseText);
        param == 'type' ? jScript.typeView(obj) : jScript.topicView(obj);
     }
     xhr.send();
 }

 jScript.typeView = function(obj) {
    var events = obj.events;
    var types = obj.types;
    var i = 0;

    document.getElementById('firstcol').innerHTML = "";
    document.getElementById('secondcol').innerHTML = "";
    document.getElementById('thirdcol').innerHTML = "";
    document.getElementById('other').innerHTML = "";

    while(events[i]){
        if (events[i].type == types[0]){
            jScript.fill('#firstcol', events[i], 'type');
        } else if (events[i].type == types[1]){
            jScript.fill('#secondcol', events[i], 'type');
        } else if (events[i].type == types[2]){
            jScript.fill('#thirdcol', events[i], 'type');
        } else {
            jScript.fill('#other', events[i], 'type');
        }
        i++;
    }

    jScript.writeHeader("firsth3", capitalizeFirst(types[0]), ': <i class="icon-' + types[0] + '"></i>');
    jScript.writeHeader("secondh3", capitalizeFirst(types[1]), ': <i class="icon-' + types[1] + '"></i>');
    jScript.writeHeader("thirdh3", capitalizeFirst(types[2]), ': <i class="icon-' + types[2] + '"></i>');
    jScript.writeHeader("otherh3", 'Other', ':');

    $('#typeButton').addClass('active');
    $('#topicButton').removeClass('active');
 };


 jScript.topicView = function(obj) {
    var events = obj.events;
    var topics = obj.topics;
    var i = 0;

    document.getElementById('firstcol').innerHTML = "";
    document.getElementById('secondcol').innerHTML = "";
    document.getElementById('thirdcol').innerHTML = "";
    document.getElementById('other').innerHTML = "";

    while(events[i]){
        if (events[i].topic == topics[0]){
            jScript.fill('#firstcol', events[i], 'topic');
        } else if (events[i].topic == topics[1]){
            jScript.fill('#secondcol', events[i], 'topic');
        }else if (events[i].topic == topics[2]){
            jScript.fill('#thirdcol', events[i], 'topic');
        } else {
            jScript.fill('#other', events[i], 'topic');
        }
        i++;
    }

    jScript.writeHeader("firsth3", capitalizeFirst(topics[0]), ':');
    jScript.writeHeader("secondh3", capitalizeFirst(topics[1]), ':');
    jScript.writeHeader("thirdh3", capitalizeFirst(topics[2]), ':');
    jScript.writeHeader("otherh3", 'Other', ':');

    $('#topicButton').addClass('active');
    $('#typeButton').removeClass('active');
 };

 jScript.writeHeader = function(colId, colName, iconHtml){
    if(colName != undefined){
        document.getElementById(colId).innerHTML = colName + iconHtml;
    } else {
        document.getElementById(colId).innerHTML = "";
    }
 }

 jScript.fill = function(myId, myEvent, templateType){
     var liDate = myEvent.date;
     var liTitleText = myEvent.titleText;
     var liTitleLink = myEvent.titleLink;
     var liDescription = myEvent.description;
     var iconType = myEvent.type;
     jScript.fillTemplate(myId, templateType, liDate, liTitleText, liDescription, iconType);
 }


 jScript.fillTemplate = function(fillId, templateType, liDate, liTitleText, liDescription, iconType){
     var template = templateType == 'type' ? $("#type-template") : $("#topic-template");
     $(fillId).loadTemplate(
        template,
        {
           icon: "icon-" + iconType,
           date: liDate,
           titleText: liTitleText,
           description: liDescription
        },
        { append: true }
     );
 }

 capitalizeFirst = function(string) {
    if(string != undefined){
        return string.charAt(0).toUpperCase() + string.slice(1);
    }
 }

 filterInnerHtml = function(someText){
    someText = someText.replace(/(\r\n|\n|\r)/gm,"");
    someText = someText.replace(/\s/g, "");
    return someText;
 }