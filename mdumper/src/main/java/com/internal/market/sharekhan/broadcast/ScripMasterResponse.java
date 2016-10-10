/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.internal.market.sharekhan.broadcast;

import com.internal.market.sharekhan.core.CConstants;
import com.internal.market.sharekhan.core.MessageHeader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Janus
 */
public class ScripMasterResponse {
   public MessageHeader msgheader;
   public String exchangecode;
   
   public ScripMasterResponse(byte[] bytes) throws UnsupportedEncodingException, Exception{
       msgheader = new MessageHeader(bytes);
       exchangecode = CConstants.getString(bytes, 6, 2);
   }
}