package com.example.homebudgetbot.bot;

import com.example.homebudgetbot.config.BotConfig;
import com.example.homebudgetbot.persistance.entity.*;
import com.example.homebudgetbot.persistance.service.CategoryService;
import com.example.homebudgetbot.persistance.service.OperationService;
import com.example.homebudgetbot.persistance.service.TypeOperationService;
import com.example.homebudgetbot.persistance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private BotConfig config;
    private List<InlineKeyboardButton> menuMainBtns;
    private List<InlineKeyboardButton> menuIncomeBtns;
    private InlineKeyboardMarkup keyboardMainMenu;
    private InlineKeyboardMarkup keyboardIncomeMenu;
    private Operation currentOperation = new Operation();

    private OperationService operationService;
    private CategoryService categoryService;
    private UserService userService;
    private TypeOperationService typeOperationService;

    //Этот бот поможет Вам отслеживать свои доходы и расходы
    //                Вычислить категорию товаров на которые больше всего
    //                уходит денежных средств
    @Autowired
    public TelegramBot(BotConfig config,OperationService operationService, CategoryService categoryService, UserService userService) {
        this.config = config;
        this.operationService = operationService;
        this.categoryService = categoryService;
        this.userService = userService;

        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "To started bot"));
        commands.add(new BotCommand("/menu", "To show menu"));
        commands.add(new BotCommand("/help", "Show information"));
        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            var msg = update.getMessage();
            if (msg.isCommand()) {
                if (msg.getText().equals("/start")) {
                    var user = msg.getFrom();
                    var chatId = msg.getChatId();
                    sendMenu(user.getId(), "started...", keyboardMainMenu);

                } else if (msg.getText().equals("/menu")) {
                    var user = msg.getFrom();
                    var chatId = msg.getChatId();
                    sendMenu(user.getId(), "Menu", keyboardMainMenu);
                }
            } else {
                var number = getNumberFromMsg(msg); //Получаем сумму
                sendMessage(msg.getFrom().getId(), "Данные сохранены!");
                currentOperation.setSum(number);
                operationService.save(currentOperation);
                currentOperation = null;
                System.out.println(number);

            }

        } else if (update.hasCallbackQuery()) {
            var cbq = update.getCallbackQuery();
            var user = cbq.getFrom();

            try {
                buttonTap(user.getId(), cbq.getId(), cbq.getData(), cbq.getMessage().getMessageId(), currentOperation);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println(currentOperation);

    }

    private Double getNumberFromMsg(Message msg) {
        return Double.parseDouble(msg.getText());
    }

    public void copyMessage(Message msg) {
        var chatId = msg.getChatId();
        CopyMessage cp = CopyMessage.builder()
                .fromChatId(chatId.toString())
                .chatId(chatId.toString())
                .messageId(msg.getMessageId())
                .build();
        try {
            execute(cp);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
        createMainMenuKeyboard();
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .parseMode("HTML")
                .text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Long who, String txt) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .parseMode("HTML")
                .text(txt)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void createBtnsMainMenu() {
        menuMainBtns = new ArrayList<>();
        var income = InlineKeyboardButton.builder()
                .text("Поступление").callbackData("income")
                .build();

        var expense = InlineKeyboardButton.builder()
                .text("Расход").callbackData("expense")
                .build();

        var statisticUrl = InlineKeyboardButton.builder()
                .text("Статистика")
                .url("https://core.telegram.org/bots/api")
                .build();
        menuMainBtns.add(income);
        menuMainBtns.add(expense);
        menuMainBtns.add(statisticUrl);
    }

    private void createBtnsIncomeMenu() {
        menuIncomeBtns = new ArrayList<>();
        var income = InlineKeyboardButton.builder()
                .text("Зарплата").callbackData("salary")
                .build();

        var expense = InlineKeyboardButton.builder()
                .text("Аванс").callbackData("prepayment")
                .build();

        menuIncomeBtns.add(income);
        menuIncomeBtns.add(expense);
    }

    private void createMainMenuKeyboard() {
        createBtnsMainMenu();
        keyboardMainMenu = InlineKeyboardMarkup.builder()
                .keyboardRow(menuMainBtns)
                .build();

    }

    private void createIncomeMenuKeyboard() {
        createBtnsIncomeMenu();
        keyboardIncomeMenu = InlineKeyboardMarkup.builder()
                .keyboardRow(menuIncomeBtns)
                .build();

    }

    private void buttonTap(Long id, String queryId, String data, int msgId, Operation operation) throws TelegramApiException {

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString())
                .messageId(msgId).build();

        if (data.equals("income")) {
            createIncomeMenuKeyboard();
            newTxt.setText("keyboardIncomeMenu");
            newKb.setReplyMarkup(keyboardIncomeMenu);
            operation.setTypeOperation(TypeOperation.builder()
                    .type(TypeEnum.INCOME)
                    .build());

        } else if (data.equals("expense")) {
            newTxt.setText("keyboardMainMenu");
            newKb.setReplyMarkup(keyboardMainMenu); //////////////////////////////////////////////
            operation.setTypeOperation(TypeOperation.builder()
                    .type(TypeEnum.EXPENSE)
                    .build());

        } else if (data.equals("salary")) {
            newTxt.setText("Введите сумму");
            newKb.setReplyMarkup(keyboardIncomeMenu);
            if (operation.getCategoryOperation() != null) {
                var ca = categoryService.findCategoryOperationByCategoryContaining("SALARY");
                operation.setCategoryOperation(ca.get());
            }
            operation.setCategoryOperation(CategoryOperation.builder()
                    .category(CategoryEnum.SALARY).build());


        } else if (data.equals("prepayment")) {
            newTxt.setText("Введите сумму");
            newKb.setReplyMarkup(keyboardIncomeMenu);
            operation.setCategoryOperation(CategoryOperation.builder()
                    .category(CategoryEnum.PREPAYMENT).build());
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
        execute(newKb);
    }


}
