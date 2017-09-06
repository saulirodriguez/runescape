# runescape
The Application has a seeder to add 20 players and the categories with a random level and score for every player in each category. 

You can use the API's /player, /category to create/modify/delete resources.
## API
### localhost:8080/scoreboard/{category_id}
#### GET
Main API, Retreive top 10 players (based on score) by category.

**Note:** Overall is the sum of all the other categories, so you can't create/modify/delete a category with the overall id.

Response (Status 200)
```
[{
    "player": "Player Name",
    "level": 288,
    "score": 3205
},
...
...
,{
    "player": "Player Name",
    "level": 349,
    "score": 3090
}]
```
### localhost:8080/player
#### GET
Response (Status 200)
```
[{
  "id": 1,
  "name": "Player Name",
  "playerCategories": [{
      "level": 87,
      "score": 359,
      "playerInfo": {
          "id": 1,
          "name": "Player Name"
      },
      "categoryInfo": {
          "id": "cateogry_id",
          "name": "Category Name"
      }
  }]
}]
```
#### GET /{id}
Response (Status 200)
```
{
  "id": {id},
  "name": "Player Name",
  "playerCategories": [{
      "level": 87,
      "score": 359,
      "playerInfo": {
          "id": {id},
          "name": "Player Name"
      },
      "categoryInfo": {
          "id": "cateogry_id",
          "name": "Category Name"
      }
  }]
}
```
#### POST
Request Body
```
{
  "name": "Player Name"
}
```
Response (Status 201)
```
{
  "id": 1,
  "name": "Player Name",
  "playerCategories": []
}
```
#### PUT /{id}
Request Body
```
{
  "name": "Player Name"
}
```
Response (Status 200)
```
{
  "id": {id},
  "name": "Player Name",
  "playerCategories": [{
      "level": 87,
      "score": 359,
      "playerInfo": {
          "id": 1,
          "name": "Player Name"
      },
      "categoryInfo": {
          "id": "cateogry_id",
          "name": "Category Name"
      }
  }]
}
```
#### DELETE /{id}
```
Response (Status 200)
```
### localhost:8080/category
#### GET
Response (Status 200)
```
[{
  "id": "category_id",
  "name": "Category Name",
  "description": "Category Description",
  "playersScores": [{
    "level": 45,
    "score": 141,
    "playerInfo": {
      "id": player_id,
      "name": "Player Name"
     },
     "categoryInfo": {
      "id": "category_id",
      "name": "Category Name"
     } 
  }]
}]
```
#### GET /{id}
Response (Status 200)
```
{
  "id": {id},
  "name": "Category Name",
  "description": "Category Description",
  "playersScores": [{
    "level": 45,
    "score": 141,
    "playerInfo": {
      "id": player_id,
      "name": "Player Name"
     },
     "categoryInfo": {
      "id": "category_id",
      "name": "Category Name"
     } 
  }]
}
```
#### POST
Request Body
```
{
  "id": "category_id"
  "name": "Category Name",
  "description": "Category Description"
}
```
Response (Status 201)
```
{
  "id": "category_id",
  "name": "Category Name",
  "description": "Category Description",
  "playersScores": []
}
```
#### PUT /{id}
Request Body
```
{
  "name": "Category Name",
  "description": "Category Description"
}
```
Response (Status 200)
```
{
  "id": {id},
  "name": "Category Name",
  "description": "Category Description",
  "playersScores": [{
    "level": 45,
    "score": 141,
    "playerInfo": {
      "id": player_id,
      "name": "Player Name"
     },
     "categoryInfo": {
      "id": "category_id",
      "name": "Category Name"
     } 
  }]
}
```
#### DELETE /{id}
```
Response (Status 200)
