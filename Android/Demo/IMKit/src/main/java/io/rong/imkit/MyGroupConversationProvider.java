package io.rong.imkit;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.widget.provider.GroupConversationProvider;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * Created by x51811danny on 2015/11/22.
 */

@ConversationProviderTag(conversationType = "group", portraitPosition = 2)
public class MyGroupConversationProvider extends GroupConversationProvider {

}
