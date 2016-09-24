# lein-docker-compose

Small leiningen plugin that starts the services configured in your `docker-compose.yml`, discovers their port mappings, and merges them into the map used by [environ](https://github.com/weavejester/environ).

You'll also need `docker-compose` installed and available on your path.
