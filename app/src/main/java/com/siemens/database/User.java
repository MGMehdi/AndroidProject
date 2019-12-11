package com.siemens.database;

import java.io.Serializable;

public class User implements Serializable {
    private String _name;
    private String _surname;
    private String _password;
    private String _mail;
    private int _privilege;

    public User() {
    }

    public User(String _mail, String _name, String _surname, String _password, int _privilege) {
        this._name = _name.substring(0,1).toUpperCase()+_name.substring(1).toLowerCase();
        this._surname = _surname.substring(0,1).toUpperCase()+_surname.substring(1).toLowerCase();
        this._password = _password;
        this._mail = _mail.toLowerCase();
        this._privilege = _privilege;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name.substring(0,1).toUpperCase()+_name.substring(1).toLowerCase();
    }

    public String get_surname() {
        return _surname;
    }

    public void set_surname(String _surname) {
        this._surname = _surname.substring(0,1).toUpperCase()+_surname.substring(1).toLowerCase();
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_mail() {
        return _mail;
    }

    public void set_mail(String _mail) {
        this._mail = _mail.toLowerCase();
    }

    public int get_privilege() {
        return _privilege;
    }

    public void set_privilege(int _privilege) {
        this._privilege = _privilege;
    }



}
