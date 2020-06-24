package com.exam.service;

import com.exam.domain.*;

import java.util.List;
import java.util.Map;

public interface IAppServices {
    User login(String username, String password, IAppObserver client) throws AppServiceException;

    void logout(Integer userID) throws AppServiceException;

    void notifyStartGame();

    void sendWord(User user, Integer toUser, Integer text);

    void sendNumbers(User user, Integer value1, Integer value2);

    Map<Integer, String> getPlayers();
}
