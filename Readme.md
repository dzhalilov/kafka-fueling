docker-compose для поднятия Kafka

To check Reactor sent CURL:
curl --location 'localhost:8080/api/v1/fueling/order' --no-buffer \
--header 'Content-Type: application/stream+json' \
--data '{
    "clientId" : 123,
    "stationId" : 90001,
    "fuelType" : "AI_92",
    "quantity" : 10000,
    "sum" : 530
}'
