run the application :

mvn spring-boot:run

run the test :
mvn test

sql local database :

sudo -i -u postgres
psql -U myuser -d mydatabase -h localhost -W

To insert data :

POST http://localhost:8080/products

{
    "code": "PRD12345",
    "name": "Sample Product",
    "description": "This is a sample product description.",
    "image": "https://example.com/sample-product.jpg",
    "category": "Electronics",
    "price": 199.99,
    "quantity": 50,
    "internalReference": "REF-001",
    "shellId": 123456,
    "inventoryStatus": "INSTOCK",
    "rating": 4.5
}

