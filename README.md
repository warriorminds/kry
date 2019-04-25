# KRY app code assignment
One of our developers built a simple weather reporting app. The app has a list of locations with weather information. Locations are fetched using a HTTP GET but the server is a bit flakey and sometimes fail. 

Unfortunately the app is not complete and it's up to you to complete it. There is a backlog of features to implement and some nice to have (bonus) features. 

Backlog (required):

- Implement the functionality to add a new location 
  - post: https://app-code-test.kry.pet/locations
  - body: { id: "random string", name: "location name", status: "SUNNY", temperature: 20}
  - header: ApiKey
- Implement the functionality to remove a location 
  - delete: https://app-code-test.kry.pet/locations/:id
  - header: ApiKey

Nice to haves (optional):

- Add nice animations
- Improvements to code
- Tests

Spend two to four hours working on this assignment - make sure to finish the issues you start.

Put the code in a GitHub repository and send us the link (niklas.holmqvist@kry.se) when you're done. 

Good luck! ☺️
