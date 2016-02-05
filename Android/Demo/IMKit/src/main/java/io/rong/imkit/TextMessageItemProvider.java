package io.rong.imkit;

/**
 * Created by x51811danny on 2015/11/22.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.rong.imkit.R.drawable;
import io.rong.imkit.R.layout;
import io.rong.imkit.R.string;
import io.rong.imkit.RongIM.ConversationBehaviorListener;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.LinkTextView;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.model.LinkTextView.LinkTextViewMovementMethod;
import io.rong.imkit.model.LinkTextView.OnLinkClickListener;
import io.rong.imkit.util.AndroidEmoji;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.ArraysDialogFragment.OnArraysDialogItemListener;
import io.rong.imkit.widget.provider.IContainerItemProvider.MessageProvider;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.message.TextMessage;

@SuppressWarnings("deprecation")
@ProviderTag(
        messageContent = TextMessage.class
)
public class TextMessageItemProvider extends MessageProvider<TextMessage> {

    public TextMessageItemProvider() {

    }

    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(layout.rc_item_text_message, (ViewGroup)null);
        TextMessageItemProvider.ViewHolder holder = new TextMessageItemProvider.ViewHolder();
        holder.message = (LinkTextView)view.findViewById(android.R.id.text1);
        holder.message.setTextSize(20);

        holder.message.setPadding(40, 10, 40, 10);
        view.setTag(holder);
        return view;
    }

    public Spannable getContentSummary(TextMessage data) {
        return data != null && data.getContent() != null?new SpannableString(AndroidEmoji.ensure(data.getContent())):null;
    }

    public void onItemClick(View view, int position, TextMessage content, UIMessage message) {
    }

    public void onItemLongClick(final View view, int position, final TextMessage content, final UIMessage message) {
        TextMessageItemProvider.ViewHolder holder = (TextMessageItemProvider.ViewHolder)view.getTag();
        holder.longClick = true;
        if(view instanceof TextView) {
            CharSequence name = ((TextView)view).getText();
            if(name != null && name instanceof Spannable) {
                Selection.removeSelection((Spannable)name);
            }
        }

        String name1 = null;
        if(!message.getConversationType().getName().equals(ConversationType.APP_PUBLIC_SERVICE.getName()) && !message.getConversationType().getName().equals(ConversationType.PUBLIC_SERVICE.getName())) {
            if(message.getSenderUserId() != null) {
                UserInfo items1 = message.getUserInfo();
                if(items1 == null || items1.getName() == null) {
                    items1 = (UserInfo)RongContext.getInstance().getUserInfoCache().get(message.getSenderUserId());
                }

                if(items1 != null) {
                    name1 = items1.getName();
                }
            }
        } else {
            ConversationKey items = ConversationKey.obtain(message.getTargetId(), message.getConversationType());
            PublicServiceProfile info = (PublicServiceProfile)RongContext.getInstance().getPublicServiceInfoCache().get(items.getKey());
            if(info != null) {
                name1 = info.getName();
            }
        }

        String[] items2 = new String[]{view.getContext().getResources().getString(string.rc_dialog_item_message_copy), view.getContext().getResources().getString(string.rc_dialog_item_message_delete)};
        ArraysDialogFragment.newInstance(name1, items2).setArraysDialogItemListener(new OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    ClipboardManager clipboard = (ClipboardManager)view.getContext().getSystemService(RongContext.CLIPBOARD_SERVICE);
                    clipboard.setText(content.getContent());
                } else if(which == 1) {
                    RongIM.getInstance().getRongIMClient().deleteMessages(new int[]{message.getMessageId()}, (ResultCallback)null);
                }

            }
        }).show(((FragmentActivity)view.getContext()).getSupportFragmentManager());
    }

    public void bindView(final View v, int position, TextMessage content, final UIMessage data) {
        TextMessageItemProvider.ViewHolder holder = (TextMessageItemProvider.ViewHolder)v.getTag();
        if(data.getMessageDirection() == MessageDirection.SEND) {
            holder.message.setTextColor(Color.BLACK);
            holder.message.setBackgroundResource(drawable.message_item_border);
        } else {
            holder.message.setTextColor(Color.WHITE);
            holder.message.setBackgroundResource(drawable.message_item_border2);
        }

        if(data.getTextMessageContent() == null) {
            final LinkTextView textView = holder.message;
            if(v.getHandler() != null) {
                v.getHandler().post(new Runnable() {
                    public void run() {
                        if(data.getTextMessageContent() != null) {
                            textView.setText(data.getTextMessageContent());
                        }

                    }
                });
            }
        } else {
            holder.message.setText(data.getTextMessageContent());
        }

        holder.message.setMovementMethod(LinkTextViewMovementMethod.getInstance());
        holder.message.setOnLinkClickListener(new OnLinkClickListener() {
            public boolean onLinkClick(String link) {
                ConversationBehaviorListener listener = RongContext.getInstance().getConversationBehaviorListener();
                return listener != null?listener.onMessageLinkClick(v.getContext(), link):false;
            }
        });
    }

    class ViewHolder {
        LinkTextView message;
        boolean longClick;

        ViewHolder() {
        }
    }
}
