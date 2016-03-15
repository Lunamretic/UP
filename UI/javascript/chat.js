function run(){
	var appContainer = document.getElementsByClassName('messanger')[0];
	
	setHeightOfMessageHistory();
	
    setScroll();
    	
	prepareTime();
	
	appContainer.addEventListener('click', delegateEvent);
	
	updateCounter();
	
}
function setScroll(){
	$('body, html').scrollTop($(document).height());
}
function setHeightOfMessageHistory(){
	var minHeight = document.getElementById('message-history');
	minHeight.style.minHeight = $(window).height()-181 +  'px';
	window.onresize = function(){
		var minHeight = document.getElementById('message-history');
		minHeight.style.minHeight = $(window).height()-181 +  'px';
	}
}
function prepareTime(){
	$(".time").replaceWith(function(){
		var options = {
			hour: 'numeric',
		  minute: 'numeric',
		  second: 'numeric'
		};
		var now = new Date();
		var str = this.getAttribute('title');
		
		
		if (str == now.toDateString()){
			return this;			
		} else {
				if (str.indexOf(':') != -1){
					return this;
					}
				else return '<p title="' +$(this).text()+'" class="time">' +str+'</p>';
		}		
	});
}

function delegateEvent(evtObj) {
	if(evtObj.type === 'click' && evtObj.target.classList.contains('button-send')){
		onAddButtonClick(evtObj);
		setScroll();		
	}
	
	if(evtObj.type === 'click' && evtObj.target.classList.contains('button-delete')){
		deleteMessage();
		setScroll();
	}	
	
	if(evtObj.type === 'click' && (evtObj.target.classList.contains('received-message') || evtObj.target.classList.contains('sent-message') )){
		selectMessage(evtObj.target);
	}		
}

function selectMessage(element){
	$(element).toggleClass('marked');
	
	var button = document.getElementById('buttonDelete');
	if ($(".marked").length != 0){
		button.style.visibility='visible';
	} else {
		button.style.visibility='hidden';
	}    
    
	updateCounter();
}

function deleteMessage() {
	$(".received-message.marked").replaceWith('<li class="deletedMessage">' + 'Message deleted.' + '</li>');
	$(".sent-message.marked").replaceWith('<li class="deletedMessage">' + 'Message deleted.' + '</li>');
	//$( ".received-message.marked").remove();
	updateCounter();
	document.getElementById('inputMessage').focus();
}

function onAddButtonClick(){
	var messageText = document.getElementById('inputMessage');
	addMessage(messageText.value);
	messageText.value = '';
	document.getElementById('inputMessage').focus();
	
	updateCounter();
} 


function addMessage(value) {
	var username = document.getElementById('username');
	
	if(!value){
		return;
	}
	if (!username.value){
		alert('Please log in!')
		return;
	}

	var message = createItem(value);
	var messages = document.getElementsByClassName('messages')[0];

	messages.appendChild(message);
	updateCounter();
}


function nl2br( str ) {
	return str.replace(/([^>])\n/g, '$1<br/>');
}

function createItem(text){
	var h2Name = document.createElement('h2');
	h2Name.appendChild(document.createTextNode(username.value));
	
	var options = {
	  hour: 'numeric',
	  minute: 'numeric',
	  second: 'numeric'
	};
	var now = new Date();
	var time = document.createElement('p');
	$(time).toggleClass('time');
	$(time).attr("title", now.toDateString());
	time.appendChild(document.createTextNode(now.toLocaleString("ru", options)));
	
	
	var pText = document.createElement('p');
	text = nl2br(text);
	pText.appendChild(document.createTextNode(text));
	
	var divItem = document.createElement('li');
	$(divItem).toggleClass('sent-message');
	divItem.appendChild(h2Name);
	divItem.appendChild(time);
	divItem.appendChild(pText);

	return divItem;
}

function updateCounter(){
	var items = document.getElementById('mes');
	var counter = document.getElementsByClassName('counter-holder')[0];
	var c;
	
	c = items.getElementsByClassName('received-message').length;
	c += items.getElementsByClassName('sent-message').length;
    counter.innerText = c.toString();
}