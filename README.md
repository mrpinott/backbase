# Backbase Android Implementation

○ Language must be Java
○ UI has to be implemented using 1 activity with multiple fragments
○ Only 3rd party libraries allowed are: GSON or Jackson.
○ Compatible with Android 4.1+


Data Source: cities.json
We have a list of cities containing around 200k entries in JSON format.
Each entry contains the following information:
{
     "country":"UA",
     "name":"Hurzuf",
     "_id":707860,
     "coord":{
           "lon":34.283333,
           "lat":44.549999
     }
}

Requirements:
1. Display this of cities on a scrollable list in alphabetic order (city first, country after)
2. Be able to filter the results by a given prefix string, over the city.
	> prefix string: a substring that matches the initial characters of the target string
3. Selecting a city will show a map centered on the coordinates associated with the city.
4. Optimize for fast searches

Data Structure:
You can preprocess the list into any other representation that you consider more efficient
for searches and display. Provide information of why that representation is more efficient
in the comments of the code.


Unit Test:
Provide unit tests, that your search algorithm is displaying the correct results giving different inputs, including invalid inputs.

Deliverables:
The code of the assignment has to be delivered along with the git repository (.git folder). We want to see the progress evolution