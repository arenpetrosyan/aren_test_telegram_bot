# Telegram Demo Bot
This is a Java-based Telegram bot built on top of the [Telegram Bot Java Library](https://github.com/rubenlagus/TelegramBots).
The bot provides various features such as getting current weather information for a city, fetching currency exchange rates, and managing user profiles.

## Features
- User registration and management
- Weather information for a city
- Currency exchange rates
- Default currency set for a user
- Set a city for a user
- Multiple command support

## Dependencies
- Java 8 or later
- Maven
- Telegram Bot Java Library
- Spring Boot
- [Emoji-java library](https://github.com/vdurmont/emoji-java)
- SLF4J

## Getting Started
1. Find bot in the telegram by the link: https://t.me/ArenTestBot
2. Interact with bot on Telegram.

## Usage
The bot supports the following commands:
-  `/start` : Starts the bot and registers the user.
-  `/help` : Provides help information.
-  `/weather` : Gets the current weather for the user's registered city.
-  `/stock_quote` : Fetches the currency exchange rate for the user's registered currency.
-  `/unregister` : Deletes the user's account.
-  `/city` : Sets the user's city.
-  `/my_currency` : Sets the user's currency.
-  `/default_stock` : Gets the exchange rate for the main currencies.
   Send a command and follow instructions if needed.
