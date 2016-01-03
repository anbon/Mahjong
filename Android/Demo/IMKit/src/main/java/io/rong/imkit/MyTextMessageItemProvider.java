package io.rong.imkit;

import io.rong.imkit.model.ProviderTag;

import io.rong.message.TextMessage;

/**
 * Created by x51811danny on 2015/11/22.
 */
@ProviderTag( messageContent = TextMessage.class , showPortrait = true )
public class MyTextMessageItemProvider extends TextMessageItemProvider {

}
