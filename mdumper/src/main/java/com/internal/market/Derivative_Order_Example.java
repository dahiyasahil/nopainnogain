/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.internal.market;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.internal.market.sharekhan.interactive.OrderRequest;
import com.internal.market.sharekhan.interactive.OrderItem;
import com.internal.market.sharekhan.broadcast.FeedRequest;
import com.internal.market.sharekhan.broadcast.FeedResponse;
import com.internal.market.sharekhan.core.SharekhanAPI;

/**
 *
 * @author Janus
 */
public class Derivative_Order_Example {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        try {
          
          //  return;
            OrderRequest req = new OrderRequest();
            req.requestID ="1";
            req.orderCount = 1;
            req.exchangecode = "NF";
            req.OrderType1 = "NEW";
            req.OrderItem = new OrderItem();
            req.OrderItem.DataLength = 227;
            req.OrderItem.CustomerID = "1565800";
            req.OrderItem.S2KID = "1565800";
            req.OrderItem.ScripToken = "42882";
            req.OrderItem.BuySell = "B";
            req.OrderItem.OrderQty = 25;
            req.OrderItem.DisclosedQty = 0;
            req.OrderItem.OrderPrice = 810000;
            req.OrderItem.AfterHour = "N";
            req.OrderItem.GTDFlag = "GFD";
            
            byte[] reqb = req.getBytes();

            SharekhanAPI api = new SharekhanAPI("sahil8888", "Password@123", "J2RP5D", "192.168.0.112");
            api.connect();
            Thread.sleep(1000L);
            
            FeedRequest feedReq = new FeedRequest((short)3, "NC3718");
            api.sendRequest(feedReq.getBytes());
            //api.sendGraphRequest(exchange, token, scrip_id)
            //api.sendRequest(reqb);
        } catch (IOException ex) {
            Logger.getLogger(Derivative_Order_Example.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Derivative_Order_Example.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
