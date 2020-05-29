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

/**
 * Adds a fun fact about me to the page.
 */
function addFunFact() {
  const fun_facts =
      ['My favorite color is purple.', 'I have a little brother.', 'I am half-Chinese.', 'I am 18 years old.', 'My elbows are double-jointed.',
      'I love Shrek (unironically).'];

  // Pick a random fun fact.
  const fun_fact = fun_facts[Math.floor(Math.random() * fun_facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fun_fact;
}

let count;

/*
 * Displays the intital image and description once the page is loaded.
 */
function initialDisplay() {
    count = -1;
    autoSlideshow();
}

/*
 * Changes the image and description displayed on the page.
 */
function autoSlideshow() {
    let experiences = document.getElementsByClassName('experience');
    console.log(experiences);
    console.log(experiences.length);

    // Hide all images.
    for (i = 0; i < experiences.length; i ++) {
      experiences[i].style.display = "none";
    }
    
    // Update count and display next image. 
    if (count == 3) {
      count = 0;
    } else {
      count ++;
    }
    console.log(count);
    experiences[count].style.display = "flex";
}

let timer = setInterval('autoSlideshow();', 6000); // Changes the image and description every 5 seconds.

function stop() {
    clearInterval(timer); // Stops the automatic changing of the image and description.
}


// let image_sources = ['/images/1.jpg', '/images/2.jpg', '/images/3.jpg', '/images/4.jpg']

// let descriptions = ['Family has always been something really important to me, especially my cousins. ' + 
//                     'Every year, we try to get together in what we call \'Cuz Unite\'. During Cuz Unites, ' +
//                     'we go on wild adventures, play games, and just spend time catching up and enjoying ' +
//                     'each other\'s company. Around my cousins I always feel loved and free to be myself.',
    
//                     'At Rice, students are randomly split into 11 residential colleges, which will serve as an ' +
//                     'inclusive community throughout their four years on campus (and beyond). My college is ' +
//                     'Wiess college, and from the moment I was welcomed during orientation week it has felt ' + 
//                     'like home. From the friendly faces I see in commons every day to our quirky and exciting ' +
//                     'traditions, I am truly grateful for my Team Family Wiess!', 

//                     'The summer after my senior year of high school, I went on an epic month-long European vacation ' +
//                     'with my family, where we visited The Netherlands, Belgium, France, Scotland, and England. It was ' +
//                     'an amazing experience to get to see and explore so many different places, and I am so ' + 
//                     'appreciative of my family for making it happen. Hopefully I can continue to travel the world ' +
//                     'on my own someday.',
                    
//                     'Ever since I was really little and just twirling around the house with scarves, ' + 
//                     'I\'ve always had a passion for dance. Whether it was performing on stage with my ' +
//                     'competitive team, at a rally with my high school class, or in the quad with my kpop ' +
//                     'dance club, I had so much fun expressing myself through movement. Sometimes the ' +
//                     'competitive nature of the dance world got to me, but I would turn back to low-pressure ' + 
//                     'dance environments with friends to remind me of the happiness it brought me.'];

/*
 * Changes the image and description on the page.
 */ 
// function changeDisplay() {
//     if (count == 3) {
//         count = 0;
//     } else {
//         count ++;
//     }
//     document.getElementById('img').src = image_sources[count];
//     document.getElementById('description').innerText = descriptions[count];
// }