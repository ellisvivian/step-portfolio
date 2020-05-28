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



/**
 * Changes the image on the page.
 */
function changeImage() {
  currentImage = document.getElementById('img');

  // Update the image based on the current image.
  for (let i = 0; i < image_sources.length; i ++) {
      if (currentImage.src.match(image_sources[i])) {
        if (i < image_sources.length - 1) {
            currentImage.src = image_sources[i + 1];
        } else {
            currentImage.src = image_sources[0];
        }
        break;
      }
  }
}

let count;

let image_sources = ['/images/1.jpg', '/images/2.jpg', '/images/3.jpg', '/images/4.jpg']

let descriptions = ['At Rice, students are randomly split into 11 residential colleges, which will serve as an' +
                    'inclusive community throughout their four years on campus (and beyond). My college is ' +
                    'Wiess college, and from the moment I was welcomed during orientation week it has felt ' + 
                    'like home. From the friendly faces I see in commons every day to our quirky and exciting ' +
                    'traditions, I am truly grateful for my Team Family Wiess!', 
                    'cousins and family wooo',
                    'gotta love epic travel adventures',
                    'dance dance revolution'];

/*
 * Displays the intital image and description once the page is loaded.
 */
function initialDisplay() {
    count = -1;
    changeDisplay()
}

/*
 * Changes the image and description on the page.
 */ 
function changeDisplay() {
    if (count == 3) {
        count = 0;
    } else {
        count ++;
    }
    document.getElementById('img').src = image_sources[count];
    document.getElementById('description').innerText = descriptions[count];
}