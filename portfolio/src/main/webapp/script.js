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
  let experiences = document.getElementsByClassName('experience');

  // Hide all images and descriptions.
  for (i = 0; i < experiences.length; i ++) {
    experiences[i].style.display = "none";
  }
    
  // Update count and display next image and description. 
  if (count == 3) {
    count = 0;
  } else {
    count ++;
  }
  experiences[count].style.display = "flex";
}

/*
 * Begins the slideshow.
 */
function start() {
  autoSlideshow();
  timer = setInterval('autoSlideshow();', 6000)
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
    document.getElementById('comment-count').innerText = 'Comments displayed: ' + count + '. Total comments: ' + comments.length + '.';
  });
}

/*
 * Creates an element that represents a comment.
 */
function createComment(comment) {
  const commentBox = document.createElement('div');
  commentBox.className = 'comment-box';

  const name = document.createElement('h3');
  name.id = 'comment-name';
  name.innerText = comment.firstName + " " + comment.lastName;

  const date = document.createElement('p');
  date.id = 'comment-date';
  date.innerText = comment.dateTime;

  const text = document.createElement('p');
  text.innerText = comment.commentText; 

  const bottomWrapper = document.createElement('div');
  bottomWrapper.id = 'comment-bottom-wrapper';

  const likesDisplay = document.createElement('div');
  likesDisplay.id = 'likes-display';

  if (comment.likes > 0) {
    const heartIcon = document.createElement('i');
    heartIcon.className = 'fa fa-heart';
    heartIcon.id = "heart-icon";
    likesDisplay.appendChild(heartIcon);
    const likes = document.createElement('p');
    likes.innerText = comment.likes;
    likes.id = 'likes';
    likesDisplay.appendChild(likes);
  }

  const commentButtons = document.createElement('div');
  commentButtons.id = 'comment-buttons';

  const likeButton = document.createElement('button');
  likeButton.className = 'button icon-button';
  const likeIcon = document.createElement('i');
  likeIcon.className = 'fa fa-heart';
  likeButton.appendChild(likeIcon);
  likeButton.addEventListener('click', () => {
    likeComment(comment);  
  });
  commentButtons.appendChild(likeButton);

  const deleteButton = document.createElement('button');
  deleteButton.className = 'button icon-button';
  const trashIcon = document.createElement('i');
  trashIcon.className = 'fa fa-trash';
  deleteButton.appendChild(trashIcon);
  deleteButton.addEventListener('click', () => {
    deleteComment(comment);
  });
  commentButtons.appendChild(deleteButton);

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
function likeComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  params.append('likes', comment.likes + 1);
  const request = new Request('/like-data', {method: 'POST', body: params});
  const promise = fetch(request);
  promise.then(loadComments);
}

/*
 * Loads certain comment functionality based on the user's login status.
 */
function getLoginStatus() {
  const promise = fetch('/login-data').then(response => response.text()).then((status) => {
    if (status.localeCompare('true\n') == 0){
      document.getElementById('comment-controls').style.display = 'block';
    } else {
      const signInButton = document.createElement('button');
      signInButton.innerText = "Login to post a comment.";
      document.getElementById('comment-section').appendChild(signInButton);
    }
  });
}
