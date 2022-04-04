package model;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class User extends Object{

    private String email, passwordHash, paymentAccountEmail;

    protected User(String email, String password, String paymentAccountEmail){
        this.email = email;
        this.passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.paymentAccountEmail = paymentAccountEmail;
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public boolean checkPasswordMatch(String password){
        return BCrypt.verifyer().verify(password.toCharArray(), this.passwordHash).verified;
    }

    public void updatePassword(String newPassword){
        this.passwordHash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
    }

    public String getPaymentAccountEmail(){
        return this.paymentAccountEmail;
    }

    public void setPaymentAccountEmail(String newPaymentAccountEmail){
        this.paymentAccountEmail = newPaymentAccountEmail;
    }
}
