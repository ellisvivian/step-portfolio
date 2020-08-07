// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

let fact_num_old = -1;

/**
 * Adds a fun fact about me to the page.
 */
function addFunFact() { 
  const fun_facts = [
    'My favorite color is purple.', 
    'I have a little brother.', 
    'I am half-Chinese.', 
    'I am 18 years old.', 
    'My elbows are double-jointed.',
    'I love Shrek (unironically).', 
    'I don\'t like eating many fruits.', 
    'I\'m allergic to cats.'
  ];

  // Pick a random fun fact different from the current one.
  let fact_num_new = Math.floor(Math.random() * fun_facts.length);
  if (fact_num_old != -1) {
    while (fact_num_old == fact_num_new) {
      fact_num_new = Math.floor(Math.random() * fun_facts.length);
    }
  }
  fact_num_old = fact_num_new;

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fun_facts[fact_num_new];
}

let count;
let timer;

/*
 * Displays the initial image and description once the page is loaded.
 */
function initialDisplay() {
  count = -1;
  start();
  getLoginStatus();
  loadComments();
}

/*
 * Changes the image and description displayed on the page.
 */
function autoSlideshow() {
  let images = document.getElementsByClassName('img-experience');
  let descriptions = document.getElementsByClassName('descrip-experience');

  // Hide all images and descriptions.
  for (i = 0; i < images.length; i ++) {
    images[i].style.display = "none";
    descriptions[i].style.display = "none";
  }
    
  // Update count and display next image and description. 
  if (count == 3) {
    count = 0;
  } else {
    count ++;
  }
  images[count].style.display = "block";
  descriptions[count].style.display = "block";

  // Update map display.
  updateMap(count);
}

/*
 * Begins the slideshow.
 */
function start() {
  autoSlideshow();
  timer = setInterval('autoSlideshow();', 8000)
  document.getElementById('continue').style.display = "none";
}

/*
 * Stops the slideshow.
 */
function stop() {
  clearInterval(timer); // Stops the automatic changing of the image and description.
  document.getElementById('continue').style.display = "block";
}

let max = -1;

/*
 * Fetches comments from the server and adds them to the DOM.
 */
function loadComments() {
  const commentContainer = document.getElementById('comment-container');
  commentContainer.innerHTML = '';
  fetch('/data').then(response => response.json()).then((comments) => {
    if (max == -1 || max > comments.length) {
      max = comments.length
    }
    let count;
    for (count = 0; count < max; count ++) {
      commentContainer.appendChild(createComment(comments[count]));
    }
    document.getElementById('comment-count').innerText = 'Comments displayed: ' + count + 
        '. Total comments: ' + comments.length + '.';
  });
}

let currentUserId;

/*
 * Creates an element that represents a comment.
 */
function createComment(comment) {
  const commentBox = document.createElement('div');
  commentBox.className = 'comment-box';

  const name = document.createElement('h3');
  name.id = 'comment-name';
  name.innerText = comment.name;

  const date = document.createElement('p');
  date.id = 'comment-date';
  date.innerText = comment.dateTime;

  const text = document.createElement('p');
  text.innerText = comment.commentText; 

  const bottomWrapper = document.createElement('div');
  bottomWrapper.id = 'comment-bottom-wrapper';

  const likesDisplay = document.createElement('div');
  likesDisplay.id = 'likes-display';

  if (comment.likes.length - 1 > 0) {
    const heartIcon = document.createElement('i');
    heartIcon.className = 'fa fa-heart';
    heartIcon.id = "heart-icon";
    likesDisplay.appendChild(heartIcon);
    const likes = document.createElement('p');
    likes.innerText = comment.likes.length - 1;
    likes.id = 'likes';
    likesDisplay.appendChild(likes);
  }

  const commentButtons = document.createElement('div');
  commentButtons.id = 'comment-buttons';

  const likeButton = document.createElement('button');
  likeButton.className = 'button icon-button';
  if (currentUserId != null) {
    if (comment.likes.includes(currentUserId)) {
      likeButton.className = 'button icon-button inverted-button';
    }
  }
  const likeIcon = document.createElement('i');
  likeIcon.className = 'fa fa-heart';
  likeButton.appendChild(likeIcon);
  likeButton.addEventListener('click', () => {
    likeComment(comment, currentUserId);  
  });
  commentButtons.appendChild(likeButton);

  if (comment.userId.localeCompare(currentUserId) == 0) {
    const deleteButton = document.createElement('button');
    deleteButton.className = 'button icon-button';
    const trashIcon = document.createElement('i');
    trashIcon.className = 'fa fa-trash';
    deleteButton.appendChild(trashIcon);
    deleteButton.addEventListener('click', () => {
    deleteComment(comment);
    });
    commentButtons.appendChild(deleteButton);
  }

  bottomWrapper.appendChild(likesDisplay);
  bottomWrapper.appendChild(commentButtons);

  commentBox.appendChild(name);
  commentBox.appendChild(date);
  commentBox.appendChild(text);
  commentBox.appendChild(bottomWrapper);
  return commentBox;
}

/*
 * Displays comments based on the inputted maximum number.
 */
function displayMaxComments() {
  max = document.getElementById('max-comments').value;
  loadComments();
}

/*
 * Deletes all existing comments.
 */
function deleteComments() {
  const request = new Request('/delete-data', {method: 'POST'});
  const promise = fetch(request);
  promise.then(loadComments);
}

/*
 * Deletes the specified comment.
 */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  const request = new Request('/delete-data', {method: 'POST', body: params});
  const promise = fetch(request);
  promise.then(loadComments);
}

/*
 * Adds a like to the specified comment.
 */
function likeComment(comment, currentUserId) {
  if (currentUserId != null) {
    const params = new URLSearchParams();
    params.append('id', comment.id);
    const request = new Request('/like-data', {method: 'POST', body: params});
    const promise = fetch(request);
    promise.then(loadComments);
  } else {
    document.getElementById('like-warning').style.display = 'block';
  }
}

/*
 * Hides the like warning once the user hits "OK".
 */
function hideLikeWarning() {
  document.getElementById('like-warning').style.display = 'none';
}

/*
 * Loads certain comment functionality based on the user's login status.
 */
function getLoginStatus() {

  const promise = fetch('/login-data').then(response => response.json()).then((json) => {
    if (json['loginStatus'].localeCompare('true') == 0) {
      currentUserId = json['user-id'];
      const namePromise = fetch('/name-data').then(response => response.text()).then((name) => {
        if (name.localeCompare('\n') == 0) {
          changeName();
        }
      });

      // Display form to submit comment and hide login statement.
      document.getElementById('comment-submission').style.display = 'block';
      document.getElementById('login-statement').style.display = 'none';

      // Add greeting to known user, change name button, and logout button.
      const userGreeting = document.getElementById('user-greeting');
      userGreeting.innerHTML = '';
      const greeting = document.createElement('p');
      greeting.innerText = 'Hi ' + json['userName'] + '!\n' + ' (' + json['userEmail'] + ')';
      const changeNameButton = document.createElement('button');
      changeNameButton.className = 'button';
      changeNameButton.innerText = 'Change name.';
      changeNameButton.addEventListener('click', () => {        
        changeName();
      });
      const logoutButton = document.createElement('button');
      logoutButton.className = 'button';
      logoutButton.id = 'logout-button';
      logoutButton.innerText = 'Logout.';
      logoutButton.addEventListener('click', () => {
        window.location.href = json['logoutUrl'];
      });
      userGreeting.appendChild(greeting);
      userGreeting.appendChild(changeNameButton);
      userGreeting.appendChild(logoutButton);
    } else {
      currentUserId = null;

      // Hide form to submit comment and display login statement.
      document.getElementById('comment-submission').style.display = 'none';
      document.getElementById('login-statement').style.display = 'block';

      // Add statement to unknown user and login button.
      const loginStatement = document.getElementById('login-statement');
      loginStatement.innerHTML = '';
      const statement = document.createElement('p');
      statement.innerText = "Login to post a comment.";
      const loginButton = document.createElement('button');
      loginButton.className = 'button';
      loginButton.innerText = 'Login.';
      loginButton.addEventListener('click', () => {
        window.location.href = json['loginUrl']
      });
      loginStatement.appendChild(statement);
      loginStatement.appendChild(loginButton);
    }
  });
}

/*
 * Presents the user with a form to change their name.
 */
function changeName() {
  document.getElementById('name-form-container').style.display = 'block';
}

/*
 * Hides the form for the user to change their name.
 */
function hideChangeName() {
  document.getElementById('name-form-container').style.displau = 'none';
}

let map;
let cities = [{lat: 44.972679, lng: -93.279569}, {lat: 29.760488, lng: -95.370274}, {lat: 52.378782, lng: 4.900246}, {lat: 38.544925, lng: -121.740818}];
let locations = [{lat: 44.915330, lng: -93.211000}, {lat: 29.715189, lng: -95.400813}, {lat: 52.366, lng: 4.886}, {lat: 38.542, lng: -121.760}];

let infowindows;
let defaultZoom = 11;

/*
 * Adds an interactive map to the display.
 */
function initMap() {
  map = new google.maps.Map(document.getElementById("map"), {
    center: cities[0],
    zoom: defaultZoom,
    styles: [
      {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
      {
        featureType: 'administrative.locality',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'poi',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'poi.park',
        elementType: 'geometry',
        stylers: [{color: '#263c3f'}]
      },
      {
        featureType: 'poi.park',
        elementType: 'labels.text.fill',
        stylers: [{color: '#6b9a76'}]
      },
      {
        featureType: 'road',
        elementType: 'geometry',
        stylers: [{color: '#38414e'}]
      },
      {
        featureType: 'road',
        elementType: 'geometry.stroke',
        stylers: [{color: '#212a37'}]
      },
      {
        featureType: 'road',
        elementType: 'labels.text.fill',
        stylers: [{color: '#9ca5b3'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry',
        stylers: [{color: '#746855'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry.stroke',
        stylers: [{color: '#1f2835'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'labels.text.fill',
        stylers: [{color: '#f3d19c'}]
      },
      {
        featureType: 'transit',
        elementType: 'geometry',
        stylers: [{color: '#2f3948'}]
      },
      {
        featureType: 'transit.station',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'water',
        elementType: 'geometry',
        stylers: [{color: '#17263c'}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.fill',
        stylers: [{color: '#515c6d'}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.stroke',
        stylers: [{color: '#17263c'}]
      }
    ]
  });
  let infowindow1 = new google.maps.InfoWindow({
    content: 'One of our favorite Cuz Unite locations is our cousin\'s house ' + 
        'in Minneapolis, Minnesota. This picture was taken after a bike ride to ' +
        'the beautiful Minnehaha Falls.'
  });
  let infowindow2 = new google.maps.InfoWindow({
    content: 'Wiess College is located on the South side of Rice campus. In this photo, ' + 
        'we are sneaking into a nearby residential college to surprise them with ' + 
        'our infamously strange "Ubangee" tradition.'
  });
  let infowindow3 = new google.maps.InfoWindow({
    content: 'Amsterdam was the first stop of our travels! Here my brother and I ' + 
        'are standing on a bridge overlooking one of the city\'s many canals.'
  });
  let infowindow4 = new google.maps.InfoWindow({
    content: 'The first cheer competition of my senior year season took place ' + 
        'at UC Davis. I originally joined cheer because of its similarity to dance, ' + 
        'but I ended up loving its more prominent emphasis on teamwork.'
  });
  infowindows = [infowindow1, infowindow2, infowindow3, infowindow4];
}

/*
 * Updates the map display based on the slideshow image and description.
 */
function updateMap(count) {
  map.panTo(cities[count]);
  map.setZoom(11);
  var marker = new google.maps.Marker({position: locations[count], map: map});
  if (count == 0) {
    infowindows[3].close();
  } else {
    infowindows[count - 1].close();
  }
  marker.addListener('click', function() {
    infowindows[count].open(map, marker);
  });
}
