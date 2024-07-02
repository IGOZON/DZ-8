package ru.netology.test;

import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;
import static ru.netology.data.SQLHelper.cleanAuthCodes;
import static ru.netology.data.SQLHelper.cleanDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();

    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }


    @Test
    @DisplayName("Позитивный тест")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.veryfyVerificationPageVisiblity();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

        @Test
        @DisplayName("Неверное имя пользователя")
        void shouldErrorInvalideLogin () {
            var authInfo = DataHelper.generateRandomUser();
            loginPage.validLogin(authInfo);
            loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
        }

        @Test
        @DisplayName("Неверный код верификации")
        void shouldInvalidCodes() {
            var authInfo = DataHelper.getAuthInfo();
            var verificationPage = loginPage.validLogin(authInfo);
            verificationPage.veryfyVerificationPageVisiblity();
            var verificationCode = DataHelper.generateRandomVerificationCode();
            verificationPage.verify(verificationCode.getCode());
            verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");
    }
}