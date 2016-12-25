
function viewChanger() {
	mode: 'default'
}

viewChanger.prototype.setTypeMode = function() {
    this.mode = 'type';
    alert(this.mode);
}

viewChanger.prototype.setTopicMode = function() {
    this.mode = 'topic';
    alert(this.mode);
}

var myChanger = new viewChanger();
