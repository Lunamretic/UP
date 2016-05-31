'use strict';

var Application = {
	mainUrl : 'http://localhost:3128/chat',
	messageList:[],
	token : 'TE11EN'
};

var messagesList = [];

var currentTime = function() {
	var options = {
	  hour: 'numeric',
	  minute: 'numeric',
	  second: 'numeric'
	};
	var now = new Date();
	return now.toLocaleString("ru", options);
};

var currentDate = function() {
	var now = new Date();
	return now.toDateString();
};

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var message = function(text, author, recipient) {
	return  {
		text: text,
		author: author,
		time: currentTime(),
		date: currentDate(),
		id: uniqueId()		
	};
};

function run(){
	var appContainer = document.getElementsByClassName('messanger')[0];
	
	setHeightOfMessageHistory();
	
    setScroll();
    	
	prepareTime();
	
	appContainer.addEventListener('click', delegateEvent);

	restore(function(){
		render(Application);
	});
	
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
	
	if(evtObj.type === 'click' && (evtObj.target.parentElement.classList.contains('received-message') || evtObj.target.parentElement.classList.contains('sent-message') )){
		selectMessage(evtObj.target.parentElement);
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
	$(".sent-message.marked").each(function () {
		for (var j = 0; j < Application.messageList.length; j++){
			if (this.attributes['message-id'].value == Application.messageList[j].id){
				var message = Application.messageList[j];
				var messageToDelete = {
					id:message.id
				};
				ajax('DELETE', Application.mainUrl, JSON.stringify(messageToDelete), function(){
					Application.messageList.splice(j, 1);
				});
				break;
			}
		}
	})

	$(".received-message.marked").replaceWith('<li class="deletedMessage">' + 'Message deleted.' + '</li>');
	$(".sent-message.marked").replaceWith('<li class="deletedMessage">' + 'Message deleted.' + '</li>');

	updateCounter();
	document.getElementById('inputMessage').focus();
}


function render(root) {
	var messages = document.getElementsByClassName('messages')[0];
	for(var i = 0; i < root.messageList.length; i++) {
		var mes = createItem(root.messageList[i]);
		messages.appendChild(mes);
	}
	updateCounter();
}

function onAddButtonClick(){
	var messageText = document.getElementById('inputMessage');
	var username = document.getElementById('username');

	if(!messageText.value){
		return;
	}
	if (!username.value){
		alert('Please, log in!')
		return;
	}

	var newMessage = message(messageText.value, username.value);

	addMessage(newMessage);

	messageText.value = '';
	document.getElementById('inputMessage').focus();
	
	updateCounter();
} 

function addMessage(message) {
	var mes = createItem(message);
	var messages = document.getElementsByClassName('messages')[0];

	messages.appendChild(mes);
	Application.messageList.push(message);

	ajax('POST', Application.mainUrl, JSON.stringify(message), function(){
		Application.messageList.push(message);
	});

	updateCounter();
}

function nl2br( str ) {
	return str.replace(/([^>])\n/g, '$1<br/>');
}

function createItem(newMessage){
	var h2Name = document.createElement('h2');
	h2Name.appendChild(document.createTextNode(newMessage.author));
	
	var time = document.createElement('p');
	$(time).toggleClass('time');
	$(time).attr("title", newMessage.date);
	time.appendChild(document.createTextNode(newMessage.time));
	
	
	var pText = document.createElement('p');
	var text = nl2br(newMessage.text);
	pText.innerHTML = text;
	
	var divItem = document.createElement('li');
	$(divItem).toggleClass('sent-message');
	divItem.appendChild(h2Name);
	divItem.appendChild(time);
	divItem.appendChild(pText);
	$(divItem).attr('message-id', newMessage.id);

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

function restore(done) {
	var url = Application.mainUrl + '?token=' + Application.token;

	ajax('GET', url, null, function(responseText){
		var response = JSON.parse(responseText);

		Application.messageList = response.messages;
		Application.token = response.token;
		done();
	});

}
function isError(text) {
	if(text == "")
		return false;
	
	try {
		var obj = JSON.parse(text);
	} catch(ex) {
		return true;
	}

	return !!obj.error;
}

function ajax(method, url, data, continueWith, continueWithError) {
	var xhr = new XMLHttpRequest();

	continueWithError = continueWithError || defaultErrorHandler;
	xhr.open(method || 'GET', url, true);

	xhr.onload = function () {
		if (xhr.readyState !== 4)
			return;

		if(xhr.status != 200) {
			continueWithError('Error on the server side, response ' + xhr.status);
			return;
		}

		if(isError(xhr.responseText)) {
			continueWithError('Error on the server side, response ' + xhr.responseText);
			return;
		}

		continueWith(xhr.responseText);
	};    

    xhr.ontimeout = function () {
    	ontinueWithError('Server timed out !');
    };

    xhr.onerror = function (e) {
    	document.getElementById('connection').style.visibility = "visible";
    	var errMsg = 'Server connection error !\n'+
    	'\n' +
    	'Check if \n'+
    	'- server is active\n'+
    	'- server sends header "Access-Control-Allow-Origin:*"\n'+
    	'- server sends header "Access-Control-Allow-Methods: PUT, DELETE, POST, GET, OPTIONS"\n';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

window.onerror = function(err) {
	console.error(err.toString());
};



function defaultErrorHandler(message) {
	console.error(message);
}