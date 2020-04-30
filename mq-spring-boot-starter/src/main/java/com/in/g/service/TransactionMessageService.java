package com.in.g.service;


import com.in.g.support.Destination;
import com.in.g.support.TxMessage;

/**
 * Created by 2YSP on 2020/2/7.
 */
public interface TransactionMessageService {

  void sendTransactionMessage(Destination destination, TxMessage txMessage);
}
