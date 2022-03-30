package model;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class User extends Object{

    private String email, password, paymentAccountEmail;

    protected User(String email, String password, String paymentAccountEmail){
        this.email = email;
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.paymentAccountEmail = paymentAccountEmail;
    }


}
