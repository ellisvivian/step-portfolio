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
  if (currentImage.src.match('/images/1.jpg')) {
    currentImage.src = '/images/2.jpg';
  } else if (currentImage.src.match('/images/2.jpg')) {
    currentImage.src = '/images/3.jpg';
  } else if (currentImage.src.match('/images/3.jpg')) {
    currentImage.src = '/images/4.jpg';
  } else if (currentImage.src.match('/images/4.jpg')) {
    currentImage.src = '/images/1.jpg';
  } 
}
