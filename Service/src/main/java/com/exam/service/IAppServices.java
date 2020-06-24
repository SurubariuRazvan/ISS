package com.exam.service;

import com.exam.domain.*;

import java.util.Map;

public interface IAppServices {
    User login(String username, String password, IAppObserver client) throws AppServiceException;

    void logout(Integer userID) throws AppServiceException;

    void notifyStartGame();

    void playerCountChanged(Integer id);

    void sendWord(User user, String text);

    String getLetterSet();
}
