## Autoscout24 code challenge

This repository is my attempt at the Autoscout24 code challenge. 
It's a small REST service for publishing, reading and listing car adverts.
  
## Sample Usage

Publish a new advert:
    
    POST /car
    {
        "title": "brand and model",
        "price": 5000,
        "fuel": "Gasoline"|"Diesel"
        "new": true
    }
    
    alternatively, for used cars:
    
    POST /car
    {
        "title": "brand and model",
        "price": 5000,
        "fuel": "Gasoline"|"Diesel"
        "new": false,
        "mileage": 120000,
        "firstRegistration": "2000-01-01"
    }
    
    >>> Response:
    201 Created
    Location: /car/{id}

Show a single advert:

    GET /car/1
    
    >>> Response:
    {
        "id": 1, 
        ...
    }

List all available adverts:

    GET /car?[sort=price][ascending=false]
    
    >>> Response:
    [{id:...}, {id:...}]

Replacing an existing advert:

    PUT /car/1
    {
        "id": 1,
        "title": "modified title",
        "price": 4000,
        "fuel": "Diesel",
        "new": true
    }
    
    >>> Response:
    200 Ok

Since the advert is replaced completely, a full data entry is required.
Note that the service will generate an id for new adverts but to update one, you have to supply the id as well.
Seems redundant, but allows sharing an entry without carrying around the URL containing the id.

Removing an existing advert:

    DELETE /car/1
    
    >>> Response:
    200 Ok

## Installation

Perform `sbt run`, point your browser to http://localhost:9000/car and apply evolutions.
The service creates a local H2 database in the repository and comes with a small test dataset.