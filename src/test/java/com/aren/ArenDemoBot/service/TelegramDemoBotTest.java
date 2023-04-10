package com.aren.ArenDemoBot.service;

import com.aren.ArenDemoBot.config.BotConfig;
import com.aren.ArenDemoBot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TelegramDemoBot.class, BotConfig.class})
@ExtendWith(SpringExtension.class)
class TelegramDemoBotTest {
    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private TelegramDemoBot telegramDemoBot;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WeatherService weatherService;

    /**
     * Method under test: {@link TelegramDemoBot#onUpdateReceived(Update)}
     */
    @Test
    void testOnUpdateReceivedDoesNotChangeArguments1() {
        Update update = new Update();

        telegramDemoBot.onUpdateReceived(update);

        assertFalse(update.hasShippingQuery());
        assertFalse(update.hasPreCheckoutQuery());
        assertFalse(update.hasPollAnswer());
        assertFalse(update.hasPoll());
        assertFalse(update.hasMyChatMember());
        assertFalse(update.hasMessage());
        assertFalse(update.hasInlineQuery());
        assertFalse(update.hasEditedMessage());
        assertFalse(update.hasEditedChannelPost());
        assertFalse(update.hasChosenInlineQuery());
        assertFalse(update.hasChatMember());
        assertFalse(update.hasChatJoinRequest());
        assertFalse(update.hasChannelPost());
    }

    /**
     * Method under test: {@link TelegramDemoBot#onUpdateReceived(Update)}
     */
    @Test
    void testOnUpdateReceivedDoesNotChangeArguments2() {
        Message message = new Message();
        InlineQuery inlineQuery = new InlineQuery();
        ChosenInlineQuery chosenInlineQuery = new ChosenInlineQuery();
        CallbackQuery callbackQuery = new CallbackQuery();
        Message message1 = new Message();
        Message channelPost = new Message();
        Message editedChannelPost = new Message();
        ShippingQuery shippingQuery = new ShippingQuery();
        PreCheckoutQuery preCheckoutQuery = new PreCheckoutQuery();
        Poll poll = new Poll();
        PollAnswer pollAnswer = new PollAnswer();
        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        ChatMemberUpdated chatMember = new ChatMemberUpdated();
        Update update = new Update(123, message, inlineQuery, chosenInlineQuery, callbackQuery, message1, channelPost,
                editedChannelPost, shippingQuery, preCheckoutQuery, poll, pollAnswer, chatMemberUpdated, chatMember,
                new ChatJoinRequest());

        telegramDemoBot.onUpdateReceived(update);

        assertTrue(update.hasShippingQuery());
        assertEquals(message, update.getChannelPost());
        ChatMemberUpdated chatMember1 = update.getChatMember();
        assertEquals(chatMemberUpdated, chatMember1);
        assertTrue(update.hasCallbackQuery());
        assertEquals(message1, update.getMessage());
        assertTrue(update.hasChatJoinRequest());
        assertEquals(chatMember1, update.getMyChatMember());
        assertTrue(update.hasChosenInlineQuery());
        assertTrue(update.hasEditedChannelPost());
        assertTrue(update.hasEditedMessage());
        assertTrue(update.hasInlineQuery());
        assertTrue(update.hasPreCheckoutQuery());
        assertTrue(update.hasPollAnswer());
        assertTrue(update.hasPoll());
        assertEquals(123, update.getUpdateId().intValue());
    }

    /**
     * Method under test: {@link TelegramDemoBot#onUpdateReceived(Update)}
     */
    @Test
    void testOnUpdateReceivedHasAndGetsMessage() {
        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(new Message());
        when(update.hasMessage()).thenReturn(true);

        telegramDemoBot.onUpdateReceived(update);

        verify(update).hasMessage();
        verify(update).getMessage();
    }
}

