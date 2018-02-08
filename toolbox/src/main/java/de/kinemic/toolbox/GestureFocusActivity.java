/*
 * Copyright (C) 2018 Kinemic GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kinemic.toolbox;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import de.kinemic.toolbox.event.PublisherEvent;
import de.kinemic.toolbox.focus.ActionStrategy;

/**
 * Created by Fabian on 05.02.18.
 */

public class GestureFocusActivity extends AdvancedGestureActivity {

    private static final String TAG = GestureFocusActivity.class.getSimpleName() + "_FOCUS";
    private static final String TAG_ACTION = GestureFocusActivity.class.getSimpleName() + "_ACTION";
    private boolean mIsInGestureMode = true;
    private ViewTreeObserver mLastObserver = null;

    private View mLastFocus = null;

    private ActionStrategy mListStrategy = new ActionStrategy.AnimatedListSelectionStrategy();

    private static String getListState(ListView list) {
        return (list != null) ? getViewResname(list) + ", selected: " + list.getSelectedItemPosition() + ", checked: " + list.getCheckedItemPosition() + ", focused: " + list.getFocusedChild() : "null";
    }

    private Runnable mRightFocusTask = new Runnable() {
        @Override
        public void run() {
            View focus = getCurrentFocus();
            if (focus != null) {
                if (focus instanceof ListView) {
                    ListView list = (ListView) focus;

                    int selection = list.getSelectedItemPosition();
                    if (selection == ListView.INVALID_POSITION) {
                        Log.w(TAG_ACTION, "no selection on current list! " + getListState(list));
                    }

                    if (selection == list.getCount()-1) {
                        Log.d(TAG_ACTION, "last list item was selected, propagate focus right/down");
                        View right = focus.focusSearch(View.FOCUS_RIGHT);
                        View down = focus.focusSearch(View.FOCUS_DOWN);
                        if (right == null && down != null) {
                            Log.d(TAG_ACTION, "no focusable view on the right, fallback to down");
                            down.requestFocusFromTouch();
                        } else if (right != null) {
                            Log.d(TAG_ACTION, "move focus to the right");
                            right.requestFocusFromTouch();
                        } else {
                            Log.d(TAG_ACTION, "did not find focus to the right or down");
                        }

                    } else {
                        mListStrategy.down(focus);
                        Log.d(TAG_ACTION, "select next item in list: " + selection + " -> " + list.getSelectedItemPosition() + ", " + getListState(list));
                    }
                } else {
                    View right = focus.focusSearch(View.FOCUS_RIGHT);
                    View down = focus.focusSearch(View.FOCUS_DOWN);
                    if (right == null && down != null) {
                        Log.d(TAG_ACTION, "no focusable view on the right, fallback to down");
                        down.requestFocusFromTouch();
                    } else if (right != null) {
                        Log.d(TAG_ACTION, "move focus to the right");
                        right.requestFocusFromTouch();
                    } else {
                        Log.d(TAG_ACTION, "did not find focus to the right or down");
                    }
                }
            }
        }
    };

    private Runnable mLeftFocusTask = new Runnable() {
        @Override
        public void run() {
            View focus = getCurrentFocus();
            if (focus != null) {
                if (focus instanceof ListView) {
                    ListView list = (ListView) focus;

                    int selection = list.getSelectedItemPosition();
                    if (selection == ListView.INVALID_POSITION) {
                        Log.w(TAG_ACTION, "no selection on current list! " + list);
                    }

                    if (selection == 0) {
                        Log.d(TAG_ACTION, "first list item was selected, propagate focus left/up");
                        View left = focus.focusSearch(View.FOCUS_LEFT);
                        View up = focus.focusSearch(View.FOCUS_UP);
                        if (left == null && up != null) {
                            Log.d(TAG_ACTION, "no focusable view on the left, fallback to up");
                            up.requestFocusFromTouch();
                        } else if (left != null) {
                            Log.d(TAG_ACTION, "move focus to the left");
                            left.requestFocusFromTouch();
                        } else {
                            Log.d(TAG_ACTION, "did not find focus to the left or up");
                        }
                    } else {
                        /* Todo: handle smooth animation */
                        mListStrategy.up(focus);
                        Log.d(TAG_ACTION, "select previous item in list: " + selection + " -> " + list.getSelectedItemPosition());
                    }
                } else {
                    View left = focus.focusSearch(View.FOCUS_LEFT);
                    View up = focus.focusSearch(View.FOCUS_UP);
                    if (left == null && up != null) {
                        Log.d(TAG_ACTION, "no focusable view on the left, fallback to up");
                        up.requestFocusFromTouch();
                    } else if (left != null) {
                        Log.d(TAG_ACTION, "move focus to the left");
                        left.requestFocusFromTouch();
                    } else {
                        Log.d(TAG_ACTION, "did not find focus to the left or up");
                    }
                }
            }
        }
    };

    private Runnable mDownFocusTask = new Runnable() {
        @Override
        public void run() {
            View focus = getCurrentFocus();
            if (focus != null) {
                View down = focus.focusSearch(View.FOCUS_DOWN);
                View right = focus.focusSearch(View.FOCUS_RIGHT);
                if (down == null && right != null) {
                    Log.d(TAG_ACTION, "no focusable view on the down, fallback to right");
                    right.requestFocusFromTouch();
                } else if (down != null) {
                    Log.d(TAG_ACTION, "move focus to the down");
                    down.requestFocusFromTouch();
                } else {
                    Log.d(TAG_ACTION, "did not find focus to the down or right");
                }
            }
        }
    };

    private Runnable mUpFocusTask = new Runnable() {
        @Override
        public void run() {
            View focus = getCurrentFocus();
            if (focus != null) {
                View up = focus.focusSearch(View.FOCUS_UP);
                View left = focus.focusSearch(View.FOCUS_LEFT);
                if (up == null && left != null) {
                    Log.d(TAG_ACTION, "no focusable view on the up, fallback to left");
                    left.requestFocusFromTouch();
                } else if (up != null) {
                    Log.d(TAG_ACTION, "move focus to the up");
                    up.requestFocusFromTouch();
                } else {
                    Log.d(TAG_ACTION, "did not find focus to the up or left");
                }
            }
        }
    };

    private Runnable mSelectTask = new Runnable() {
        @Override
        public void run() {
            View focus = getCurrentFocus();
            if (focus != null) {
                if (focus instanceof ListView) {
                    ListView list = (ListView) focus;
                    View item = list.getSelectedView();
                    if (item != null) {
                        if (!item.performClick() && !list.performItemClick(list.getSelectedView(),list.getSelectedItemPosition(), list.getSelectedItemId())) {
                            Log.w(TAG_ACTION, "click was not handled");
                        }
                    } else {
                        Log.w(TAG_ACTION, "nothing selected in list");
                    }
                } else {
                    focus.performClick();
                }
            }
        }
    };

    private Runnable mBackTask = new Runnable() {
        @Override
        public void run() {
            onBackPressed();
        }
    };

    protected void handleEvent(PublisherEvent event) throws JSONException {
        if (event.type == PublisherEvent.Type.Gesture) {
            PublisherEvent.Gesture g = event.asGesture();
            Log.d(TAG_ACTION, "got gesture: " + g.name);

            switch (g.name) {
                case "Swipe R":
                    runOnUiThread(mRightFocusTask);
                    break;
                case "Swipe L":
                    runOnUiThread(mLeftFocusTask);
                    break;
                case "Swipe Up":
                    runOnUiThread(mUpFocusTask);
                    break;
                case "Swipe Down":
                    runOnUiThread(mDownFocusTask);
                    break;
                case "Rotate RL":
                    runOnUiThread(mSelectTask);
                    break;
                case "Rotate LR":
                    runOnUiThread(mBackTask);
                    break;
            }
        }
    }

    private void registerViewTreeObservers() {
        unregisterViewTreeObservers();

        View content = this.findViewById(android.R.id.content);
        if (content != null) {
            mLastObserver = content.getRootView().getViewTreeObserver();
            mLastObserver.addOnGlobalFocusChangeListener(mFocusChangeListener);
            mLastObserver.addOnTouchModeChangeListener(mTouchModeListener);

            Log.d(TAG, "register view tree observers");
        }
    }

    private void unregisterViewTreeObservers() {
        if (mLastObserver != null && mLastObserver.isAlive()) {
            Log.d(TAG, "unregister view tree observers");
            mLastObserver.removeOnGlobalFocusChangeListener(mFocusChangeListener);
            mLastObserver.removeOnTouchModeChangeListener(mTouchModeListener);
            mLastObserver = null;
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        validateViewHierarchy(getRootGestureView());
        registerViewTreeObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerViewTreeObservers();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterViewTreeObservers();
    }

    protected View getRootGestureView() {
        return this.findViewById(android.R.id.content);
    }

    private ViewTreeObserver.OnTouchModeChangeListener mTouchModeListener = new ViewTreeObserver.OnTouchModeChangeListener() {
        @Override
        public void onTouchModeChanged(boolean isInTouchMode) {
            Log.d(TAG, "touch mode changed to: " + (isInTouchMode ? "TouchMode" : "GestureMode") + " " + getThreadInfo());
            if (isInTouchMode && mIsInGestureMode) {
                if (mLastFocus != null) {
                    Log.d(TAG, "resume GestureMode with last focus: " + getViewResname(mLastFocus));
                    mLastFocus.requestFocusFromTouch();
                } else if (getRootGestureView() != null) {
                    Log.d(TAG, "resume GestureMode with root focus: " + getViewResname(getRootGestureView()));
                    getRootGestureView().requestFocusFromTouch();
                }
            }
        }
    };

    private static String getViewResname(View v) {
        if (v == null) return "[nothing]";
        String info = v.getResources().getResourceEntryName(v.getId());
        return "[" + info + "]";
    }

    private static String getThreadInfo() {
        return "";
        //return "<" + Thread.currentThread().getName() + ">";
    }

    private ViewTreeObserver.OnGlobalFocusChangeListener mFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            Log.d(TAG, "focus changed: " + getViewResname(oldFocus) + " -> " + getViewResname(newFocus) + " " + getThreadInfo());
            checkFocus(oldFocus, newFocus);
        }
    };

    private void checkFocus(final View oldFocus, final View newFocus) {
        final View root = getRootGestureView();

        if ((newFocus == null && (oldFocus != null && oldFocus.isInTouchMode()))) {
            /* focus changed due to touch */
            Log.d(TAG, "lost focus due to touch");
        } else {
            if (newFocus == null) {
                /* lost focus despite still in gesture mode, this should not happen */
                /* TODO: this is triggered after!! isInTouchMode is false again, so it is due to touch loke above */
                //Log.w(TAG, "lost focus in gesture mode");
                oldFocus.requestFocusFromTouch();
            } else {
                if (newFocus.isInTouchMode()) {
                    Log.d(TAG, "focus changed from touch - ignore");
                } else if (root != null && root.hasFocus()) {
                    Log.d(TAG, "save focus: " + getViewResname(newFocus));
                    mLastFocus = newFocus;
                } else if (root != null && !root.hasFocus()) {
                    if (mLastFocus != null) {
                        Log.d(TAG, "out of tree focus - go back to: " + getViewResname(mLastFocus));
                        mLastFocus.requestFocusFromTouch();
                    } else {
                        Log.d(TAG, "out of tree focus - go back root: " + getViewResname(root));
                        root.requestFocusFromTouch();
                    }
                }
            }
        }
    }



    /*
    check if the supplied view hierarchy is valid for use with gestures
     */
    private void validateViewHierarchy(View view) {
        if (view instanceof ListView) {
            final ListView list = (ListView) view;
            Log.d(TAG, "___ setup List: " + getViewResname(list));

            list.setFocusable(true);
            list.setFocusableInTouchMode(false);
            list.setItemsCanFocus(false);
            //list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

            final ListFocusListener listener = new ListFocusListener();
            list.setOnFocusChangeListener(listener);
            list.setOnItemSelectedListener(listener);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); ++i) {
                View child = group.getChildAt(i);
                validateViewHierarchy(child);
            }
        }
    }

    private class ListFocusListener implements View.OnFocusChangeListener, AdapterView.OnItemSelectedListener {
        private int mLastSelection = 0;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus && v.isInTouchMode()) {
                Log.d(TAG, "ignore list focus change in touch mode");
            } else {
                Log.d(TAG, "list focus changed: " + getViewResname(v) + " -> " + (hasFocus ? "focus" : "defocus"));
                if (hasFocus) {
                    ((ListView) v).setSelection(mLastSelection);
                    ((ListView) v).setItemChecked(mLastSelection, true);
                    Log.d(TAG, "list " + getViewResname(v) + " gained focus - select last selected element: " + mLastSelection);
                } else {
                    int selection = ((ListView) v).getSelectedItemPosition();
                    if (selection != -1) mLastSelection = selection;
                    Log.d(TAG, "list " + getViewResname(v) + " lost focus - deselect elements");
                    //Log.d(TAG, "selected: " + ((ListView) v).getSelectedItemPosition() + ", " + ((ListView) v).getCheckedItemPosition() + " saved: " + mLastSelection + ", current: " + selection);
                }
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            mLastSelection = i;
            Log.d(TAG, "selection changed: " + i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //Log.d(TAG, "nothing selected");
        }
    }
}
