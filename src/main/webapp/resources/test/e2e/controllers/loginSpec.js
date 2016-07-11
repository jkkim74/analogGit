describe('login', function () {
    var username;
    var password;
    var login-button;

    beforeEach(function () {
        browser.get('/login');
        username = element(by.id("login-username"));
        password = element(by.id("login-password"));
        login-button = element(by.id("login-btn"));
    });

    it('should be login', function () {
        username.sendKeys('pp39093');
        password.sendKeys('Wn664483');
        login-button.click();
        //expect(display.getText()).toEqual("TEST");
    });
});
