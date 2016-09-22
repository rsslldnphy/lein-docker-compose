# lein-docker-compose

Small leiningen plugin that starts the services configured in your `docker-compose.yml`, discovers their port mappings, and writes that to a file for use by [environ](https://github.com/weavejester/environ).

Currently requires you to be using my fork of environ, [`[rsslldnphy/environ "1.1.1"]`](https://github.com/rsslldnphy/environ), which adds support for `.lein-docker-env` files.

You'll also need `docker-compose` installed and available on your path.
