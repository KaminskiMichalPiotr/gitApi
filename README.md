# Git repostories and branches searcher

This app allows You to search for given Git user repositories and corresponding branches.


# Usage

Clone or download repository and simply run: "java -jar build/gitapi-1.jar" from project directory. Using browser or http client of your choice go to: *localhost:8080/repositories/{username}* where username is Git login of your choice.

## Response

Succesfull response - list of repostories with branches (HTTP 200):

```json
[
  {
    "repositoryName": "Repository 1",
    "ownerLogin": "Owner",
    "branches": [
      {
        "branchName": "master",
        "commitSHA": "sha1"
      }
    ]
  },
  {
    "repositoryName": "Repository 2",
    "ownerLogin": "Owner",
    "branches": [
      {
        "branchName": "master",
        "commitSHA": "sha1"
      },
      {
        "branchName": "other",
        "commitSHA": "sha2"
      }
    ]
  }
]
```
Username not found (HTTP 404)
```json
{
     "message": "Username: {username} not found",
     "status": "NOTFOUND"
}
```

Exceeded api rate limit (HTTP 403)
```json
{
     "message": "Error fetching data from Git Api, exceeded rate limit",
     "status": "FORBIDDEN"
}
```

## Git API rate limits
As authenticated user you can only make 60 requests to the api per hour. Each search will resault in 1 + m requests, where m is amount of repositories searched user have.

Read more: [rate limits](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api?apiVersion=2022-11-28)
