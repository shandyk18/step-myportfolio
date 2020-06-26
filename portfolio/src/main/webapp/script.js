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

let factIndex = -1;

/**
 * Traverses the array of facts and presents them in order
 */
function showFact() {
    const facts = 
      ['I am the oldest out of three kids', 'My first programming language was Python', 'My favorite game is Fallout: New Vegas', 'I hate raw tomatoes with a great passion', 
      'I firmly believe there is a Spongebob quote for every situation'];

    factIndex += 1;

    // if reached end of facts  
    if (factIndex >= facts.length) {factIndex = 0}

    const fact = facts[factIndex];

    const factContainer = document.getElementById('fact-container');
    factContainer.innerText = fact;
}
