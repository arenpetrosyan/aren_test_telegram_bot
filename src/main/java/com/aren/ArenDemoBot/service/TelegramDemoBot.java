package com.aren.ArenDemoBot.service;

import com.aren.ArenDemoBot.config.BotConfig;
import com.aren.ArenDemoBot.entities.User;
import com.aren.ArenDemoBot.model.WeatherNow;
import com.aren.ArenDemoBot.repositories.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * This class is responsible for handling the Telegram bot commands
 */

@Component
@Slf4j
public class TelegramDemoBot extends TelegramLongPollingBot {

    //commands
    private static final String START_COMMAND = "/start";
    private static final String HELP_COMMAND = "/help";
    private static final String WEATHER_COMMAND_1 = "/weather";
    private static final String WEATHER_COMMAND_2 = "Погода";
    private static final String STOCK_QUOTE_COMMAND_1 = "/stockqoute";
    private static final String STOCK_QUOTE_COMMAND_2 = "Курс валют";
    private static final String MY_CITY_COMMAND = "/mycity";
    private static final String UNREGISTER_COMMAND = "/unregister";
    private static final String MY_CURRENCY_COMMAND = "/mycurrency";
    private static final String DEFAULT_STOCK_COMMAND_1 = "/defaultstock";
    private static final String SETTINGS_COMMAND = "/settings";
    private static final String DEFAULT_STOCK_COMMAND_2 = "Курсы основных валют";

    private static final String HELP_TEXT = "Я могу помочь тебе с командами бота:\n" +
                                            START_COMMAND + " - запуск бота и авторегистрация пользователя\n" +
                                            MY_CITY_COMMAND + " - установить город пользователя\n" +
                                            WEATHER_COMMAND_1 + " - узнать погоду в твоем городе\n" +
                                            MY_CURRENCY_COMMAND + " - установить валюту пользователя\n" +
                                            STOCK_QUOTE_COMMAND_1 + " - получить курс валют\n" +
                                            DEFAULT_STOCK_COMMAND_1 + " - курс самых распрастраненных валют\n" +
                                            SETTINGS_COMMAND + " - получить настройки профиля\n" +
                                            UNREGISTER_COMMAND + " - удалить аккаунт пользователя\n";

    private static final String PROFILE_CURRENCY_TEXT = """
                                                                Введите валюту, которая будет сохранена в вашем профиле.
                                                                Используйте международные коды валют, например RUB, EUR или USD.
                                                                В дальнейшем курсы валют будут приводится к этой валюте.
                                                                Изменить его можно будет по команде""" + " " + MY_CURRENCY_COMMAND;

    private static final List<String> DEFAULT_CURRENCIES = asList("USD", "RUB", "EUR", "GBP", "JPY", "CNY");
    private static final String REGISTRATION_SUCCESSFUL_TEXT = "Пользователь зарегистрирован!" + parseToUnicode(":tada:");
    private static final String CURRENCY_CHANGE_SUCCESSFUL_TEXT = "Валюта пользователя успешно изменена!" + parseToUnicode(":tada:") + "\n" +
                                                                  "Введите код валюты, курс которой вы хотите увидеть.";
    private static final String CURRENCY_NEEDED_TEXT = "Введите код валюты, курс которой вы хотите увидеть." + parseToUnicode(":arrow_down:");
    private static final String CITY_TEXT = "Напишите название города в формате 'город 'Ваш город'' без кавычек" + parseToUnicode(":arrow_down:");
    private static final String REGISTRATION_NEEDED_TEXT = "Сначала надо зарегистрироваться! Нажмите команду /start" + parseToUnicode(":pray:");
    private static final String WRONG_CURRENCY_TEXT = "Эта валюта выбрана как основная." + parseToUnicode(":heavy_dollar_sign:") + "\n" +
                                                      "Выберите другую чтобы увидеть её курс к основной.";

    private final WeatherService weatherService;
    private final CurrencyService currencyService;
    private final UserRepository userRepository;
    private final BotConfig config;

    public TelegramDemoBot(UserRepository userRepository, WeatherService weatherService, CurrencyService currencyService, BotConfig config) {
        this.userRepository = userRepository;
        this.weatherService = weatherService;
        this.currencyService = currencyService;
        this.config = config;
        // Create list of commands
        List<BotCommand> listOfCommands = getBotCommands();
        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Chat chat = update.getMessage().getChat();
            switch (text) {
                case START_COMMAND -> {
                    registerUser(chat);
                    startCommandReceived(chat);
                }
                case HELP_COMMAND -> helpCommandReceived(chat);
                case UNREGISTER_COMMAND -> deleteUserCommandReceived(chat);
                case WEATHER_COMMAND_1, WEATHER_COMMAND_2 -> weatherCommandReceived(chat);
                case MY_CURRENCY_COMMAND -> myCurrencyCommandReceived(chat);
                case STOCK_QUOTE_COMMAND_1, STOCK_QUOTE_COMMAND_2 -> stockQuoteCommandReceived(chat);
                case DEFAULT_STOCK_COMMAND_1, DEFAULT_STOCK_COMMAND_2 -> defaultStockCommandReceived(chat);
                case MY_CITY_COMMAND -> cityCommandReceived(chat);
                case SETTINGS_COMMAND -> settingsCommandReceived(chat);
                default -> {
                    if (text.toLowerCase().startsWith("город ")) {
                        handleCityMessages(chat, text);
                    } else if (currencyService.isCurrency(text)) {
                        currencyInputReceived(text, chat);
                    } else {
                        sendMessage(chatId, "Команда не найдена!");
                    }
                }
            }
        }
    }

    /**
     * Handles the /settings command
     *
     * @param chat The Chat object of the user
     */
    private void settingsCommandReceived(Chat chat) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            String answer = "Пользователь: " + user.getUserName() + "\n" +
                            "Валюта: " + user.getCurrency() + "\n" +
                            "Город: " + user.getCity();
            sendMessage(chat.getId(), answer);
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Handles the /defaultstock command
     *
     * @param chat The Chat object of the user
     */
    private void defaultStockCommandReceived(Chat chat) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            if (user.getCurrency() == null) {
                sendMessage(chat.getId(), PROFILE_CURRENCY_TEXT);
            } else {
                List<String> froms = DEFAULT_CURRENCIES.stream()
                        .filter(currency -> !currency.equals(user.getCurrency()))
                        .collect(toList());
                log.info("Getting default stock for {} to {}", froms, user.getCurrency());
                getDefaultCurrencyRates(chat, froms, user.getCurrency());
            }
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Handles city messages in the chat
     *
     * @param chat The Chat object of the user
     * @param text The message text containing city name
     */
    private void handleCityMessages(Chat chat, String text) {
        text = text.toLowerCase();
        text = text.replace("город ", "");
        if (weatherService.isCity(text)) {
            log.info("Getting weather for city {}", text);
            checkIfUserCityIsNullAndSetUserCity(chat, text);
        }
    }

    /**
     * Handles the /mycurrency command
     *
     * @param chat The chat object of the user
     */
    private void myCurrencyCommandReceived(Chat chat) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            user.setCurrency(null);
            userRepository.save(user);
            sendMessage(chat.getId(), PROFILE_CURRENCY_TEXT);
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Processes currency input from the user
     *
     * @param text The message text containing currency code
     * @param chat The Chat object of the user
     */
    private void currencyInputReceived(String text, Chat chat) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            if (user.getCurrency() == null) {
                log.info("Setting currency {} for user {}", text, user.getChatId());
                setUserCurrency(chat, text.toUpperCase());
            } else {
                if (text.toUpperCase().equals(user.getCurrency())) {
                    sendMessage(chat.getId(), WRONG_CURRENCY_TEXT);
                } else {
                    log.info("Getting currency rate of {} for user {}", text, user.getChatId());
                    getCurrencyRate(chat, text.toUpperCase(), user.getCurrency());
                }
            }
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Handles the /stockquote command
     *
     * @param chat The chat object of the user
     */
    private void stockQuoteCommandReceived(Chat chat) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            String userCurrency = user.getCurrency();
            if (userCurrency == null) {
                sendMessage(chat.getId(), PROFILE_CURRENCY_TEXT);
            } else {
                sendMessage(chat.getId(), CURRENCY_NEEDED_TEXT);
            }
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Handles the /city command
     *
     * @param chat The chat object of the user
     */
    private void cityCommandReceived(Chat chat) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            user.setCity(null);
            userRepository.save(user);
            sendMessage(chat.getId(), CITY_TEXT);
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Handles the /weather command
     *
     * @param chat The chat object of the user
     */
    private void weatherCommandReceived(Chat chat) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            String city = user.getCity();
            if (city != null) {
                log.info("Getting weather for city {}", city);
                checkIfUserCityIsNullAndSetUserCity(chat, city);
                WeatherNow nowWeather = weatherService.getCurrentWeather(city);
                log.info("Current weather: {}", nowWeather);
                String answer = parseToUnicode(":thermometer:") + "Температура " + nowWeather.getMain().getTemp().toString() + "°C, " +
                                nowWeather.getWeather().get(0).getDescription() + ".\n" +
                                "Ощущается как " + nowWeather.getMain().getFeelsLike().toString() + "°C\n";
                sendMessage(chat.getId(), answer);
            } else {
                sendMessage(chat.getId(), CITY_TEXT);
            }
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Handles the /help command
     *
     * @param chat The Chat object of the user
     */
    private void helpCommandReceived(Chat chat) {
        String answer = String.format("Привет, %s!\n" + HELP_TEXT, chat.getFirstName());
        sendMessage(chat.getId(), answer);
    }

    /**
     * Handles the /start command
     *
     * @param chat The Chat object of the user
     */
    private void startCommandReceived(Chat chat) {
        String answer = String.format("Привет, %s! Добро пожаловать!%s", chat.getFirstName(), parseToUnicode(":blush:"));
        sendMessageAndShowMenu(chat, answer);
    }

    /**
     * Handles the /unregister command
     *
     * @param chat The Chat object of the user
     */
    private void deleteUserCommandReceived(Chat chat) {
        if (userIsPresent(chat.getId())) {
            log.info("Deleting user {}", chat.getId());
            userRepository.deleteById(chat.getId());
            log.info("User deleted: " + chat.getId());
            sendMessage(chat.getId(), String.format("Пользователь %s удален.", chat.getUserName()));
        } else {
            sendMessage(chat.getId(), String.format("Пользователя %s нет в базе данных.", chat.getUserName()));
        }
    }

    /**
     * Gets default currencies for the user
     *
     * @param chat     The Chat object of the user
     * @param froms    List of base currencies
     * @param currency The target currency
     */
    private void getDefaultCurrencyRates(Chat chat, List<String> froms, String currency) {
        Map<String, String> currencyRates = currencyService.getCurrencyRates(froms, currency);
        StringBuilder answer = new StringBuilder(parseToUnicode(":heavy_dollar_sign:") + "Курсы основных валют:\n");
        log.info("Default currencies: {}", currencyRates);
        currencyRates.forEach((key, value) -> {
            String temp = key + " - " + value + " " + currency;
            answer.append(temp).append("\n");
        });
        sendMessage(chat.getId(), answer.toString());
    }

    /**
     * Gets the currency rate between two currencies
     *
     * @param chat         The Chat object of the user
     * @param currencyFrom The base currency
     * @param userCurrency The target currency
     */
    private void getCurrencyRate(Chat chat, String currencyFrom, String userCurrency) {
        String currencyRate = currencyService.getCurrencyRate(currencyFrom.toUpperCase(), userCurrency);
        log.info("Currency rate: " + currencyRate);
        String answer = parseToUnicode(":heavy_dollar_sign:") + "Курс валют на сегодня: " + currencyFrom + " = " + currencyRate + " " + userCurrency;
        sendMessage(chat.getId(), answer);
    }

    /**
     * Sends a message to the user and activates the menu
     *
     * @param chat   The Chat object of the user
     * @param answer The message to be sent
     */
    private void sendMessageAndShowMenu(Chat chat, String answer) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chat.getId()));
        message.setText(answer);
        setRightMenuButtons(message);
        executeSendingMessage(message);
    }

    /**
     * Sends a message to the user without activating the menu
     *
     * @param chatId The chat id of the user
     * @param answer The message to be sent
     */
    private void sendMessage(long chatId, String answer) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(answer);
        executeSendingMessage(message);
    }

    /**
     * Executes the sending message
     *
     * @param message The message to be sent
     */
    private void executeSendingMessage(SendMessage message) {
        try {
            execute(message);
            log.debug("Message sent: {}", message.getText());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the right menu buttons for the message
     *
     * @param message The message to which the buttons are to be added
     */
    private void setRightMenuButtons(SendMessage message) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(WEATHER_COMMAND_2);
        row.add(STOCK_QUOTE_COMMAND_2);
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add(DEFAULT_STOCK_COMMAND_2);
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
    }

    /**
     * Registers a user in the database
     *
     * @param chat The Chat object of the user
     */
    private void registerUser(Chat chat) {
        if (!userIsPresent(chat.getId())) {
            createUser(chat.getId(), chat);
            sendMessageAndShowMenu(chat, REGISTRATION_SUCCESSFUL_TEXT);
        }
    }

    /**
     * Creates a user object and saves it to the database
     *
     * @param chatId The chat id of the user
     * @param chat   The chat object of the user
     * @return The created user object
     */
    private User createUser(Long chatId, Chat chat) {
        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUserName(chat.getUserName());
        userRepository.save(user);
        log.info("User created: " + user);
        return user;
    }

    /**
     * Checks if the user city is null and sets it if it is
     *
     * @param chat The chat object of the user
     * @param text The text of the city
     */
    private void checkIfUserCityIsNullAndSetUserCity(Chat chat, String text) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            if (user.getCity() == null) {
                log.info("Setting city of the user {}", user.getChatId());
                String toUpperCase = text.substring(0, 1).toUpperCase() + text.substring(1);
                user.setCity(toUpperCase);
                userRepository.save(user);
                log.info("City of the user {} city set to {}", user.getChatId(), toUpperCase);
                sendMessage(chat.getId(), parseToUnicode(":cityscape:") + "Город установлен: " + toUpperCase);
                weatherCommandReceived(chat);
            }
        }
    }

    /**
     * Sets the currency of the user
     *
     * @param chat     The chat object of the user
     * @param currency The currency to be...
     */
    private void setUserCurrency(Chat chat, String currency) {
        if (userIsPresent(chat.getId())) {
            User user = userRepository.findById(chat.getId()).get();
            user.setCurrency(currency);
            log.info("Setting currency of the user {}", user.getChatId());
            userRepository.save(user);
            log.info("Currency of the user {} set to {}", user.getChatId(), currency);
            sendMessageAndShowMenu(chat, CURRENCY_CHANGE_SUCCESSFUL_TEXT);
        } else {
            sendMessage(chat.getId(), REGISTRATION_NEEDED_TEXT);
        }
    }

    /**
     * Checks if the user is present in the database
     *
     * @param chatId The chat id of the user
     * @return true if
     */
    private boolean userIsPresent(long chatId) {
        log.info("Checking if user {} is exists in the DB", chatId);
        boolean present = userRepository.findById(chatId).isPresent();
        log.info("User exists: " + present);
        return present;
    }

    /**
     * Gets the bot commands
     *
     * @return The bot commands
     */
    private List<BotCommand> getBotCommands() {
        return new ArrayList<>(asList(
                new BotCommand(START_COMMAND, "Запуск бота и авторегистрация пользователя"),
                new BotCommand(HELP_COMMAND, "Помощь"),
                new BotCommand(MY_CITY_COMMAND, "Установить город пользователя"),
                new BotCommand(WEATHER_COMMAND_1, "Погода в твоем городе"),
                new BotCommand(MY_CURRENCY_COMMAND, "Установить валюту пользователя"),
                new BotCommand(STOCK_QUOTE_COMMAND_1, "Курсы валют"),
                new BotCommand(DEFAULT_STOCK_COMMAND_1, "Получить курс основных валют"),
                new BotCommand(SETTINGS_COMMAND, "Посмотреть настройки пользователя"),
                new BotCommand(UNREGISTER_COMMAND, "Удалить аккаунт пользователя")
        ));
    }
}