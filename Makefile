build:
	./gradlew shadowJar

up: build
	docker compose up --build

down:
	docker compose down -v

logs-consumers:
	docker compose logs -f consumer-a consumer-b